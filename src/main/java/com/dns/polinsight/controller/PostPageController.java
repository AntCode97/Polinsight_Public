package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.PostDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
public class PostPageController {

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("admin/posts/new")
  public String adminCreateForm(Model model, @CurrentUser User user) throws IOException {
    model.addAttribute("postDTO", new PostDTO());
    model.addAttribute("user", user);

    return "admin/admin_post_register";
  }

}
