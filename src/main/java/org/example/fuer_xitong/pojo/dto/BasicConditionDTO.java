package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

@Data
public class BasicConditionDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建人工号
     */
    private String createBy;

    /**
     * 备案院区，多个用逗号分隔
     */
    private String campusList;

    /**
     * 床位数
     */
    private Integer bedCount;

    /**
     * 住院人数（人次/年）
     */
    private Integer inpatientCount;

    /**
     * 平均日门急诊量（人次/日）
     */
    private Integer avgDailyOutpatientCount;

    /**
     * 病源病种
     */
    private String diseaseSource;


    private String keshi;
}