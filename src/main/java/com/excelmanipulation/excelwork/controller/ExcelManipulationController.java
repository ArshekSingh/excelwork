package com.excelmanipulation.excelwork.controller;

import com.excelmanipulation.excelwork.response.Response;
import com.excelmanipulation.excelwork.service.ExcelManipulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ExcelManipulationController {

    @Autowired
    private ExcelManipulationService excelManipulationService;

    @PostMapping("/excelManipulation")
    public Response excelManipulation() {
        log.info("Request initiated for excelManipulation");
        return excelManipulationService.excelManipulation();
    }
}
