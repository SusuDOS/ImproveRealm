package com.bilibili.juc.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @auther zzyy
 * @create 2022-01-17 18:44
 */
public class CompletableFutureFastDemo {
    public static void main(String[] args) {

        // 异步方式运行A
        CompletableFuture<String> playA = CompletableFuture.supplyAsync(() -> {
            System.out.println("A come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "playA";
        });

        // 异步方式运行B
        CompletableFuture<String> playB = CompletableFuture.supplyAsync(() -> {
            System.out.println("B come in");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "playB";
        });

        // 要Ab中先执行完的
        CompletableFuture<String> result = playA.applyToEither(playB, f -> {
            return f + " is winer";
        });

        // 获取结果
        System.out.println(Thread.currentThread().getName() + "\t" + "-----: " + result.join());
    }
}
