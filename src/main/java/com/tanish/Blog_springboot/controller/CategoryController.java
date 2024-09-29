package com.tanish.Blog_springboot.controller;

import com.tanish.Blog_springboot.dto.CategoryRequest;
import com.tanish.Blog_springboot.service.interf.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategory(){
        return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("ADMIN")
    public ResponseEntity<?> getCategory(@PathVariable Long id){
        return new ResponseEntity<>(categoryService.getCategory(id), HttpStatus.CREATED);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.addCategory(categoryRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest updateCategory){
        return new ResponseEntity<>(categoryService.updateCategory(id,updateCategory), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.CREATED);
    }
}
