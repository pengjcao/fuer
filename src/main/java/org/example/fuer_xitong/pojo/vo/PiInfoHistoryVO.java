package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PiInfoHistoryVO {

    private String id;
    private String professional;
    /**
     * 申请类型：
     * 0 - 新增 PI
     * 1 - 新增专业组
     */
    private Integer applyType;

    /**
     * 当前审批节点
     * =0 表示驳回，允许回退历史数据
     */
    private int currentStep;

    // ================= 文件路径 =================
    private String piPhotoPath;
    private String seniorTitleCertificatePath;
    private String seniorTitleAppointmentPath;
    private String signedResumePath;
    private String qualificationCertificatePath;
    private String practiceCertificatePath;
    private String gcpCertificatePath;

    /**
     * 专业组自评报告文件路径
     */
    private String reportFilePath;

    // ================= 业务字段 =================
    private String shanchang;
    private Boolean clinicalParticipation;
    private String clinicalReason;

    /**
     * 临床材料（如果你在历史页需要展示）
     */
    private List<ClinicalMaterialVO> clinicalMaterials;

    /**
     * 专业组备案类型（逗号分隔）
     */
    private String recordTypes;

    /**
     * 专业组涉及院区（逗号分隔）
     */
    private String hospitalAreas;

    /**
     * pi_info 主键（用于重新提交）
     */
    private int piInfoId;
}
