package com.bannad927.cosplay.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 程彬彬
 */
@Controller
@Slf4j
public class ImageController {

    @Value("${receivePath}")
    private String receivePath;

    /**
     * 多文件上传
     */
    @RequestMapping(value = "/uploadData")
    @ResponseBody
    public Map<String, Object> handleFileUpload(HttpServletRequest request) {
        Map<String, Object> resultData = new HashMap<>();
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String fileName = file.getOriginalFilename();
                    String suffixName = fileName.substring(fileName.lastIndexOf("."));
                    // 解决中文问题，liunx下中文路径，图片显示问题
                    fileName = getNowTime("yyyyMMddHHmmssSSS") + ((i++) + suffixName);
                    String imagePath = receivePath + fileName;
                    File dest = new File(imagePath);
                    // 检测是否存在目录
                    if (!dest.getParentFile().exists()) {
                        dest.getParentFile().mkdirs();
                    }

                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(dest));
                    stream.write(bytes);
                    stream.close();

                } catch (Exception e) {
                    stream = null;
                    resultData.put("code", -1);
                    resultData.put("msg", "上传错误");
                    return resultData;
                }
            } else {
                resultData.put("code", -1);
                resultData.put("msg", "选择要上传文件");
                return resultData;
            }
        }

        resultData.put("code", 0);
        resultData.put("msg", "上传成功");
        resultData.put("data", "");
        return resultData;
    }

    public static String getNowTime(String pattern) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }
}
