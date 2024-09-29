package com.tanish.Blog_springboot.controller;

import com.tanish.Blog_springboot.constant.PaginationConstant;
import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.CommentDto;
import com.tanish.Blog_springboot.dto.CommentRequest;
import com.tanish.Blog_springboot.dto.PaginationResponse;
import com.tanish.Blog_springboot.entity.Comment;
import com.tanish.Blog_springboot.service.interf.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postId}/comment")
    public ResponseEntity<PaginationResponse<List<CommentDto>>> getPostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = PaginationConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = PaginationConstant.PAGE_SIZE,required = false) Integer pageSize
    ){
        return new ResponseEntity<>(commentService.getAllComments(postId,pageNumber,pageSize), HttpStatus.OK);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<ApiResponse> addComment(@RequestBody @Valid CommentRequest commentRequest, @PathVariable Long postId ){
        return new ResponseEntity<>(commentService.addComment(commentRequest,postId), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@RequestBody @Valid CommentRequest commentRequest, @PathVariable Long postId,@PathVariable Long commentId ){
        return new ResponseEntity<>(commentService.updateComment(commentRequest,postId,commentId), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment( @PathVariable Long postId,@PathVariable Long commentId ){
        return new ResponseEntity<>(commentService.deleteComment(postId,commentId), HttpStatus.OK);
    }
}
