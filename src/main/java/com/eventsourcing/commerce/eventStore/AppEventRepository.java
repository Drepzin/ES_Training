package com.eventsourcing.commerce.eventStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppEventRepository extends JpaRepository<AppEvent, StreamSequenceId> {

    boolean existsByEventId(UUID eventId);

    @Query("SELECT 1 FROM AppEvent e WHERE e.streamSequenceId.streamId = :streamId AND e.streamSequenceId.sequence = :sequence")
    boolean existsByStreamIdAndSequence(String streamId, Integer sequence);

    @Query("SELECT e FROM AppEvent e WHERE e.streamSequenceId.streamId = :streamId ORDER BY (e.streamSequenceId.sequence) ASC")
    List<AppEvent> findEventByStreamId(@Param("streamId") String streamId);

    @Query("SELECT MAX(e.streamSequenceId.sequence) FROM AppEvent e WHERE e.streamSequenceId.streamId = :streamId")
    Optional<Integer> findByLastSequence(@Param("streamId") String streamId);
}
