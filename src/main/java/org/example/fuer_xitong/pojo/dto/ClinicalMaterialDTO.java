package org.example.fuer_xitong.pojo.dto;

import org.springframework.web.multipart.MultipartFile;

public class ClinicalMaterialDTO {
    private String projectName;
    private MultipartFile nmpaApproval;
    private MultipartFile delegationTable;
    private MultipartFile trainingRecord;
    private MultipartFile processFiles;
    private MultipartFile completionFiles;
    private MultipartFile otherFiles;
}
