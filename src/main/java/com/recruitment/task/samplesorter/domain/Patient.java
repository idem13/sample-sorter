package com.recruitment.task.samplesorter.domain;

import lombok.Builder;

@Builder
public record Patient(int age, String company, String cityDistrict, String visionDefect) {
}
