package com.example.patterns.object_pool;

class Foo {

    private int var;

    public Foo() {
        this.var = -1;
    }

    public Foo(int value) {
        this.var = value;
    }

    public int getVar() {
        return var;
    }

    public void setVar(int var) {
        this.var = var;
    }
}
