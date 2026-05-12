package ltd.newbee.mall.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ltd.newbee.mall.util.AliOssUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/admin")
public class UploadController {

    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;

    // 注入我们写好的 OSS 工具类
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 单文件上传
     */
    @PostMapping({"/upload/file"})
    @ResponseBody
    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        try {
            // 通过UUID保证文件名不重复并动态地把原始文件名后缀截取
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = "bubble/" + UUID.randomUUID().toString() + extension;

            // 核心：调用工具类上传并获取返回的URL
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            Result resultSuccess = ResultGenerator.genSuccessResult();
            resultSuccess.setData(filePath);
            return resultSuccess;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }

    /**
     * 多文件上传（用于商品相册等多图场景）
     */
    @PostMapping({"/upload/files"})
    @ResponseBody
    public Result uploadV2(HttpServletRequest httpServletRequest) {
        List<MultipartFile> multipartFiles = new ArrayList<>(8);
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames();
            int totalCount = 0;
            while (iter.hasNext()) {
                if (totalCount > 5) {
                    return ResultGenerator.genFailResult("最多上传5张图片");
                }
                totalCount += 1;
                MultipartFile file = multiRequest.getFile(iter.next());
                multipartFiles.add(file);
            }
        }
        if (CollectionUtils.isEmpty(multipartFiles) || multipartFiles.size() > 5) {
            return ResultGenerator.genFailResult("参数异常或最多上传5张图片");
        }

        List<String> fileNames = new ArrayList<>(multipartFiles.size());

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String originalFilename = multipartFile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String objectName = "bubble/" + UUID.randomUUID().toString() + extension;

                String filePath = aliOssUtil.upload(multipartFile.getBytes(), objectName);
                fileNames.add(filePath);
            } catch (IOException e) {
                log.error("多文件上传至阿里云OSS失败: {}", e.getMessage(), e);
                return ResultGenerator.genFailResult("文件上传失败");
            }
        }

        Result resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(fileNames);
        return resultSuccess;
    }
}