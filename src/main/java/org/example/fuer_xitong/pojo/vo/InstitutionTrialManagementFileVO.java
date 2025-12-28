package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstitutionTrialManagementFileVO {

    /**
     * 历史表主键ID
     */
    private Integer id;

    /**
     * 机构ID
     */
    private String institutionId;

    /**
     * 主表ID，对应 institution_file.institution_file_id
     */
    private Integer institutionFileId;

    /**
     * 文件路径（数据库原始路径）
     */
    private String filePath;

    /**
     * 是否有效：1=有效，0=作废
     */
    private Integer isInvalid;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 文件URL（前端可直接访问）
     */
    private String fileUrl;
}