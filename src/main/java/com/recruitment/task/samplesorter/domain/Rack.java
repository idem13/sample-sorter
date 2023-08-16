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

    private static final int CAPACITY = 2;

    private final RackId id;
    private int slotsOccupied;
    private final Set<Sample> samples = new HashSet<>(CAPACITY);

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
        return slotsOccupied == CAPACITY;
    }
}
