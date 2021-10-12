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

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUtil {

  static String fileSeparator = File.separator;

  int width = 800, height = 600;

  @Value("${file.upload.baseLocation}")
  private String baseLocation;

  public String imageResize(MultipartFile originImage, String uuidName, String ext) throws ImageResizeException {
    try {
      String thumbnailPath = "thumbnail/" + uuidName + ext;
      String thumbnailAbsPath = baseLocation + fileSeparator + thumbnailPath;
      Image image = ImageIO.read(originImage.getInputStream());
      Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

      BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

      Graphics g = newImage.getGraphics();
      g.drawImage(resizedImage, 0, 0, null);
      g.dispose();
      ImageIO.write(newImage, "png", new File(thumbnailAbsPath));

      return thumbnailPath;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ImageResizeException(e.getMessage());
    }
  }

}
