package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FacilitySampleStorageRoomVO {

    private Long id;

    private String keshi;

    private String campus;

    private String location;

    private String photo;

    private Integer hasSampleFridge;

    private Integer hasAccessRecord;

    private Integer hasTempHumidityRecord;

    private Integer hasMaintenanceRecord;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}