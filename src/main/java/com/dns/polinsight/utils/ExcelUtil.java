package com.dns.polinsight.utils;

import com.dns.polinsight.exception.InvalidValueException;
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

    log.info("start download excel");
    if (data.size() < 1) {
      throw new InvalidValueException("데이터가 존재하지 않습니다.");
    }
    createExcel(sheet, data);
    log.info("created row: " + sheet.getLastRowNum());

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
