package org.ticket_processing;

import org.entity.Ticket;
import org.entity.TypesOfPriceProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PriceProcessingImpl implements PriceProcessing {
    @Override
    public float getResultOf(TypesOfPriceProcessing typesOfPriceProcessing, List<Integer> integersPrice) {
        if (integersPrice.isEmpty()) {
            throw new IllegalArgumentException("Price list is empty");
        }

        switch (typesOfPriceProcessing) {
            case MEDIAN -> {
                List<Integer> sortedList = quickSort(integersPrice, 0, integersPrice.size() - 1);
                int size = sortedList.size();
                if (size % 2 == 1) {
                    // Непарное количество элементов, медиана — это средний элемент
                    return sortedList.get(size / 2);
                } else {
                    // Парное количество элементов, медиана — это среднее значение двух центральных элементов
                    int midIndex1 = size / 2;
                    int midIndex2 = midIndex1 - 1;
                    return (sortedList.get(midIndex1) + sortedList.get(midIndex2)) / 2.0f;
                }
            }
            case AVERAGE_PRICE -> {
                // Вычисляем среднюю цену
                return (float) integersPrice.stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElseThrow(() -> new IllegalStateException("Unexpected empty list"));
            }
            default -> throw new IllegalArgumentException("Unsupported price processing type");
        }
    }


    //Из каждого билета берём только цену для последующей обработки
    @Override
    public List<Integer> getEveryPrice(List<Ticket> tickets) {
        return tickets.stream()
                .map(Ticket::getPrice)
                .collect(Collectors.toList());
    }

    public List<Integer> quickSort(List<Integer> integers, int from, int to) {
        if (from < to) {
            int divideIndex = partition(integers, from, to);

            // Рекурсивно сортируем подсписки
            quickSort(integers, from, divideIndex - 1);
            quickSort(integers, divideIndex + 1, to);
        }
        return integers;
    }

    public int partition(List<Integer> list, int from, int to) {
        int leftIndex = from;
        int rightIndex = to;

        // Выбираем опорный элемент (первый элемент подмассива)
        int pivot = list.get(from);

        while (leftIndex <= rightIndex) {
            // Двигаем левый указатель вправо, пока не найдем элемент больше или равный опорному
            while (leftIndex <= rightIndex && list.get(leftIndex) < pivot) {
                leftIndex++;
            }
            // Двигаем правый указатель влево, пока не найдем элемент меньше или равный опорному
            while (leftIndex <= rightIndex && list.get(rightIndex) > pivot) {
                rightIndex--;
            }
            // Если указатели не пересеклись, меняем элементы местами
            if (leftIndex <= rightIndex) {
                swap(list, leftIndex, rightIndex);
                leftIndex++;
                rightIndex--;
            }
        }
        // Возвращаем индекс, где элементы больше опорного должны начать
        return leftIndex;
    }

    // Меняем значения двух элементов в списке.
    public void swap(List<Integer> integerList, int index1, int index2) {
        int temporary = integerList.get(index1);
        integerList.set(index1, integerList.get(index2));
        integerList.set(index2, temporary);
    }


    //Фильтрует билеты, чтобы найти те, которые либо из name1 в name2, либо наоборот.
    //То есть либо из "VVO" в "TLV" либо наоборот
    @Override
    public List<Ticket> getTicketsListBetween(List<Ticket> jsonTickets, String name1, String name2) {
        return jsonTickets.stream()
                .filter(ticket ->
                        (name1.equals(ticket.getOrigin()) && name2.equals(ticket.getDestination())) ||
                                (name2.equals(ticket.getOrigin()) && name1.equals(ticket.getDestination())))
                .collect(Collectors.toList());
    }


}
