package com.dns.polinsight.domain;

import com.dns.polinsight.domain.BoardType;
import com.dns.polinsight.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class BoardDTO {

    private Long id;
    private String title;

    private BoardType boardType;

    private String content;
    private String viewcontent;
    private User user;
    private LocalDateTime registeredAt;

    private List<Attach> attaches;
    private List<MultipartFile> files;
}
