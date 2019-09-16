package com.lib.annotation.gradle.plugin

import com.lib.annotation.Insert
import javassist.ClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.CtMember
import javassist.CtMethod
import javassist.CtPrimitiveType
import javassist.Modifier
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.FieldInfo
import javassist.bytecode.MethodInfo
import javassist.bytecode.annotation.Annotation
import javassist.bytecode.annotation.ClassMemberValue

class CustomClassPool extends ClassPool {
    def map = [:]
    public def insertMap = [:]
    def list = []
    def pathList = []
    def clsList = []
    def insertInfoMap = [:]

    CustomClassPool(boolean useDefaultPath) {
        super(useDefaultPath)
    }

    String getCachedClassName(String path) {
        return map[path]
    }

    void appendClassPathAndCached(File file, boolean isJar) {
        ClassPath path = appendClassPath(file.absolutePath)
        pathList.add(path)

        if (!isJar) {
            InputStream inputStream = new FileInputStream(file)
            CtClass ctClass = makeClass(inputStream)
            map[file.absolutePath] = ctClass.name

            if (inputStream != null) {
                inputStream.close()
            }

            checkInsertCode(file.absolutePath, ctClass)
        }
    }

    boolean isContainInsertCode(String name) {
        return insertMap.containsKey(name)
    }

    void checkInsertCode(String name, CtClass ctClass) {
        if (name != null && name.length() > 0) {
            map[name] = ctClass.name
        }

        clsList.add(ctClass)
        //遍历当前class文件中的字段, 找到有注入注解的字段, 添加到注入列表中
        CtField[] ctFields = ctClass.getDeclaredFields()
//        println "declared fields : ${ctClass.getDeclaredFields()}"
        if (ctFields.length > 0) {
            for (CtField field : ctFields) {
                if (field.hasAnnotation(Insert.class)) {
                    println "field annotation : ${field}"
                    InsertInfo info = new InsertInfo()
                    info.srcField = field
                    info.ctClassName = ctClass.name
                    list.add(info)
                }
            }
        }

        //遍历当前class文件中的方法, 找到有注入注解的方法, 添加到注入列表中
        CtMethod[] ctMethods = ctClass.methods
        if (ctMethods.size() > 0) {
            for (CtMethod method : ctMethods) {
                if (method.hasAnnotation(Insert.class)) {
                    InsertInfo info = new InsertInfo()
                    info.srcMtd = method
                    info.ctClassName = ctClass.name
                    list.add(info)
                }
            }
        }
    }

    void injectMethods(String absolutePath, String directoryName) {
        if (list.size() == 0) {
            return
        }

        Iterator<InsertInfo> iterator = list.iterator()
        while (iterator.hasNext()) {
            InsertInfo info = iterator.next()

            CtMember ctMember = info.srcField
            if (ctMember == null) {
                ctMember = info.srcMtd
            }
            CtMethod method = info.srcMtd
            Insert annotation = ctMember.getAnnotation(Insert.class)

            String clsName = annotation.classPath()
            if (clsName == null || clsName.length() == 0) {
                clsName = getAnnotationClassValue(ctMember, Insert.class, "target")
            }

            if (absolutePath != null && !absolutePath.isEmpty()) {
                if (clsName != getCachedClassName(absolutePath)) {
                    continue
                }
            }
            println "annotation : ${annotation}"
            insertMap[clsName] = true
            CtClass targetCtCls = get(clsName)
            if (method != null) {
                println "method : $method.name, target : ${clsName}, isFrozen : ${targetCtCls.isFrozen()}"
            } else if (info.srcField != null) {
                println "field : $info.srcField.name, target : ${clsName}, isFrozen : ${targetCtCls.isFrozen()}"
            }
            if (targetCtCls.isFrozen()) {
                targetCtCls.defrost()
            }

            if (info.srcField != null) {
                CtField[] fields = targetCtCls.getDeclaredFields()
                if (fields.length > 0) {
                    for (CtField field : fields) {
                        if (field.name == annotation.name() || field.name == info.srcField.name) {
                            println "find replace field : ${field.name}, annotation ${annotation.name()}, src ${info.srcField.name}"
                            FieldInfo fieldInfo = info.srcField.fieldInfo
                            if (fieldInfo != null) {
                                println "${fieldInfo.name}, ${info.srcField.constantValue}, ${fieldInfo.descriptor}"

                                List<MethodInfo> list = targetCtCls.classFile.methods
                                for (int i = 0; i < list.size(); ++i) {
                                    MethodInfo methodInfo = targetCtCls.classFile.methods.get(i)
                                    if (methodInfo.name == "<clinit>") {
                                        //删除旧的field字段时, 对应静态初始化方法也删掉, 不然添加字段后值并未变化
                                        targetCtCls.classFile.methods.remove(i)
                                        break
                                    }
                                }
                                println "constantValue : ${info.srcField.constantValue}, ${field.fieldInfo.descriptor}"
                                Object constantValue = info.srcField.constantValue

                                //以复制的方式创建一个新的field
                                CtField newField = new CtField(field, targetCtCls)
                                //删除旧的field
                                targetCtCls.removeField(field)
                                //添加之前创建的field, 并用注入的field的值初始化
                                targetCtCls.addField(newField, CtField.Initializer.constant(constantValue))
                            }
                            break
                        }
                    }
                }
                if (directoryName != null && !directoryName.isEmpty()) {
                    targetCtCls.writeFile(directoryName)
                    println "writeFile directoryName : ${directoryName}, field : ${info.srcField.name}"
                } else {
                    targetCtCls.writeFile()
                }
            } else if (info.srcMtd != null) {
                CtMethod[] methods = targetCtCls.getMethods()
                for (CtMethod m : methods) {
                    if (m.name == annotation.name()) {
                        CtClass mtdCls = get(info.ctClassName)
                        if (mtdCls.isFrozen()) {
                            mtdCls.defrost()
                        }

                        String code
                        if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
                            code = mtdCls.name + "." + method.name + "(\$\$);"
                        } else {
                            code = ByteCodeToSource.getSourceTextByByteCode(mtdCls, method)
                        }
                        println "method : ${method}, code : $code, modifyer : ${method.getModifiers()}"
                        if (annotation.replace()) {
                            //方式一 : source text方式替换
//                        m.setBody(code)
                            //方式二 : 复制method的方法替换
                            if (clsName.endsWith("\$Companion")) {
                                method.addLocalVariable("this", targetCtCls)
                            }
                            m.setBody(method, null)
                        } else if (annotation.before()) {
                            m.insertBefore(code)
                        } else {
                            m.insertAfter(code)
                        }
                        if (directoryName != null && !directoryName.isEmpty()) {
                            targetCtCls.writeFile(directoryName)
                            println "writeFile directoryName : ${directoryName}"
                        } else {
                            targetCtCls.writeFile()
                        }
                        iterator.remove()
                        break
                    }
                }
            }
        }
    }

    void collectInsertInfo() {
        if (list.size() > 0) {
            for (InsertInfo info : list) {
                CtMember ctMember = info.srcField
                if (ctMember == null) {
                    ctMember = info.srcMtd
                }
                Insert annotation = ctMember.getAnnotation(Insert.class)

                String clsName = annotation.classPath()
                if (clsName == null || clsName.length() == 0) {
                    clsName = getAnnotationClassValue(ctMember, Insert.class, "target")
                }

                if (insertInfoMap.containsKey(clsName)) {
                    List<InsertInfo> list = insertInfoMap[clsName]
                    list.add(info)
                } else {
                    List<InsertInfo> list = new ArrayList<>()
                    list.add(info)
                    insertInfoMap[clsName] = list
                }
            }
        }

        Set<String> set = insertInfoMap.keySet()
        for (String key : set) {
            println "collect map[$key] = ${insertInfoMap[key]}"
        }
    }

    void release() {
        map.clear()
        insertMap.clear()

        list.clear()
        for (CtClass cls : clsList) {
            if (cls != null) {
                cls.detach()
            }
        }
        clsList.clear()

        Enumeration enumeration = classes.elements()
        while (enumeration.hasMoreElements()) {
            try {
                CtClass ctClass = (CtClass) enumeration.nextElement()
                if ((ctClass != null) && !(ctClass instanceof CtPrimitiveType)) {
                    ctClass.detach()
                }
            } catch (Throwable e) {
                e.printStackTrace()
            }
        }

        for (ClassPath path : pathList) {
            removeClassPath(path)
        }
        pathList.clear()
    }

    static String getAnnotationClassValue(CtMember ctMember, Class annotationClass, String member) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) ctMember.getAttribute(AnnotationsAttribute.visibleTag)
        Annotation annotation = attribute.getAnnotation(annotationClass.getName())
        return ((ClassMemberValue) annotation.getMemberValue(member)).getValue()
    }
}
