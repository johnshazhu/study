翻译来自[asynchronous-programming-with-coroutines](https://kotlinlang.org/spec/asynchronous-programming-with-coroutines.html)的内容。  

[挂起和恢复](https://kt.academy/article/cc-suspension)  

[协程实现细节](https://kt.academy/article/cc-under-the-hood#definition-2)  

[CoroutineContext](https://kt.academy/article/cc-coroutine-context)  

[Dispatcher](https://kt.academy/article/cc-dispatchers)  

[最佳实践](https://kt.academy/article/cc-best-practices)


1、suspendinging函数  

  kotlin中以suspend关键字来标识suspending函数。  
  
  suspendinging函数和普通函数的区别在于，它可能有0个或者多个挂起点语句在函数体内，这些挂起点会暂停函数的执行，并在一段时间后恢复。挂起点的来源主要是对其他代表可能挂起点suspending函数的调用。 
  
    注意：挂起点是很重要的，因为在挂起点另外的函数可能开始同样的执行流程，这可能会导致在共享状态时有潜在的变化。  
  
  非suspending函数不能直接调用suspending函数，因为它们不支持挂起点。suspending函数调用非suspending函数是没有限制的。以上两种调用情况都不会创建挂起点，这个限制被称之为“函数闭包”。  
  
    重要：对于这个规则的一个例外是非suspending函数中的inline lambda参数，如果这样调用一个lambda表达式的高阶函数是在一个suspending函数中被调用的，那么这个lambda表达式被允许也含有挂起点，并且可以调用其他suspending函数。  
    
    注意：suspending函数的这种相互交织的方式，与支持多线程的平台上不同线程间函数的交互没有什么不同。几个关键差别是，第一suspending函数只能在挂起点暂停，它们不能在任意执行点暂停。第二这种交织方式也可能发生在单线程平台。  
  
    在多线程平台下suspending函数也许会以平台相关的同步执行来交错，独立于协程的交错。  
  
  suspending函数的实现是平台依赖的，不同平台的实现需要参考平台的文档来了解。  

2、Coroutines  

  协程是一个与传统并发编程中线程相似的概念，但是基于协作多任务，比如不同执行上下文间的切换是通过协程来完成，而不是操作系统或者虚拟机。  

  在kotlin中，协程用来实现suspending函数，并只在挂起点切换上下文。  

  对一个supending函数的调用会创建并启动一个协程，因为只能从一个suspending函数中调用另一个suspending函数，我们需要一种从非suspending上下文来引导这个过程的方式。  
  
    注意：这是必要的，因为多数平台对协程和suspending函数是无感的，也不会提供suspending入口点。但是，kotlin编译器会在特定平台上选择提供suspending入口点。  

  一个从非suspending上下文启动suspending函数的方式是通过协程builder，非suspending函数有一个suspending类型的参数（比如一个suspending lambda表达式），并且可以处理协程的生命周期。  

  协程的实现也是平台依赖的，可参考平台文档了解实现细节。  

3、实现细节  

  尽管是平台依赖的，但协程实现的细节还是有一些方面的在kotlin/core中可见的。  

  1、kotlin.coroutines.Continuation  

    接口kotlin.coroutines.Continuation<T>是所有协程的父类，代表了协程机制实现的基础。  
    
    ```
    public interface Continuation<in T> {
        public val context: CoroutineContext
        public fun resumeWith(result: Result<T>)
    }
    ```
    每个suspending函数都与一个生成的Continuation子类型相联系，这个Continuation处理了挂起操作的实现。函数自身被适配为添加了一个额外的continuation参数，来支持CPS（Continuation Passing Style）。
    suspending函数的返回类型变成了continuation的泛型类型T。  
  
    CoroutineContext表示continuation的上下文，是一个从CoroutineContext.Key到CoroutineContext.Element的索引集合（比如一个特殊类型的map）。它用来保存coroutine-local信息，在Continuation interception中扮演重要角色。  
  
    resumeWith是用来在挂起点中传递结果的，被调用时会带着上一个挂起点的结果或异常，并恢复协程的执行。  
  
    为了避免调用resumeWith要显式的创建Result<T>，协程实现提供了下面两个扩展函数。  
    ```
    fun <T> Continuation<T>.resume(value: T)
    fun <T> Continuation<T>.resumeWithException(exception: Throwable)
    ```
  2、Continuation Passing Style  

  每个可挂起的函数通过一个变换，由正常的调用函数变成一个CPS样式的函数。对一个参数为p1，p2...pn返回类型为T的函数，经过变换后，一个新的函数为参数p1，p2...pn，p(n+1)，其中p(n+1)类型为kotlin.coroutines.Continuation<T>，并且返回类型为kotlin.Any?。对这种函数的约定与正常函数是不同的，因为它可能返回suspend，也可能直接return。  

  如果函数返回一个result，那么就像正常一样返回即可。  

  如果函数挂起，它返回一个特殊的标识符COROUTINE_SUSPENDED，用来表示它的挂起状态。  

  CPS变换期间调用约定是由编译器管理的，会阻止用户手动返回COROUTINE_SUSPENDED。如果用户想要挂起一个协程，需要按下面步骤操作。  

    通过内部调用suspendCoroutineUninterceptedOrReturn（或其他封装函数）来访问协程的continuation object  

    保存continuation object等resume时使用  

    传递COROUTINE_SUSPENDED给自身，然后会被函数返回  

  因为kotlin暂不支持uinon类型，返回类型修改为Kotlin.Any?，所以可以支持T和COROUTINE_SUSPENDED  

  3、协程状态机  

  kotlin以状态机方式实现挂起函数，因为这样的实现不需要特定的runtime支持。这规定了kotlin协程的显式挂起标记（函数着色），编译器需要知道哪个函数可能会挂起，然后把其变成状态机。  

  每个可挂起的lambda表达式被编译为一个continuation类，有代表局部变量的字段，有一个整数字段表示状态机里的当前状态。挂起点是这些lambda表达式可挂起的地方：一个suspending函数调用或suspendCoroutineUninterceptedOrReturn的内部调用。对一个有N个挂起点和M个return语句的lambda而言，会有N + M个状态生成（一个对应挂起点，一个对应非挂起返回）。  
  ```
    Example:
    
    // Lambda body with multiple suspension points
    val a = a()
    val y = foo(a).await() // suspension point #1
    b()
    val z = bar(a, y).await() // suspension point #2
    c(z)
    // State machine code for the lambda after CPS transformation
    //     (written in pseudo-Kotlin with gotos)
    class <anonymous> private constructor(
        completion: Continuation<Any?>
    ): SuspendLambda<...>(completion) {
        // The current state of the state machine
        var label = 0
        
        // local variables of the coroutine
        var a: A? = null
        var y: Y? = null
        
        fun invokeSuspend(result: Any?): Any? {
            // state jump table
            if (label == 0) goto L0
            if (label == 1) goto L1
            if (label == 2) goto L2
            else throw IllegalStateException()
            
          L0:
            // result is expected to be `null` at this invocation
            
            a = a()
            label = 1
            // 'this' is passed as a continuation 
            result = foo(a).await(this) 
            // return if await had suspended execution
            if (result == COROUTINE_SUSPENDED)
                return COROUTINE_SUSPENDED
          L1:
            // error handling
            result.throwOnFailure()
            // external code has resumed this coroutine
            // passing the result of .await() 
            y = (Y) result
            b()
            label = 2
            // 'this' is passed as a continuation
            result = bar(a, y).await(this)
            // return if await had suspended execution
            if (result == COROUTINE_SUSPENDED)
                return COROUTINE_SUSPENDED
          L2:
            // error handling
            result.throwOnFailure()
            // external code has resumed this coroutine
            // passing the result of .await()
            Z z = (Z) result
            c(z)
            label = -1 // No more steps are allowed
            return Unit
        }          
        
        fun create(completion: Continuation<Any?>): Continuation<Any?> {
          <anonymous>(completion)
        }          
        
        fun invoke(completion: Continuation<Any?>): Any? {
            create(completion).invokeSuspend(Unit)
        }
    }
  ```

  4、Continuation拦截  

  Continuation拦截器允许我们在挂起点间拦截协程的执行，并在上面执行一些操作。通常在另一个continuation中包装协程的continuation。主要通过接口kotlin.coroutines.ContinuationInterceptor实现。  

  ```
    interface ContinuationInterceptor : CoroutineContext.Element {
        companion object Key : CoroutineContext.Key<ContinuationInterceptor>
        fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T>
        fun releaseInterceptedContinuation(continuation: Continuation<*>)
    }
  ```

  一个ContinuationInterceptor的实例，通常应该在协程上下文中可用，类似于下面的代码这样使用。  
  
  ```
    val intercepted = continuation.context[ContinuationInterceptor]?.interceptContinuation(continuation) ?: continuation
  ```
  5、Coroutine intrinsics(协程内部？)
    访问低级的连续是用一些有限的内置的内部函数来实现的，这些函数构成了整个协程的API。其余的异步编程支持是以库kotlinx.coroutines的形式提供。  
    
    完整的协程内置API如下，所有这些都在标准库下的kotlin.coroutines.intrinsics中。  
    ```
        // 创建一个与扩展receiver挂起函数相对应的的协程，在完成时调用传递过来的完成连续（continuation）。  
        // 这个函数不会启动协程，要启动协程的话，需要在创建的continuation对象上调用Continuation<T>.resumeWith。
        fun <T> (suspend () -> T).createCoroutineUnintercepted(completion: Continuation<T>): Continuation<Unit>
        
        // 提供对当前continuation的访问，如果它的lambda返回COROUTINE_SUSPENDED，那么会挂起协程。和Continuation<T>.resumeWith  
        // (启动或恢复协程)一起，这些函数构成了kotlin编译器内置的协程API。
        suspend fun <T> suspendCoroutineUninterceptedOrReturn(block: (Continuation<T>) -> Any?): T
        
        fun <T> (suspend () -> T).startCoroutineUninterceptedOrReturn(completion: Continuation<T>): Any?
        
        fun <T> Continuation<T>.intercepted(): Continuation<T>
        
        // Additional functions for types with explicit receiver
        
        fun <R, T> (suspend R.() -> T).createCoroutineUnintercepted(completion: Continuation<T>): Continuation<Unit>
        
        fun <T> (suspend R.() -> T).startCoroutineUninterceptedOrReturn(completion: Continuation<T>): Any?
    ```


 
  

  
  

  

