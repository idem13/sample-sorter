package com.recruitment.task.samplesorter.service;

import com.recruitment.task.samplesorter.domain.Rack;
import com.recruitment.task.samplesorter.domain.Sample;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class DefaultPolicyChecker implements DefaultSampleToRackAssigner.PolicyChecker {

    public interface Policy {
        boolean check(Sample sample, Rack rack);
    }

    private final Set<Policy> policies;

    @Override
    public boolean check(final Sample sample, final Rack rack) {
        return policies.stream()
                .allMatch(policy -> policy.check(sample, rack));
    }
}
