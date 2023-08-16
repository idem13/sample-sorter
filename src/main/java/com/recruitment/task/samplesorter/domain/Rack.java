package com.recruitment.task.samplesorter.domain;

import com.recruitment.task.samplesorter.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Rack {

    private final RackId id;
    private final int capacity;
    private final Set<Sample> samples = new HashSet<>();
    private int slotsOccupied = 0;

    public void addSample(final Sample sample) {
        if (samples.contains(sample)) {
            throw new BusinessException("Sample already added [sample id: %s]".formatted(sample.id()));
        }
        if (isFull()) {
            throw new BusinessException("Rack is full");
        }
        samples.add(sample);
        ++slotsOccupied;
    }

    public boolean isFull() {
        return slotsOccupied == capacity;
    }
}
