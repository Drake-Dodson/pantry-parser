package com.example.pantryparserbackend.Images;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil{

    private List<String> acceptedFileTypes = new ArrayList<String>();

    public ImageUtil(){
        acceptedFileTypes.add("jpg");
        acceptedFileTypes.add("png");
        acceptedFileTypes.add("jpeg");
    }

    public boolean isAcceptedFileType(String fileExtension){
        return acceptedFileTypes.contains(fileExtension);
    }

    public void compressFile(String filePath, String fileName) throws IOException {
        String inputPath = filePath + "/" + fileName;
        String outputPath = filePath + "/" + fileName.replace(".png", "") + "_compressed.png";


        // Worth noting that the compression is very miniscule and doesn't seem to do much. I tried changing the quality and didn't notice much of
        // a difference. Also sometimes the file size is larger than it should be so idk what that's about
        // Code provide courtesy of this stack overflow post https://stackoverflow.com/questions/2721303/how-to-compress-a-png-image-using-java
        BufferedImage image;
        IIOMetadata metadata;

        try (ImageInputStream in = ImageIO.createImageInputStream(Files.newInputStream(Paths.get(inputPath)))) {
            ImageReader reader = ImageIO.getImageReadersByFormatName("png").next();
            reader.setInput(in, true, false);
            image = reader.read(0);
            metadata = reader.getImageMetadata(0);
            reader.dispose();
        }

        try (ImageOutputStream out = ImageIO.createImageOutputStream(Files.newOutputStream(Paths.get(outputPath)))) {
            ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(image);
            ImageWriter writer = ImageIO.getImageWriters(type, "png").next();

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.0000000000005f);
            }

            writer.setOutput(out);
            writer.write(null, new IIOImage(image, null, metadata), param);
            writer.dispose();
        }
    }

    public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
}
