package com.dns.polinsight.utils;

import com.dns.polinsight.exception.ImageResizeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUtil {

  int width = 480, height = 320;

  @Value("${file.upload.thumbnail}")
  private String thumbnailLocation;

  /**
   * @param originImage
   *     : image file
   * @param imageName
   *     : uuid + image origin name
   * @param ext
   *     : image extension  ex) jpg, png, jpeg, bmp ...
   *
   * @return : image name that down scaled
   *
   * @throws ImageResizeException
   */
  public String imageResize(MultipartFile originImage, String imageName, String ext) throws ImageResizeException {
    try {
      String thumbnailPath = "thumbnails/" + imageName;
      Path thumbnailAbsPath = Paths.get(thumbnailLocation + imageName).normalize();

      Image image = ImageIO.read(originImage.getInputStream());
      Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

      BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

      Graphics g = newImage.getGraphics();
      g.drawImage(resizedImage, 0, 0, null);
      g.dispose();

      ImageIO.write(newImage, ext, new File(String.valueOf(thumbnailAbsPath)));

      return thumbnailPath;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ImageResizeException(e.getMessage());
    }
  }

}
