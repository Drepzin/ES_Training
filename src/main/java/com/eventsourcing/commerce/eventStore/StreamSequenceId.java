package com.eventsourcing.commerce.eventStore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StreamSequenceId implements Serializable {

    @Column(name = "stream_id")
    private String streamId;
    private int sequence;
}
