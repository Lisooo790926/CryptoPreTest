package com.demo.cryptopretest.service.impl;

import com.demo.cryptopretest.data.ApiResult;
import com.demo.cryptopretest.service.RestfulService;
import com.demo.cryptopretest.service.VerifyService;
import com.demo.cryptopretest.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {

    private static final int FIRST = 0;
    private static final int LAST = 1;
    private static final int HIGHEST = 2;
    private static final int LOWEST = 3;

    private final RestfulService restfulService;

    @Override
    public List<Map<Long, Boolean>> isTradeAndCandleSticksConsistency(final String instrumentName, final String timeframe) {

        final ApiResult candleSticks = getRestfulService().getCandleSticks(instrumentName, timeframe);
        final ApiResult trades = getRestfulService().getTrades(instrumentName);

        Assert.notNull(candleSticks, "candleSticks api response could not be null");
        Assert.notNull(trades, "trades api response could not be null");

        int index = 0;
        final List<Map<String, String>> tradeDatas = trades.getData();
        final List<Map<String, String>> candleStickDatas = candleSticks.getData();

        Assert.notNull(candleStickDatas, "trades data could not be null");

        // temporary result for transferring from trade to stick
        double[] tradeStick = new double[4];

        List<Map<Long, Boolean>> result = new ArrayList<>();

        for (int i = candleStickDatas.size() - 1; i >= 0; i--) {

            if (index == -1) break;

            final Map<String, String> candleStick = candleStickDatas.get(i);

            initTradeStick(tradeStick);

            // get current candle stick time and last candle stick time
            Date candleTime = new Time(Long.parseLong(candleStick.get("t")));
            Date lastTime = TimeUtils.getLastTimeSlot(timeframe, candleTime);

            // return current moving index (for next start point)
            index = fillTradeStick(index, tradeDatas, candleTime, lastTime, tradeStick);

            // verify tradeStick by candleStick
            result.add(Map.of(candleTime.getTime(), isValidTradeStick(tradeStick, candleStick)));
        }

        return result;
    }

    private boolean isValidTradeStick(double[] tradeStick, Map<String, String> candleStick) {

        double realFirst = getValue(candleStick, "o");
        double realLast = getValue(candleStick, "c");
        double realHigh = getValue(candleStick, "h");
        double realLow = getValue(candleStick, "l");

        return realFirst == tradeStick[FIRST] && realLast == tradeStick[LAST] &&
                realHigh == tradeStick[HIGHEST] && realLow == tradeStick[LOWEST];
    }

    private void initTradeStick(double[] tradeStick) {
        tradeStick[FIRST] = 0.0;
        // last one will always be first one
        tradeStick[LAST] = 0.0;
        tradeStick[HIGHEST] = Double.MIN_VALUE;
        tradeStick[LOWEST] = Double.MAX_VALUE;
    }

    /**
     * fill the trade stick result into double array
     *
     * @return the next starting trade data index
     */
    private int fillTradeStick(int index, final List<Map<String, String>> tradeDatas,
                               final Date candleTime, final Date lastTime,
                               final double[] tradeStick) {

        if (tradeDatas == null || index >= tradeDatas.size()) return -1;

        Map<String, String> tradeData = tradeDatas.get(index);
        while (true) {

            Date curr = new Time(Long.parseLong(tradeData.get("dataTime")));
            if (curr.before(lastTime)) return index;

            if (curr.before(candleTime) && curr.after(lastTime)) {
                // keep updating the first until current time is not available
                tradeStick[FIRST] = getValue(tradeData, "p");
                // last one will always be first one
                tradeStick[LAST] = tradeStick[LAST] == 0.0 ? getValue(tradeData, "p") : tradeStick[LAST];
                tradeStick[HIGHEST] = Math.max(getValue(tradeData, "p"), tradeStick[HIGHEST]);
                tradeStick[LOWEST] = Math.min(getValue(tradeData, "p"), tradeStick[LOWEST]);
            }

            if (index + 1 >= tradeDatas.size()) return -1;
            tradeData = tradeDatas.get(++index);
        }
    }

    private Double getValue(final Map<String, String> map, final String key) {
        return Double.parseDouble(map.get(key));
    }

    public RestfulService getRestfulService() {
        return restfulService;
    }
}
