package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SystemNoticeVO {

    private Integer noticeId;

    /** 富文本标题 HTML */
    private String title;

    /** 富文本正文 HTML */
    private String content;

    /** 数据库存的 JSON 字符串 */
    private String attachmentPaths;

    /** 给前端用的 URL 列表（不入库） */
    private List<String> attachmentUrls;

    private String publisherId;
    private Integer publisherRole;

    private LocalDateTime createTime;
}

