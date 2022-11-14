package com.bilibili.juc.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用自定义的线程池，而不是默认的线程池.
 */
public class CompletableFutureBuildDemo {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // ExecutorService threadPool = Executors.newFixedThreadPool(4);

        CompletableFuture<Void> completableFuture_0 = CompletableFuture.runAsync(() -> {
            // 1. 无返回值
            System.out.println(Thread.currentThread().getName());
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, threadPool);
        System.out.println(completableFuture_0.get());
        System.out.println("###############");

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {

            // 2. 有返回值
            System.out.println(Thread.currentThread().getName());
            // 线程睡1秒钟，模拟执行.
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello supplyAsync";
        }, threadPool);
        System.out.println(completableFuture.get());

        threadPool.shutdown();
    }
}
