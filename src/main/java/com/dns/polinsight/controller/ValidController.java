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

  @GetMapping("/user/email/{email}")
  public ApiUtils.ApiResult<Boolean> isExistEmail(@PathVariable("email") Email email) throws NotFoundException {
    try {
      return success(!userService.isExistEmail(email));
    } catch (RuntimeException e) {
      log.error(e.getMessage());
      throw new NotFoundException("Email Number Not Found " + e.getMessage());
    }
  }

  @GetMapping("/user/recommend/{phone}")
  public ApiUtils.ApiResult<Boolean> isExistPhoneForRecommend(@PathVariable("phone") String recommendPhone) throws NotFoundException {
    try {
      if (recommendPhone.length() < 11) {
        return success(Boolean.FALSE);
      } else {
        return success(userService.isExistPhone(Phone.of(recommendPhone)));
      }
    } catch (Exception e) {
      throw new NotFoundException("Phone Number Not Found " + e.getMessage());
    }
  }

}
