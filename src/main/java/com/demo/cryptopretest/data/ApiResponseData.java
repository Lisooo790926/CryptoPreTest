package com.demo.cryptopretest.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseData {

    String code;
    String method;
    ApiResult result;
}
