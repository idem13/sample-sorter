package com.recruitment.task.samplesorter.service.policies;

import com.recruitment.task.samplesorter.domain.Rack;
import com.recruitment.task.samplesorter.domain.Sample;
import com.recruitment.task.samplesorter.service.DefaultPolicyChecker;

public class NoPatientOfTheSameCompanyPolicy implements DefaultPolicyChecker.Policy {
    @Override
    public boolean check(final Sample sample, final Rack rack) {
        return rack.getSamples().stream()
                .noneMatch(assignedSample -> assignedSample.patient().company().equals(sample.patient().company()));
    }
}
