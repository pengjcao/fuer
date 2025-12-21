package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PiApprovalLogVO {
    private Integer piInfoId;      // pi_info 表自增ID
    private String piId;           // 研究者ID
    private String approverId;     // 审批人ID
    private Integer role;          // 审批角色 2=机构办秘书, 3=机构办主任, 4=机构主任
    private Integer currentStep;   // 当前审批步骤
    private String applyStatus;    // 审批状态: PENDING_APPROVAL, APPROVE, REJECT
    private String comment;        // 审批意见
    private Date approveTime;      // 审批时间
    private Date createdAt;        // 创建时间
    private Date updatedAt;        // 更新时间
}