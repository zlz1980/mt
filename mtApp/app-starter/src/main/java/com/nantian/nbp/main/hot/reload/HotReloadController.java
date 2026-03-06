package com.nantian.nbp.main.hot.reload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 对原有bean进行覆盖时，beanName保持一样，类全包名不能与原有一样
 */

@RequestMapping("reload")
@RestController
public class HotReloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotReloadController.class);

    private final HotReloadSpringBeanService hotReloadSpringBeanService;

    public HotReloadController(HotReloadSpringBeanService hotReloadSpringBeanService) {
        this.hotReloadSpringBeanService = hotReloadSpringBeanService;
    }

    @RequestMapping("jar")
    public Map<String,String> uploadAndReload(@RequestParam("file") MultipartFile file) {
        validateUploadedFile(file);
        File tempFile = null;
        Map<String,String> res = new HashMap<>(3);
        res.put("note","对原有bean进行覆盖时，beanName保持不变，类全包名不能与原有一致");
        try{
            // 创建临时文件并保存上传内容
            tempFile = File.createTempFile("HotReload", ".jar");
            file.transferTo(tempFile);
            hotReloadSpringBeanService.reloadBeanFromJar(tempFile);
        } catch (Exception e) {
            LOGGER.error("uploadAndReload exception",e);
            res.put("code","500");
            res.put("msg",e.getMessage());
        }finally {
            if(Objects.nonNull(tempFile)){
                boolean resFlag = tempFile.delete();
                LOGGER.info("Del Res[{}] file[{}]",resFlag,tempFile.getPath());
            }
        }
        res.put("code","200");
        res.put("msg","SUCCESS");

        return res;
    }

    private void validateUploadedFile(MultipartFile file) {
        // 文件大小限制（例如50MB）
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小超过限制");
        }
        // 文件类型检查
        String fileName = file.getOriginalFilename();
        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        // 长度检查
        if (fileName.length() > 255) {
            throw new IllegalArgumentException("文件名长度超过限制(255字符)");
        }
        if (!fileName.toLowerCase().endsWith(".jar")) {
            throw new IllegalArgumentException("只允许上传JAR文件");
        }
        // 禁止敏感目录访问
        if (fileName.contains("..") ||
                fileName.contains("/") ||
                fileName.contains("&") ||
                fileName.contains("\\")) {
            throw new IllegalArgumentException("文件名包含非法字符");
        }
    }
}
