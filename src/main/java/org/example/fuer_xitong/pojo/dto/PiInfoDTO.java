package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PiInfoDTO {
    private String Id;
    private String professional;
    private MultipartFile piPhoto;
    private MultipartFile seniorTitleCertificate;
    private MultipartFile seniorTitleAppointment;
    private MultipartFile signedResume;
    private MultipartFile qualificationCertificate;
    private MultipartFile practiceCertificate;
    private MultipartFile gcpCertificate;
    private String shanchang;

    private Boolean clinicalParticipation; // 是否上传临床材料
    private String clinicalReason;         // 不上传时说明原因

    private List<ClinicalMaterialDTO> clinicalMaterials; // 上传的临床材料列表


    // ========== 专业组备案 ==========
    private List<String> recordTypes; // 专业组备案类型
    private List<String> hospitalAreas; // 专业组涉及院区
    private MultipartFile selfAssessmentReport; // 自评报告
}

