package org.example.fuer_xitong.pojo.minimal;

import lombok.Data;

@Data
public class DrugTrialSopFileMinimalDTO {

    /**
     * 主键ID（插入后回填）
     */
    private Integer institutionDrugTrialSopFileId;

    /**
     * 机构ID
     */
    private String institutionId;

    /**
     * institution_file 主表ID
     */
    private Integer institutionFileId;

    /**
     * 上传人ID
     */
    private String createdBy;
}
