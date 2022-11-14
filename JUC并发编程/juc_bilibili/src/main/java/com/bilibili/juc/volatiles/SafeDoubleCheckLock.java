package com.bilibili.juc.volatiles;

public class SafeDoubleCheckLock {

}

// 单列模式
class SafeDoubleCheckSingleton {
    // private static SafeDoubleCheckSingleton singleton;
    private volatile static SafeDoubleCheckSingleton singleton;// 解决下面隐患

    private SafeDoubleCheckSingleton() {
    }

    // 双重锁设计
    public static SafeDoubleCheckSingleton getInstance() {
        if (singleton == null) {
            synchronized (SafeDoubleCheckSingleton.class) {
                if (singleton == null) {
                    // 隐患：多线程环境下，由于重排序，该对象可能还未完成初始化就被其他线程读取
                    // 1.分配内存
                    // 2.初始化对象
                    // 3.指向内存
                    singleton = new SafeDoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }
}
