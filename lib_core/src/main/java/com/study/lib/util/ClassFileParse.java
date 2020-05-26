package com.study.lib.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javassist.bytecode.Mnemonic;

//https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html
public class ClassFileParse {
    static class ClassFile {
        int magic;
        short minor_version;
        short major_version;
        short constant_pool_count;
        ConstPoolInfo constant_pool[];
        short access_flags;
        // The value of the this_class item must be a valid index into the constant_pool table.
        // The constant_pool entry at that index must be a CONSTANT_Class_info
        // structure (§4.4.1) representing the class or interface defined by this class file.
        short this_class;
        short super_class;
        short interfaces_count;
        short interfaces[];
        short fields_count;
        FieldInfo fields[];
        short methods_count;
        MethodInfo methods[];
        short attributes_count;
        AttributeInfo attributes[];

        public int getMagic() {
            return magic;
        }

        public void setMagic(int magic) {
            this.magic = magic;
        }

        public short getMinor_version() {
            return minor_version;
        }

        public void setMinor_version(short minor_version) {
            this.minor_version = minor_version;
        }

        public short getMajor_version() {
            return major_version;
        }

        public void setMajor_version(short major_version) {
            this.major_version = major_version;
        }

        public short getConstant_pool_count() {
            return constant_pool_count;
        }

        public void setConstant_pool_count(short constant_pool_count) {
            this.constant_pool_count = constant_pool_count;
        }

        public ConstPoolInfo[] getConstant_pool() {
            return constant_pool;
        }

        public void setConstant_pool(ConstPoolInfo[] constant_pool) {
            this.constant_pool = constant_pool;
        }

        public short getAccess_flags() {
            return access_flags;
        }

        public void setAccess_flags(short access_flags) {
            this.access_flags = access_flags;
        }

        public short getThis_class() {
            return this_class;
        }

        public void setThis_class(short this_class) {
            this.this_class = this_class;
        }

        public short getSuper_class() {
            return super_class;
        }

        public void setSuper_class(short super_class) {
            this.super_class = super_class;
        }

        public short getInterfaces_count() {
            return interfaces_count;
        }

        public void setInterfaces_count(short interfaces_count) {
            this.interfaces_count = interfaces_count;
        }

        public short[] getInterfaces() {
            return interfaces;
        }

        public void setInterfaces(short[] interfaces) {
            this.interfaces = interfaces;
        }

        public short getFields_count() {
            return fields_count;
        }

        public void setFields_count(short fields_count) {
            this.fields_count = fields_count;
        }

        public FieldInfo[] getFields() {
            return fields;
        }

        public void setFields(FieldInfo[] fields) {
            this.fields = fields;
        }

        public short getMethods_count() {
            return methods_count;
        }

        public void setMethods_count(short methods_count) {
            this.methods_count = methods_count;
        }

        public MethodInfo[] getMethods() {
            return methods;
        }

        public void setMethods(MethodInfo[] methods) {
            this.methods = methods;
        }

        public short getAttributes_count() {
            return attributes_count;
        }

        public void setAttributes_count(short attributes_count) {
            this.attributes_count = attributes_count;
        }

        public AttributeInfo[] getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributeInfo[] attributes) {
            this.attributes = attributes;
        }
    }

    static class ConstPoolInfo {
        byte tag;
        byte info[];

        public byte getTag() {
            return tag;
        }

        public void setTag(byte tag) {
            this.tag = tag;
        }

        public byte[] getInfo() {
            return info;
        }

        public void setInfo(byte[] info) {
            this.info = info;
        }
    }

    static class CONSTANT_String_info {
        byte tag;
        short string_index;

        public byte getTag() {
            return tag;
        }

        public void setTag(byte tag) {
            this.tag = tag;
        }

        public short getString_index() {
            return string_index;
        }

        public void setString_index(short string_index) {
            this.string_index = string_index;
        }
    }

    static class MethodInfo {
        short access_flags;
        short name_index;
        short descriptor_index;
        short attributes_count;
        AttributeInfo attributes[];

        public short getAccess_flags() {
            return access_flags;
        }

        public void setAccess_flags(short access_flags) {
            this.access_flags = access_flags;
        }

        public short getName_index() {
            return name_index;
        }

        public void setName_index(short name_index) {
            this.name_index = name_index;
        }

        public short getDescriptor_index() {
            return descriptor_index;
        }

        public void setDescriptor_index(short descriptor_index) {
            this.descriptor_index = descriptor_index;
        }

        public short getAttributes_count() {
            return attributes_count;
        }

        public void setAttributes_count(short attributes_count) {
            this.attributes_count = attributes_count;
        }

        public AttributeInfo[] getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributeInfo[] attributes) {
            this.attributes = attributes;
        }
    }

    static class FieldInfo {
        short access_flags;
        short name_index;
        short descriptor_index;
        short attributes_count;
        AttributeInfo attributes[];

        public short getAccess_flags() {
            return access_flags;
        }

        public void setAccess_flags(short access_flags) {
            this.access_flags = access_flags;
        }

        public short getName_index() {
            return name_index;
        }

        public void setName_index(short name_index) {
            this.name_index = name_index;
        }

        public short getDescriptor_index() {
            return descriptor_index;
        }

        public void setDescriptor_index(short descriptor_index) {
            this.descriptor_index = descriptor_index;
        }

        public short getAttributes_count() {
            return attributes_count;
        }

        public void setAttributes_count(short attributes_count) {
            this.attributes_count = attributes_count;
        }

        public AttributeInfo[] getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributeInfo[] attributes) {
            this.attributes = attributes;
        }
    }

    static class AttributeInfo {
        short attribute_name_index;
        int attribute_length;
        byte info[];

        public short getAttribute_name_index() {
            return attribute_name_index;
        }

        public void setAttribute_name_index(short attribute_name_index) {
            this.attribute_name_index = attribute_name_index;
        }

        public int getAttribute_length() {
            return attribute_length;
        }

        public void setAttribute_length(int attribute_length) {
            this.attribute_length = attribute_length;
        }

        public byte[] getInfo() {
            return info;
        }

        public void setInfo(byte[] info) {
            this.info = info;
        }
    }

    static class CONSTANT_Class_info {
        byte tag;
        short name_index;
    }

    //The CONSTANT_Fieldref_info, CONSTANT_Methodref_info, and CONSTANT_InterfaceMethodref_info Structures
    static class CONSTANT_Methodref_info {
        byte tag;
        short class_index;
        short name_and_type_index;
    }

    static class CONSTANT_Integer_info {
        byte tag;
        short bytes;
    }

    static class CONSTANT_Float_info {
        byte tag;
        short bytes;
    }

    class CONSTANT_NameAndType_info {
        byte tag;
        short name_index;
        short descriptor_index;
    }

    static class CONSTANT_Utf8_info {
        byte tag;
        short length;
        byte bytes[];
    }

    static int getInt(byte[] bytes) {
        return getInt(bytes, 0);
    }

    static int getInt(byte[] bytes, int index) {
        long value = (((long) (bytes[index] & 0xFF)) << 24) + (((long) (bytes[index + 1] & 0xFF)) << 16) + (((long) (bytes[index + 2] & 0xFF)) << 8) + ((long) (bytes[index + 3] & 0xFF));
//        Log.i("xdebug", "0X" + Long.toHexString(value).toUpperCase());
        return (int) value;
    }

    static short getShort(byte[] bytes) {
        return getShort(bytes, 0);
    }

    static short getShort(byte[] bytes, int index) {
        int value = (((int) (bytes[index] & 0xFF)) << 8) + ((int) (bytes[index + 1] & 0xFF));
//        Log.i("xdebug", Integer.toHexString(value).toUpperCase());
        return (short) value;
    }

    private static final int CONSTANT_Utf8 = 1;

    private static final int CONSTANT_Integer = 3;
    private static final int CONSTANT_Float = 4;
    private static final int CONSTANT_Long = 5;
    private static final int CONSTANT_Double = 6;
    private static final int CONSTANT_Class = 7;
    private static final int CONSTANT_String = 8;
    private static final int CONSTANT_Fieldref = 9;
    private static final int CONSTANT_Methodref = 10;
    private static final int CONSTANT_InterfaceMethodref = 11;
    private static final int CONSTANT_NameAndType = 12;

    private static final int ACC_PUBLIC = 0x0001;
    private static final int ACC_FINAL = 0x0010;
    private static final int ACC_SUPER = 0x0020;
    private static final int ACC_INTERFACE = 0x0200;
    private static final int ACC_ABSTRACT = 0x1000;
    private static final int ACC_ANNOTATION = 0x2000;
    private static final int ACC_ENUM = 0x4000;

    private static String[] refs = {
            "FieldRef",
            "MethodRef",
            "InterfaceMethodRef",
    };

    public static void parseClassFile(Context context, Uri uri) throws IOException {
//        String name = Environment.getExternalStorageDirectory().getPath() + File.separator + "TestInsertUtil.class";
        String name = Environment.getExternalStorageDirectory().getPath() + File.separator + "abnormal.class";
        File file = new File(name);
        InputStream inputStream = context.getContentResolver().openInputStream(uri == null ? Uri.fromFile(file) : uri);
        BufferedInputStream is = new BufferedInputStream(inputStream);
        if (file.exists()) {
//            FileInputStream is = null;
            try {
                boolean readable = file.canRead();
                Log.i("xdebug", "file readable : " + readable);
//                is = new FileInputStream(file);
                int size = is.available();
                Log.i("xdebug", "file size : " + size);
                ClassFile classFile = new ClassFile();
                int offset = 0;

                byte[] header = new byte[4];
                offset += is.read(header, 0, header.length);
                classFile.setMagic(getInt(header));

                byte[] minor = new byte[2];
                offset += is.read(minor, 0, minor.length);
                classFile.setMinor_version(getShort(minor));

                byte[] major = new byte[2];
                offset += is.read(major, 0, major.length);
                classFile.setMajor_version(getShort(major));

                byte[] const_pool_count = new byte[2];
                offset += is.read(const_pool_count, 0, const_pool_count.length);
                classFile.setConstant_pool_count(getShort(const_pool_count));

                ConstPoolInfo[] constPoolInfos = new ConstPoolInfo[classFile.getConstant_pool_count()];

                if (classFile.getConstant_pool_count() > 0) {
                    int i = 1;
                    while (i < classFile.getConstant_pool_count()) {
                        ConstPoolInfo info = new ConstPoolInfo();
                        byte[] tag = new byte[1];
                        offset += is.read(tag, 0, 1);
                        int tagIndex = tag[0] & 0xFF;
                        info.tag = tag[0];
                        switch (tagIndex) {
                            case CONSTANT_Utf8:
                                //u2 : length
                                //u1 : byte[]
                                byte[] len = new byte[2];
                                offset += is.read(len, 0, len.length);
                                int length = getShort(len);
                                byte[] content = new byte[length];
                                offset += is.read(content, 0, content.length);
                                info.info = new byte[len.length + length];
                                System.arraycopy(len, 0, info.info , 0, len.length);
                                System.arraycopy(content, 0, info.info , len.length, length);
                                Log.i("xdebug", "utf8 len : " + length + ", content : " + new String(content));
                                break;

                            case CONSTANT_Integer:
                            case CONSTANT_Float:
                                //u4 bytes;
                                info.info = new byte[4];
                                offset += is.read(info.info, 0, info.info.length);
                                if (tagIndex == CONSTANT_Integer) {
                                    Log.i("xdebug", "integer : " + getInt(info.info));
                                }
                                break;

                            case CONSTANT_Long:
                            case CONSTANT_Double:
                                //u4 high_bytes;
                                //u4 low_bytes;
                                info.info = new byte[8];
                                offset += is.read(info.info, 0, info.info.length);
                                break;

                            case CONSTANT_Class:
                                //u2 : class_index
                                info.info = new byte[2];
                                offset += is.read(info.info, 0, info.info.length);
                                Log.i("xdebug", "class index : " + getShort(info.info));
                                break;

                            case CONSTANT_String:
                                //u2 : string_index
                                info.info = new byte[2];
                                offset += is.read(info.info, 0, info.info.length);
                                Log.i("xdebug", "String index : " + getShort(info.info));
                                break;

                            case CONSTANT_Fieldref:
                            case CONSTANT_Methodref:
                            case CONSTANT_InterfaceMethodref:
                                //u2 : class_index
                                //u2 : name_and_type_index
                                info.info = new byte[4];
                                offset += is.read(info.info, 0, info.info.length);
                                Log.i("xdebug", refs[tagIndex - CONSTANT_Fieldref] + " class_index : " + getShort(info.info, 0) + ", name_and_type_index : " + getShort(info.info, 2));
                                break;

                            case CONSTANT_NameAndType:
                                //u2 name_index;
                                //u2 descriptor_index
                                info.info = new byte[4];
                                offset += is.read(info.info, 0, info.info.length);
                                Log.i("xdebug", "nameAndType name_index : " + getShort(info.info, 0) + ", descriptor_index : " + getShort(info.info, 2));
                                break;

                            default:
                                break;
                        }

                        constPoolInfos[i] = info;
                        ++i;
                    }
                    classFile.setConstant_pool(constPoolInfos);
                }
                byte[] flags = new byte[2];
                offset += is.read(flags, 0, flags.length);
                classFile.setAccess_flags(getShort(flags));
                Log.i("xdebug", "access_flags : " + classFile.getAccess_flags());

                byte[] this_class = new byte[2];
                offset += is.read(this_class, 0, this_class.length);
                classFile.setThis_class(getShort(this_class));
                int name_index = getShort(constPoolInfos[classFile.getThis_class()].info);
                Log.i("xdebug", "this_class : " + getString(constPoolInfos[name_index].info, 2));

                byte[] super_class = new byte[2];
                offset += is.read(super_class, 0, super_class.length);
                classFile.setSuper_class(getShort(super_class));
                name_index = getShort(constPoolInfos[classFile.getSuper_class()].info);
                Log.i("xdebug", "super_class : " + getString(constPoolInfos[name_index].info, 2));

                byte[] interfaces_count = new byte[2];
                offset += is.read(interfaces_count, 0, interfaces_count.length);
                classFile.setInterfaces_count(getShort(interfaces_count));
                Log.i("xdebug", "interfaces_count : " + classFile.getInterfaces_count());
                if (classFile.getInterfaces_count() > 0) {
                    classFile.interfaces = new short[classFile.getInterfaces_count()];
                    //TODO
                }

                byte[] fields_count = new byte[2];
                offset += is.read(fields_count, 0, fields_count.length);
                classFile.setFields_count(getShort(fields_count));
                Log.i("xdebug", "fields_count : " + classFile.getFields_count());
                if (classFile.getFields_count() > 0) {

                }

                byte[] methods_count = new byte[2];
                offset += is.read(methods_count, 0, methods_count.length);
                classFile.setMethods_count(getShort(methods_count));
                Log.i("xdebug", "methods_count : " + classFile.getMethods_count());
                if (classFile.getMethods_count() > 0) {
                    MethodInfo[] methodInfos = new MethodInfo[classFile.getMethods_count()];
                    for (int i = 0; i < classFile.getMethods_count(); ++i) {
                        byte[] content = new byte[2];
                        offset += is.read(content, 0, content.length);
                        MethodInfo info = new MethodInfo();
                        info.setAccess_flags(getShort(content));

                        offset += is.read(content, 0, content.length);
                        info.setName_index(getShort(content));

                        offset += is.read(content, 0, content.length);
                        info.setDescriptor_index(getShort(content));

                        offset += is.read(content, 0, content.length);
                        info.setAttributes_count(getShort(content));
                        Log.i("xdebug", "method name index : " + info.getName_index() +
                                ", name : " + getString(classFile.getConstant_pool()[info.getName_index()].info, 2) +
                                ", attr_count : " + info.getAttributes_count());

                        if (info.getAttributes_count() > 0) {
                            info.attributes = new AttributeInfo[info.getAttributes_count()];
                            for (int j = 0; j < info.getAttributes_count(); ++j) {
                                AttributeInfo attributeInfo = new AttributeInfo();
                                byte[] nameIndex = new byte[2];
                                offset += is.read(nameIndex, 0, nameIndex.length);

                                String attr_name = getString(classFile.getConstant_pool()[getShort(nameIndex)].info, 2);
                                Log.i("xdebug", "attribute nameIndex : " + getShort(nameIndex) + ", name : " + attr_name);
                                byte[] len = new byte[4];
                                offset += is.read(len, 0, len.length);
                                int length = getInt(len);
                                Log.i("xdebug", "attribute len : " + length);

                                attributeInfo.attribute_name_index = getShort(nameIndex);
                                attributeInfo.attribute_length = length;
                                attributeInfo.info = new byte[length];
                                offset += is.read(attributeInfo.info, 0, length);
                                info.attributes[j] = attributeInfo;
                                List<Object> localVariableList = new LinkedList<>();
                                if (attr_name.equals("Code")) {
                                    int off = 0;
                                    short max_stack = getShort(attributeInfo.info, off);
                                    short max_locals = getShort(attributeInfo.info, off = off + 2);
                                    int code_length = getInt(attributeInfo.info, off = off + 2);
                                    byte[] code = new byte[code_length];
                                    System.arraycopy(attributeInfo.info, off = off + 4, code, 0, code_length);
                                    short exception_table_length = getShort(attributeInfo.info, off = off + code_length);
                                    short attributes_count = getShort(attributeInfo.info, off = off + 2);
                                    Log.i("xdebug", "Code max_stack " + max_stack +
                                            ", max_locals : " + max_locals +
                                            ", code_length : " + code_length +
                                            ", exception_table_length : " + exception_table_length +
                                            ", attributes_count : " + attributes_count);
                                    for (int k = 0; k < attributes_count; ++k) {
                                        short attr_name_index = getShort(attributeInfo.info, off = off + 2);
                                        int attr_len = getInt(attributeInfo.info, off = off + 2);
                                        String sub_attr_name = getString(classFile.getConstant_pool()[attr_name_index].info, 2);
                                        Log.i("xdebug", "attr_len :" + attr_len + ", sub_attr_name : " + sub_attr_name);
                                        if (sub_attr_name.equals("LineNumberTable")) {
                                            short line_number_table_length = getShort(attributeInfo.info, off = off + 4);
                                            if (line_number_table_length > 0) {
                                                for (int l = 0; l < line_number_table_length; ++l) {
                                                    short start_pc = getShort(attributeInfo.info, off = off + 2);
                                                    short line_number = getShort(attributeInfo.info, off = off + 2);
                                                    Log.i("xdebug", "LineNumberTable start_pc : " + start_pc + ", line_number : " + line_number);
                                                }
                                            }
                                        } else if (sub_attr_name.equals("LocalVariableTable")) {
                                            short local_variable_table_length = getShort(attributeInfo.info, off = off + 4);
                                            if (local_variable_table_length > 0) {
                                                for (int l = 0; l < local_variable_table_length; ++l) {
                                                    short start_pc = getShort(attributeInfo.info, off = off + 2);
                                                    short local_length = getShort(attributeInfo.info, off = off + 2);
                                                    short local_name_index = getShort(attributeInfo.info, off = off + 2);
                                                    short descriptor_index = getShort(attributeInfo.info, off = off + 2);
                                                    short index = getShort(attributeInfo.info, off = off + 2);
                                                    String local_variable_name = getString(classFile.getConstant_pool()[local_name_index].info, 2);
                                                    Log.i("xdebug", "LocalVariableTable start_pc : " + start_pc +
                                                            ", local_length : " + local_length +
                                                            ", local_name_index : " + local_name_index +
                                                            ", local_name : " + local_variable_name +
                                                            ", descriptor_index : " + descriptor_index +
                                                            ", index : " + index);
                                                    if (index > 0) {
                                                        localVariableList.add(local_variable_name);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Log.i("xdebug", "byte code : " + Arrays.toString(code));
                                    List<Object> argList = new LinkedList<>();
                                    boolean useLocalVariable = false;
                                    boolean useInstance = false;
                                    String source = "";
                                    for (int k = 0; k < code_length; ++k) {
                                        int op = code[k] & 0xFF;
                                        ConstPoolInfo cpi;
                                        int index;
                                        switch (Mnemonic.OPCODE[op]) {
                                            case "getstatic":
                                                //Get static field from class
                                                index = ((code[++k] & 0xFF) << 8) | (code[++k] & 0xFF);
                                                cpi = classFile.constant_pool[index];
                                                if (cpi.tag == CONSTANT_Fieldref) {
                                                    short class_index = getShort(cpi.info);
                                                    short name_and_type_index = getShort(cpi.info, 2);
                                                    ConstPoolInfo classPool = classFile.constant_pool[class_index];
                                                    String className = "";
                                                    if (classPool.tag == CONSTANT_Class) {
                                                        short class_name_index = getShort(classPool.info);
                                                        className = getString(classFile.constant_pool[class_name_index].info, 2);
                                                        className = className.replaceAll("/", ".");
                                                    }
                                                    String methodName = "";
                                                    String methodDes = "";
                                                    ConstPoolInfo nameTypePool = classFile.constant_pool[name_and_type_index];
                                                    if (nameTypePool.tag == CONSTANT_NameAndType) {
                                                        short name_type_index = getShort(nameTypePool.info);
                                                        short descriptor_index = getShort(nameTypePool.info, 2);
                                                        methodName = getString(classFile.constant_pool[name_type_index].info, 2);
                                                        methodDes = getString(classFile.constant_pool[descriptor_index].info, 2);
                                                    }
                                                    Log.i("xdebug", "field className : " + className + ", methodName : " + methodName +
                                                            ", des : " + methodDes);
                                                    source += className + "." + methodName;
                                                }
                                                break;

                                            case "aload_1":
                                                //Load reference from local variable
                                                useLocalVariable = true;
                                                break;

                                            case "ldc":
                                                //Push item from run-time constant pool
                                                cpi = classFile.constant_pool[code[++k]];
                                                if (cpi.tag == CONSTANT_String) {
                                                    short string_index = getShort(cpi.info);
                                                    argList.add(getString(classFile.constant_pool[string_index].info, 2));
                                                }
                                                break;

                                            case "invokevirtual":
                                                useInstance = true;
                                            case "invokestatic":
                                                //Invoke a class (static) method
                                                index = ((code[++k] & 0xFF) << 8) | (code[++k] & 0xFF);
                                                ConstPoolInfo poolInfo = classFile.constant_pool[index];
                                                if (poolInfo.tag == CONSTANT_Methodref) {
                                                    short class_index = getShort(poolInfo.info);
                                                    short name_and_type_index = getShort(poolInfo.info, 2);

                                                    ConstPoolInfo classPool = classFile.constant_pool[class_index];
                                                    String className = "";
                                                    if (!useInstance && classPool.tag == CONSTANT_Class) {
                                                        short class_name_index = getShort(classPool.info);
                                                        className = getString(classFile.constant_pool[class_name_index].info, 2);
                                                        className = className.replaceAll("/", ".");
                                                    }

                                                    String methodName = "";
                                                    String methodDes = "";
                                                    ConstPoolInfo nameTypePool = classFile.constant_pool[name_and_type_index];
                                                    if (nameTypePool.tag == CONSTANT_NameAndType) {
                                                        short name_type_index = getShort(nameTypePool.info);
                                                        short descriptor_index = getShort(nameTypePool.info, 2);
                                                        methodName = getString(classFile.constant_pool[name_type_index].info, 2);
                                                        methodDes = getString(classFile.constant_pool[descriptor_index].info, 2);
                                                    }
                                                    String methodDesSub = methodDes.substring(1, methodDes.length() - 2);
                                                    String[] params = methodDesSub.split(";");
                                                    source += className + "." + methodName + methodDes.substring(0, 1);
                                                    for (int l = 0; l < params.length; ++l) {
                                                        if (useLocalVariable) {
                                                            if (localVariableList.size() > 0) {
                                                                source += localVariableList.remove(0);
                                                            }
                                                        } else if (argList.size() > 0) {
                                                            Object param = argList.remove(0);
                                                            if (param instanceof String) {
                                                                source += "\"" + param + "\"";
                                                            } else {
                                                                source += param;
                                                            }
                                                        }
                                                        if (l != params.length - 1) {
                                                            source += ", ";
                                                        } else {
                                                            source += ");";
                                                        }
                                                    }
                                                    Log.i("xdebug", "source text : " + source);
                                                }
                                                useLocalVariable = false;
                                                useInstance = false;
                                                source = "";
                                                break;
                                        }

                                    }
                                } else if (attr_name.equals("RuntimeVisibleAnnotations")) {
                                    int off = 0;
                                    short num_annotations = getShort(attributeInfo.info, off);
                                    Log.i("xdebug", "RuntimeVisibleAnnotations num_annotations : " + num_annotations);
                                    if (num_annotations > 0) {
                                        for (int m = 0; m < num_annotations; ++m) {
                                            short type_index = getShort(attributeInfo.info, off = off + 2);
                                            short num_element_value_pairs = getShort(attributeInfo.info, off += 2);
                                            Log.i("xdebug", "RuntimeVisibleAnnotations type_index : " + type_index +
                                                    ", type_name : " + getString(classFile.getConstant_pool()[type_index].info, 2) +
                                                    ", pairs : " + num_element_value_pairs);
                                            if (num_element_value_pairs > 0) {
                                                for (int n = 0; n < num_element_value_pairs; ++n) {
                                                    short element_name_index = getShort(attributeInfo.info, off += 2);
                                                    int tag = attributeInfo.info[off += 2] & 0xFF;
                                                    Log.i("xdebug", "RuntimeVisibleAnnotations tag : " + ((char) tag) +
                                                            ", element_name : " + getString(classFile.getConstant_pool()[element_name_index].info, 2));
                                                    switch (tag) {
                                                        case 'B':
                                                        case 'C':
                                                        case 'D':
                                                        case 'F':
                                                        case 'I':
                                                        case 'J':
                                                        case 'S':
                                                        case 'Z':
                                                        case 's':
                                                            short const_value_index = getShort(attributeInfo.info, off += 1);
                                                            String valueString = "";
                                                            if (classFile.getConstant_pool()[const_value_index].tag == CONSTANT_Integer) {
                                                                valueString = ", value : " + getInt(classFile.getConstant_pool()[const_value_index].info);
                                                            } else {
                                                                valueString = ", value : " + getString(classFile.getConstant_pool()[const_value_index].info, 2);
                                                            }
                                                            Log.i("xdebug", "RuntimeVisibleAnnotations const_value_index : " + const_value_index +
                                                                    ", tag : " + classFile.getConstant_pool()[const_value_index].tag + valueString);
                                                            break;

                                                        case 'e':
                                                            short type_name_index = getShort(attributeInfo.info, off += 1);
                                                            short const_name_index = getShort(attributeInfo.info, off += 2);
                                                            Log.i("xdebug", "RuntimeVisibleAnnotations type_name_index : " + type_name_index +
                                                                    ", type_name_value : " + getString(classFile.getConstant_pool()[type_name_index].info, 2) +
                                                                    ", const_name_index : " + const_name_index +
                                                                    ", value : " + getString(classFile.getConstant_pool()[const_name_index].info, 2));
                                                            break;

                                                        case 'c':
                                                            short class_info_index = getShort(attributeInfo.info, off += 1);
                                                            Log.i("xdebug", "RuntimeVisibleAnnotations class_info_index : " + class_info_index +
                                                                    ", class_info : " + getString(classFile.getConstant_pool()[class_info_index].info, 2));
                                                            break;

                                                        case '@':
                                                            Log.i("xdebug", "RuntimeVisibleAnnotations nested annotation");
                                                            break;

                                                        case '[':
                                                            Log.i("xdebug", "RuntimeVisibleAnnotations array_value");
                                                            short num_values = getShort(attributeInfo.info, off += 1);
                                                            if (num_values > 0) {

                                                            }
                                                            break;

                                                        default:
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    classFile.setMethods(methodInfos);

                    byte[] attributes_count = new byte[2];
                    offset += is.read(attributes_count, 0, attributes_count.length);
                    classFile.setAttributes_count(getShort(attributes_count));
                    Log.i("xdebug", "attributes_count : " + classFile.getAttributes_count());

                    if (classFile.getAttributes_count() > 0) {
                        AttributeInfo[] attributeInfos = new AttributeInfo[classFile.getAttributes_count()];
                        for (int i = 0; i < classFile.getAttributes_count(); ++i) {
                            AttributeInfo attributeInfo = new AttributeInfo();
                            byte[] attribute_name_index = new byte[2];
                            offset += is.read(attribute_name_index, 0, attribute_name_index.length);

                            byte[] len = new byte[4];
                            offset += is.read(len, 0, len.length);

                            attributeInfo.attribute_name_index = getShort(attribute_name_index);
                            attributeInfo.attribute_length = getInt(len);
                            attributeInfo.info = new byte[attributeInfo.attribute_length];
                            offset += is.read(attributeInfo.info, 0, attributeInfo.attribute_length);
                            attributeInfos[i] = attributeInfo;
                        }
                        classFile.setAttributes(attributeInfos);
                    }

                    if (offset == size) {
                        Log.i("xdebug", "file read end");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    private static String getString(byte[] bytes) {
        return new String(bytes);
    }

    private static String getString(byte[] bytes, int start) {
        return new String(bytes, start, bytes.length - start);
    }
}
