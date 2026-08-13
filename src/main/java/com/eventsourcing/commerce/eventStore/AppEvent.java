package com.eventsourcing.commerce.eventStore;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppEvent {

    @EmbeddedId
    private StreamSequenceId streamSequenceId;
    private UUID eventId;
    private UUID correlation;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String payload;
    @CreationTimestamp
    private Instant timestamp;

    private AppEvent(StreamSequenceId streamSequenceId, UUID eventId, UUID correlation, EventType eventType, String payload){
        this.streamSequenceId = streamSequenceId;
        this.eventId = eventId;
        this.correlation = correlation;
        this.eventType = eventType;
        this.payload = payload;
    }

    public static AppEvent register(StreamSequenceId streamSequenceId, UUID eventId, UUID correlation, EventType eventType, String payload){
        Objects.requireNonNull(streamSequenceId, "null reference on StreamSequenceId");
        Objects.requireNonNull(eventId, "null reference on UUID eventId");
        Objects.requireNonNull(correlation, "null reference on UUID correlation");
        Objects.requireNonNull(payload, "Payload cannot be null");
        Objects.requireNonNull(eventType, "Invalid EventType");
        return new AppEvent(streamSequenceId, eventId, correlation, eventType, payload);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AppEvent appEvent = (AppEvent) o;
        return Objects.equals(streamSequenceId, appEvent.streamSequenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(streamSequenceId);
    }
}
