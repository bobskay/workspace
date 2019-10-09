package com;

import lombok.Data;

public class CloneTest {

    @Data
    public static  class User implements Cloneable{
        String name;

        public User clone() throws CloneNotSupportedException {
            return (User)super.clone();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        User a=new User();
        a.name="a1";
        User b=a.clone();
        a.name="a2";
        System.out.println("a:"+a.name+",b:"+b.name);
    }

}
