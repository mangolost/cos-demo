package com.mangolost.cos.controller;

import com.mangolost.cos.common.CommonResult;
import com.mangolost.cos.service.CosService;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/api/cos")
public class CosController {

    @Autowired
    private CosService cosService;

    /**
     *
     * @param prefix
     * @return
     */
    @RequestMapping("object/list")
    public CommonResult list(@RequestParam String prefix) {
        CommonResult commonResult = new CommonResult();
        List<COSObjectSummary> list = cosService.listObjects(prefix);
        return commonResult.setData(list);
    }

    /**
     *
     * @param key
     * @return
     */
    @RequestMapping("object/metadata")
    public CommonResult metadata(@RequestParam String key) {
        CommonResult commonResult = new CommonResult();
        ObjectMetadata objectMetadata = cosService.metadata(key);
        return commonResult.setData(objectMetadata);
    }

    /**
     *
     * @param key
     * @return
     */
    @RequestMapping("object/downloadFile")
    public CommonResult downloadFile(@RequestParam String key) {
        CommonResult commonResult = new CommonResult();
        cosService.downloadFile(key);
        return commonResult;
    }

    /**
     * 获取腾讯云下载url
     * @param key
     * @return
     */
    @RequestMapping("object/url")
    public CommonResult getDownLoadUrl(String key) {
        CommonResult commonResult = new CommonResult();
        String url = cosService.getDownloadUrl(key);
        return commonResult.setData(url);
    }



}
