package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

@Data
public class FacilityEmergencyEquipmentDTO {

    /**
     * 科室
     */
    private String keshi;

    /**
     * 是否具有抢救设施设备
     */
    private Integer hasEmergencyEquipment;
}