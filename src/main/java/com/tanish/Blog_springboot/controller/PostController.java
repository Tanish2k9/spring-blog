package com.tanish.Blog_springboot.controller;

import com.tanish.Blog_springboot.constant.PaginationConstant;
import com.tanish.Blog_springboot.dto.postDtos.PostRequest;
import com.tanish.Blog_springboot.dto.postDtos.UpdatePostRequest;
import com.tanish.Blog_springboot.service.interf.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id){
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }
    @GetMapping("/post/{id}/similar")
    public ResponseEntity<?> getSimilarPost(@PathVariable Long id){
        return new ResponseEntity<>(postService.getSimilarPosts(id), HttpStatus.OK);
    }

    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPost(
            @RequestParam(defaultValue = PaginationConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = PaginationConstant.PAGE_SIZE,required = false) Integer pageSize
    ){
        return new ResponseEntity<>(postService.getMyPosts(pageNumber,pageSize), HttpStatus.OK);
    }

    @GetMapping("/all-posts")
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = PaginationConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = PaginationConstant.PAGE_SIZE,required = false) Integer pageSize
    ){
        return new ResponseEntity<>(postService.getAllPosts(pageNumber,pageSize), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@ModelAttribute @Valid PostRequest postRequest){

        return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.CREATED);
    }

    @GetMapping("/post/{id}/like")
    public ResponseEntity<?> LikePost(@PathVariable Long id){
        return new ResponseEntity<>(postService.LikePost(id), HttpStatus.OK);
    }
    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,  @ModelAttribute @Valid UpdatePostRequest postRequest){
        return new ResponseEntity<>(postService.updatePost(id,postRequest), HttpStatus.OK);
    }
    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        return new ResponseEntity<>(postService.deletePost(id), HttpStatus.OK);
    }
    @GetMapping("/searchPost")
    public ResponseEntity<?> getPostBySearching(
            @RequestParam(required = false, defaultValue = "") String search ,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = PaginationConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = PaginationConstant.PAGE_SIZE,required = false) Integer pageSize

//            @RequestParam(defaultValue = PaginationConstant.SORT_BY_Likes,required = false) String sortBy,
//            @RequestParam(defaultValue = PaginationConstant.SORT_DIR,required = false) String sortDir
    ){
        return new ResponseEntity<>(postService.getPostBySearching(search,categoryId,pageNumber,pageSize), HttpStatus.OK);
    }

    @GetMapping("/popular-post")
    public ResponseEntity<?> getPopularPost(
            @RequestParam(defaultValue = PaginationConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = PaginationConstant.PAGE_SIZE,required = false) Integer pageSize
    ){
        return new ResponseEntity<>(postService.getPopularPosts(pageNumber,pageSize), HttpStatus.OK);
    }
    @GetMapping("/most-viewed-post")
    public ResponseEntity<?> getMostViewedPost(
            @RequestParam(defaultValue = PaginationConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = PaginationConstant.PAGE_SIZE,required = false) Integer pageSize
    ){
        return new ResponseEntity<>(postService.getMostViewedPosts(pageNumber,pageSize), HttpStatus.OK);
    }

}
