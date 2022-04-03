package com.demo.cryptopretest.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult {

    String instrument_name;
    Integer depth;
    String interval;
    List<Map<String, String>> data;
}
