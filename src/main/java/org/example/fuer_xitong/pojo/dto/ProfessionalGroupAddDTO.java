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


    private List<String> recordNames;


    private List<String> hospitalAreas;


    private MultipartFile selfAssessmentReport;
}
