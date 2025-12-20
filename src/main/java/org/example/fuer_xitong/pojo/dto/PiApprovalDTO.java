package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

@Data
public class PiApprovalDTO {
    private String id;             // pi_info 表 ID（研究者ID）
    private Boolean approved;      // true: 通过, false: 拒绝
    private String remarks;        // 审批意见（可选）
}