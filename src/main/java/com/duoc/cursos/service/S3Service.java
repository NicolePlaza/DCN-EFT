package com.duoc.cursos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    @Value("${aws.s3-bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    private S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    public String subirMaterial(String key, byte[] contenido) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/pdf")
                .build();
        s3Client().putObject(putRequest, RequestBody.fromBytes(contenido));
        return key;
    }

    public byte[] descargarMaterial(String key) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client().getObjectAsBytes(getRequest);
        return objectBytes.asByteArray();
    }
}
