package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FacilitySampleStorageRoomDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 科室
     */
    private String keshi;

    /**
     * 院区
     */
    private String campus;

    /**
     * 地点
     */
    private String location;

    /**
     * 照片文件（上传用）
     */
    private MultipartFile photo;

    /**
     * 是否具有样本冰箱及温湿度计
     */
    private Integer hasSampleFridge;

    /**
     * 是否有人员出入记录
     */
    private Integer hasAccessRecord;

    /**
     * 是否有温湿度记录
     */
    private Integer hasTempHumidityRecord;

    /**
     * 是否有设备保养/校正/维修记录
     */
    private Integer hasMaintenanceRecord;
}