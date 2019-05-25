package com.example.demo.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClient {


    private AmazonS3 s3client;

    @Value("${endpointUrl}")
    private String endpointUrl;
    @Value("${jsa.s3.bucket}")
    private String bucketName;
    @Value("${jsa.aws.access_key_id}")
    private String accessKey;
    @Value("${jsa.aws.secret_access_key}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public String uploadFile(MultipartFile multipartFile){
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
            return fileUrl;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName , fileName , file));
    }

    private String generateFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File conFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(conFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return conFile;

    }

    public String deleteFileFromS3Bucket(String url){
        String fileName = url.substring(url.lastIndexOf("/")+1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName , fileName));
        return "deleted";
    }


}
