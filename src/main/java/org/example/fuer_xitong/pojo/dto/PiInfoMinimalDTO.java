package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

@Data
public class PiInfoMinimalDTO {
    private String id;               // 研究者工号
    private String professional;     // 专业
    private Integer piInfoId;        // 用于接收自增主键



    private String recordTypes;     // 专业组备案类型，逗号分隔
    private String hospitalAreas;   // 所属院区，逗号分隔
    private String reportFilePath;  // 自评报告存储路径
}
