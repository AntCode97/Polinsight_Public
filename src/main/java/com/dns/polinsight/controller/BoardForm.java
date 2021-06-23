package com.dns.polinsight.controller;

import com.dns.polinsight.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
public class BoardForm {


    private String title;

    private String content;

    private LocalDateTime registeredAt;
}
