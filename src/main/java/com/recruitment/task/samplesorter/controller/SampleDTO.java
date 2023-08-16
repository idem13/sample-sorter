package com.recruitment.task.samplesorter.controller;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record SampleDTO(@NonNull UUID sampleId, @NonNull PatientDTO patient) {
}
