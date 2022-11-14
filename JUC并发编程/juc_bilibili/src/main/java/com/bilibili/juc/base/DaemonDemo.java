package com.bilibili.juc.base;

import java.util.concurrent.TimeUnit;

/**
 * @auther zzyy
 * @create 2022-01-12 16:23
 */
public class DaemonDemo {
    public static void main(String[] args)// 一切方法运行的入口
    {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 开始运行, " +
                    (Thread.currentThread().isDaemon() ? "守护线程" : "用户线程"));
            while (true) {
                // 功能是卡住方便显示而不是直接闪退.
            }
        }, "t1");
        t1.setDaemon(true);
        t1.start();

        // 启动后再设置为守护进程直接异常
        // t1.start();
        // t1.setDaemon(true);

        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "\t ----end 主线程");
    }
}
