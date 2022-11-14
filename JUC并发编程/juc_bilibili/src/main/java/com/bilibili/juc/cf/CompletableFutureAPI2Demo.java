package com.bilibili.juc.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 一般采用thenApply()
 * thenApply()传递一个参数v/f，不传递e，可能中途直接断了报异常。
 * handle处理：传递参数(f,e,),将执行流程串行化,有异常会继续执行下去.
 */
public class CompletableFutureAPI2Demo {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(8);

    public static void main(String[] args) {
        // ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture.supplyAsync(() -> {
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("买鱼-111");
            return 1;
        }, threadPool).handle((f, e) -> {
            // int i = 10 / 0;
            int i = 10 / 2;
            System.out.println("调料-222");
            return f + 2;
        }).handle((f, e) -> {
            System.out.println("下锅-333");
            return f + 3;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----计算结果： " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + "----主线程先去忙其它任务");

        // 关闭线程池.
        threadPool.shutdown();
    }
}
