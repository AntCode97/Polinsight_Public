package com.dns.polinsight.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TypeCheckUtil {

  private final Pattern imageExt = Pattern.compile("\\.(?<ext>png|jpg|jpeg|bmp|gif)$");

  /**
   * @param fileName
   *     파일 이름
   *
   * @return 이미지 파일의 여부
   */
  public boolean isImageFile(String fileName) {
    return imageExt.matcher(fileName).find();
  }

  public String getImageFileExt(String fileName) {
    Matcher matcher = imageExt.matcher(fileName);
    String ext = "";
    while (matcher.find())
      ext = matcher.group("ext");
    return ext;
  }

  /**
   * @param fileName
   *     파일 이름
   *
   * @return 이미지 파일 이외의 파일인지 여부
   */
  public boolean isNormalFile(String fileName) {
    return !imageExt.matcher(fileName).find();
  }

}
