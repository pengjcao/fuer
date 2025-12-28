package org.example.fuer_xitong.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstitutionFile {

    private Integer institutionFileId;

    private String institutionId;

    private String trialManagementPath;
    private String standardOperationPath;
    private String emergencyPlanPath;

    private String trainingPlanPath;
    private String qualityPlanPath;

    private String otherFilePath;

    private String createdBy;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
