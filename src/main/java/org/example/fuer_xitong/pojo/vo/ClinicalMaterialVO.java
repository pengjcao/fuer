package org.example.fuer_xitong.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ClinicalMaterialVO {
    private Long id;
    private Integer piInfoId;
    private String projectName;

    // 数据库存储的原始路径串，多个文件按换行分隔
    @JsonIgnore
    private String nmpaApprovalPath;
    @JsonIgnore
    private String delegationTablePath;
    @JsonIgnore
    private String trainingRecordPath;
    @JsonIgnore
    private String processFilesPath;
    @JsonIgnore
    private String completionFilesPath;
    @JsonIgnore
    private String otherFilesPath;

    // 返回给前端的可访问 URL 列表
    private List<String> nmpaApprovalPaths;
    private List<String> delegationTablePaths;
    private List<String> trainingRecordPaths;
    private List<String> processFilesPaths;
    private List<String> completionFilesPaths;
    private List<String> otherFilesPaths;
}
