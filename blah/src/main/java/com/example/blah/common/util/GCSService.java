package com.example.blah.common.util;

import java.io.IOException;

public interface GCSService {
	void uploadObject(GCSRequest gcsRequest) throws IOException;
}
