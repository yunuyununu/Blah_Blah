package com.example.blah.common.util;

import java.io.IOException;

public interface GCSService {
	String uploadObject(GCSRequest gcsRequest) throws IOException;
	
	String uploadCompany(GCSRequest gcsRequest) throws IOException;
	
	String uploadCompanyChange(GCSRequest gcsRequest) throws IOException;
	
	String uploadJoinChange(GCSRequest gcsRequest) throws IOException;
	
	String uploadBoard(GCSRequest gcsRequest) throws IOException;
	
	boolean deleteFile(String imageUrl);
}
