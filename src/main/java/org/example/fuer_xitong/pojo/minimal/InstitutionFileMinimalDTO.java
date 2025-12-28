package org.example.fuer_xitong.pojo.minimal;

import lombok.Data;

@Data
public class InstitutionFileMinimalDTO {

    /**
     * 自增主键（insert 后回填）
     */
    private Integer institutionFileId;

    /**
     * 机构ID（业务主键）
     */
    private String institutionId;

    /**
     * 创建人ID
     */
    private String createdBy;
}
