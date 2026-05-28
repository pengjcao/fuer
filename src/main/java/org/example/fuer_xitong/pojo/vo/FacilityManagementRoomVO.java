package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FacilityManagementRoomVO {

    private Long id;

    private String keshi;

    private String campus;

    private String location;

    private String photo; // 前端显示URL

    private Integer hasTempHumidityRecord;

    private Integer hasAccessRecord;

    private Integer hasFileBorrowRecord;

    private Integer hasProtectionCondition;

    private String createBy;

    private Date createTime;
}