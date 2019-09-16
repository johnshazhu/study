package com.lib.annotation.gradle.util;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LifecycleOnCreateMethodVisitor extends MethodVisitor implements Opcodes {
    private String name;

    public LifecycleOnCreateMethodVisitor(MethodVisitor mv, String name) {
        super(ASM4, mv);
        this.name = name;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("visitCode name : " + name);
        if (name.equals("<clinit>")) {
            mv.visitLdcInsn("Test Modify Tag");
            mv.visitFieldInsn(PUTSTATIC, "com/drcuiyutao/babyhealth/test/TestAsm", "TAG", "Ljava/lang/String;");
            mv.visitInsn(RETURN);
        } else {
            mv.visitMethodInsn(INVOKESTATIC, "com/drcuiyutao/babyhealth/test/TestAsm", "showLog", "()V", false);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }
}
