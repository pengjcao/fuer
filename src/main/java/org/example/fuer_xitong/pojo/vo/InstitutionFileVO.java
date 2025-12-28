package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

@Data
public class InstitutionFileVO {

    private Integer institutionFileId;

    private String institutionId;

    private String trialManagementPath;       // 临床试验管理制度路径
    private String standardOperationPath;     // 临床试验标准操作规程路径
    private String emergencyPlanPath;         // 临床试验应急预案路径
    private String trainingPlanPath;          // 年度培训计划路径
    private String qualityPlanPath;           // 年度质控计划路径
    private String otherFilePath;             // 其他文件路径

    private String createdBy;
    private String createdTime;
    private String updatedTime;
}
