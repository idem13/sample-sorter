package com.recruitment.task.samplesorter.service;

import com.recruitment.task.samplesorter.controller.SampleSorterController;
import com.recruitment.task.samplesorter.domain.Assignment;
import com.recruitment.task.samplesorter.domain.Rack;
import com.recruitment.task.samplesorter.domain.Sample;
import com.recruitment.task.samplesorter.persistance.RacksRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultSampleSorterService implements SampleSorterController.SampleSorterService {

    interface SampleToRackAssigner {
        Assignment assign(Sample sample);
    }

    private final SampleToRackAssigner sampleToRackAssigner;
    private final SampleSorterMachineClient sampleSorterMachineClient;
    private final RacksRepository racksRepository;

    @Override
    public Assignment assignSampleToRack(@NonNull final Sample sample) {
        log.info("Assigning sample to rack [sample id: {}]", sample.id());
        final  var assignment = sampleToRackAssigner.assign(sample);
        racksRepository.persist(assignment);
        sampleSorterMachineClient.assignSampleToRack(assignment);
        return assignment;
    }

    @Override
    public Set<Rack> obtainRacks() {
        return racksRepository.findAll();
    }
}
