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
}

