package com.example.blah.common.util;

import java.io.IOException;

public interface GCSService {
	String uploadObject(GCSRequest gcsRequest) throws IOException;
}
