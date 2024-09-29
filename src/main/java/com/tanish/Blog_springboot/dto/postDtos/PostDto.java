package com.tanish.Blog_springboot.dto.postDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tanish.Blog_springboot.dto.CategoryDto;
import com.tanish.Blog_springboot.dto.UserDto;
import com.tanish.Blog_springboot.entity.Category;
import com.tanish.Blog_springboot.entity.Comment;
import com.tanish.Blog_springboot.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private int likeCount = 0;
    private int viewCount = 0;
    private boolean isLiked = false;
    private LocalDateTime createdDate;
    private LocalDateTime LastModifiedDate;

    private UserDto createdBy;
    private CategoryDto category;

    private Set<User> usersWhoLiked = new HashSet<>();
    private Set<Comment> comments = new HashSet<>();


}
