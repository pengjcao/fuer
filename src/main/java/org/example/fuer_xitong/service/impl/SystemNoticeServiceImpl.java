package org.example.fuer_xitong.service.impl;

import com.alibaba.fastjson.JSON;
import jakarta.transaction.Transactional;
import org.example.fuer_xitong.mapper.SystemNoticeMapper;
import org.example.fuer_xitong.pojo.dto.SystemNoticePublishDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.SystemNoticeVO;
import org.example.fuer_xitong.service.SystemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemNoticeServiceImpl implements SystemNoticeService {
    @Autowired
    private SystemNoticeMapper systemNoticeMapper;
    @Override
    @Transactional
    public void publish(SystemNoticePublishDTO dto) {

        // ================== 1. 获取当前操作人 ==================
        String publisherId = BaseContext.getCurrentId();
        Integer publisherRole = BaseContext.getCurrentRole();

        // ================== 2. 插入通知主体，获取 notice_id ==================
        // ================== 2. 插入通知主体，获取 notice_id ==================
        String groupIdsJson = dto.getGroupIds() != null ? JSON.toJSONString(dto.getGroupIds()) : null;
        systemNoticeMapper.insertNoticeMinimal(
                dto.getTitle(),
                dto.getContent(),
                publisherId,
                publisherRole,
                groupIdsJson
        );

        Integer noticeId = systemNoticeMapper.getLastInsertId();
        if (noticeId == null) {
            throw new RuntimeException("获取 notice_id 失败");
        }

        // ================== 3. 构建通知附件存储目录 ==================
        String baseDir = "D:/yan/upload/SystemNotice/"
                + noticeId + "/";

        // ================== 4. 保存附件 ==================
        List<String> pathList = new ArrayList<>();

        MultipartFile[] files = dto.getFiles();
        if (files != null) {
            for (MultipartFile file : files) {
                String filePath = saveFile(file, baseDir);
                if (filePath != null) {
                    pathList.add(filePath);
                }
            }
        }

        // ================== 5. 回写附件路径（JSON 数组） ==================
        if (!pathList.isEmpty()) {
            String attachmentPaths = JSON.toJSONString(pathList);
            systemNoticeMapper.updateAttachmentPaths(noticeId, attachmentPaths);
        }
    }


    private String saveFile(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) return null;

        try {

            File folder = new File(path);
            if (!folder.exists()) folder.mkdirs();

            // 文件名加时间戳，避免覆盖
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = path + fileName;

            // 保存文件到磁盘
            file.transferTo(new File(filePath));

            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }


    @Override
    public List<SystemNoticeVO> listAll() {

        List<SystemNoticeVO> list = systemNoticeMapper.selectAll();

        List<SystemNoticeVO> result = new ArrayList<>();
        for (SystemNoticeVO vo : list) {
            result.add(convertFilePaths(vo));
        }

        return result;
    }



    @Override
    public SystemNoticeVO getById(Integer noticeId) {
        SystemNoticeVO vo = systemNoticeMapper.selectById(noticeId);
        if (vo == null) {
            throw new RuntimeException("通知不存在或已删除");
        }

        // 附件路径转换为可访问 URL
        if (vo.getAttachmentPaths() != null && !vo.getAttachmentPaths().isEmpty()) {
            List<String> pathList = JSON.parseArray(vo.getAttachmentPaths(), String.class);
            List<String> urlList = new ArrayList<>();
            for (String path : pathList) {
                urlList.add(toFileUrl(path));
            }
            vo.setAttachmentUrls(urlList);
        }

        return vo;
    }


    @Override
    public List<SystemNoticeVO> listByUserGroups(String userId) {
        List<SystemNoticeVO> notices = systemNoticeMapper.selectByUserGroups(userId);

        return notices.stream()
                .map(this::convertFilePaths)
                .collect(Collectors.toList());
    }



    /**
     * 转换附件路径（磁盘路径 -> 前端访问 URL）
     */
    private SystemNoticeVO convertFilePaths(SystemNoticeVO vo) {

        if (vo.getAttachmentPaths() == null || vo.getAttachmentPaths().isEmpty()) {
            return vo;
        }

        // attachmentPaths 是 JSON 数组字符串
        List<String> pathList = JSON.parseArray(vo.getAttachmentPaths(), String.class);

        List<String> urlList = new ArrayList<>();
        for (String path : pathList) {
            urlList.add(toFileUrl(path));
        }

        // 前端用的字段（建议 VO 里单独放）
        vo.setAttachmentUrls(urlList);

        return vo;
    }
    private String toFileUrl(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) return null;
        return "http://localhost:8080/files/"
                + dbPath.replace("D:/yan/upload/", "");
    }

}

