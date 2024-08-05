package org.practice;

import org.entity.Ticket;
import org.entity.TypesOfPriceProcessing;
import org.ticket_processing.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class CalculationResult {
    public static TimeProcessing timeProcessing = new TimeProcessingImpl();
    public static PriceProcessing priceProcessing = new PriceProcessingImpl();
    public static JsonPars jsonPars = new JsonParsImpl();

    public static void main(String[] args) throws IOException {
        File file = new File("src\\main\\java\\org\\resources\\tickets.json");
        List<Ticket> tickets = jsonPars.getJsonTickets(file);

        //Получаем Минимальное время полета между городами
        Map<String, LocalTime> minTimeForCarriers = timeProcessing.minTimeFlyPerCarrier(tickets);

        //Получаем Разницу между средней ценой  и медианой
        List<Integer> priceList = priceProcessing.getEveryPrice(tickets);
        float avgPrice = priceProcessing.getResultOf(TypesOfPriceProcessing.AVERAGE_PRICE, priceList);
        float medianPrice = priceProcessing.getResultOf(TypesOfPriceProcessing.MEDIAN, priceList);

        String result = String.format("Минимальное время полета между городами" +
                " Владивосток и Тель-Авив для каждого авиаперевозчика : %s.\n " +
                "Разницу между средней ценой  и медианой для полета между городами  " +
                "Владивосток и Тель-Авив: %.2f", minTimeForCarriers, avgPrice - medianPrice);
        System.out.println(result);
    }
}
