package com.syduck.hitchaccount.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syduck.hitchcommons.enums.BusinessErrors;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import com.syduck.hitchmodules.po.VehiclePO;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

@Component
public class AiHelper {

    @Value("${baidu.apikey}")
    private String API_KEY;
    @Value("${baidu.secretkey}")
    private String SECRET_KEY;

    private final static Logger logger = LoggerFactory.getLogger(AiHelper.class);
    final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();


    //获取车牌号
    public String getLicense(VehiclePO vehiclePO) throws IOException {
        //通过vehiclePO.getCarFrontPhoto()获取车辆前部照片的URL
        String front = vehiclePO.getCarFrontPhoto();
        //创建临时文件对象
        File tempFile = new File(Objects.requireNonNull(AiHelper.class.getResource("/"))
                .getPath() +"front-" +vehiclePO.getId() +front.substring(front.lastIndexOf(".")));
        //这行代码会下载 URL 的内容（照片数据）并保存到 tempFile 中
        FileUtils.copyURLToFile(new URL(vehiclePO.getCarFrontPhoto()),tempFile);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String image = getFileContentAsBase64(tempFile.getAbsolutePath(),true);
        RequestBody body = RequestBody.create(mediaType, "image="+image);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        assert response.body() != null;
        String json = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        if (jsonNode.get("error_code") != null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_STATUS_ERROR,jsonNode.get("error_msg").asText());
        }
        // 删除临时文件
        if (tempFile.exists()) {
            tempFile.delete();
        }
        // 获取车牌号
        return jsonNode.get("words_result").get("number").asText();
    }

    //Base64 是一种将二进制数据（比如图片）转为字符串的编码方式
    public String getFileContentAsBase64(String path,boolean urlEncode) throws IOException{
        byte[] b = Files.readAllBytes(Paths.get(path));
        String base64 = Base64.getEncoder().encodeToString(b);
        if (urlEncode){
            base64 = URLEncoder.encode(base64, StandardCharsets.UTF_8);
        }
        return base64;
    }

    //生成token
    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        assert response.body() != null;
        return new ObjectMapper().readTree(response.body().string()).get("access_token").asText();
    }
}
