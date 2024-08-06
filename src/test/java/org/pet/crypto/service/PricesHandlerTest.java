package org.pet.crypto.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pet.crypto.model.Crypto;
import org.pet.crypto.model.SymbolRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = { "prises.path=./src/test/resources/prices" })
class PricesHandlerTest {

    @Autowired
    private PricesHandler handler;

    @Test
    @DisplayName("Counts existing values files sizes.")
    void pricesFor() {
        assertEquals(100, handler.pricesFor("BTC").size());
        assertEquals(90, handler.pricesFor("DOGE").size());
        assertEquals(95, handler.pricesFor("ETH").size());
        assertEquals(85, handler.pricesFor("LTC").size());
        assertEquals(80, handler.pricesFor("XRP").size());
    }

    @Test
    @DisplayName("Counts all cryptos map size.")
    void canCreateAllCryptosMap() {
        Map<String, List<Crypto>> allCryptos = handler.findAll();

        Set<String> symbols = allCryptos.keySet();

        assertEquals(5, symbols.size());
        assertTrue(symbols.containsAll(Arrays.asList("BTC", "DOGE", "ETH", "LTC", "XRP")));

        for (String symbol : symbols) {
            assertNotNull(allCryptos.get(symbol));
            assertFalse(allCryptos.get(symbol).isEmpty());
        }
    }

    @Test
    @DisplayName("Should find oldest record for symbol specified.")
    void canFindOldestRecord() {
        long expected = 1641009600000L;

        Crypto record = handler.findOldestFor("BTC");

        assertEquals(expected, record.timestamp());
    }

    @Test
    @DisplayName("Should throw exception for non existing symbol specified.")
    void throwExceptionWhenFindOldestRecord() {
        String fakeSymbol = "BTa";

        String expectedMessage = "Cannot collect values for symbol - " + fakeSymbol;
        Exception expected = assertThrows(RuntimeException.class, () -> handler.findOldestFor(fakeSymbol));

        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    @DisplayName("Should find newest record for symbol specified.")
    void canFindNewestRecord() {
        long expected = 1643659200000L;

        Crypto record = handler.findNewestFor("BTC");

        assertEquals(expected, record.timestamp());
    }

    @Test
    @DisplayName("Should throw exception for non existing symbol specified.")
    void throwExceptionWhenFindNewestRecord() {
        String fakeSymbol = "BTa";

        String expectedMessage = "Cannot collect values for symbol - " + fakeSymbol;
        Exception expected = assertThrows(RuntimeException.class, () -> handler.findNewestFor(fakeSymbol));

        assertEquals(expectedMessage, expected.getMessage());
    }

    @Test
    @DisplayName("Should find min price for symbol specified.")
    void returnsMinPriceForSymbol() {
        Crypto crypto = handler.findMinPriceFor("BTC");

        assertEquals(33276.59, crypto.price());
    }

    @Test
    @DisplayName("Should find max price for symbol specified.")
    void returnsMaxPriceForSymbol() {
        Crypto crypto = handler.findMaxPriceFor("BTC");

        assertEquals(47722.66, crypto.price());
    }

    @Test
    @DisplayName("Should return descending sorted map.")
    void returnsCryptosDescendingByRange() {
        Map<String, Double> sorted = handler.descendingByRange();

        assertEquals(5, sorted.size());
        final Iterator<Double> iterator = sorted.values().iterator();
        assertEquals(0.6383810110763016, (double) iterator.next());
        assertEquals(0.5060541310541311, (double) iterator.next());
        assertEquals(0.5046511627906975, (double) iterator.next());
        assertEquals(0.4651837524177949, (double) iterator.next());
        assertEquals(0.43412110435594536, (double) iterator.next());
    }

    @Test
    @DisplayName("Should return highest normalized range for a specific day.")
    void canFindCryptoByHighestRangeForDate() {

        SymbolRange result = handler.highestRangeForDate("2022-01-01");

        assertEquals("XRP", result.symbol());
        assertEquals(0.019281754639672227, result.range());
    }

    @Test
    @DisplayName("Should return null for non existing day.")
    void returnsNullForNonPopulatedDate() {

        SymbolRange result = handler.highestRangeForDate("2023-01-01");

        assertNull(result);
    }
}