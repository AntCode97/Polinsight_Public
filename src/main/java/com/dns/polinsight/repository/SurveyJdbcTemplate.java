package com.dns.polinsight.repository;

import com.dns.polinsight.domain.dto.SurveyDto;
import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyJdbcTemplate {

  private final JdbcTemplate jdbcTemplate;

  static RowMapper<SurveyDto> surveyDtoRowMapper = (rs, rowNum) -> SurveyDto.builder()
                                                                            .surveyId(rs.getLong("surveyId"))
                                                                            .point(rs.getLong("point"))
                                                                            .count(rs.getLong("count"))
                                                                            .title(rs.getString("title"))
                                                                            .participateUrl(rs.getString("participate_url"))
                                                                            .createdAt(localDateParser(rs.getString("created_at")))
                                                                            .endAt(localDateParser(rs.getString("end_at")))
                                                                            .minimumTime(rs.getInt("minimum_time"))
                                                                            .progress(ProgressType.valueOf(rs.getString("progress")))
                                                                            .status(CollectorStatusType.valueOf(rs.getString("status")))
                                                                            .build();

  private static LocalDate localDateParser(String date) {
    if (date == null || date.isEmpty()) {
      return LocalDate.now();
    }
    return LocalDate.parse(date);
  }

  public List<SurveyDto> findAllSurveyWithCollector(String orderType, int lim) {
    return jdbcTemplate.query("SELECT s.title AS title, s.point AS point, s.survey_id AS surveyid, s.progress AS progress, s.minimum_time AS minimumtime, s.created_at AS createdat, s.end_at  AS " +
            "endat, c.participate_url AS participateurl, s.question_count AS count, c.status AS status FROM survey s JOIN collector c ON s.survey_id = c.survey_id WHERE progress LIKE  ? LIMIT ?",
        surveyDtoRowMapper,
        orderType, lim);
  }

}
