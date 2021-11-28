package com.example.pantryparserbackend.Images;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;


@RestController
public class ImageController {

    @Autowired
    RecipeRepository recipeRepo;

    @Autowired
    UserRepository userRepo;

    private final ImageUtil imageUtil = new ImageUtil();

    @PostMapping(path = "/recipe/{recipe_id}/image")
    public String setRecipeImage(@RequestParam("image") MultipartFile image, @PathVariable int recipe_id) throws IOException {
        Recipe recipe = recipeRepo.findById(recipe_id);

        if(recipe == null){
            return MessageUtil.newResponseMessage(false, "Recipe Id not found");
        }
        if(image == null || image.getOriginalFilename() == null){
            return MessageUtil.newResponseMessage(false, "Image was null");
        }
        if(!image.getOriginalFilename().contains(".") || !imageUtil.isAcceptedFileType(image.getOriginalFilename().split("\\.")[1])) {
            return MessageUtil.newResponseMessage(false, "Accepted file types are .jpg .jpeg and .png");
        }

        String fileDirectory = "mainImageDirectory/recipes/" + recipe_id;
        String fileName = "Recipe" + recipe_id + "Image.png";

        // If file is a jpg
        if(image.getOriginalFilename().contains(".jpg") || image.getOriginalFilename().contains(".jpeg")){
            File file = new File(fileName);
            imageUtil.saveFile(fileDirectory, "temp.jpg", image);
            BufferedImage bufferedImage = ImageIO.read(new File(fileDirectory + "/temp.jpg"));
            ImageIO.write(bufferedImage, "png", new File(fileDirectory + "/" + fileName));
            imageUtil.compressFile(fileDirectory, fileName);
            Files.delete(Paths.get(fileDirectory + "/temp.jpg"));

            return MessageUtil.newResponseMessage(true, "Image converted to png and uploaded to server successfully");
        }
        else
        {
            imageUtil.saveFile(fileDirectory, fileName, image);
            imageUtil.compressFile(fileDirectory, fileName);
            return MessageUtil.newResponseMessage(true, "Image uploaded to server successfully");
        }
    }

    @GetMapping(path = "/recipe/{recipe_id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getRecipeImage( @PathVariable int recipe_id, @RequestParam(defaultValue = "false") boolean compressed) throws IOException{
        String compressionString = compressed ? "_compressed" : "";
        Path filePath = Paths.get("mainImageDirectory/recipes/" + recipe_id + "/Recipe" + recipe_id + "Image" + compressionString + ".png");

        if(Files.notExists(filePath)){
            throw new IOException("Image does not exist");
        }

        return Files.readAllBytes(Paths.get("mainImageDirectory/recipes/" + recipe_id + "/Recipe" + recipe_id + "Image" + compressionString + ".png"));
    }

    @PostMapping(path = "/user/{user_id}/image")
    public String setUserProfileImage(@RequestParam("image") MultipartFile image, @PathVariable int user_id) throws IOException {
        User user = userRepo.findById(user_id);

        if(user == null){
            return MessageUtil.newResponseMessage(false, "User Id not found");
        }
        if(image == null || image.getOriginalFilename() == null){
            return MessageUtil.newResponseMessage(false, "Image was null");
        }
        if(!image.getOriginalFilename().contains(".") || !imageUtil.isAcceptedFileType(image.getOriginalFilename().split("\\.")[1])) {
            return MessageUtil.newResponseMessage(false, "Accepted file types are .jpg .jpeg and .png");
        }

        String fileDirectory = "mainImageDirectory/users/" + user_id;
        String fileName ="User" + user_id + "Image.png";

        // If file is a jpg
        if(image.getOriginalFilename().contains(".jpg") || image.getOriginalFilename().contains(".jpeg")){
            imageUtil.saveFile(fileDirectory, "temp.jpg", image);
            BufferedImage bufferedImage = ImageIO.read(new File(fileDirectory + "/temp.jpg"));
            ImageIO.write(bufferedImage, "png", new File(fileDirectory + "/" + fileName));
            imageUtil.compressFile(fileDirectory, fileName);
            Files.delete(Paths.get(fileDirectory + "/temp.jpg"));

            return MessageUtil.newResponseMessage(true, "Image converted and uploaded to server");
        }
        else
        {
            imageUtil.saveFile(fileDirectory, fileName, image);
            imageUtil.compressFile(fileDirectory, fileName);
            return MessageUtil.newResponseMessage(true, "Image uploaded to server successfully");
        }
    }

    @GetMapping(path = "/user/{user_id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getUserProfileImage( @PathVariable int user_id, @RequestParam(defaultValue = "false") boolean compressed) throws IOException{
        String compressionString = compressed ? "_compressed" : "";
        Path filePath = Paths.get("mainImageDirectory/users/" + user_id + "/Recipe" + user_id + "Image" + compressionString + ".png");

        if(Files.notExists(filePath)){
            throw new IOException("Image does not exist");
        }

        return Files.readAllBytes(Paths.get("mainImageDirectory/users/" + user_id + "/Recipe" + user_id + "Image" + compressionString + ".png"));
    }
}
