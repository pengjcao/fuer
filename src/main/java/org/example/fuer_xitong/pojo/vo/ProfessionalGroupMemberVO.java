package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProfessionalGroupMemberVO {

    private Integer id;

    /** 科室 */
    private Integer departmentId;
    private String keshi;

    /** 专业组 */
    private String groupPath;

    /** 人员信息 */
    private String personType;
    private String name;
    private String academicPosition;
    private String talentTitle;

    /** 专业组任职（数据库原始字段）*/
    private String roles;   // <- 先用 String 接收

    /** 转换后的前端多选 */
    private List<String> rolesList; // 前端用

    private String resumeFileUrl;

    /** 证书 */
    private String gcpCertUrl;
    private String practiceCertUrl;

    private String createBy;
    private Date createTime;
}
