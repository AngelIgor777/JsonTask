package org.ticket_processing;

import org.entity.Ticket;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface TimeProcessing {
    public LocalTime checkTimeBetween(Ticket ticket);
    public LocalDateTime fromStringToLocalDateTime(String localDateType, String LocalTimeType);
    public Map<String, LocalTime> minTimeFlyPerCarrier(List<Ticket> jsonTickets);
}
