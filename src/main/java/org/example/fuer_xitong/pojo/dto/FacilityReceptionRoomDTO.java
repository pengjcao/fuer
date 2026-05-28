package org.example.fuer_xitong.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FacilityReceptionRoomDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 科室
     */
    private String keshi;

    /**
     * 院区，支持：江南 或 江南,渝中
     */
    private String campus;

    /**
     * 地点
     */
    private String location;

    /**
     * 照片文件
     */
    private MultipartFile photo;

    /**
     * 是否满足知情同意及随访需要：1是 0否
     */
    private Integer canMeetNeed;
}