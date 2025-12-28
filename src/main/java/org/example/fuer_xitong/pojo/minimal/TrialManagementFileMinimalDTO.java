package org.example.fuer_xitong.pojo.minimal;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class TrialManagementFileMinimalDTO {

    private Integer institutionTrialManagementFileId; // 自增主键
    private String institutionId;
    private Integer institutionFileId;
    private String filePath;
    private Integer isInvalid = 0; // 默认作废标志 0
    private String createdBy; // 上传人ID
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}