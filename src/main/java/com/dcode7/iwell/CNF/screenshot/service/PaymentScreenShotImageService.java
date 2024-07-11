package com.dcode7.iwell.CNF.screenshot.service;

import com.dcode7.iwell.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PaymentScreenShotImageService {
    String saveImageToStorage(User user, String uploadDirectory, MultipartFile multipartFile);

    public byte[] getImage(String imageDirectory, String imageName) throws IOException;

    String deleteImage(String imageDirectory, String imageName) throws IOException;

}
