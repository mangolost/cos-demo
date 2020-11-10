package com.mangolost.cos.service;

import com.mangolost.cos.config.CosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class CosService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CosService.class);

    private static final int MAX_KEY_NUM = 1000; //设置最大遍历出多少个对象, 单次最大支持1000

    @Autowired
    private CosProperties cosProperties;

    /**
     *
     * @return
     */
    private COSClient getCOSClient() {
        // 1 初始化用户身份信息
        COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
        // 2 设置bucket的区域
        ClientConfig clientConfig = new ClientConfig(new Region(cosProperties.getRegion()));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        return cosclient;
    }

    /**
     *
     * @param prefix
     */
    public List<COSObjectSummary> listObjects(String prefix) {
        COSClient cosClient = getCOSClient();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称, 需包含appid
        listObjectsRequest.setBucketName(cosProperties.getBucketName());
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix(prefix);
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(MAX_KEY_NUM);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
        ObjectListing objectListing = null;
        try {
            objectListing = cosClient.listObjects(listObjectsRequest);
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            return cosObjectSummaries;
        } catch (Exception e) {
            LOGGER.error("listObjects error: ", e);
            return null;
        } finally {
            cosClient.shutdown();
        }
    }

    /**
     *
     * @param key
     */
    public ObjectMetadata metadata(String key) {
        COSClient cosClient = getCOSClient();
        String bucketName = cosProperties.getBucketName();
        try {
            ObjectMetadata objectMetadata = cosClient.getObjectMetadata(bucketName, key);
            return objectMetadata;
        } catch (Exception e) {
            LOGGER.error("metadata error: ", e);
            return null;
        } finally {
            // 关闭客户端
            cosClient.shutdown();
        }
    }



    /**
     *
     * @param key
     */
    public void downloadFile(String key) {
        COSClient cosClient = getCOSClient();
        String bucketName = cosProperties.getBucketName();
        boolean useTrafficLimit = false;
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        if(useTrafficLimit) {
            getObjectRequest.setTrafficLimit(8*1024*1024);
        }
        File localFile = new File("download/download1.txt");
        try {
            ObjectMetadata objectMetadata = cosClient.getObject(getObjectRequest, localFile);
            System.out.println(objectMetadata.getContentLength());
        } catch (Exception e) {
            LOGGER.error("downloadFile error: ", e);
        } finally {
            cosClient.shutdown();
        }

    }

    /**
     *
     * @param key
     */
    public String getDownloadUrl(String key) {
        COSClient cosClient = getCOSClient();
        String bucketName = cosProperties.getBucketName();
        // 设置URL过期时间为1小时 60*60*1000()
        Date expiration = new Date(new Date().getTime() + 60 * 60 * 1000);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(5分钟)
        generatePresignedUrlRequest.setExpiration(expiration);
        try {
            String url = cosClient.generatePresignedUrl(generatePresignedUrlRequest).toString();
            LOGGER.info("获取腾讯云url：{}", url);
            return url;
        } catch (Exception e) {
            LOGGER.error("getDownloadUrl error: ", e);
            return null;
        } finally {
            cosClient.shutdown();
        }

    }


}