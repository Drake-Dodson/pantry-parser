package com.example.pantryparserbackend.Images;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;


@RestController
public class ImageController {

    @Autowired
    RecipeRepository recipeRepo;

    // - We need make sure that we can overwrite a file
    @PostMapping(path = "/recipe/{recipe_id}/image")
    String setRecipeImage(@RequestParam("image") MultipartFile image, @PathVariable int recipe_id) throws IOException {
        Recipe recipe = recipeRepo.findById(recipe_id);

        if(recipe == null){
            return MessageUtil.newResponseMessage(false, "Recipe Id not found");
        }

        if(image == null){
            return MessageUtil.newResponseMessage(false, "Image was null");
        }

        if(!image.getOriginalFilename().contains(".jpg") && !image.getOriginalFilename().contains(".png") && !image.getOriginalFilename().contains(".jpeg")) {
            return MessageUtil.newResponseMessage(false, "Accepted file types are .jpg .jpeg and .png");
        }

        String fileName = StringUtils.cleanPath("Recipe" + recipe_id + "Image.png");
        String fileDirectory = "mainImageDirectory/recipes/" + recipe_id;

        saveFile(fileDirectory, fileName, image);

        compressFile(fileDirectory, fileName);

        return MessageUtil.newResponseMessage(true, "Image uploaded to server successfully");
    }

    private void compressFile(String filePath, String fileName) throws IOException {
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

    private void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
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
