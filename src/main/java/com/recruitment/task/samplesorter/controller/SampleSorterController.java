package com.recruitment.task.samplesorter.controller;

import com.recruitment.task.samplesorter.domain.Assignment;
import com.recruitment.task.samplesorter.domain.Patient;
import com.recruitment.task.samplesorter.domain.Sample;
import com.recruitment.task.samplesorter.domain.SampleId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SampleSorterController {

    public interface SampleSorterService {
        Assignment assignSampleToRack(Sample sample);
    }

    private final SampleSorterService sampleSorterService;

    @PostMapping("/assignments")
    @ResponseStatus(code = HttpStatus.CREATED)
    AssignmentDTO assignSampleToRack(@RequestBody final SampleDTO sampleDTO) {
        log.info("Received request to assign sample to rack [sample id: {}]", sampleDTO.sampleId());
        final var assignment = sampleSorterService.assignSampleToRack(mapToSample(sampleDTO));
        return mapToAssignmentDTO(assignment);
    }

    private AssignmentDTO mapToAssignmentDTO(final Assignment assignment) {
        return new AssignmentDTO(assignment.sampleId().id(), assignment.rackId().id());
    }

    private Sample mapToSample(final SampleDTO sampleDTO) {
        final var sampleId = new SampleId(sampleDTO.sampleId());
        return new Sample(sampleId, new Patient(sampleDTO.patientAge(), sampleDTO.patientCompany(),
                sampleDTO.patientCityDistrict(), sampleDTO.patientVisionDefect()));
    }
}
