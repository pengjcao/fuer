package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

@Data
public class ApprovalStepVO {

    /**
     * 第几步
     */
    private Integer stepOrder;

    /**
     * 步骤名称（如：管理员审核）
     */
    private String stepName;

    /**
     * 状态：
     * WAITING   未开始
     * PENDING   审批中
     * APPROVED  已通过
     * REJECTED  未通过
     */
    private String status;

    /**
     * 审批意见（拒绝时展示）
     */
    private String comment;

    /**
     * 审批时间
     */
    private String approveTime;
}