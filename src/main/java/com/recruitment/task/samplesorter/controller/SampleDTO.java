package com.recruitment.task.samplesorter.controller;

import java.util.UUID;

public record SampleDTO(UUID sampleId, int patientAge, String patientCompany, String patientCityDistrict,
                        String patientVisionDefect) {
}
