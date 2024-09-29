package com.tanish.Blog_springboot.service.impl;

import com.tanish.Blog_springboot.constant.PaginationConstant;
import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.CommentDto;
import com.tanish.Blog_springboot.dto.CommentRequest;
import com.tanish.Blog_springboot.dto.PaginationResponse;
import com.tanish.Blog_springboot.entity.Comment;
import com.tanish.Blog_springboot.entity.Post;
import com.tanish.Blog_springboot.entity.User;
import com.tanish.Blog_springboot.exception.NotFoundException;
import com.tanish.Blog_springboot.mapper.EntityDtoMapper;
import com.tanish.Blog_springboot.repository.CommentRepo;
import com.tanish.Blog_springboot.repository.PostRepo;
import com.tanish.Blog_springboot.repository.UserRepo;
import com.tanish.Blog_springboot.service.interf.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final PostRepo postRepo;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional
    public ApiResponse addComment(CommentRequest commentRequest, Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(()-> new NotFoundException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = commentRepo.save(comment);
        CommentDto commentDto =entityDtoMapper.mapCommentToCommentDtoPlusUser(savedComment);

        return ApiResponse.<CommentDto>builder()
                .status(201)
                .message("Comment add successfully")
                .data(commentDto)
                .build();
    }

    @Override
    public ApiResponse updateComment(CommentRequest commentRequest ,Long postId, Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(()-> new NotFoundException("Post not found"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Comment not found"));

        Set<Comment> userComments = user.getComments();

        if(!userComments.contains(comment)){
            throw new RuntimeException("you cant edit other user comment");
        }

        comment.setContent(commentRequest.getContent());
        Comment updatedComment = commentRepo.save(comment);

        CommentDto commentDto = entityDtoMapper.mapCommentToCommentDtoPlusUser(updatedComment);

        return ApiResponse.<CommentDto>builder()
                .status(201)
                .message("Comment updated successfully")
                .data(commentDto)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse deleteComment(Long postId, Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(()-> new NotFoundException("Post not found"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Comment not found"));

        Set<Comment> userComments = user.getComments();

        if(!userComments.contains(comment)){
            throw new RuntimeException("you cant delete other user comment");
        }
        commentRepo.delete(comment);
        post.getComments().remove(comment);
        user.getComments().remove(comment);
        postRepo.save(post);
        userRepo.save(user);

        return ApiResponse.<String>builder()
                .status(200)
                .message("Comment deleted successfully")
                .data(null)
                .build();
    }

    @Override
    public PaginationResponse<List<CommentDto>> getAllComments(Long postId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(Sort.Direction.DESC, PaginationConstant.SORT_BY_CREATED_DATE));
        Post post = postRepo.findById(postId)
                .orElseThrow(()-> new NotFoundException("Post not found"));

        Page<Comment> commentPage = commentRepo.findByPost(post,pageable);
        List<CommentDto> commentDtos = commentPage.getContent().stream()
                .map(comment ->entityDtoMapper.mapCommentToCommentDtoPlusUser(comment))
                .collect(Collectors.toList());


        return PaginationResponse.<List<CommentDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("comments fetched successfully")
                .pageNumber(commentPage.getNumber())
                .pageSize(commentPage.getSize())
                .totalElements(commentPage.getTotalElements())
                .totalPages(commentPage.getTotalPages())
                .isLast(commentPage.isLast())
                .data(commentDtos)
                .build();
    }
}
