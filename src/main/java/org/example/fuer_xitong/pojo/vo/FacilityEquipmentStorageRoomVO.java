package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FacilityEquipmentStorageRoomVO {

    private Long id;

    private String keshi;

    private Integer hasStorageRoom;

    private String campus;

    private String location;

    private String photo;

    private Integer hasFridge;

    private Integer hasAccessRecord;

    private Integer hasTempHumidityRecord;

    private Integer hasFridgeAccount;

    private Integer hasMaintenanceRecord;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}
