package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

@Data
public class DeptProfessionalGroupCreateDTO {

    private Integer departmentId;
    private String keshi;

    /**
     * 专业组完整路径
     * 如：泌尿外科专业
     * 或：泌尿外科专业/肾病移植项目
     */
    private String groupPath;

    private String createBy;
}
