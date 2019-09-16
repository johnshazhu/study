package com.lib.annotation.gradle.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LifecycleClassVisitor extends ClassVisitor implements Opcodes {
    private String mClassName;

    public LifecycleClassVisitor(ClassVisitor classVisitor) {
        super(ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("LifecycleClassVisitor : visit -----> started : " + name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("LifecycleClassVisitor : visitMethod : " + name);
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (this.mClassName.endsWith("TestAsm")) {
            if ("test".equals(name) ) {
                return new LifecycleOnCreateMethodVisitor(mv, name);
            } else if ("<clinit>".equals(name)) {
                return new LifecycleOnCreateMethodVisitor(mv, name);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        System.out.println("LifecycleClassVisitor : visit -----> end");
        super.visitEnd();
    }
}
