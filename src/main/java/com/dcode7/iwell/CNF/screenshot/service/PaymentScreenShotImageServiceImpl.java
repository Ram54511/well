package com.dcode7.iwell.CNF.screenshot.service;

import com.dcode7.iwell.CNF.screenshot.PaymentScreenShotImage;
import com.dcode7.iwell.CNF.screenshot.ScreenShotRepository;
import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentScreenShotImageServiceImpl implements PaymentScreenShotImageService {

    private final LocalService localService;

    @Value("${project.image}")
    private String path;

    private final ScreenShotRepository screenShotRepository;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    /**
     * Saves the uploaded image to the specified storage directory.
     *
     * @param uploadDirectory the directory where the image will be stored.
     * @param multipartFile   the image file to be stored.
     * @return the generated unique file name of the stored image.
     * @throws CustomException if the file is empty or if an error occurs during file storage.
     */
    @Override
    public String saveImageToStorage(User user, String uploadDirectory, MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new CustomException(localService.getMessage("upload.file.empty"), HttpStatus.BAD_REQUEST);
        }
        validateFileExtension(multipartFile);
        Path directoryPath = Paths.get(uploadDirectory);
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException ioe) {
            throw new CustomException(localService.getMessage("not.create.directory" + uploadDirectory, ioe), HttpStatus.BAD_REQUEST);
        }
        // Create a unique file name with the appropriate extension
        String fileName = UUID.randomUUID().toString() + "." + getFileExtension(multipartFile);
        Path filePath = directoryPath.resolve(fileName);
        saveScreenShot(user, fileName, multipartFile);
        try {
            Files.write(filePath, multipartFile.getBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException ioe) {
            throw new CustomException(localService.getMessage("not.upload" + fileName, ioe), HttpStatus.BAD_REQUEST);
        }
        return fileName;
    }

    /**
     * Retrieves the image from the specified directory.
     *
     * @param imageDirectory the directory where the image is stored.
     * @param imageName      the name of the image file to be retrieved.
     * @return a byte array containing the image data.
     * @throws IOException if the image file is not found or an error occurs during file retrieval.
     */
    @Override
    public byte[] getImage(String imageDirectory, String imageName) throws IOException {
        Path imagePath = Path.of(imageDirectory, imageName);
        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
           throw new CustomException(localService.getMessage("not.found"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves the file extension from the given MultipartFile.
     *
     * @param file the file from which the extension will be extracted.
     * @return the file extension as a string.
     * @throws CustomException if the file has an invalid format.
     */
    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && originalFileName.contains(".")) {
            return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        } else {
            throw new IllegalArgumentException(localService.getMessage("invalid.file.extension",HttpStatus.BAD_REQUEST) + originalFileName);
        }
    }

    private void validateFileExtension(MultipartFile file) {
        String fileExtension = getFileExtension(file);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException(localService.getMessage("invalid.file",HttpStatus.BAD_REQUEST));
        }
    }

    /**
     * Deletes the image from the specified directory.
     *
     * @param imageDirectory the directory where the image is stored.
     * @param imageName      the name of the image file to be deleted.
     * @return a message indicating whether the deletion was successful or failed.
     * @throws IOException if the image file is not found or an error occurs during file deletion.
     */
    @Override
    public String deleteImage(String imageDirectory, String imageName) throws IOException {
        Path imagePath = Path.of(imageDirectory, imageName);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);

            return localService.getMessage("delete.success",HttpStatus.BAD_REQUEST);
        } else {
            throw new CustomException(localService.getMessage("not.found"),HttpStatus.BAD_REQUEST);
        }
    }

    private void saveScreenShot(User user, String fileName, MultipartFile multipartFile){
        PaymentScreenShotImage paymentScreenShotImage = new PaymentScreenShotImage();
        paymentScreenShotImage.setUrl(path+fileName);
        paymentScreenShotImage.setName(multipartFile.getOriginalFilename());
        paymentScreenShotImage.setUser(user);
        screenShotRepository.save(paymentScreenShotImage);
    }
}
