import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 虚拟线程 (Virtual Thread) 示例
 * Java 21 LTS 特性演示
 */
public class VirutalThread {

    public static void main(String[] args) {
        System.out.println("========== 方式 1: 使用 Thread 静态构建器 ==========");
        demoSingleVirtualThread();

        System.out.println("\n========== 方式 2: 使用 ExecutorService 结合使用 ==========");
        demoVirtualThreadPoolWithoutTry();
    }

    /**
     * 演示单个虚拟线程的创建
     * 注意：虚拟线程默认是后台守护线程 (Daemon Thread)，主线程结束时 JVM 会直接关闭，
     * 所以必须使用 thread.join() 等待它执行完毕。
     */
    private static void demoSingleVirtualThread() {
        Runnable runnable = () -> {
            // Thread.currentThread().toString() 会输出类似 [VirtualThread[#21]/runnable@ForkJoinPool-1-worker-1] 的信息
            System.out.println("你好，虚拟线程正在运行！当前线程信息: " + Thread.currentThread());
        };

        // 创建并启动虚拟线程
        Thread virtualThread = Thread.startVirtualThread(runnable);

        try {
            // 等待虚拟线程执行结束，否则主线程直接走完，控制台什么都打印不出来
            virtualThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("主线程在等待虚拟线程时被中断");
        }
    }

    /**
     * 演示虚拟线程池 (每一个提交的任务都独占一个全新的虚拟线程)
     * 在 Java 19 及以后，ExecutorService 实现了 AutoCloseable 接口。
     * 使用 try-with-resources 块时，在退出 try 块时会自动调用 close() 方法，
     * 该方法会阻塞并等待线程池中所有已提交的任务全部执行完毕。
     */
    // private static void demoVirtualThreadPool() {
    //     Runnable task = () -> {
    //         System.out.println("线程池中的任务正在由虚拟线程执行: " + Thread.currentThread());
    //     };

    //     // 创建一个为每个任务创建新虚拟线程的 Executor
    //     try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
    //         for (int i = 0; i < 5; i++) {
    //             executorService.submit(task);
    //         }
    //     } // 退出 try 时自动 close()，阻塞等待这 5 个虚拟线程执行完，再继续往下走
        
    //     System.out.println("所有虚拟线程任务执行完毕！");
    // }

    private static void demoVirtualThreadPoolWithoutTry() {
    Runnable task = () -> {
        System.out.println("虚拟线程执行: " + Thread.currentThread());
    };

    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    for (int i = 0; i < 5; i++) {
        executorService.submit(task);
    }
    
    System.out.println("所有虚拟线程任务提交完毕（但未必执行完）！");
    }

}
