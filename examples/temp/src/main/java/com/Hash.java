package com;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Hash {
    public static void main(String[] args) {
        Map map=new HashMap();
        map.put(null,123);
        System.out.println(map.get(null));

        System.out.println(new A().hashCode());
        System.out.println(new A().hashCode());
        String a="ss";
        StringBuilder builder=new StringBuilder();
        builder.append(1);
        CountDownLatch latch;
        ThreadLocal s=new ThreadLocal();


    }

    public static  class  A{

    }
}
