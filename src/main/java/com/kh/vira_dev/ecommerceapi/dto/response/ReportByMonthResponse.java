package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportByMonthResponse {

    private List<Number> month = new ArrayList<>();

    private List<BigDecimal> revenue = new ArrayList<>();

    private List<Number> orders = new ArrayList<>();

}
