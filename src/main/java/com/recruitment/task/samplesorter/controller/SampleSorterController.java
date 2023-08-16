package com.recruitment.task.samplesorter.controller;

import com.recruitment.task.samplesorter.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SampleSorterController {

    public interface SampleSorterService {
        Assignment assignSampleToRack(Sample sample);

        Set<Rack> obtainRacks();
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
        return new AssignmentDTO(assignment.sample().id().toUUID(), assignment.rackId().toInt());
    }

    private Sample mapToSample(final SampleDTO sampleDTO) {
        final var sampleId = new SampleId(sampleDTO.sampleId());
        return new Sample(sampleId,
                Patient.builder()
                        .age(sampleDTO.patient().age())
                        .cityDistrict(sampleDTO.patient().cityDistrict())
                        .company(sampleDTO.patient().company())
                        .visionDefect(sampleDTO.patient().visionDefect())
                        .build());
    }

    @GetMapping("/assignments")
    @ResponseStatus(code = HttpStatus.OK)
    AssignmentsDTO obtainAssignments() {
        final var racks = sampleSorterService.obtainRacks();
        return mapToAssignmentsDTO(racks);
    }

    private AssignmentsDTO mapToAssignmentsDTO(final Set<Rack> racks) {
        return AssignmentsDTO.builder()
                .racks(racks.stream()
                        .map(this::mapToRackDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    private RackDTO mapToRackDTO(final Rack rack) {
        return RackDTO.builder()
                .rackId(rack.getId().toInt())
                .samples(rack.getSamples().stream()
                        .map(this::mapToSampleDTO)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    private SampleDTO mapToSampleDTO(final Sample sample) {
        return SampleDTO.builder()
                .sampleId(sample.id().toUUID())
                .patient(mapToPatientDTO(sample.patient()))
                .build();
    }

    private PatientDTO mapToPatientDTO(final Patient patient) {
        return PatientDTO.builder()
                .age(patient.age())
                .cityDistrict(patient.cityDistrict())
                .company(patient.company())
                .visionDefect(patient.visionDefect())
                .build();
    }
}
