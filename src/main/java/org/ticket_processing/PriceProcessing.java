package org.ticket_processing;

import org.entity.Ticket;
import org.entity.TypesOfPriceProcessing;

import java.util.List;

public interface PriceProcessing {
    public float getResultOf(TypesOfPriceProcessing typesOfPriceProcessing, List<Integer> integers);
    public List<Integer> getEveryPrice(List<Ticket> tickets);
    public List<Ticket> getTicketsListBetween(List<Ticket> jsonTickets, String name1, String name2);


}
