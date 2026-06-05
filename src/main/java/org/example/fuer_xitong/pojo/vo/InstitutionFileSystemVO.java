package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstitutionFileSystemVO {

    private Long id;
    private String systemCode;
    private String systemName;
    private String description;
    private Boolean isFixed;
    private String keshi;
    private String groupPath;
    private String createdBy;
    private LocalDateTime createdTime;
}
