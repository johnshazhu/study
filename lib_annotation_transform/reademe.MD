#ASM框架中的核心类有以下几个:
    ClassReader: 用来解析编译过的class字节码文件
    ClassWriter: 用来重新构建编译后的类，比如修改类名、属性以及方法，甚至可以生成新的类的字节码文件
    ClassVisitor: 主要负责“拜访”类成员信息。其中包括标记在类上的注解、类的构造方法、类的字段、类的方法、静态代码块。
    AdviceAdapter: 实现了MethodVisitor接口，主要负责“拜访”方法的信息，用来具体的方法字节码操作
[一起玩转Android项目中的字节码](http://quinnchen.me/2018/09/13/2018-09-13-asm-transform/)

[javassist使用指南](https://www.javassist.org/tutorial/tutorial.html)
[class文件格式](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.6)