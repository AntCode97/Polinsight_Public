package com.dns.polinsight.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class ExcelUtil<T> {

  private int rowNum = 0;

  public void createExcelToResponse(List<T> data, String filename, HttpServletResponse response) throws IllegalAccessException, IOException {
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    SXSSFSheet sheet = workbook.createSheet("sheet1");
    rowNum = 0;
    log.warn("start download excel");
    log.warn(String.valueOf(data.size()));

    createExcel(sheet, data);

    log.warn("last row: " + sheet.getLastRowNum());
    //    for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
    //      HSSFRow row = sheet.getRow(i);
    //      if (row != null) {
    //        for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
    //          HSSFCell cell = row.getCell(j);
    //          if (cell == null)
    //            continue;
    //          else
    //            System.out.println(cell.getCellFormula());
    //        }
    //      }
    //    }

    //    response.setHeader("Set-Cookie", "fileDownload=true; path=/");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
    ServletOutputStream out = response.getOutputStream();
    workbook.write(out);
    out.flush();
    out.close();
  }


  private void createExcel(SXSSFSheet sheet, List<T> list) throws IllegalAccessException {
    if (list.size() == 0)
      return;

    SXSSFRow row = sheet.createRow(rowNum++);
    int cellNum = 0;
    System.out.println("=======================");
    System.out.println(list);
    System.out.println("=======================");
    for (Field field : list.get(0).getClass().getDeclaredFields()) {
      field.trySetAccessible();
      Cell cell = row.createCell(cellNum++);
      cell.setCellValue(field.getName());
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
