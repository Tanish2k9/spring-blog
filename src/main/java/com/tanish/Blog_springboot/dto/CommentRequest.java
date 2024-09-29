package com.tanish.Blog_springboot.dto;

import com.tanish.Blog_springboot.entity.Post;
import com.tanish.Blog_springboot.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
//    private Long id;

    @NotBlank(message = "Comment cannot be empty")
    private String content;
}
