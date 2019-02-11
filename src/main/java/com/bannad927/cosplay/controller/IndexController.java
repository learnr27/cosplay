package com.bannad927.cosplay.controller;

import com.bannad927.cosplay.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 程彬彬
 */
@Controller
public class IndexController {


    @Value("${receivePath}")
    private String receivePath;

    @RequestMapping(value = "/index")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/uploadPic")
    public String uploadPic() {
        return "upload_pic.html";
    }


    @RequestMapping(value = "/getData")
    @ResponseBody
    public List<String> getData(int size,int page) {
        int start = (page-1)*size;
        int end=start+size;
        List<File> result = FileUtil.getFileSort(receivePath);
        int flag = result.size();
        if (end>flag){
            end = flag;
        }
        List<String> resultData = new ArrayList<>();
        result = result.subList(start, end);
        result.forEach(file -> {
            String src = "/cp/" + file.getName();
            String url = "<li><img style=\"width: 90%;height: 90%; margin: 2px; border: none;\" src=\"" + src + "\"></li>";
            resultData.add(url);
        });
        return resultData;
    }
}
