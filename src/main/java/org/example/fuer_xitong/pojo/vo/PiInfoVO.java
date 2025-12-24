package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PiInfoVO {
    private String id;
    private String professional;

    // 文件 URL
    private String piPhotoPath;
    private String seniorTitleCertificatePath;
    private String seniorTitleAppointmentPath;
    private String signedResumePath;
    private String qualificationCertificatePath;
    private String practiceCertificatePath;
    private String gcpCertificatePath;

    private String shanchang;
    private Boolean clinicalParticipation;
    private String clinicalReason;

    private List<ClinicalMaterialVO> clinicalMaterials;

    // 审批信息
    private String applyStatus;
    private int currentStep;
    private Date submitTime;

    private int piInfoId;


    private String recordTypes;


    /**
     * 专业组涉及院区
     */
    private String hospitalAreas;

    /**
     * 专业组自评报告文件路径
     */
    private String reportFilePath;
}
