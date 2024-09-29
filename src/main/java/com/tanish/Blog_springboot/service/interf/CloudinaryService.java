package com.tanish.Blog_springboot.service.interf;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map uploadFile(MultipartFile file);
    Map updateFile(MultipartFile file, String publicId);
    Map deleteFile(String imageUrl);
}
