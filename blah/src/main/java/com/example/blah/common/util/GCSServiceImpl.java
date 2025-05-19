package com.example.blah.common.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GCSServiceImpl implements GCSService {
	
	@Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public void uploadObject(GCSRequest gcsRequest) throws IOException {

        String keyFileName = "blahblah-storage-08872a7fc585.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, gcsRequest.getFile().getOriginalFilename())
                .setContentType(gcsRequest.getFile().getContentType()).build();

        Blob blob = storage.create(blobInfo, gcsRequest.getFile().getInputStream());

    }
}
