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
    private LocalDateTime createdTime;
}
