package com.tanish.Blog_springboot.repository;

import com.tanish.Blog_springboot.entity.Comment;
import com.tanish.Blog_springboot.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment,Long> {

    Page<Comment>  findByPost(Post post, Pageable pageable);
}
