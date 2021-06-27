package com.dns.polinsight.controller;

import com.dns.polinsight.domain.BoardType;
import com.dns.polinsight.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
public class BoardForm {

    private Long id;
    private String title;

    private BoardType boardType;

    private String content;

    private LocalDateTime registeredAt;

    private MultipartFile file;
}
