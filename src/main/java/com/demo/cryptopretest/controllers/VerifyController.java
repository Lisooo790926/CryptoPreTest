package com.demo.cryptopretest.controllers;

import com.demo.cryptopretest.service.VerifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crypto")
public class VerifyController {

    @Resource
    private VerifyService verifyService;

    @GetMapping("/verify")
    public List<Map<Long, Boolean>> isTradeAndCandleSticksConsistency(@RequestParam final String instrumentName,
                                                                      @RequestParam final String timeframe) {
        return verifyService.isTradeAndCandleSticksConsistency(instrumentName, timeframe);
    }

}
