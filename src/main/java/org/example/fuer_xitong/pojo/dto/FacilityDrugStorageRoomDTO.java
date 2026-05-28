package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FacilityDrugStorageRoomDTO {
    private Long id;

    private Integer hasStorageRoom;

    private String campus;

    private String location;

    /**
     * 科室
     */
    private String keshi;
    /**
     * 照片文件（上传用）
     */
    private MultipartFile photo;

    private Integer hasFridge;

    private Integer hasAccessRecord;

    private Integer hasTempHumidityRecord;

    private Integer hasFridgeAccount;

    private Integer hasMaintenanceRecord;
}