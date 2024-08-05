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
                // Сортируем список цен
                List<Integer> sortedList = quickSort(integersPrice, 0, integersPrice.size() - 1);
                int listSize = sortedList.size() / 2;
                // Если количество элементов нечетное, медиана — это средний элемент
                if (sortedList.size() % 2 == 1) {
                    return (float) sortedList.get(listSize);
                } else {
                    // Если количество элементов четное, медиана — это среднее значение двух центральных элементов
                    int number1 = listSize;
                    int number2 = number1 - 1;
                    return (float) (sortedList.get(number1) + sortedList.get(number2)) / 2;
                }
            }
            case AVERAGE_PRICE -> {
                // Вычисляем среднюю цену
                int result = 0;
                for (Integer integerPrice : integersPrice) {
                    result += integerPrice;
                }
                return (float) result / integersPrice.size();
            }
            default -> throw new IllegalArgumentException("Unsupported price processing type");
        }
    }

    //Из каждого билета берём только цену для последующей обработки
    @Override
    public List<Integer> getEveryPrice(List<Ticket> tickets) {
        ArrayList<Integer> everyPriceList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            everyPriceList.add(ticket.getPrice());
        }
        return everyPriceList;
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

        int pivot = list.get(from); // Выбираем опорный элемент

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
        return leftIndex; // Возвращаем индекс, где элементы больше опорного должны начать
    }


    //Меняем значения двух элементов в списке.
    public void swap(List<Integer> integerList, int index1, int index2) {
        int temporary = integerList.get(index1);
        integerList.set(index1, integerList.get(index2));
        integerList.set(index2, temporary);
    }


    //Фильтрует билеты, чтобы найти те, которые либо из name1 в name2, либо наоборот.
    //То есть либо из "VVO" в "TLV" либо наоборот
    public List<Ticket> getTicketsListBetween(List<Ticket> jsonTickets, String name1, String name2) {
        return jsonTickets.stream()
                .filter(ticket ->
                        name1.equals(ticket.getOrigin()) && name2.equals(ticket.getDestination()) ||
                                name2.equals(ticket.getOrigin()) && name1.equals(ticket.getDestination()))
                .collect(Collectors.toList());
    }


}
