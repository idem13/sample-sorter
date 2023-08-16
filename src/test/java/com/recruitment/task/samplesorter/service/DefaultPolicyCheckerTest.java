package com.recruitment.task.samplesorter.service;

import com.recruitment.task.samplesorter.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultPolicyCheckerTest {

    private DefaultPolicyChecker defaultPolicyChecker;

    @Mock
    private DefaultPolicyChecker.Policy policy1;

    @Mock
    private DefaultPolicyChecker.Policy policy2;

    @BeforeEach

    void setUp() {
        final var policies = new LinkedHashSet<DefaultPolicyChecker.Policy>(2);
        policies.add(policy1);
        policies.add(policy2);
        defaultPolicyChecker = new DefaultPolicyChecker(policies);
    }

    @Test
    void shouldReturnTrueWhenNoneOfPoliciesAreBroken() {
        // given
        final var sample = createSample();
        final var rack = new Rack(new RackId(1));

        given(policy1.check(sample, rack)).willReturn(true);
        given(policy2.check(sample, rack)).willReturn(true);

        // when
        final var actualCheck = defaultPolicyChecker.check(sample, rack);

        // then
        assertThat(actualCheck).isTrue();
    }

    private Sample createSample() {
        return new Sample(new SampleId(UUID.randomUUID()), new Patient(30, "company1", "district1", "visionDefect1"));
    }

    @Test
    void shouldReturnFalseWhenOneOfPoliciesBroken() {
        // given
        final var sample = createSample();
        final var rack = new Rack(new RackId(1));

        given(policy1.check(sample, rack)).willReturn(true);
        given(policy2.check(sample, rack)).willReturn(false);

        // when
        final var actualCheck = defaultPolicyChecker.check(sample, rack);

        // then
        assertThat(actualCheck).isFalse();
    }
}