package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SystemNoticePublishDTO {

    /**
     * 通知标题（富文本HTML）
     */

    private String title;

    /**
     * 通知正文（富文本HTML）
     */

    private String content;


    /** 附件（可多个） */
    private MultipartFile[] files;

    /**
     * 发布通知选择的分组ID列表
     */
    private List<Integer> groupIds;
}
