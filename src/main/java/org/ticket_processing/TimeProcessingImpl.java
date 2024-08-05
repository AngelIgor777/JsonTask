package org.ticket_processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.entity.Ticket;
import org.entity.Tickets;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeProcessingImpl implements TimeProcessing {


    @Override
    public LocalTime checkTimeBetween(Ticket ticket) {
        LocalDateTime arrivalDateTime = fromStringToLocalDateTime(ticket.getArrival_date(), ticket.getArrival_time());
        LocalDateTime departureDateTime = fromStringToLocalDateTime(ticket.getDeparture_date(), ticket.getDeparture_time());

        Duration between = Duration.between(departureDateTime, arrivalDateTime);
        long hours = between.toHours();
        long minutes = between.toMinutes() % 60;
        return LocalTime.of((int) hours, (int) minutes);
    }


    @Override
    public LocalDateTime fromStringToLocalDateTime(String localDateType, String LocalTimeType) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder().appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).toFormatter();

        LocalDate localDate = LocalDate.parse(localDateType, dateFormatter);
        LocalTime localTime = LocalTime.parse(LocalTimeType, timeFormatter);

        return LocalDateTime.of(localDate, localTime);
    }

    @Override
    public Map<String, LocalTime> minTimeFlyPerCarrier(List<Ticket> jsonTickets) {
        Map<String, LocalTime> minTimes = new HashMap<>();
        for (Ticket ticket : jsonTickets) {
            if ("VVO".equals(ticket.getOrigin()) && "TLV".equals(ticket.getDestination()) ||
                    ("TLV".equals(ticket.getOrigin()) && "VVO".equals(ticket.getDestination()))) {

                LocalTime flightTime = checkTimeBetween(ticket);
                String carrier = ticket.getCarrier();
                minTimes.merge(carrier, flightTime, (existingTime, newTime) -> existingTime.isBefore(newTime) ? existingTime : newTime);
            }
        }
        return minTimes;
    }
}
