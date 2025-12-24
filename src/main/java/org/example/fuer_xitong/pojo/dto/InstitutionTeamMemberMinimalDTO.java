package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

@Data
public class InstitutionTeamMemberMinimalDTO {

    /** 自增主键 */
    private Integer ziziId;

    /** 机构成员工号 */
    private String institutionMemberId;

    /** 机构ID */
    private String institutionId;

    /** 创建人 */
    private String createById;

    /** 姓名 */
    private String name;
}
