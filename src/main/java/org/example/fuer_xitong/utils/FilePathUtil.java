package org.example.fuer_xitong.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FilePathUtil {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.base-url}")
    private String baseUrl;

    public String buildUploadDir(String... paths) {
        Path dir = Paths.get(getEffectiveUploadPath());
        if (paths != null) {
            for (String item : paths) {
                if (item == null || item.trim().isEmpty()) {
                    continue;
                }
                dir = dir.resolve(item.trim());
            }
        }
        return ensureEndSlash(dir.toString());
    }

    public String saveFile(MultipartFile file, String baseDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            File folder = new File(baseDir);
            if (!folder.exists() && !folder.mkdirs()) {
                throw new RuntimeException("上传目录创建失败: " + folder.getAbsolutePath());
            }
            if (!folder.isDirectory()) {
                throw new RuntimeException("上传路径不是目录: " + folder.getAbsolutePath());
            }

            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.trim().isEmpty()) {
                originalName = "file";
            }
            originalName = originalName.replace("\\", "/");
            originalName = Paths.get(originalName).getFileName().toString();

            String fileName = System.currentTimeMillis() + "_" + originalName;
            File dest = new File(folder, fileName).getAbsoluteFile();
            file.transferTo(dest);

            return normalizeSlash(dest.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }

    public String toFileUrl(String dbPath) {
        if (dbPath == null || dbPath.trim().isEmpty()) {
            return null;
        }

        String normalized = normalizeSlash(dbPath.trim());
        String filesPrefix = "/files/";
        String apiFilesPrefix = "/api/files/";

        if (normalized.startsWith("http://") || normalized.startsWith("https://")) {
            int filesIndex = normalized.indexOf(filesPrefix);
            if (filesIndex < 0) {
                return normalized;
            }

            String afterFiles = normalized.substring(filesIndex + filesPrefix.length());
            if (looksLikePhysicalPath(afterFiles)) {
                return toFileUrl(afterFiles);
            }
            return buildFileUrl(afterFiles);
        }

        if (normalized.startsWith(filesPrefix)) {
            return buildFileUrl(normalized.substring(filesPrefix.length()));
        }

        if (normalized.startsWith(apiFilesPrefix)) {
            return buildFileUrl(normalized.substring(apiFilesPrefix.length()));
        }

        String relativePath = toRelativePath(normalized);
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }

        return buildFileUrl(relativePath);
    }

    public String toStoragePath(String fileUrlOrPath) {
        if (fileUrlOrPath == null || fileUrlOrPath.trim().isEmpty()) {
            return null;
        }

        String normalized = normalizeSlash(fileUrlOrPath.trim());
        if (normalized.startsWith("http://") || normalized.startsWith("https://")) {
            int filesIndex = normalized.indexOf("/files/");
            if (filesIndex >= 0) {
                String relativePath = decodeUrlPath(normalized.substring(filesIndex + "/files/".length()));
                return normalizeSlash(Paths.get(getEffectiveUploadPath()).resolve(relativePath).toString());
            }
            return normalized;
        }

        String filesPrefix = "/files/";
        String apiFilesPrefix = "/api/files/";
        if (normalized.startsWith(filesPrefix)) {
            return normalizeSlash(Paths.get(getEffectiveUploadPath()).resolve(decodeUrlPath(normalized.substring(filesPrefix.length()))).toString());
        }
        if (normalized.startsWith(apiFilesPrefix)) {
            return normalizeSlash(Paths.get(getEffectiveUploadPath()).resolve(decodeUrlPath(normalized.substring(apiFilesPrefix.length()))).toString());
        }

        return normalized;
    }

    private String decodeUrlPath(String path) {
        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }

    public String getUploadRootLocation() {
        return "file:" + ensureEndSlash(normalizeSlash(getEffectiveUploadPath()));
    }

    public String getLegacyUploadRootLocation() {
        return "file:" + ensureEndSlash(normalizeSlash(getEffectiveUploadPath() + "upload"));
    }

    public String getNestedUploadRootLocation() {
        return "file:" + ensureEndSlash(normalizeSlash(Paths.get(getEffectiveUploadPath()).resolve("upload").toString()));
    }

    private String toRelativePath(String path) {
        String effectiveUploadPath = getEffectiveUploadPath();
        String normalizedRoot = ensureEndSlash(normalizeSlash(effectiveUploadPath));
        String legacyRoot = ensureEndSlash(normalizeSlash(effectiveUploadPath + "upload"));
        String nestedRoot = ensureEndSlash(normalizeSlash(Paths.get(effectiveUploadPath).resolve("upload").toString()));

        if (path.startsWith(normalizedRoot)) {
            return path.substring(normalizedRoot.length());
        }

        if (path.startsWith(legacyRoot)) {
            return path.substring(legacyRoot.length());
        }

        if (path.startsWith(nestedRoot)) {
            return path.substring(nestedRoot.length());
        }

        if (looksLikePhysicalPath(path)) {
            int uploadIndex = path.lastIndexOf("/upload/");
            if (uploadIndex >= 0) {
                return path.substring(uploadIndex + "/upload/".length());
            }

            int colonIndex = path.indexOf(":");
            if (colonIndex >= 0 && colonIndex + 1 < path.length()) {
                String noDisk = path.substring(colonIndex + 1);
                while (noDisk.startsWith("/")) {
                    noDisk = noDisk.substring(1);
                }
                return noDisk;
            }
        }

        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    private boolean looksLikePhysicalPath(String path) {
        return path.matches("^[A-Za-z]:/.*") || path.startsWith("/");
    }

    private String getEffectiveUploadPath() {
        String configuredPath = uploadPath == null ? "" : uploadPath.trim();
        if (configuredPath.isEmpty()) {
            configuredPath = "upload";
        }

        String normalized = normalizeSlash(configuredPath);
        boolean isWindowsPath = normalized.matches("^[A-Za-z]:/.*");
        boolean runningOnWindows = System.getProperty("os.name", "")
                .toLowerCase()
                .contains("win");

        if (isWindowsPath && !runningOnWindows) {
            return "/opt/upload";
        }

        return normalized;
    }

    private String normalizeSlash(String path) {
        return path == null ? null : path.replace("\\", "/");
    }

    private String ensureEndSlash(String path) {
        String normalized = normalizeSlash(path);
        if (normalized == null || normalized.endsWith("/")) {
            return normalized;
        }
        return normalized + "/";
    }

    private String trimEndSlash(String path) {
        if (path == null) {
            return "";
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    private String buildFileUrl(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            return null;
        }

        String normalizedRelativePath = normalizeSlash(relativePath.trim());
        while (normalizedRelativePath.startsWith("/")) {
            normalizedRelativePath = normalizedRelativePath.substring(1);
        }

        String prefix = trimEndSlash(baseUrl);
        if (prefix.isEmpty()) {
            prefix = "/api";
        }
        return prefix + "/files/" + normalizedRelativePath;
    }
}
