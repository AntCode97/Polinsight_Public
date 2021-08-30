package com.dns.polinsight.repository;

import com.dns.polinsight.domain.dto.SurveyDto;
import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SurveyJdbcTemplate {

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

  private final JdbcTemplate jdbcTemplate;

  private static LocalDate localDateParser(String date) {
    if (date == null || date.isEmpty()) {
      return LocalDate.now();
    }
    return LocalDate.parse(date);
  }

  public Page<SurveyDto> findAllSurveysByProgressType(ProgressType progressType, Pageable pageable) {
    String sql = "SELECT s.id AS id, s.title AS title, IFNULL(s.point, 0) AS point, s.survey_id AS surveyid, s.progress AS progress, s.minimum_time AS minimumtime, IFNULL" +
        "(DATE_FORMAT(s.created_at, '%Y-%m-%d'),DATE_FORMAT(NOW(), '%Y-%m-%d')) AS createdat, IFNULL(DATE_FORMAT(s.end_at, '%Y-%m-%d'), DATE_FORMAT(NOW(), '%Y-%m-%d'))AS endat, c.participate_url AS participateurl, s.question_count AS count" +
        " FROM survey s LEFT JOIN collector c ON s.survey_id = c.survey_id WHERE s.progress LIKE ' " +
        progressType.name() +
        "' LIMIT " +
        pageable.getPageSize() +
        " OFFSET " +
        pageable.getOffset();
    List<SurveyDto> list = jdbcTemplate.query(sql, (rs, rowNum) -> SurveyDto.builder()
                                                                            .id(rs.getLong("id"))
                                                                            .surveyId(rs.getLong("surveyid"))
                                                                            .progress(ProgressType.valueOf(rs.getString("progress")))
                                                                            .minimumTime(rs.getInt("minimumtime"))
                                                                            .point(rs.getLong("point"))
                                                                            .createdAt(LocalDate.parse(rs.getString("createdat")))
                                                                            .endAt(LocalDate.parse(rs.getString("endat")))
                                                                            .participateUrl(rs.getString("participateurl"))
                                                                            .count(rs.getLong("count"))
                                                                            .title(rs.getString("title"))
                                                                            .build()
    );
    sql = "SELECT count(*) AS total from survey s LEFT JOIN collector c on s.survey_id=c.survey_id WHERE s.progress like '" + progressType.name() + "'";
    int count = jdbcTemplate.queryForObject(sql, int.class);
    return new PageImpl<>(list, pageable, count);
  }

  public Page<SurveyDto> findAllSurveys(Pageable pageable) {
    String sql = "SELECT s.id AS id, s.title AS title, IFNULL(s.point, 0) AS point, s.survey_id AS surveyid, s.progress AS progress, s.minimum_time AS minimumtime, IFNULL" +
        "(DATE_FORMAT(s.created_at, '%Y-%m-%d'),DATE_FORMAT(NOW(), '%Y-%m-%d')) AS createdat, IFNULL(DATE_FORMAT(s.end_at, '%Y-%m-%d'), DATE_FORMAT(NOW(), " +
        "'%Y-%m-%d'))AS endat, c.participate_url AS participateurl, s.question_count AS count " +
        "FROM survey s LEFT JOIN collector c ON s.survey_id = c.survey_id " +
        "order by progress desc, end_at desc" +
        " LIMIT " +
        pageable.getPageSize() +
        " OFFSET " + pageable.getOffset();
    List<SurveyDto> list = jdbcTemplate.query(sql, (rs, rowNum) -> SurveyDto.builder()
                                                                            .id(rs.getLong("id"))
                                                                            .surveyId(rs.getLong("surveyid"))
                                                                            .progress(ProgressType.valueOf(rs.getString("progress")))
                                                                            .minimumTime(rs.getInt("minimumtime"))
                                                                            .point(rs.getLong("point"))
                                                                            .createdAt(LocalDate.parse(rs.getString("createdat")))
                                                                            .endAt(LocalDate.parse(rs.getString("endat")))
                                                                            .participateUrl(rs.getString("participateurl"))
                                                                            .count(rs.getLong("count"))
                                                                            .title(rs.getString("title"))
                                                                            .build()
    );
    sql = "SELECT COUNT(*) AS total FROM survey s LEFT JOIN collector c ON s.survey_id=c.survey_id";
    int count = jdbcTemplate.queryForObject(sql, int.class);
    return new PageImpl<>(list, pageable, count);
  }

  public Page<SurveyDto> findAllSurveysByExcludedProgressType(ProgressType progressType, Pageable pageable) {
    String sql = "SELECT s.id AS id, s.title AS title, IFNULL(s.point, 0) AS point, s.survey_id AS surveyid, s.progress AS progress, s.minimum_time AS minimumtime, IFNULL" +
        "(DATE_FORMAT(s.created_at, '%Y-%m-%d'),DATE_FORMAT(NOW(), '%Y-%m-%d')) AS createdat, IFNULL(DATE_FORMAT(s.end_at, '%Y-%m-%d'), DATE_FORMAT(NOW(), '%Y-%m-%d'))AS endat, c.participate_url AS participateurl, s.question_count AS count" +
        " FROM survey s LEFT JOIN collector c ON s.survey_id = c.survey_id WHERE s.progress NOT LIKE ' " +
        progressType.name() +
        "' LIMIT " +
        pageable.getPageSize() +
        " OFFSET " +
        pageable.getOffset();
    List<SurveyDto> list = jdbcTemplate.query(sql, (rs, rowNum) -> SurveyDto.builder()
                                                                            .id(rs.getLong("id"))
                                                                            .surveyId(rs.getLong("surveyid"))
                                                                            .progress(ProgressType.valueOf(rs.getString("progress")))
                                                                            .minimumTime(rs.getInt("minimumtime"))
                                                                            .point(rs.getLong("point"))
                                                                            .createdAt(LocalDate.parse(rs.getString("createdat")))
                                                                            .endAt(LocalDate.parse(rs.getString("endat")))
                                                                            .participateUrl(rs.getString("participateurl"))
                                                                            .count(rs.getLong("count"))
                                                                            .title(rs.getString("title"))
                                                                            .build()
    );
    sql = "SELECT count(*) AS total from survey s LEFT JOIN collector c on s.survey_id=c.survey_id WHERE s.progress not like'" + progressType.name() + "'";
    int count = jdbcTemplate.queryForObject(sql, int.class);
    return new PageImpl<>(list, pageable, count);
  }

}
