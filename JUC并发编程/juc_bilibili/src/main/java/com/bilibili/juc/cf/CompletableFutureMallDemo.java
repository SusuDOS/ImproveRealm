package com.bilibili.juc.cf;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 *
 * 案例说明：电商比价需求，模拟如下情况：
 *
 * 1需求：
 * 1.1 同一款产品，同时搜索出同款产品在各大电商平台的售价;
 * 1.2 同一款产品，同时搜索出本产品在同一个电商平台下，各个入驻卖家售价是多少
 *
 * 2输出：出来结果希望是同款产品的在不同地方的价格清单列表，返回一个List<String>
 * 《mysql》 in jd price is 88.05
 * 《mysql》 in dangdang price is 86.11
 * 《mysql》 in taobao price is 90.43
 *
 * 3 技术要求
 * 3.1 函数式编程
 * 3.2 链式编程
 * 3.3 Stream流式计算
 */
public class CompletableFutureMallDemo {
    // public static ExecutorService threadPool = Executors.newFixedThreadPool(4);
    public static ExecutorService threadPool = Executors.newFixedThreadPool(8);

    // 元素3个的时候是3是和1秒钟;
    // 元素是4，5个的时候是4,2和5,2;
    // 可能是自带线程是只有3个大小(应该是cpuNumer-1),getPriceByCompletableFuturePool证明了该疑问.
    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("dangdang"),
            new NetMall("taobao"),
            new NetMall("pdd"),
            new NetMall("tmall"));

    /**
     * step by step 一家家搜查
     * List<NetMall> ----->map------> List<String>
     * 
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPrice(List<NetMall> list, String productName) {
        // 在我的印象中，stream流有使用多线程对数据处理的优化，此处并没有显示出来.
        // 《mysql》 in taobao price is 90.43
        return list
                .stream()
                .map(netMall -> String.format(productName + " in %s price is %.2f",
                        netMall.getNetMallName(),
                        netMall.calcPrice(productName)))
                .collect(Collectors.toList());
    }

    /**
     * 多线程技术
     * List<NetMall> ----->List<CompletableFuture<String>>------> List<String>
     * 
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall -> CompletableFuture.supplyAsync(() -> String.format(productName + " in %s price is %.2f",
                        netMall.getNetMallName(),
                        netMall.calcPrice(productName))))
                .collect(Collectors.toList())
                .stream()
                .map(s -> s.join())
                .collect(Collectors.toList());
    }

    public static List<String> getPriceByCompletableFuturePool(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall -> CompletableFuture.supplyAsync(() -> String.format(productName + " in %s price is %.2f",
                        netMall.getNetMallName(),
                        netMall.calcPrice(productName)), threadPool))
                .collect(Collectors.toList())
                .stream()
                .map(s -> s.join())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> list1 = getPrice(list, "mysql");
        for (String element : list1) {
            System.out.println(element);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("----costTime: " + (endTime - startTime) + " 毫秒");

        System.out.println("####################");

        long startTime2 = System.currentTimeMillis();
        List<String> list2 = getPriceByCompletableFuture(list, "mysql");
        for (String element : list2) {
            System.out.println(element);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("----costTime: " + (endTime2 - startTime2) + " 毫秒");

        System.out.println("####################");

        long startTime3 = System.currentTimeMillis();
        List<String> list3 = getPriceByCompletableFuturePool(list, "mysql");
        for (String element : list3) {
            System.out.println(element);
        }
        long endTime3 = System.currentTimeMillis();
        System.out.println("----costTime: " + (endTime3 - startTime3) + " 毫秒");
    }
}

class NetMall {
    @Getter
    private String netMallName;

    public NetMall(String netMallName) {
        this.netMallName = netMallName;
    }

    public double calcPrice(String productName) {
        // 线程睡1秒钟，模拟执行时间.
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 0-1的double数据类型,再+m.
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}