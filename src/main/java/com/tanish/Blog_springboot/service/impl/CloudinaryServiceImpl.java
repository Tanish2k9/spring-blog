package com.tanish.Blog_springboot.service.impl;

import com.cloudinary.Cloudinary;
import com.tanish.Blog_springboot.service.interf.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    @Override
    public Map uploadFile(MultipartFile file) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "blog_post_images");
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            System.out.println(uploadedFile);
            return uploadedFile;

        }catch ( IOException e){
            throw new RuntimeException("Image uploading fail");
        }
    }
    public Map updateFile(MultipartFile file,String imageUrl) {
        try{
            String publicId = extractPublicIdFromUrl(imageUrl);
            HashMap<Object, Object> options = new HashMap<>();
            options.put("public_id", publicId);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);

            return uploadedFile;
        }catch ( IOException e){
            throw new RuntimeException("Image updating fail fail");
        }
    }

    public Map deleteFile(String imageUrl) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            String publicId = extractPublicIdFromUrl(imageUrl);
            Map deletedFile = cloudinary.uploader().destroy(publicId, options);
            return deletedFile;
        }catch ( IOException e){
            throw new RuntimeException("Image deleting fail");
        }
    }
    private String extractPublicIdFromUrl(String imageUrl) {
        String[] data = imageUrl.split("/");
        String value = data[data.length-1];
        int dotIndex = value.lastIndexOf('.');
        String lastPart = value.substring(0, dotIndex);
        String publicId = data[data.length-2]+"/"+ lastPart;
        return publicId;
    }
}
