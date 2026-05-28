package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FacilityManagementRoomDTO {

    private Long id;

    private String keshi;

    private String campus;

    private String location;

    private MultipartFile photo;

    private Integer hasTempHumidityRecord;

    private Integer hasAccessRecord;

    private Integer hasFileBorrowRecord;

    private Integer hasProtectionCondition;

    private String createBy;
}