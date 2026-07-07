package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProfessionalGroupMemberCreateDTO {

    /** 科室信息 */
    private Integer departmentId;
    private String keshi;

    /** 专业组（支持 一级 / 两级，如：泌尿外科专业/肾病移植项目） */
    private String groupPath;

    /** 人员基本信息 */
    private String personType;   // 研究医生 / 研究护士 / 其他
    private String name;
    private String academicPosition;
    private String talentTitle;

    /** 专业组任职（多选，List<String> 接收） */
    private List<String> roles;

    /** 简历 */
    private String resumeText;
    private MultipartFile resumeFile;

    /** 证书文件（前端传文件，数据库存路径） */
    private MultipartFile gcpCertFile;
    private MultipartFile practiceCertFile;

    /** 创建人 */
    private String createBy;
}
