package com.tanish.Blog_springboot.dto.postDtos;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3 , max = 200 ,message = "title must be between 3 and 200 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Lob
    @Size(min =1000,message = "content must be minimum 1000 character long")
    private String content;

    @NotNull(message = "Image is mandatory")
    private MultipartFile image;

    @NotNull(message = "CategoryId is mandatory")
    private Long categoryId;

}
