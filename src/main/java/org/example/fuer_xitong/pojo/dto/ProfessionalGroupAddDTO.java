package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProfessionalGroupAddDTO {

      /* 专业组备案类型（多选结构，当前只允许 1 个）
            * 例如：药物临床试验 / 医疗器械临床试验
     */
    private List<String> recordTypes;

    /**
     * 专业组备案名称（多选结构）
     */
    private List<String> recordNames;

    /**
     * 专业组备案院区（多选结构）
     */
    private List<String> hospitalAreas;

    /**
     * 专业组自评报告（Word）
     */
    private MultipartFile selfAssessmentReport;
}
