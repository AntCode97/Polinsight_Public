package com.dns.polinsight.utils;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.UserRoleType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ExcelUtil {

  private static final String[] participateSurveyField = {"참여자", "설문 제목", "참여일", "포인트", "종료 여부"};

  private static final String[] userField = {"이메일", "이름", "연락처", "포인트", "추천인 연락처", "타입", "이메일 수신 여부", "SMS 수신 여부", "주소", "가입일", "성별", "직업", "직종", "학력", "결혼", "생일"};

  private static final String[] pointHistoryField = {"내용", "이메일", "이름", "변동 포인트", "전체 포인트", "요청일"};

  private static final String[] pointRequestField = {"이메일", "이름", "요청 포인트", "은행", "계좌번호", "요청일", "상태"};

  private int rowNum = 0, cellNum = 0;

  private void sendExcelToFE(HttpServletResponse response, String filename, SXSSFWorkbook workbook) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
    ServletOutputStream out = response.getOutputStream();
    workbook.write(out);
    out.flush();
    out.close();
  }

  public void createPointRequestExcel(List<PointRequest> list, HttpServletResponse response, String filename) throws IllegalAccessException, IOException {
    if (list.size() <= 0) {
      return;
    }

    SXSSFSheet sheet = makeSheet();
    rowNum = 0;
    SXSSFRow row = sheet.createRow(rowNum++);
    makeHeader(row, pointRequestField);

    for (PointRequest prq : list) {
      row = sheet.createRow(rowNum++);
      cellNum = 0;

      makeCell(row, prq.getEmail().toString());
      makeCell(row, prq.getName());
      makeCell(row, String.valueOf(prq.getRequestPoint()));
      makeCell(row, prq.getBank().name());
      makeCell(row, prq.getAccount());
      String[] tmp = prq.getRequestedAt().toString().split("T");
      makeCell(row, tmp[0] + " " + tmp[1]);
      makeCell(row, prq.getProgress().getName());
    }
    sendExcelToFE(response, filename, sheet.getWorkbook());
  }

  public void createUserExcel(List<User> list, HttpServletResponse response, String filename) throws IOException {

    if (list.size() <= 0) {
      return;
    }
    SXSSFSheet sheet = makeSheet();
    rowNum = 0;
    SXSSFRow row = sheet.createRow(rowNum++);
    makeHeader(row, userField);

    for (User user : list) {
      row = sheet.createRow(rowNum++);
      cellNum = 0;

      makeCell(row, user.getEmail().toString());
      makeCell(row, user.getName());
      makeCell(row, user.getPhone().toString());
      makeCell(row, String.valueOf(user.getPoint()));
      makeCell(row, user.getRecommend());
      makeCell(row, user.getRole().name());
      makeCell(row, user.getIsEmailReceive() ? "수신" : "수신안함");
      makeCell(row, user.getIsSmsReceive() ? "수신" : "수신안함");
      if (!user.getRole().equals(UserRoleType.USER)) {
        makeCell(row, user.getPanel().getAddress());
        makeCell(row, user.getRegisteredAt().toString());
        makeCell(row, String.valueOf(user.getPanel().getGender()).equals("MALE") ? "남성" : "여성");
        makeCell(row, user.getPanel().getJob());
        makeCell(row, user.getPanel().getIndustry());
        makeCell(row, user.getPanel().getEducation());
        makeCell(row, user.getPanel().getMarry());
        makeCell(row, user.getPanel().getBirth().toString() + " " + user.getPanel().getBirthType());
      }
    }
    sendExcelToFE(response, filename, sheet.getWorkbook());
  }

  public void createParticipateSurveyExcel(List<ParticipateSurvey> list, HttpServletResponse response, String filename) throws IOException {
    if (list.size() <= 0) {
      return;
    }
    SXSSFSheet sheet = makeSheet();
    rowNum = 0;
    SXSSFRow row = sheet.createRow(rowNum++);
    makeHeader(row, participateSurveyField);

    for (ParticipateSurvey ps : list) {
      row = sheet.createRow(rowNum++);
      cellNum = 0;

      makeCell(row, ps.getUser().getEmail().toString());
      makeCell(row, ps.getSurvey().getTitle());
      String[] tmp = ps.getParticipatedAt().toString().split("T");
      makeCell(row, tmp[0] + " " + tmp[1]);
      makeCell(row, String.valueOf(ps.getSurveyPoint()));
      makeCell(row, ps.getFinished() ? "완료" : "미완료");
    }
    sendExcelToFE(response, filename, sheet.getWorkbook());
  }

  public void createExcelPointHistory(List<PointHistory> list, HttpServletResponse response, String filename) throws IOException {
    // name, 내역 설명, 변동 (amount + sign), 총합, 요청 날짜
    if (list.size() <= 0) {
      return;
    }
    SXSSFSheet sheet = makeSheet();
    rowNum = 0;
    SXSSFRow row = sheet.createRow(rowNum++);
    makeHeader(row, pointHistoryField);

    for (PointHistory ph : list) {
      row = sheet.createRow(rowNum++);
      cellNum = 0;

      makeCell(row, ph.getContent());
      makeCell(row, ph.getUser().getEmail().toString());
      makeCell(row, ph.getUser().getName());
      makeCell(row, ph.getSign() ? "+" + ph.getAmount() : "-" + ph.getAmount());
      makeCell(row, String.valueOf(ph.getTotal()));
      String[] tmp = ph.getRequestedAt().toString().split("T");
      makeCell(row, tmp[0] + " " + tmp[1]);
    }

    sendExcelToFE(response, filename, sheet.getWorkbook());
  }

  private void makeHeader(SXSSFRow row, String[] header) {
    int cellNum = 0;
    for (String head : header) {
      Cell cell = row.createCell(cellNum++);
      cell.setCellValue(head);
    }
  }

  private void makeCell(SXSSFRow row, Object obj) {
    String data = obj == null ? "" : obj.toString();
    row.createCell(cellNum++).setCellValue(String.valueOf(data.toCharArray(), 0, Math.min(data.length(), 32767)));
  }

  private SXSSFSheet makeSheet() {
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    SXSSFSheet sheet = workbook.createSheet("sheet");
    rowNum = 0;
    return sheet;
  }

}
