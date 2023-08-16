package com.recruitment.task.samplesorter.service.policies;

import com.recruitment.task.samplesorter.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NoPatientOfTheSameAgeTest {

    private NoPatientOfTheSameAgePolicy noPatientOfTheSameAgePolicy;

    @BeforeEach
    void setUp() {
        noPatientOfTheSameAgePolicy = new NoPatientOfTheSameAgePolicy();
    }

    @Test
    void shouldReturnFalseWhenPatientWithTheSameAgeIsAlreadyInRack() {
        // given
        final var rack = new Rack(new RackId(1));
        final var sample1 = createSample(30, "company1", "district1", "visionDefect1");
        final var sample2 = createSample(30, "company2", "district2", "visionDefect2");
        rack.addSample(sample1);

        // when
        final var actualCheck = noPatientOfTheSameAgePolicy.check(sample2, rack);

        // then
        assertThat(actualCheck).isFalse();
    }

    private static Sample createSample(final int patientAge, final String patientCompany,
                                       final String patientCityDistrict, final String patientVisionDefect) {
        return new Sample(new SampleId(UUID.randomUUID()), new Patient(patientAge, patientCompany, patientCityDistrict, patientVisionDefect));
    }

    @Test
    void shouldReturnTrueWhenNoPatientWithTheSameAgeIsInRack() {
        // given
        final var rack = new Rack(new RackId(1));
        final var sample1 = createSample(30, "company1", "district1", "visionDefect1");
        final var sample2 = createSample(31, "company2", "district2", "visionDefect2");
        rack.addSample(sample1);

        // when
        final var actualCheck = noPatientOfTheSameAgePolicy.check(sample2, rack);

        // then
        assertThat(actualCheck).isTrue();
    }
}