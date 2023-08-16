package com.recruitment.task.samplesorter.domain;

import java.util.UUID;

public record SampleId(UUID id) {

    public UUID toUUID() {
        return id;
    }
}
