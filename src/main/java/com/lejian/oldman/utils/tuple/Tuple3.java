package com.lejian.oldman.utils.tuple;

import lombok.Data;

@Data
public class Tuple3<A,B,C> {

    private A a;
    private B b;
    private C c;

    public Tuple3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static  <A, B, C> Tuple3 of(A a, B b, C c){
        return new Tuple3(a,b,c);
    }
}