CREATE TABLE IF NOT EXISTS events(
    stream_id VARCHAR(150) NOT NULL,
    sequence INTEGER NOT NULL,
    event_id UUID NOT NULL,
    event_type VARCHAR(35) NOT NULL,
    payload JSONB NOT NULL,
    correlation UUID NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY(stream_id, sequence)
);