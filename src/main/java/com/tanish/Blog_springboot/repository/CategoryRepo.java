package com.tanish.Blog_springboot.repository;

import com.tanish.Blog_springboot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
