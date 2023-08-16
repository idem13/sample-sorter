package com.recruitment.task.samplesorter.persistance;

import com.recruitment.task.samplesorter.domain.Assignment;
import com.recruitment.task.samplesorter.domain.Rack;
import com.recruitment.task.samplesorter.domain.RackId;
import com.recruitment.task.samplesorter.exception.BusinessException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Slf4j
public class RacksRepository {

    private final Set<Rack> racks = new LinkedHashSet<>();

    public RacksRepository() {
        racks.add(new Rack(new RackId(1)));
        racks.add(new Rack(new RackId(2)));
    }

    public void persist(@NonNull Assignment assignment) {
        log.debug("Persisting assignment [sample: {}, rackId: {}]", assignment.sample().id(), assignment.rackId());
        racks.stream()
                .filter(rack -> rack.getId() == assignment.rackId())
                .findFirst()
                .orElseThrow(() -> new BusinessException("Rack does not exist"))
                .addSample(assignment.sample());
    }

    public Set<Rack> findAll() {
        return racks;
    }
}
