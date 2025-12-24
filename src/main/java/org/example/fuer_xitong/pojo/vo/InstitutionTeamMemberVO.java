package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InstitutionTeamMemberVO {

    private int ziziId;

    /** 机构成员工号 */
    private String institutionMemberId;

    /** 机构ID */
    private String institutionId;

    /** 姓名 */
    private String name;

    /** 任职（数组形式） */
    private List<String> positions;

    /** 数据库存的原始字符串（仅后端用） */
    private String positionsStr;

/** 文件存储路径或访问 URL */
    private String resumePath;

    private String gcpPath;

    private String licensePath;


    /** 创建人/维护人工号 */
    private String createById;

    /** 创建时间 */
    private LocalDateTime createTime;
}
