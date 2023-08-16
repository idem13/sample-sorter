package com.recruitment.task.samplesorter.service;

import com.recruitment.task.samplesorter.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultSampleToRackAssignerTest {

    private DefaultSampleSorterService.SampleToRackAssigner sampleToRackAssigner;
    @Mock
    private DefaultSampleToRackAssigner.PolicyChecker policyChecker;

    @BeforeEach
    void setUp() {
        sampleToRackAssigner = new DefaultSampleToRackAssigner(policyChecker);
    }

    @Test
    void shouldAssignSampleToRack() {
        // given
        final var sample = createSample();
        given(policyChecker.check(isA(Sample.class), isA(Rack.class))).willReturn(true);

        // when
        final var actualAssignment = sampleToRackAssigner.assign(sample);

        // then
        assertThat(actualAssignment.rackId()).isEqualTo(new RackId(1));
    }

    private Sample createSample() {
        return new Sample(new SampleId(UUID.randomUUID()), new Patient(30, "company1", "district1", "visionDefect1"));
    }

    @Test
    void shouldAssignSampleToAnotherRackWhenRackIsFull() {
        // given
        final var sample = createSample();
        given(policyChecker.check(isA(Sample.class), isA(Rack.class))).willReturn(true);
        sampleToRackAssigner.assign(createSample());

        // when
        final var actualAssignment = sampleToRackAssigner.assign(sample);

        // then
        assertThat(actualAssignment.rackId()).isEqualTo(new RackId(2));
    }

    @Test
    void shouldAssignSampleToAnotherRackWhenSampleBreakPolicy() {
        // given
        final var sample = createSample();
        final var rack1 = new Rack(new RackId(1));
        final var rack2 = new Rack(new RackId(2));
        given(policyChecker.check(same(sample), eq(rack1))).willReturn(false);
        given(policyChecker.check(same(sample), eq(rack2))).willReturn(true);

        // when
        final var actualAssignment = sampleToRackAssigner.assign(sample);

        // then
        assertThat(actualAssignment.rackId()).isEqualTo(new RackId(2));
    }
}