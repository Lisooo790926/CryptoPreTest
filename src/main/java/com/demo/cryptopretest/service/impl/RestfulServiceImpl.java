package com.demo.cryptopretest.service.impl;

import com.demo.cryptopretest.data.ApiResponseData;
import com.demo.cryptopretest.data.ApiResult;
import com.demo.cryptopretest.service.RestfulService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestfulServiceImpl implements RestfulService {

    private static final String INSTRUMENT_NAME = "instrument_name";
    private static final String TIME_FRAME = "timeframe";

    final RestTemplate restTemplate;

    @Value("${crypto.api.schema}")
    private String schema;
    @Value("${crypto.api.url}")
    private String apiUrl;
    @Value("${crypto.api.trade.url}")
    private String tradeApiUrl;
    @Value("${crypto.api.candlesticks.url}")
    private String candleStickUrl;

    @Override
    public ApiResult getTrades(String instrumentName) {

        Asserts.notBlank(instrumentName, "Instrument Name should not be empty");
        final URIBuilder builder = new URIBuilder();
        builder.setPath(tradeApiUrl)
                .addParameter(INSTRUMENT_NAME, instrumentName);
        try {
            return sendApiRequest(builder);
        } catch (URISyntaxException e) {
            log.error("Raise Error when sending instruement name {} to get trades result", instrumentName, e);
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiResult getCandleSticks(String instrumentName, String timeframe) {

        Asserts.notBlank(instrumentName, "Instrument Name should not be empty");
        Asserts.notBlank(timeframe, "Time frame should not be empty");

        final URIBuilder builder = new URIBuilder();
        builder.setPath(candleStickUrl)
                .addParameter(INSTRUMENT_NAME, instrumentName)
                .addParameter(TIME_FRAME, timeframe);
        try {
            return sendApiRequest(builder);
        } catch (URISyntaxException e) {
            log.error("Raise Error when sending instruement name {} and timeframe {} to get candlesticks result",
                    instrumentName, timeframe, e);
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ApiResult sendApiRequest(final URIBuilder builder) throws URISyntaxException {
        final ApiResponseData responseData = getRestTemplate().getForObject(
                builder.setHost(apiUrl).setScheme(schema).build(), ApiResponseData.class);
        return Objects.nonNull(responseData) ? responseData.getResult() : null;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
