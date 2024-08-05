package org.ticket_processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.entity.Ticket;
import org.entity.Tickets;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonParsImpl implements JsonPars{
    @Override
    public List<Ticket> getJsonTickets(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        Tickets tickets = new Tickets();
        try {
            tickets = objectMapper.readValue(file, Tickets.class); //С помощью Jackson парсим jason обьёк в POJO
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return tickets.getTickets(); //Возвращаем Лист с нашими билетами для последующей обработки
    }
}
