package com.dns.polinsight.controller;

import com.dns.polinsight.service.UserService;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import com.dns.polinsight.utils.ApiUtils;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@PermitAll
@RestController
@CrossOrigin("*")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ValidController {

  private final UserService userService;

  @GetMapping("/user/{email}")
  public ApiUtils.ApiResult<Boolean> isExistEmail(@PathVariable("email") Email email) throws NotFoundException {
    try {
      return success(!userService.isExistEmail(email));
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw new NotFoundException("Email Number Not Found");
    }
  }

  @GetMapping("/user/recommend/{phone}")
  public ApiUtils.ApiResult<Boolean> isExistPhoneForRecommend(@PathVariable("phone") Phone recommendPhone) throws NotFoundException {
    try {
      return success(!userService.isExistPhone(recommendPhone));
    } catch (Exception e) {
      throw new NotFoundException("Phone Number Not Found");
    }
  }

}
