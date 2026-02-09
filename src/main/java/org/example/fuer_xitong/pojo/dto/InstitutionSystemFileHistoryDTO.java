package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

@Data
public class InstitutionSystemFileHistoryDTO {

    private Long fileId;
    private Long systemId;
    private String fileName;
    private String filePath;
    private String operatedBy;
    private String remark;
}
