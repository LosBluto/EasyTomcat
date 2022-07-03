package cn.bluto.easytomcat.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName ThreadPoolUtil.java
 * @Description TODO
 * @createTime 2022年07月03日 20:55:00
 */
public class ThreadPoolUtil {
    private static final int coreSize = 20;
    private static final int maxSize = 100;
    private static final long keepAlive = 60;           //单位s
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize,maxSize,keepAlive, TimeUnit.SECONDS
            ,new LinkedBlockingDeque<>(10));

    public static void run(Runnable task) {
        executor.execute(task);
    }
}
