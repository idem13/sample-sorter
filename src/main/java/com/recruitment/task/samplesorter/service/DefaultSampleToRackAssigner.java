package com.recruitment.task.samplesorter.service;

import com.recruitment.task.samplesorter.domain.Assignment;
import com.recruitment.task.samplesorter.domain.Rack;
import com.recruitment.task.samplesorter.domain.Sample;
import com.recruitment.task.samplesorter.exception.BusinessException;
import com.recruitment.task.samplesorter.persistance.RacksRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSampleToRackAssigner implements DefaultSampleSorterService.SampleToRackAssigner {

    public interface PolicyChecker {
        boolean check(Sample sample, Rack rack);
    }

    private final PolicyChecker policyChecker;
    private final RacksRepository racksRepository;

    @Override
    public Assignment assign(@NonNull final Sample sample) {
        final var actualRack = racksRepository.findAll().stream()
                .filter(rack -> !rack.isFull())
                .filter(rack -> isSampleWithIdAlreadyAssigned(sample, rack))
                .filter(rack -> policyChecker.check(sample, rack))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Cannot assign sample to any rack"));
        return new Assignment(actualRack.getId(), sample);
    }

    private boolean isSampleWithIdAlreadyAssigned(final Sample sample, final Rack rack) {
        return rack.getSamples().stream().noneMatch(s -> s.id().equals(sample.id()));
    }
}
