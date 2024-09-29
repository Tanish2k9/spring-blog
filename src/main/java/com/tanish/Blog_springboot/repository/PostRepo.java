package com.tanish.Blog_springboot.repository;

import com.tanish.Blog_springboot.entity.Category;
import com.tanish.Blog_springboot.entity.Post;
import com.tanish.Blog_springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Long> {

    Page<Post> findByCreatedBy(User user, Pageable pageable);
    List<Post> findByCategory(Category category, Pageable pageable);
    @Query("SELECT p FROM Post p JOIN p.category c " +
            "WHERE (:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId)")
    Page<Post> findyByTitleAndCategoryID(@Param("search") String search,
                                         @Param("categoryId") Long categoryId,
                                         Pageable pageable);

}
