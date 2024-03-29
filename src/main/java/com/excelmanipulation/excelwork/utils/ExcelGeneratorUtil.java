package com.excelmanipulation.excelwork.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Component
@Slf4j
public class ExcelGeneratorUtil {

    public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) {
        String sheetName = (String) model.get("Excel Name");
        List<String> headers = (List<String>) model.get("HEADERS");
        List<List<String>> results = (List<List<String>>) model.get("RESULTS");
        List<String> numericColumns = new ArrayList<>();
        if (model.containsKey("numericcolumns")) numericColumns = (List<String>) model.get("numericcolumns");
        HSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(15);
        int currentRow = 0;
        short currentColumn = 0;
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        HSSFRow headerRow = sheet.createRow(currentRow);
        for (String header : headers) {
            HSSFRichTextString text = new HSSFRichTextString(header);
            HSSFCell cell = headerRow.createCell(currentColumn);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(text);
            currentColumn++;
        }
        currentRow++;
        for (List<String> result : results) {
            currentColumn = 0;
            HSSFRow row = sheet.createRow(currentRow);
            for (String value : result) {
                HSSFCell cell = row.createCell(currentColumn);
                if (numericColumns.contains(headers.get(currentColumn))) {
                    cell.setCellValue(value);
                } else {
                    HSSFRichTextString text = new HSSFRichTextString(value);
                    cell.setCellValue(text);
                }
                currentColumn++;
            }
            currentRow++;
        }
    }

    /**
     * @param headerParam WILL BE HEADERNAME WITH COMMA SEPARATED
     * @param fileName    WILL BE THE NAME OF EXCEL FILE
     * @return
     */

    public Map<String, Object> populateHeaderAndName(String headerParam, String fileName) {
        Map<String, Object> map = new HashMap<>();
        map.put("Excel Name", fileName);
        map.put("HEADERS", Arrays.asList(headerParam.split(",")));
        return map;
    }

    public byte[] downloadDocument(HSSFWorkbook workbook) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();


        } catch (Exception exception) {
            log.error("Exception occurs while downloading Collection Detail Excel {}", exception.getMessage());
            return new byte[0];
        }
    }
}
