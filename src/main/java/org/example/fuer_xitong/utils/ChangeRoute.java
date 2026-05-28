package org.example.fuer_xitong.utils;

import org.springframework.beans.factory.annotation.Value;

public class ChangeRoute {
    @Value("${file.base-url}")
    private String baseUrl;

    @Value("${file.upload-path}")
    private String uploadPath;

    private String toFileUrl(String physicalPath) {

        if (physicalPath == null || physicalPath.isEmpty()) {
            return null;
        }

        // 统一斜杠
        String normalizedPhysical = physicalPath.replace("\\", "/");
        String normalizedRoot = uploadPath.replace("\\", "/");

        // 确保 uploadPath 以 / 结尾
        if (!normalizedRoot.endsWith("/")) {
            normalizedRoot += "/";
        }

        if (!normalizedPhysical.startsWith(normalizedRoot)) {

            return null;
        }

        String relativePath = normalizedPhysical.substring(normalizedRoot.length());

        return baseUrl + "/files/" + relativePath;
    }

}
