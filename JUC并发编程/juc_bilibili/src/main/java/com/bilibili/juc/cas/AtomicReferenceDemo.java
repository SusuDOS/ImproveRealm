package com.bilibili.juc.cas;

import java.util.concurrent.atomic.AtomicReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
class User {
    String userName;
    int age;
}

/**
 * @auther zzyy
 * @create 2022-02-24 14:50
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        AtomicReference<User> atomicReference = new AtomicReference<>();

        User z3 = new User("z3", 22);
        User li4 = new User("li4", 28);

        atomicReference.set(z3);

        // 将张三修改为李四，原子引用方式.

        System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());

    }
}
