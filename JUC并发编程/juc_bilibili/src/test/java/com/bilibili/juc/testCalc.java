package com.bilibili.juc;

import java.util.concurrent.ThreadLocalRandom;

public class testCalc {
    public static void main(String[] args) {
        System.out.println(String.format("%s price: %.2f", "mysql", getPrice("mysql")));
    }

    public static double getPrice(String productName) {
        System.out.println(ThreadLocalRandom.current().nextDouble());
        System.out.println(productName.charAt(0));

        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }

}
