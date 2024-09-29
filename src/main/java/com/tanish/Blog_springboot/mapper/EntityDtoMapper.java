package com.tanish.Blog_springboot.mapper;

import com.tanish.Blog_springboot.dto.CategoryDto;
import com.tanish.Blog_springboot.dto.CommentDto;
import com.tanish.Blog_springboot.dto.UserDto;
import com.tanish.Blog_springboot.dto.postDtos.PostDto;
import com.tanish.Blog_springboot.entity.Category;
import com.tanish.Blog_springboot.entity.Comment;
import com.tanish.Blog_springboot.entity.Post;
import com.tanish.Blog_springboot.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {
    public UserDto mapUserToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setCreatedDate(user.getCreatedDate());
        userDto.setLastModifiedDate(user.getLastModifiedDate());

        return userDto;
    }

    public CategoryDto mapCategoryToCategoryDto(Category category){
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .build();
        return categoryDto;
    }

    public CommentDto mapCommentToCommentDtoPlusUser(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .LastModifiedDate(comment.getLastModifiedDate())
                .user(mapUserToUserDto(comment.getUser()))
                .build();
    }

    public PostDto mapPostToPostDtoWithUserPlusCategory(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setImageUrl(post.getImageUrl());
        postDto.setLikeCount(post.getLikeCount());
        postDto.setViewCount(post.getViewCount());
        postDto.setLiked(post.isLiked());

        postDto.setCreatedBy(mapUserToUserDto(post.getCreatedBy()));
        postDto.setCategory(mapCategoryToCategoryDto(post.getCategory()));

        postDto.setCreatedDate(post.getCreatedDate());
        postDto.setLastModifiedDate(post.getLastModifiedDate());

        return postDto;
    }
}
