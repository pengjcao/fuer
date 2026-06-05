package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ClinicalMaterialDTO {
    private String projectName;
    private List<MultipartFile> nmpaApproval;
    private List<MultipartFile> delegationTable;
    private List<MultipartFile> trainingRecord;
    private List<MultipartFile> processFiles;
    private List<MultipartFile> completionFiles;
    private List<MultipartFile> otherFiles;
}
