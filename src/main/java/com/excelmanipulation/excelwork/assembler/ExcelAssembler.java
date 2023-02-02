package com.excelmanipulation.excelwork.assembler;

import com.excelmanipulation.excelwork.entity.Customers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ExcelAssembler {
    public List<List<String>> prepareCollectionDetailData(List<Customers> customerList) {
        return customerList.stream().map(customers -> mapEntitytoList(customers)).collect(Collectors.toList());
    }

    private List<String> mapEntitytoList(Customers customers) {
        List<String> list = new ArrayList<>();
        list.add(customers.getId() != null ? String.valueOf(customers.getId()) : "");
        list.add(StringUtils.hasText(customers.getFirstName()) ? customers.getFirstName() : "");
        list.add(StringUtils.hasText(customers.getLastName()) ? customers.getLastName() : "");
        list.add(StringUtils.hasText(customers.getContactNo()) ? customers.getContactNo() :  "");
        list.add(StringUtils.hasText(customers.getCountry()) ? customers.getCountry() : "");
        list.add(StringUtils.hasText(customers.getDob()) ? customers.getDob() : "");
        list.add(StringUtils.hasText(customers.getGender()) ? customers.getGender() : "");
        list.add(StringUtils.hasText(customers.getEmail()) ? customers.getEmail() : "");
        return list;
    }
}
