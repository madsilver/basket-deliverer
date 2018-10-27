package br.com.silver.dybapp.domain;

import io.realm.RealmObject;

public class Counter extends RealmObject {

    private int id;

    private int value;

    public Counter() {

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void increment() {
        this.value++;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
