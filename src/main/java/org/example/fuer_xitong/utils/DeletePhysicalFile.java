package org.example.fuer_xitong.utils;

import java.io.File;

public class DeletePhysicalFile {
    /**
     * 删除物理文件（安全版）
     * ⚠️ 失败只记录日志，不抛异常
     */
    public static void deleteFile(String filePath) {

        if (filePath == null || filePath.isEmpty()) {
            return;
        }

        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    // 建议用日志，不要 throw
                    System.err.println("物理文件删除失败：" + filePath);
                }
            }
        } catch (Exception e) {
            // ⚠️ 绝对不要影响数据库事务
            System.err.println("删除物理文件异常：" + filePath);
            e.printStackTrace();
        }
    }

    private DeletePhysicalFile() {
    }

    /**
     * 递归删除目录（不抛异常）
     */
    public static void deleteDirectoryQuietly(String dirPath) {

        if (dirPath == null || dirPath.isEmpty()) {
            return;
        }

        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                return;
            }
            deleteRecursively(dir);
        } catch (Exception e) {
            // 吞掉异常，避免影响业务
        }
    }

    private static void deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        file.delete();
    }




}
