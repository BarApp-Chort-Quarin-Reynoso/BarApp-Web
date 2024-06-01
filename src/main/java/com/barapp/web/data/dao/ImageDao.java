package com.barapp.web.data.dao;

import java.io.InputStream;

public interface ImageDao {
    String saveImage(String dest, InputStream inputStream, String id, String contentType);
}
