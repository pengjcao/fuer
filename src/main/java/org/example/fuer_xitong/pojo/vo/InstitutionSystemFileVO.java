package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstitutionSystemFileVO {

    /** 文件ID */
    private Long id;

    /** 文件体系ID */
    private Long systemId;

    /** 文件名称 */
    private String fileName;

    /** 当前文件路径 */
    private String currentPath;

    /** 创建人 */
    private String createdBy;

    /** 是否失效 */
    private Long isActive;

    /** 创建时间 */
    private LocalDateTime createdTime;

    /** 更新时间 */
    private LocalDateTime updatedTime;


}
