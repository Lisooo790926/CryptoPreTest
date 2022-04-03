package com.demo.cryptopretest.service;

import java.util.List;
import java.util.Map;

public interface VerifyService {

    List<Map<Long, Boolean>> isTradeAndCandleSticksConsistency(final String instrumentName, final String timeframe);
}
