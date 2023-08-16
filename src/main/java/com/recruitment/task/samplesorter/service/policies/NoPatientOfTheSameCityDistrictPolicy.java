package com.recruitment.task.samplesorter.service.policies;

import com.recruitment.task.samplesorter.domain.Rack;
import com.recruitment.task.samplesorter.domain.Sample;
import com.recruitment.task.samplesorter.service.DefaultPolicyChecker;

public class NoPatientOfTheSameCityDistrictPolicy implements DefaultPolicyChecker.Policy {
    @Override
    public boolean check(final Sample sample, final Rack rack) {
        return rack.getSamples().stream()
                .noneMatch(assignedSample -> assignedSample.patient().cityDistrict().equals(sample.patient().cityDistrict()));
    }
}
