package com.dns.polinsight.types;

public enum ProgressType implements Comparable<ProgressType> {
  /*
   * @Param BEFORE - 등록 전
   * @Param ONGOING - 진행 중
   * @Param END - 설문 종료
   * */
  ONGOING(2), END(3), BEFORE(1);

  int order;

  ProgressType(int order) {
    this.order = order;
  }
}
