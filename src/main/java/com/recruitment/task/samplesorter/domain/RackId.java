package com.recruitment.task.samplesorter.domain;

public record RackId(int id) {

    public int toInt() {
        return id;
    }
}
