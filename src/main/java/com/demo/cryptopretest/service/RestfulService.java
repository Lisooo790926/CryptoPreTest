package com.demo.cryptopretest.service;

import com.demo.cryptopretest.data.ApiResult;

public interface RestfulService {

    ApiResult getTrades(String instrumentName);

    ApiResult getCandleSticks(String instrumentName, String timeframe);

}
