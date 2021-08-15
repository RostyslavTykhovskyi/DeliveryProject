package com.mycompany.delivery.entity;

public enum Status {
    PROCESSING("Processing"),
    AWAITING_PAYMENT("Awaiting payment"),
    PAID("Paid"),
    DONE("Done");

    public final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
