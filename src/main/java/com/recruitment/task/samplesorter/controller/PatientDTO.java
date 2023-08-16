package com.recruitment.task.samplesorter.controller;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record PatientDTO(int age, @NonNull String company, @NonNull String cityDistrict, @NonNull String visionDefect) {
}
