package com.example.demo.Controller;

import com.example.demo.Service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("bucket")
public class BucketController {

    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("upload")
    public String upload(@RequestPart(value = "file")MultipartFile multipartFile){
        return this.amazonClient.uploadFile(multipartFile);
    }

    @DeleteMapping("delete")
    public String delete(@RequestPart(value = "url") String fileUrl){
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}
