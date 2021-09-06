package com.lib.annotation.gradle.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.lib.annotation.gradle.util.LifecycleClassVisitor
import javassist.CtClass
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

class AnnotationPlugin extends Transform implements Plugin<Project> {
    AppExtension android
    CustomClassPool clsPool
    List<String> jarList = new ArrayList<>()

    @Override
    void apply(Project project) {
        //registerTransform
        println 'AnnotationPlugin apply start'
        android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
        println 'AnnotationPlugin apply end'
    }

    //Transform的名称，但是这里并不是真正的名称，真正的名称还需要进行拼接
    @Override
    String getName() {
        return AnnotationPlugin.class.getSimpleName()
    }

    //Transform处理文件的类型
    //CLASSES 表示要处理编译后的字节码，可能是jar包也可能是目录
    //RESOURCES表示处理标准的java资源
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    //Transform的作用域
    //PROJECT                   ->      只处理当前的文件
    //SUB_PROJECTS              ->      只处理子项目
    //EXTERNAL_LIBRARIES        ->      只处理外部的依赖库
    //TESTED_CODE               ->      测试代码
    //PROVIDED_ONLY             ->      只处理本地或远程以provided形式引入的依赖库
    //PROJECT_LOCAL_DEPS        ->      只处理当前项目的本地依赖，例如jar、aar(Deprecated，使用EXTERNAL_LIBRARIES)
    //SUB_PROJECTS_LOCAL_DEPS   ->      只处理子项目的本地依赖(Deprecated，使用EXTERNAL_LIBRARIES)
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    //是否支持增量编译，增量编译就是如果第二次编译相应的task没有改变，那么就直接跳过，节省时间
    @Override
    boolean isIncremental() {
        return false
    }

    //对文件或jar进行处理，进行代码的插入
    //inputs            ->  对输入的class文件转变成目标字节码文件，TransformInput就是这些输入文件的抽象。目前它包含DirectoryInput集合与JarInput集合。
    //directoryInputs   ->  源码方式参与项目编译的所有目录结构及其目录下的源文件。
    //jarInputs         ->  Jar包方式参与项目编译的所有本地jar或远程jar包
    //outputProvider    ->  通过这个类来获取输出路径
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        clsPool = new CustomClassPool(true)

        android.bootClasspath.forEach {
//            println "transform bootClasspath : ${it.absolutePath}"
            clsPool.appendClassPath(it.absolutePath)
        }
//        println "transform start incremental : ${transformInvocation.incremental}, path : ${android.bootClasspath[0].toString()}"
        def startTime = System.currentTimeMillis()
        //如果非增量，删除之前的输出
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (outputProvider != null) {
            outputProvider.deleteAll()
        }

        //遍历inputs, inputs中包括directoryInputs和jarInputs，directoryInputs为文件夹中的class文件，而jarInputs为jar包中的class文件
        Collection<TransformInput> inputs = transformInvocation.inputs

        collectInjectClassesInfo(inputs)

        injectPrepare(inputs, outputProvider)

        injectClassFile(inputs, outputProvider)

        clsPool.release()
        def cost = (System.currentTimeMillis() - startTime) / 1000
        println "transform cost : $cost s"
    }

    void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider, boolean inject) {
        boolean find = false
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse {
                def name = it.name
                if (!Util.isFilterClassFile(name)) {
                    if (!inject) {
                        clsPool.injectPrepare(it)
                    } else {
//                        clsPool.injectMethods(it.absolutePath, directoryInput.file.path)
                        clsPool.injectInsertInfo(it.absolutePath, directoryInput.file.path)
                    }
                }
                if (inject && "TestAsm.class" == name) {
                    println "handleDirectoryInput found name : $name"
                    ClassReader classReader = new ClassReader(it.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new LifecycleClassVisitor(classWriter)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(
                            it.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }

        if (inject) {
            //处理完输入文件之后，要把输出给下一个任务
            def dest = outputProvider.getContentLocation(directoryInput.name,
                    directoryInput.contentTypes, directoryInput.scopes,
                    Format.DIRECTORY)

            if (find) {
                println "directoryInput.file : ${directoryInput.file}, dest : $dest"
            }

            FileUtils.copyDirectory(directoryInput.file, dest)
        }
    }

    void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            //重名名输出文件,因为同名会覆盖
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - ".jar".length())
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            //用于保存
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                CtClass ctClass = null
                if (!Util.isFilterClassFile(entryName)) {
                    clsPool.checkInsertCode(entryName, ctClass = clsPool.makeClass(inputStream), null)
                }
                //插桩class
                if ("TestAsm.class" == entryName) {
                    //class文件处理
                    println '----------- deal with "jar" class file <' + entryName + '> -----------'
                    jarOutputStream.putNextEntry(zipEntry)
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new LifecycleClassVisitor(classWriter)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    if (ctClass != null) {
//                        println "handleJarInputs $entryName"
                        jarOutputStream.write(ctClass.toBytecode())
                        if (ctClass.isFrozen()) {
                            ctClass.defrost()
                        }
                    } else {
                        jarOutputStream.write(IOUtils.toByteArray(inputStream))
                    }
                }
                jarOutputStream.closeEntry()
                inputStream.close()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)
            jarList.add(dest.absolutePath)
            tmpFile.delete()
        } else {
            println "jarInput path : ${jarInput.file.getAbsolutePath()}"
        }
    }

    void updateModifiedClassInJar() {
        jarList.each {
            File src = new File(it)
            File tempFile = new File(it + "_tmp")
            try {
                ZipInputStream inputStream = new ZipInputStream(new FileInputStream(src))
                ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(tempFile))
                while (true) {
                    ZipEntry nextEntry = inputStream.getNextEntry()
                    if (nextEntry == null) {
                        break
                    }
                    outputStream.putNextEntry(Util.createZipEntry(nextEntry))
                    String className = clsPool.getCachedClassName(nextEntry.name)
                    if (clsPool.isContainInsertCode(className)) {
                        println "updateModifiedClassInJar ${nextEntry.name} : ${className}, jar : ${src.name}"
                        CtClass ctClass = clsPool.get(className)
                        byte[] bytes = ctClass.toBytecode()
                        IOUtils.write(bytes, outputStream)
                        Util.saveTestClassFile(bytes, className)
                    } else {
                        Util.copy(inputStream, outputStream, 16384)
                    }
                }

                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
                src.delete()
                tempFile.renameTo(it)
            } catch (Throwable e) {
                e.printStackTrace()
            }
        }
    }

    //遍历文件搜集包含注入代码的类及注入目标类
    void collectInjectClassesInfo(Collection<TransformInput> inputs) {
        inputs?.forEach {
            it.directoryInputs?.forEach {
                collectDirectoryInput(it)
            }

            it.jarInputs?.forEach {
                collectJarInput(it)
            }
        }

        println "targetMap : ${clsPool.targetMap}"
    }

    void collectDirectoryInput(DirectoryInput directoryInput) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse {
                def name = it.name
                if (!Util.isFilterClassFile(name)) {
                    clsPool.appendClassPathWithFile(it)
                    clsPool.collectUsedClass(it, null, null)
                }
            }
        }
    }

    void collectJarInput(JarInput jarInput) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            JarFile jarFile = new JarFile(jarInput.file)
            clsPool.appendClassPathWithFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            //用于保存
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                if (!Util.isFilterClassFile(entryName)) {
                    clsPool.collectUsedClass(null, entryName, clsPool.makeClass(inputStream))
                }
                inputStream.close()
            }
            //结束
            jarFile.close()
        } else {
            println "jarInput path : ${jarInput.file.getAbsolutePath()}"
        }
    }

    void injectPrepare(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) {
        inputs?.forEach {
            it.directoryInputs?.forEach {
                handleDirectoryInput(it, outputProvider, false)
            }

            it.jarInputs?.forEach {
                handleJarInputs(it, outputProvider)
            }
        }
    }

    void injectClassFile(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) {
        //按注入目标文件合并注入列表信息
        clsPool.mergeInsertInfoByTargetFile()

        //目录下class文件遍历修改
        inputs?.forEach {
            it.directoryInputs?.forEach {
                handleDirectoryInput(it, outputProvider, true)
            }
        }

        //修改class文件
        //clsPool.injectMethods(null, null)
        clsPool.injectInsertInfo(null, null)

        //复制相关修改的class文件到jar中
        updateModifiedClassInJar()
    }
}