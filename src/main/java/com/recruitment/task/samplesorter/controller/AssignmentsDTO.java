package com.recruitment.task.samplesorter.controller;

import lombok.Builder;

import java.util.Set;

@Builder
public record AssignmentsDTO(Set<RackDTO> racks) {
}
