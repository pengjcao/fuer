package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InstitutionFileDTO {

    /**
     * 主键（可空，第一次插入不传）
     */
    private Integer institutionFileId;

    /**
     * 机构ID
     */
    private String institutionId;

    /** 临床试验管理制度 */
    private MultipartFile trialManagementFile;

    /** 临床试验标准操作规程 */
    private MultipartFile standardOperationFile;

    /** 临床试验应急预案 */
    private MultipartFile emergencyPlanFile;

    /** 年度培训计划 */
    private MultipartFile trainingPlanFile;

    /** 年度质控计划 */
    private MultipartFile qualityPlanFile;

    /** 其他文件 */
    private MultipartFile otherFile;
}
