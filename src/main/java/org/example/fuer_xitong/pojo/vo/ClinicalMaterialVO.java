package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

@Data
public class ClinicalMaterialVO {
    private String projectName;

    // 文件路径
    private String nmpaApprovalPath;
    private String delegationTablePath;
    private String trainingRecordPath;
    private String processFilesPath;
    private String completionFilesPath;
    private String otherFilesPath;
}
