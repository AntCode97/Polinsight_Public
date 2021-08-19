package com.dns.polinsight.utils;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelUtil<T> {

  private int rowNum = 0;

  public void createExcelToResponse(List<T> data, String filename, HttpServletResponse response) throws IllegalAccessException, IOException {
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("sheet 1");
    rowNum = 0;

    createExcel(sheet, data);

    response.setHeader("Set-Cookie", "fileDownload=true; path=/");
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", String.format("attachment;filename=%s.xls", filename));
    workbook.write(response.getOutputStream());
    response.getOutputStream().flush();
    response.getOutputStream().close();
  }


  private void createExcel(HSSFSheet sheet, List<T> list) throws IllegalAccessException {
    if (list.size() == 0)
      return;

    HSSFRow row = sheet.createRow(rowNum++);
    int cellNum = 0;

    for (Field field : list.get(0).getClass().getDeclaredFields()) {
      field.trySetAccessible();
      row.createCell(cellNum++).setCellValue(field.getName());
    }
    for (T data : list) {
      row = sheet.createRow(rowNum++);
      cellNum = 0;

      for (Field field : data.getClass().getDeclaredFields()) {
        field.trySetAccessible();
        row.createCell(cellNum++).setCellValue(field.get(data).toString());
      }
    }
  }

}
