package com.demo.cryptopretest.service;

import com.demo.cryptopretest.data.ApiResult;
import com.demo.cryptopretest.service.impl.VerifyServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestVerifyServiceImpl {

    private ApiResult tradeData;
    private ApiResult candleStickDatas;

    @MockBean
    private RestfulService restfulService;

    private VerifyServiceImpl verifyServiceImpl;

    @Before
    public void setupEvery() {

        verifyServiceImpl = new VerifyServiceImpl(restfulService);

        tradeData = new ApiResult();
        candleStickDatas = new ApiResult();

        when(restfulService.getTrades(anyString())).thenReturn(tradeData);
        when(restfulService.getCandleSticks(anyString(), anyString())).thenReturn(candleStickDatas);
    }

    ///////////////////////////////////////////////////////////////////////////
    //////////////////////// BEHAVIOR UNIT TEST ///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void testIsTradeAndCandleSticksConsistency_one_trade_5m_with_one_candle() {

        tradeData.setData(List.of(
                Map.of("dataTime", "1573343800000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46641.72",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                )
        ));
        candleStickDatas.setData(List.of(
                Map.of("t", "1573344000000",
                        "o", "46641.72",
                        "h", "46641.72",
                        "l", "46641.72",
                        "c", "46641.72"
                )
        ));

        List<Map<Long, Boolean>> result = verifyServiceImpl.
                isTradeAndCandleSticksConsistency("test", "5m");

        long time = 1573344000000L;
        result.forEach(map -> {
            Assert.assertTrue(map.get(time));
        });

    }

    @Test
    public void testIsTradeAndCandleSticksConsistency_two_trades_5m_with_one_candle() {

        tradeData.setData(List.of(
                Map.of("dataTime", "1573343900000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46650.00",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                ),
                Map.of("dataTime", "1573343800000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46641.72",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                )
        ));
        candleStickDatas.setData(List.of(
                Map.of("t", "1573344000000",
                        "o", "46641.72",
                        "h", "46650.00",
                        "l", "46641.72",
                        "c", "46650.00"
                )
        ));

        List<Map<Long, Boolean>> result = verifyServiceImpl.
                isTradeAndCandleSticksConsistency("test", "5m");

        long time = 1573344000000L;
        result.forEach(map -> {
            Assert.assertTrue(map.get(time));
        });

    }

    @Test
    public void testIsTradeAndCandleSticksConsistency_multi_trades_5m_with_one_candle() {

        tradeData.setData(List.of(
                Map.of("dataTime", "1573343960000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46651.00",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                ),
                Map.of("dataTime", "1573343950000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46653.00",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                ),
                Map.of("dataTime", "1573343900000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46640.00",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                ),
                Map.of("dataTime", "1573343800000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46641.72",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                )
        ));
        candleStickDatas.setData(List.of(
                Map.of("t", "1573344000000",
                        "o", "46641.72",
                        "h", "46653.00",
                        "l", "46640.00",
                        "c", "46651.00"
                )
        ));

        List<Map<Long, Boolean>> result = verifyServiceImpl.
                isTradeAndCandleSticksConsistency("test", "5m");

        long time = 1573344000000L;
        result.forEach(map -> {
            Assert.assertTrue(map.get(time));
        });

    }

    @Test
    public void testIsTradeAndCandleSticksConsistency_multi_trades_5m_with_multicandles() {

        tradeData.setData(List.of(
                Map.of("dataTime", "1573344200000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46651.00",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                ),
                Map.of("dataTime", "1573344100000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46653.00",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                ),
                Map.of("dataTime", "1573343900000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46640.00",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                ),
                Map.of("dataTime", "1573343800000",
                        "d", "2384384992617380192",
                        "s", "SELL",
                        "p", "46641.72",
                        "q", "0.000174",
                        "t", "1648897010246",
                        "i", "BTC_USDT"
                )
        ));
        candleStickDatas.setData(List.of(
                Map.of("t", "1573344000000",
                        "o", "46641.72",
                        "h", "46641.72",
                        "l", "46640.00",
                        "c", "46640.00"
                ),
                Map.of("t", "1573344300000",
                        "o", "46653.00",
                        "h", "46653.00",
                        "l", "46651.00",
                        "c", "46651.00"
                )
        ));

        List<Map<Long, Boolean>> result = verifyServiceImpl.
                isTradeAndCandleSticksConsistency("test", "5m");

        long timeSlot1 = 1573344300000L;
        long timeSlot2 = 1573344000000L;
        Assert.assertTrue(result.get(0).get(timeSlot1));
        Assert.assertTrue(result.get(1).get(timeSlot2));
    }

    ///////////////////////////////////////////////////////////////////////////
    //////////////////////// BASIC UNIT TEST //////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void testIsTradeAndCandleSticksConsistency_no_api_response() {

        when(restfulService.getTrades(anyString())).thenReturn(null);
        when(restfulService.getCandleSticks(anyString(), anyString())).thenReturn(null);
        verifyServiceImpl.isTradeAndCandleSticksConsistency("test", "5m");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTradeAndCandleSticksConsistency_candlesticks_data_is_null() {

        candleStickDatas.setData(null);
        verifyServiceImpl.isTradeAndCandleSticksConsistency("test", "5m");
    }

    @Test
    public void testIsTradeAndCandleSticksConsistency_candlesticks_data_is_empty() {

        candleStickDatas.setData(Collections.emptyList());
        List<Map<Long, Boolean>> result = verifyServiceImpl.
                isTradeAndCandleSticksConsistency("test", "5m");
        Assert.assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void testIsTradeAndCandleSticksConsistency_trades_data_is_null() {

        tradeData.setData(null);
        candleStickDatas.setData(List.of(
                Map.of("t", "1573344000000",
                        "o", "46641.72",
                        "h", "46641.72",
                        "l", "46641.72",
                        "c", "46641.72"
                )
        ));
        List<Map<Long, Boolean>> result = verifyServiceImpl.
                isTradeAndCandleSticksConsistency("test", "5m");

        long time = 1573344000000L;
        result.forEach(map -> {
            Assert.assertFalse(map.get(time));
        });
    }

    @Test
    public void testIsTradeAndCandleSticksConsistency_trades_data_is_empty() {

        tradeData.setData(Collections.emptyList());
        candleStickDatas.setData(List.of(
                Map.of("t", "1573344000000",
                        "o", "46641.72",
                        "h", "46641.72",
                        "l", "46641.72",
                        "c", "46641.72"
                )
        ));
        List<Map<Long, Boolean>> result = verifyServiceImpl.
                isTradeAndCandleSticksConsistency("test", "5m");

        long time = 1573344000000L;
        result.forEach(map -> {
            Assert.assertFalse(map.get(time));
        });
    }


}
