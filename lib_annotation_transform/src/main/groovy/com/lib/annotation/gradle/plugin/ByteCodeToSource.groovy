package com.lib.annotation.gradle.plugin

import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.AttributeInfo
import javassist.bytecode.CodeAttribute
import javassist.bytecode.CodeIterator
import javassist.bytecode.ConstPool
import javassist.bytecode.LocalVariableAttribute
import javassist.bytecode.MethodInfo
import javassist.bytecode.Mnemonic

class ByteCodeToSource {
    static String getSourceTextByByteCode(CtClass ctClass, CtMethod method, boolean replace) {
        println "getSourceTextByByteCode"
        MethodInfo info = method.methodInfo
        CodeAttribute ca = info.codeAttribute
        ConstPool cp = info.constPool

        for (int i = 1; i < cp.size; ++i) {
            try {
                int tag = cp.getTag(i)
                switch (tag) {
                    case ConstPool.CONST_Methodref://10
                        println "methodref[${i}] : ${cp.getMethodrefName(i)}, ${cp.getMethodrefClassName(i)}"
                        break
                    case ConstPool.CONST_Fieldref://9
                        println "field[${i}] : ${cp.getFieldrefName(i)}"
                        break
                    case ConstPool.CONST_String://8
                        println "string[${i}] : ${cp.getStringInfo(i)}"
                        break
                    case ConstPool.CONST_Class://7
                        println "class[${i}] : ${cp.getClassInfo(i)}"
                        break
                    case ConstPool.CONST_NameAndType://12
                        println "nameAndType[${i}] : ${cp.getNameAndTypeName(i)}"
                        break
                    case ConstPool.CONST_Utf8://1
                        println "utf8[${i}] : ${cp.getUtf8Info(i)}"
                        break
                    case ConstPool.CONST_Integer://3
                        println "int[${i}] : ${cp.getIntegerInfo(i)}"
                        break
                    default:
                        break
                }
            } catch (Throwable r) {
                println "tag[${i}] = null"
            }
        }

        LinkedList<Object> localVariableList = new LinkedList<>()
        println "method : ${method.name}, bytes : ${ca.getCode()}, size :${cp.size}, attrs : ${info.attributes.size()}"
        for (int i = 0; i < info.attributes.size(); ++i) {
            AttributeInfo attributes = info.attributes[i]
            switch (attributes.name) {
                case "Code":
                    CodeAttribute codeAttribute = attributes
//                    println "maxStack : ${codeAttribute.maxStack}, maxLocals : ${codeAttribute.maxLocals}, attributes : ${codeAttribute.attributes.size()}"
                    List<AttributeInfo> list = codeAttribute.attributes
                    for (AttributeInfo ai : list) {
//                        println "attribute ${ai.name}"
                        switch (ai.name) {
                            case "LineNumberTable":
//                                println "attribute LineNumberTable : ${ai.toString()}"
                                break

                            case "LocalVariableTable":
                                println "attribute LocalVariableTable : ${ai.toString()}"
                                LocalVariableAttribute lva = (LocalVariableAttribute) ai
                                if (lva.tableLength() > 0) {
                                    for (int j = 0; j < lva.tableLength(); ++j) {
                                        int nameIndex = lva.nameIndex(j)
                                        //println "index ${lva.index(j)}, desIndex ${lva.descriptorIndex(j)}"
                                        //println "attribute LocalVariableTable nameIndex : $nameIndex, name : ${lva.name}, index : ${lva.index(j)}"
                                        LocalVariableInfo lvi = new LocalVariableInfo()
                                        lvi.name = cp.getUtf8Info(nameIndex)
                                        lvi.type = cp.getUtf8Info(lva.descriptorIndex(j))
                                        if (lvi.type.startsWith("L")) {
                                            lvi.type = lvi.type.replaceAll("/", ".").substring(1, lvi.type.length() - 1)
                                        }
                                        localVariableList.add(lvi)
                                    }
                                }
                                break
                        }
                    }
                    break

                default:
                    break
            }
        }

        StringBuilder sourceTextBuilder = new StringBuilder()
        String source = ""
        List<Object> argList = new LinkedList<>()
        int byteCodeIndex = 0
        boolean useLocalVariable = false
        boolean isInstance = false
        boolean isBeforeNew = false
        boolean isMethodExecuted = false
        boolean isNewExecute = false
        Map<String, String> map = new TreeMap<>()
        CodeIterator ci = ca.iterator()
        while (ci.hasNext()) {
            int index = ci.next()
            int op = ci.byteAt(index)
            println "index = ${index}, op = ${op}, str : ${Mnemonic.OPCODE[op]}"
            int cpIndex
            switch (Mnemonic.OPCODE[op]) {
                case "ldc":
                    cpIndex = ca.code[++byteCodeIndex]
                    println "cpIndex : $cpIndex"
                    if (cp.getTag(cpIndex) == ConstPool.CONST_String) {
                        argList.add("\"" + cp.getStringInfo(cpIndex) + "\"")
                    }
                case "pop":
                case "return":
                    if (isMethodExecuted) {
                        isMethodExecuted = false
                        sourceTextBuilder.append(";\n")
                    }
                    break

                case "new":
                    cpIndex = ca.code[++byteCodeIndex] << 8 | ca.code[++byteCodeIndex]
                    println "cpIndex : ${cpIndex}"
                    isBeforeNew = useLocalVariable
                    if (cp.getTag(cpIndex) == ConstPool.CONST_Class) {
                        source += "new " + cp.getClassInfo(cpIndex) + "("
                    }
                    isNewExecute = true
                    break

                case "invokespecial":
                    cpIndex = ca.code[++byteCodeIndex] << 8 | ca.code[++byteCodeIndex]
                    println "cpIndex : $cpIndex"
                    int size = argList.size()
                    if (size > 0 && !isBeforeNew) {
                        for (int i = 0; i < size; ++i) {
                            Object arg = argList.remove(i)
                            if (arg instanceof Integer) {
                                if (localVariableList.get(arg) instanceof LocalVariableInfo) {
                                    source += replace ? ("\$" + arg) : ((LocalVariableInfo) localVariableList.get(arg)).name
                                } else {
                                    source += localVariableList.get(arg)
                                }
                            }
                            if (i != size - 1) {
                                source += ", "
                            }
                        }
                    }
                    source += ")"
                    if (!isBeforeNew) {
                        sourceTextBuilder.append(source)
                        println "source : ${sourceTextBuilder.toString()}"
                        source = ""
                    } else {
                        argList.add(source)
                        source = ""
                    }
                    isNewExecute = false
                    isBeforeNew = false
                    isMethodExecuted = true
                    break

                case "checkcast":
                    cpIndex = ca.code[++byteCodeIndex] << 8 | ca.code[++byteCodeIndex]
                    println "cpIndex : $cpIndex"
                    break

                case "invokevirtual":
                    isInstance = true
                case "invokestatic":
                    cpIndex = ca.code[++byteCodeIndex] << 8 | ca.code[++byteCodeIndex]
                    println "cpIndex : $cpIndex, useLocalVariable : $useLocalVariable, isInstance : $isInstance"
                    if (cp.getTag(cpIndex) == ConstPool.CONST_Methodref) {
                        String methodClassName = cp.getMethodrefClassName(cpIndex)
                        //println "method cls name : $methodClassName"
                        if (map.containsKey(methodClassName)) {
                            //static field as object to call method
                            source += map.get(methodClassName)
                            map.remove(methodClassName)
                            argList.remove(source)
                        } else if (!map.isEmpty()) {
                            //static field as method params
                            map.clear()
                        }

                        if (!isInstance) {
                            source += cp.getMethodrefClassName(cpIndex)
                        }

                        if (isInstance && argList.size() > 0) {
                            Object arg = argList.get(argList.size() - 1)
                            if (arg instanceof Integer) {
                                if (localVariableList.get(arg) instanceof LocalVariableInfo) {
                                    LocalVariableInfo lvi = ((LocalVariableInfo) localVariableList.get(arg))
                                    if (lvi.type == methodClassName) {
                                        source += replace ? ("\$" + arg) : lvi.name
                                        argList.remove(argList.size() - 1)
                                    }
                                }
                            }
                        }

                        source += "." + cp.getMethodrefName(cpIndex) + "("

                        LinkedList<Object> list = argList
                        int size = list.size()
                        if (size > 0) {
                            for (int i = 0; i < size; ++i) {
                                Object arg = list.remove(0)
                                if (arg instanceof Integer) {
                                    if (localVariableList.get(arg) instanceof LocalVariableInfo) {
                                        source += replace ? ("\$" + arg) : ((LocalVariableInfo) localVariableList.get(arg)).name
                                    } else {
                                        source += localVariableList.get(arg)
                                    }
                                } else {
                                    source += arg
                                }

                                if (i != size - 1) {
                                    source += ", "
                                } else {
                                    source += ")"
                                }
                            }
                        } else {
                            source += ")"
                        }
                    }
                    if (!isNewExecute) {
                        println "source : ${source}"
                    }
                    sourceTextBuilder.append(source)
                    source = ""
                    isInstance = false
                    useLocalVariable = false
                    isMethodExecuted = true
                    break

                case "getstatic":
                    cpIndex = ca.code[++byteCodeIndex] << 8 | ca.code[++byteCodeIndex]
                    //println "cpIndex : $cpIndex"
                    String fieldType
                    if (cp.getTag(cpIndex) == ConstPool.CONST_Fieldref) {
                        source += cp.getFieldrefClassName(cpIndex)
                        fieldType = cp.getFieldrefType(cpIndex)
                        if (fieldType.startsWith("L")) {
                            //L ClassName ;
                            fieldType = fieldType.replaceAll("/", ".").substring(1, fieldType.length() - 1)
                        }
                        //println "class : ${cp.getFieldrefClassName(cpIndex)}, name_type : ${cp.getFieldrefNameAndType(cpIndex)}"
                        cpIndex = cp.getFieldrefNameAndType(cpIndex)
                        if (cp.getTag(cpIndex) == ConstPool.CONST_NameAndType) {
                            cpIndex = cp.getNameAndTypeName(cpIndex)
                            if (cp.getTag(cpIndex) == ConstPool.CONST_Utf8) {
                                println "field name : ${cp.getUtf8Info(cpIndex)}, type : $fieldType"
                                source += "." + cp.getUtf8Info(cpIndex)
                            }
                        }
                        map.put(fieldType, source)
                        argList.add(source)
                        source = ""
                    }
                    break

                case "aload_0":
                case "aload_1":
                case "aload_2":
                case "aload_3":
                    argList.add(Integer.parseInt(Mnemonic.OPCODE[op].replace("aload_", "")))
                    useLocalVariable = true
                    break

                case "dup":
                    break

                default:
                    break
            }
            ++byteCodeIndex
        }

        println "sourceText : \n${sourceTextBuilder}"
        return sourceTextBuilder.toString()
    }
}