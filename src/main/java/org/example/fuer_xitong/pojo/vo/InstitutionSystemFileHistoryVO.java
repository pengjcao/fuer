package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstitutionSystemFileHistoryVO {

    private Long id;             // 历史记录主键
    private String fileName;     // 文件名
    private String currentPath;  // 文件路径
    private String operatedBy;   // 操作人
    private String remark;       // 操作备注
    private LocalDateTime createdTime; // 操作时间
}
