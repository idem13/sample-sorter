package com.recruitment.task.samplesorter.controller;

import lombok.Builder;

import java.util.Set;

@Builder
public record RackDTO(int rackId, Set<SampleDTO> samples) {
}
