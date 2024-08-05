package org.ticket_processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.entity.Ticket;
import org.entity.Tickets;

import java.io.File;
import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface JsonPars {
    public List<Ticket> getJsonTickets(File file);
}
