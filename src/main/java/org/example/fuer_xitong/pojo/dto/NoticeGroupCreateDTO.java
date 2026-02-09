package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class NoticeGroupCreateDTO {


    private String groupName;

    /**
     * 该分组包含的研究者ID列表
     */

    private List<String> userIds;


    private String creatorId;

    // 只用于回填，不需要前端传
    private Integer groupId;
}
