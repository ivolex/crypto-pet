package org.pet.crypto.service;

import lombok.extern.slf4j.Slf4j;
import org.pet.crypto.model.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class PricesHandler {

    private static final String COMMA_DELIMITER = ",";
    private static final String LOW_DASH = "_";
    private static final String DAY_START = "00:00:00";
    private static final String DAY_END = "23:59:59";

    @Value("${prises.path:./prices}")
    private String path;

    public List<Crypto> pricesFor(String symbol) {
        File text = Stream.of(Objects.requireNonNull(new File(path).listFiles()))
                .filter(file -> file.getName().contains(symbol))
                .findFirst()
                .orElse(null);

        if (null != text) {
            try {
                return Files.readAllLines(text.toPath())
                        .stream()
                        .skip(1)
                        .map(line -> {
                            String[] elements = line.split(COMMA_DELIMITER);
                            return new Crypto(Long.parseLong(elements[0]),
                                    elements[1],
                                    Double.parseDouble(elements[2]));
                        })
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file - " + text.getName());
            }
        }
        throw new RuntimeException("Cannot collect values for symbol - " + symbol);
    }

    protected Map<String, List<Crypto>> findAll() {
        return Stream.of(Objects.requireNonNull(new File(path).listFiles()))
                .filter(file -> !file.isDirectory())
                .collect(Collectors.toMap(this::symbolOfFile, file -> pricesFor(symbolOfFile(file))));
    }

    public Crypto findOldestFor(String symbol) {
        return pricesFor(symbol).stream()
                .min(Comparator.comparing(Crypto::timestamp)).orElse(null);
    }

    public Crypto findNewestFor(String symbol) {
        return pricesFor(symbol).stream()
                .max(Comparator.comparing(Crypto::timestamp)).orElse(null);
    }

    public Crypto findMinPriceFor(String symbol) {
        return pricesFor(symbol).stream()
                .min(Comparator.comparing(Crypto::price)).orElse(null);
    }

    public Crypto findMaxPriceFor(String symbol) {
        return pricesFor(symbol).stream()
                .max(Comparator.comparing(Crypto::price)).orElse(null);
    }

    public Map<String, Double> descendingByRange() {
        return sortByValueDescending(calculateRanges(findAll()));
    }

    public Map.Entry<String, Double> highestRangeForDate(String date) {
        Map<String, List<Crypto>> cryptosByDate = findAllByDate(date);
        Map<String, Double> calculatedRanges = calculateRanges(cryptosByDate);

        Optional<Map.Entry<String, Double>> maxEntry = calculatedRanges.entrySet()
                .stream().max(Map.Entry.comparingByValue());

        return maxEntry.orElse(null);
    }

    private Map<String, Double> calculateRanges(Map<String, List<Crypto>> input) {
        Map<String, Double> nonSorted = new HashMap<>();

        for (String symbol : input.keySet()) {
            Optional<Crypto> minPriceCrypto = input.get(symbol).stream()
                    .min(Comparator.comparing(Crypto::price));
            Optional<Crypto> maxPriceCrypto  = input.get(symbol).stream()
                    .max(Comparator.comparing(Crypto::price));
            if (minPriceCrypto.isPresent() && maxPriceCrypto.isPresent()){
                double min = minPriceCrypto.get().price();
                double max = maxPriceCrypto.get().price();
                double range = (max - min) / min;
                nonSorted.put(symbol, range);
            }
        }

        return nonSorted;
    }

    private Map<String, List<Crypto>> findAllByDate(String date) {
        String startTime = date + " " + DAY_START;
        String endTime = date + " " + DAY_END;

        Instant startInst = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant endInst = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant();
        long startTimestamp = startInst.toEpochMilli();
        long endTimestamp = endInst.toEpochMilli();

        Map<String, List<Crypto>> result = new HashMap<>();
        Map<String, List<Crypto>> allCryptos = findAll();
        for (String symbol : allCryptos.keySet()) {
            List<Crypto> cryptosForDate = allCryptos.get(symbol).stream()
                    .filter(crypto -> crypto.timestamp() > startTimestamp && crypto.timestamp() < endTimestamp)
                    .toList();
            result.put(symbol, cryptosForDate);
        }
        return result;
    }

    private String symbolOfFile(File file) {
        String[] split = file.getName().split(LOW_DASH);
        return split[0];
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<K, V>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
