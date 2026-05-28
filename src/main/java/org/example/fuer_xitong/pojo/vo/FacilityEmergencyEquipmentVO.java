package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FacilityEmergencyEquipmentVO {

    private Long id;

    private String keshi;

    private Integer hasEmergencyEquipment;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}