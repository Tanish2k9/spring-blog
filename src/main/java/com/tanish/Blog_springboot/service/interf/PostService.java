package com.tanish.Blog_springboot.service.interf;

import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.PaginationResponse;
import com.tanish.Blog_springboot.dto.postDtos.PostRequest;
import com.tanish.Blog_springboot.dto.postDtos.UpdatePostRequest;
import com.tanish.Blog_springboot.entity.Post;

public interface PostService {
    ApiResponse createPost(PostRequest postRequest);
    ApiResponse updatePost(Long id, UpdatePostRequest updatePost);
    ApiResponse deletePost(Long id);
    ApiResponse getPost(Long id);
    ApiResponse LikePost(Long id);
    ApiResponse getSimilarPosts(Long id);
    PaginationResponse getPopularPosts(Integer pageNumber,Integer pageSize); //by likes
    PaginationResponse getMostViewedPosts(Integer pageNumber,Integer pageSize); // by views

    PaginationResponse getMyPosts(Integer pageNumber,Integer pageSize);
    PaginationResponse getAllPosts(Integer pageNumber,Integer pageSize);

    PaginationResponse getPostBySearching(String search, Long categoryId, Integer pageNumber, Integer pageSize);
}
