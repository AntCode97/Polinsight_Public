package com.dns.polinsight.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelUtil<T> {

  private int rowNum = 0;

  public void createExcelToResponse(List<T> data, String filename, HttpServletResponse response) throws IllegalAccessException {
    Workbook workbook = new HSSFWorkbook();
    Sheet sheet = workbook.createSheet("테스트 데이터");
    rowNum = 0;

    createExcel(sheet, data);

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", String.format("attachment;filename=%s.xlsx", filename));
  }


  private void createExcel(Sheet sheet, List<T> list) throws IllegalAccessException {
    if(list.size() == 0) return;

    Row row = sheet.createRow(rowNum++);
    int cellNum = 0;

    for (Field field : list.get(0).getClass().getDeclaredFields()) {
      Cell cell = row.createCell(cellNum++);
      cell.setCellValue(field.getName());
      System.out.println(field.getName().toString());
    }
    System.out.println("------------------------------------------------");
    for (T data : list) {
      row = sheet.createRow(rowNum++);
      cellNum = 0;

      for (Field field : data.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        Cell cell = row.createCell(cellNum++);
        cell.setCellValue(field.get(data).toString());
        System.out.println(field.get(data).toString());
      }
    }
  }

}
