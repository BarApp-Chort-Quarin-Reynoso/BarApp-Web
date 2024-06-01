package com.barapp.web.data.impl;

import com.barapp.web.data.dao.ImageDao;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Repository
public class ImageDaoImpl implements ImageDao {
    private final StorageClient storageClient;

    public ImageDaoImpl(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    @Override
    public String saveImage(String dest, InputStream inputStream, String id, String contentType) {
        String blobString = String.format(dest, id, contentType.substring(contentType.indexOf("/") + 1));
        Blob blob = storageClient
                .bucket()
                .create(blobString, inputStream, contentType, Bucket.BlobWriteOption.userProject("barapp-b1bc0"));
        URL signedUrl = blob.signUrl(32850, TimeUnit.DAYS);

        return signedUrl.toString();
    }
}
