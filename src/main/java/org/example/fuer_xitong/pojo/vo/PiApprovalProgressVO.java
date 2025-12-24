package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.util.List;

/*研究者「查看审批状态」专用 VO（新增）*/
@Data
public class PiApprovalProgressVO {

    private Integer piInfoId;        // 一条 PI 申请
    private Integer currentStep;     // 当前步骤
    private String applyStatus;      // 当前状态

    private List<PiApprovalLogVO> logs; // 审批日志列表
}

