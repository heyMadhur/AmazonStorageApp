package com.StorageService.StorageApp.service.implementation;

import com.StorageService.StorageApp.dto.FileSearchResponse;
import com.StorageService.StorageApp.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    private final String bucketName;

    public FileServiceImpl(S3Client s3Client, @Value("${app.s3.bucket}") String bucketName) {
        this.s3Client= s3Client;
        this.bucketName= bucketName;
    }

    @Override
    public List<FileSearchResponse> searchFiles(String userName, String searchTerm) {

        String prefixQuery= userName+"/";
        List<S3Object> allObjects= new ArrayList<>();
        String continuationToken= null;

        do {
            ListObjectsV2Request request= ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefixQuery)
                    .continuationToken(continuationToken)
                    .build();
            ListObjectsV2Response response= s3Client.listObjectsV2(request);
            allObjects.addAll(response.contents());
            continuationToken= response.nextContinuationToken();
        } while (continuationToken!=null);


        return allObjects.stream()
                .filter(obj -> {
                    String key= obj.key();
                    String fileName= key.substring(key.lastIndexOf('/')+1);
                    return fileName.contains(searchTerm);
                })
                .map(obj -> new FileSearchResponse(
                        obj.key().substring(prefixQuery.length()),
                        obj.lastModified(),
                        obj.size()
                )).collect(Collectors.toList());
    }

    @Override
    public Resource downloadFile(String username, String fileName) throws FileNotFoundException {
        String key= username + "/"+fileName;

        try{
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());

            GetObjectRequest getObjectRequest= GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> response= s3Client.getObject(getObjectRequest);
            return new InputStreamResource(response);
        }
        catch (S3Exception e) {
            if(e.statusCode()==404) {
                throw new FileNotFoundException("File not found: "+fileName);
            }
            throw new RuntimeException("Error downloading file", e);
        }
    }

    @Override
    public void uploadFile(String userName, MultipartFile file) {
        if(file==null) throw new RuntimeException("File is null!!");
        String key= userName+"/"+file.getOriginalFilename();
        try {
            PutObjectRequest putObjectRequest= PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
