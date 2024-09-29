package com.tanish.Blog_springboot.service.interf;

import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.CommentDto;
import com.tanish.Blog_springboot.dto.CommentRequest;
import com.tanish.Blog_springboot.dto.PaginationResponse;
import com.tanish.Blog_springboot.entity.Comment;

import java.util.List;

public interface CommentService {
    ApiResponse addComment(CommentRequest commentRequest, Long postId);
    ApiResponse updateComment(CommentRequest commentRequest,Long postId ,Long commentId);
    ApiResponse deleteComment(Long postId ,Long commentId);
    PaginationResponse<List<CommentDto>> getAllComments(Long postId, Integer pageNumber, Integer pageSize);
}
