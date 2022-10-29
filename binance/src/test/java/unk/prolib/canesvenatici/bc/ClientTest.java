package unk.prolib.canesvenatici.bc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.WebsocketClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import unk.prolib.canesvenatici.bc.api.BCExchangeInfo;

class ClientTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    void test() throws Exception {
//        SpotClientImpl client = new SpotClientImpl();
//        String result = client.createMarket().exchangeInfo(new LinkedHashMap<>());
//        BCExchangeInfo info = objectMapper.readValue(result, BCExchangeInfo.class);
//        System.err.println("WWBEG-XX info=" + info.getSymbols().size());
//        var list = info.getSymbols().stream().map(x -> x.getSymbol()).collect(Collectors.toList());
//        System.err.println("WWBEG-XX list=" + list.subList(5, 100));
        WebsocketClientImpl client = new WebsocketClientImpl();
        int streamID1 = client.aggTradeStream("btcusdt",((event) -> {
            System.out.println(event);
        }));
        Thread.sleep(10000L);
        client.closeAllConnections();
    }

}
