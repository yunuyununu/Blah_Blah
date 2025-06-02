package com.example.blah.common.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GCSServiceImpl implements GCSService {
	
	@Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

	@Override
    public String uploadObject(GCSRequest gcsRequest) throws IOException {

    	String keyFileName = "blahblah-storage-08872a7fc585.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        String fileName = gcsRequest.getName(); // uuid 포함된 고유 파일명

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(gcsRequest.getFile().getContentType())
                .build();

        storage.create(blobInfo, gcsRequest.getFile().getInputStream());

        // GCS Public URL 생성 (버킷이 공개 설정이어야 접근 가능)
        String publicUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        return publicUrl;
    }
	
	@Override
    public String uploadCompany(GCSRequest gcsRequest) throws IOException {

    	String keyFileName = "blahblah-storage-08872a7fc585.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        String fileName = gcsRequest.getName(); // uuid 포함된 고유 파일명

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(gcsRequest.getFile().getContentType())
                .build();

        storage.create(blobInfo, gcsRequest.getFile().getInputStream());
        
        System.out.println("여기 지나간다!");
        // GCS Public URL 생성 (버킷이 공개 설정이어야 접근 가능)
        String publicUrl = "https://storage.googleapis.com/" + bucketName + "/company/" + fileName;
        return publicUrl;
    }
}
