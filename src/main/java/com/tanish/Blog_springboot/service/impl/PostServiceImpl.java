package com.tanish.Blog_springboot.service.impl;

import com.tanish.Blog_springboot.constant.PaginationConstant;
import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.PaginationResponse;
import com.tanish.Blog_springboot.dto.postDtos.PostDto;
import com.tanish.Blog_springboot.dto.postDtos.PostRequest;
import com.tanish.Blog_springboot.dto.postDtos.UpdatePostRequest;
import com.tanish.Blog_springboot.entity.Category;
import com.tanish.Blog_springboot.entity.Post;
import com.tanish.Blog_springboot.entity.User;
import com.tanish.Blog_springboot.exception.NotFoundException;
import com.tanish.Blog_springboot.mapper.EntityDtoMapper;
import com.tanish.Blog_springboot.repository.CategoryRepo;
import com.tanish.Blog_springboot.repository.PostRepo;
import com.tanish.Blog_springboot.repository.UserRepo;
import com.tanish.Blog_springboot.service.interf.CloudinaryService;
import com.tanish.Blog_springboot.service.interf.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PostServiceImpl implements PostService {
    private  final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final CloudinaryService cloudinaryService;
    private final EntityDtoMapper entityDtoMapper;
    private final PostRepo postRepo;

    @Override
    public ApiResponse createPost(PostRequest postRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));

        Category category = categoryRepo.findById(postRequest.getCategoryId())
                .orElseThrow(()-> new NotFoundException("Category Not found"));


        Map image = cloudinaryService.uploadFile(postRequest.getImage());

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setImageUrl((String) image.get("secure_url"));
        post.setCreatedBy(user);
        post.setCategory(category);

        Post savedPost = postRepo.save(post);

        PostDto postDto = entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(savedPost);
        return ApiResponse.<PostDto>builder()
                .status(201)
                .message("post created successfully")
                .data(postDto)
                .build();
    }

    @Override
    public ApiResponse updatePost(Long id, UpdatePostRequest updatePostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));

        Post post = postRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Post not found"));

        Category category = categoryRepo.findById(updatePostRequest.getCategoryId())
                .orElseThrow(()-> new NotFoundException("Category Not found"));

        if(user.getId() != post.getCreatedBy().getId()){
            throw new RuntimeException("You can't update others user's post");
        }
        MultipartFile file = updatePostRequest.getImage();

        if(file != null && !file.isEmpty()) {
            Map image = cloudinaryService.updateFile(updatePostRequest.getImage(),post.getImageUrl());
            post.setImageUrl((String) image.get("secure_url"));
        }
        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContent());

        post.setCategory(category);
        Post savedPost = postRepo.save(post);
        PostDto postDto = entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(savedPost);
        return ApiResponse.<PostDto>builder()
                .status(200)
                .message("post update successfully successfully")
                .data(postDto)
                .build();
    }

    @Override
    public ApiResponse deletePost(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));

        Post post = postRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Post not found"));

        if(user.getId() != post.getCreatedBy().getId()){
            throw new RuntimeException("You cant delete others user's post");
        }

        Map deletedFile = cloudinaryService.deleteFile(post.getImageUrl());
        postRepo.delete(post);
        return ApiResponse.<String>builder()
                .status(200)
                .message("post deleted successfully")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse getPost(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Post not found"));
        post.setViewCount(post.getViewCount()+1);
        Post savedPost =postRepo.save(post);

        PostDto postDto = entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(savedPost);

        return ApiResponse.<PostDto>builder()
                .status(200)
                .message("post fetched successfully")
                .data(postDto)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse LikePost(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));

        Post post = postRepo.findById(id).orElseThrow(()-> new NotFoundException("Post not found"));

        Set<User> likedUser = post.getUsersWhoLiked();
        Set<Post> likedPosts = user.getLikedPosts();
//        likedPosts.add(post);
//        user.setLikedPosts(likedPosts);
//        userRepo.save(user);

        if(likedUser.contains(user)){
            likedUser.remove(user);
            likedPosts.remove(post);

            post.decrementLike();
            post.setLiked(false);

        }else {
            likedUser.add(user);
            likedPosts.add(post);
            post.incrementLike();
            post.setLiked(true);
        }
        Post savedPost = postRepo.save(post);
        userRepo.save(user);


        return ApiResponse.builder()
                .status(200)
                .message("likes update successfully")
                .data(savedPost.isLiked())
                .build();
    }

    @Override
    public ApiResponse getSimilarPosts(Long id) {
        Post post = postRepo.findById(id).orElseThrow(()-> new NotFoundException("Post not found"));
        Category category = post.getCategory();

        Pageable pageable = PageRequest.of(0,2, Sort.by(Sort.Direction.DESC, PaginationConstant.SORT_BY_LIKES));
        List<Post> similarPosts = postRepo.findByCategory(category,pageable);

        similarPosts = similarPosts.stream().filter((item)->{
           return item.getId() != post.getId();
        }).collect(Collectors.toList());

        if(similarPosts.size() == 0){
            pageable = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, PaginationConstant.SORT_BY_LIKES));
            Page<Post> postPage = postRepo.findAll(pageable);
            similarPosts = postPage.getContent().stream().filter((item)->{
                return item.getId() != post.getId();
            }).collect(Collectors.toList());
        }

        List<PostDto> postDtos = similarPosts.stream()
                .map(data->entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(data))
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .status(200)
                .message("similar post fetched  successfully")
                .data(postDtos)
                .build();
    }

    @Override
    public PaginationResponse getPopularPosts(Integer pageNumber,Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(Sort.Direction.DESC, PaginationConstant.SORT_BY_LIKES));
        Page<Post> postPage = postRepo.findAll(pageable);
        List<PostDto> allPosts = postPage.getContent().stream()
                .map(post -> entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(post))
                .collect(Collectors.toList());

        return PaginationResponse.<List<PostDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("popular post fetched successfully")
                .pageNumber(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .isLast(postPage.isLast())
                .data(allPosts)
                .build();
    }

    @Override
    public PaginationResponse getMostViewedPosts(Integer pageNumber,Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(Sort.Direction.DESC, PaginationConstant.SORT_BY_VIEWS));
        Page<Post> postPage = postRepo.findAll(pageable);
        List<PostDto> allPosts = postPage.getContent().stream()
                .map(post -> entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(post))
                .collect(Collectors.toList());;

        return PaginationResponse.<List<PostDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("most viewed post fetched successfully")
                .pageNumber(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .isLast(postPage.isLast())
                .data(allPosts)
                .build();
    }

    @Override
    public PaginationResponse<List<PostDto>> getMyPosts(Integer pageNumber, Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Post> postPage = postRepo.findByCreatedBy(user,pageable);
        List<PostDto> myPosts = postPage.getContent().stream()
                .map(post -> entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(post))
                .collect(Collectors.toList());;

        return PaginationResponse.<List<PostDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("my posts fetched successfully")
                .pageNumber(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .isLast(postPage.isLast())
                .data(myPosts)
                .build();
    }

    @Override
    public PaginationResponse<List<PostDto>> getAllPosts(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Post> postPage = postRepo.findAll(pageable);
        List<PostDto> posts = postPage.getContent().stream()
                .map(post -> entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(post))
                .collect(Collectors.toList());;
        return PaginationResponse.<List<PostDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Posts fetched successfully")
                .pageNumber(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .isLast(postPage.isLast())
                .data(posts)
                .build();
    }

    @Override
    public PaginationResponse<List<PostDto>> getPostBySearching(String search, Long categoryId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Post> postPage = postRepo.findyByTitleAndCategoryID(search,categoryId,pageable);
        List<PostDto> allPosts = postPage.getContent().stream()
                .map(post -> entityDtoMapper.mapPostToPostDtoWithUserPlusCategory(post))
                .collect(Collectors.toList());;

        return PaginationResponse.<List<PostDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Posts fetched successfully")
                .pageNumber(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .isLast(postPage.isLast())
                .data(allPosts)
                .build();

    }

}
