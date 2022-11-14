package com.bilibili.juc.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @auther zzyy
 * @create 2022-01-17 15:20
 */
public class CompletableFutureAPIDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        group1();
    }

    /**
     * 获得结果和触发计算
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void group1() throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "abc";
        });

        // System.out.println(completableFuture.get());
        // System.out.println(completableFuture.get(2L,TimeUnit.SECONDS));
        // System.out.println(completableFuture.join());

        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // getNow,获取计算的值，若没有返回xxx这个缺省值.
        // System.out.println(completableFuture.getNow("xxx"));
        // completableFuture.complete("completeValue")是否打断。若获取的是缺省值为true，获取到的是计算值则为false.
        System.out.println(completableFuture.complete("completeValue") + "\t" + completableFuture.get());
    }
}
