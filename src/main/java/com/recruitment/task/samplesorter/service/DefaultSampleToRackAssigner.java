package com.recruitment.task.samplesorter.service;

import com.recruitment.task.samplesorter.domain.Assignment;
import com.recruitment.task.samplesorter.domain.Rack;
import com.recruitment.task.samplesorter.domain.RackId;
import com.recruitment.task.samplesorter.domain.Sample;
import com.recruitment.task.samplesorter.exception.BusinessException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class DefaultSampleToRackAssigner implements DefaultSampleSorterService.SampleToRackAssigner {

    public interface PolicyChecker {
        boolean check(Sample sample, Rack rack);
    }

    private final PolicyChecker policyChecker;
    private final Set<Rack> racks = new LinkedHashSet<>();

    public DefaultSampleToRackAssigner(final PolicyChecker policyChecker) {
        this.policyChecker = policyChecker;
        racks.add(new Rack(new RackId(1)));
        racks.add(new Rack(new RackId(2)));
    }


    @Override
    public Assignment assign(@NonNull final Sample sample) {
        final var actualRack = racks.stream()
                .filter(rack -> !rack.isFull())
                .filter(rack -> policyChecker.check(sample, rack))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Cannot assign sample to any rack"));
        actualRack.addSample(sample);
        return new Assignment(actualRack.getId(), sample.id());
    }
}
