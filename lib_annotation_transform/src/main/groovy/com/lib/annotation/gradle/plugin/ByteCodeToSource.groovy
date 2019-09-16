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
    static String getSourceTextByByteCode(CtClass ctClass, CtMethod method) {
        println "getSourceTextByByteCode"
        MethodInfo info = method.methodInfo
        CodeAttribute ca = info.codeAttribute
        ConstPool cp = info.constPool

        for (int i = 1; i < cp.size; ++i) {
            try {
                int tag = cp.getTag(i)
                switch (tag) {
                    case ConstPool.CONST_Methodref://10
                        MethodInfo
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
//                                println "attribute LocalVariableTable : ${ai.toString()}"
                                LocalVariableAttribute lva = (LocalVariableAttribute) ai
                                if (lva.tableLength() > 0) {
                                    for (int j = 0; j < lva.tableLength(); ++j) {
                                        int nameIndex = lva.nameIndex(j)
//                                        println "attribute LocalVariableTable nameIndex : $nameIndex, name : ${lva.name}, index : ${lva.index(j)}"
                                        if (lva.index(j) > 0) {
                                            localVariableList.add(cp.getUtf8Info(nameIndex))
                                        }
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
        int byteCodeIndex = 0
        boolean useLocalVariable = false
        boolean isInstance = false
        LinkedList<Object> argList = new LinkedList<>()
        CodeIterator ci = ca.iterator()
        while (ci.hasNext()) {
            int index = ci.next()
            int op = ci.byteAt(index)
            println "index = ${index}, op = ${op}, str : ${Mnemonic.OPCODE[op]}"
            int cpIndex = 0
            switch (Mnemonic.OPCODE[op]) {
                case "ldc":
                    cpIndex = ca.code[++byteCodeIndex]
//                    println "cpIndex : $cpIndex"
                    if (cp.getTag(cpIndex) == ConstPool.CONST_String) {
                        argList.add(cp.getStringInfo(cpIndex))
                    }
                    break

                case "invokevirtual":
                    isInstance = true
                case "invokestatic":
                    cpIndex = ca.code[++byteCodeIndex] << 8 | ca.code[++byteCodeIndex]
//                    println "cpIndex : $cpIndex"
                    if (cp.getTag(cpIndex) == ConstPool.CONST_Methodref) {
                        if (!isInstance) {
                            source += cp.getMethodrefClassName(cpIndex)
                        }
                        source += "." + cp.getMethodrefName(cpIndex) + "("

                        LinkedList<Object> list = useLocalVariable ? localVariableList : argList
                        int size = list.size()
                        if (size > 0) {
                            for (int i = 0; i < size; ++i) {
                                Object arg = list.remove(0)
                                if (!isInstance && arg instanceof String) {
                                    source += "\"" + arg +  "\""
                                } else {
                                    source += arg
                                }

                                if (i != size - 1) {
                                    source += ", "
                                } else {
                                    source += ");"
                                }
                            }
                        }
                    }
                    println "source : ${source}"
                    sourceTextBuilder.append(source)
                    source = ""
                    sourceTextBuilder.append("\n")
                    isInstance = false
                    useLocalVariable = false
                    break

                case "getstatic":
                    cpIndex = ca.code[++byteCodeIndex] << 8 | ca.code[++byteCodeIndex]
                    if (cp.getTag(cpIndex) == ConstPool.CONST_Fieldref) {
                        source += cp.getFieldrefClassName(cpIndex)
//                        println "class : ${cp.getFieldrefClassName(cpIndex)}, name_type : ${cp.getFieldrefNameAndType(cpIndex)}"
                        cpIndex = cp.getFieldrefNameAndType(cpIndex)
                        if (cp.getTag(cpIndex) == ConstPool.CONST_NameAndType) {
//                            println "name_type index : ${cp.getNameAndTypeName(cpIndex)}"
                            cpIndex = cp.getNameAndTypeName(cpIndex)
                            if (cp.getTag(cpIndex) == ConstPool.CONST_Utf8) {
//                                println "name : ${cp.getUtf8Info(cpIndex)}"
                                source += "." + cp.getUtf8Info(cpIndex)
                            }
                        }
                    }
                    break

                case "aload_1":
                    useLocalVariable = true
                    break

                default:
                    break
            }
            ++byteCodeIndex
        }

        println "sourceText : ${sourceTextBuilder}"
        return sourceTextBuilder.toString()
    }
}