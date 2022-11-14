package com.bilibili.juc.volatiles;

import java.util.concurrent.TimeUnit;

class MyNumber {
    // public static volatile int number;
    volatile int number;

    public void addPlusPlus() {
        number++;
    }
}

/**
 * 开10个线程对变量number进行++操作证明volatile无原子性.
 */
public class VolatileNoAtomicDemo {
    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myNumber.addPlusPlus();
                }
            }, String.valueOf(i)).start();
        }

        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(myNumber.number);

    }
}
