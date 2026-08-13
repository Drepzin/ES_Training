package com.eventsourcing.commerce.eventStore.exception;

import org.springframework.http.ProblemDetail;

public class ConcurrencyWriteException extends RuntimeException {
    public ConcurrencyWriteException(String message) {
        super(message);
    }

    public ProblemDetail toProblemDetail(){
        var pb = ProblemDetail.forStatus(422);
        pb.setTitle("Concurrency error");
        pb.setDetail(getMessage());
        return pb;
    }
}
