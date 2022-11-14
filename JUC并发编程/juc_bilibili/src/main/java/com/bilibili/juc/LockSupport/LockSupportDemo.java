package com.bilibili.juc.LockSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther zzyy
 * @create 2022-01-20 16:14
 */
public class LockSupportDemo {
    static int x = 0;
    static int y = 0;

    public static void main(String[] args) {
        // 每一个方法就是一个演示，演示中最好注释掉其他方法

        // 必须先wait再notify,不然唤醒无效；
        syncWaitNotify();

        // 必须先condition.await()，后condition.signal();
        lockAwaitSignal();

        // LockSupport.park()与LockSupport.unpark(t1)无先后要求，无需锁.
        parkAndUnpark();

    }

    private static void parkAndUnpark() {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t ----come in" + System.currentTimeMillis());
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t ----被唤醒" + System.currentTimeMillis());
        }, "t1");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // unpark后执行仍然不会报错.

        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\t ----发出通知");
        }, "t2").start();
    }

    private static void lockAwaitSignal() {

        // 必须先wait再notify,不然唤醒无效.
        // 演示无效只需要将2修改为1即可；
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t ----come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "\t ----被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        // 演示必须先condition.await，后condition.signal();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\t ----发出通知");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    private static void syncWaitNotify() {

        // 必须先wait再notify,不然唤醒无效.
        // 演示无效只需要将2修改为1即可；
        Object objectLock = new Object();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "\t ----come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t ----被唤醒");
            }
        }, "t1").start();

        // 演示无效只需要将2修改为1即可；
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "\t ----发出通知");
            }
        }, "t2").start();
    }
}
