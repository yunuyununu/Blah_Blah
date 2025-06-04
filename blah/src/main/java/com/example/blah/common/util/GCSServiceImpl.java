package com.example.blah.common.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GCSServiceImpl implements GCSService {
	
	@Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
	
	private final Storage storage = StorageOptions.getDefaultInstance().getService();

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
	
	// 회사 신청 시
	@Override
    public String uploadCompany(GCSRequest gcsRequest) throws IOException {

    	String keyFileName = "blahblah-storage-08872a7fc585.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        String fileName = gcsRequest.getName(); // uuid 포함된 고유 파일명
        String objectName = "company/" + fileName;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName,objectName)
                .setContentType(gcsRequest.getFile().getContentType())
                .build();

        storage.create(blobInfo, gcsRequest.getFile().getInputStream());
        
        // GCS Public URL 생성 (버킷이 공개 설정이어야 접근 가능)
        String publicUrl = "https://storage.googleapis.com/" + bucketName +"/"+ objectName;
        return publicUrl;
    }
	
	// 회사 변경 시
	@Override
    public String uploadCompanyChange(GCSRequest gcsRequest) throws IOException {

    	String keyFileName = "blahblah-storage-08872a7fc585.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        String fileName = gcsRequest.getName(); // uuid 포함된 고유 파일명
        String objectName = "company_change/" + fileName;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName,objectName)
                .setContentType(gcsRequest.getFile().getContentType())
                .build();

        storage.create(blobInfo, gcsRequest.getFile().getInputStream());
        
        // GCS Public URL 생성 (버킷이 공개 설정이어야 접근 가능)
        String publicUrl = "https://storage.googleapis.com/" + bucketName + "/" + objectName;
        return publicUrl;
    }
	
	// 회원가입 시
	@Override
    public String uploadJoinChange(GCSRequest gcsRequest) throws IOException {

    	String keyFileName = "blahblah-storage-08872a7fc585.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        String fileName = gcsRequest.getName(); // uuid 포함된 고유 파일명
        String objectName = "join/" + fileName;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName,objectName)
                .setContentType(gcsRequest.getFile().getContentType())
                .build();

        storage.create(blobInfo, gcsRequest.getFile().getInputStream());
        
        // GCS Public URL 생성 (버킷이 공개 설정이어야 접근 가능)
        String publicUrl = "https://storage.googleapis.com/" + bucketName + "/" + objectName;
        return publicUrl;
    }
	
	// 게시물 등록 시
	@Override
    public String uploadBoard(GCSRequest gcsRequest) throws IOException {

    	String keyFileName = "blahblah-storage-08872a7fc585.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        String fileName = gcsRequest.getName(); // uuid 포함된 고유 파일명
        String objectName = "board/" + fileName;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName,objectName)
                .setContentType(gcsRequest.getFile().getContentType())
                .build();

        storage.create(blobInfo, gcsRequest.getFile().getInputStream());
        // GCS Public URL 생성 (버킷이 공개 설정이어야 접근 가능)
        String publicUrl = "https://storage.googleapis.com/" + bucketName +"/"+ objectName;
        return publicUrl;
    }
	
    // 이미지 URL로부터 GCS 경로 추출하여 삭제
    public boolean deleteFile(String imageUrl) {
    	try {
            String objectName = extractObjectName(imageUrl);
            if (objectName == null) return false;

            BlobId blobId = BlobId.of(bucketName, objectName);
            System.out.println("이미지 이름이름 :: "+objectName);
            System.out.println("이미지 :::: "+blobId);
            System.out.println("이미지 지워졌나 확인요망!!");
            return storage.delete(blobId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // GCS URL에서 실제 object 경로 추출 (버킷 기준 상대 경로)
    private String extractObjectName(String url) {
        // 예: https://storage.googleapis.com/your-bucket-name/folder/image.jpg
        String prefix = "https://storage.googleapis.com/" + bucketName + "/";
        return url.replace(prefix, "");
    }
}
