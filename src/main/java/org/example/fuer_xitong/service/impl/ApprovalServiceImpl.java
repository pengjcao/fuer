package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class ApprovalServiceImpl implements ApprovalService {
    @Autowired
    private ProfessionalGroupMapper professionalGroupMapper;

    public boolean handleApproval(String piId, int pi_info_id, String approverId, Integer role, Boolean approve, String comment)
    {
        // 1. 查询数据库最新 PI 信息，保证安全
        PiInfoVO pi = professionalGroupMapper.selectPiinfoById(piId,pi_info_id);
        if (pi == null) return false;

        // 2. 权限校验：当前审批者角色必须等于数据库里的 current_step
        if (!role.equals(pi.getCurrentStep()+1)) {
            return false;
        }

        // 3. 审批逻辑
        if (approve) {
            // 审批通过
            if (pi.getCurrentStep() == 4) {
                // 流程完成
                pi.setCurrentStep(4);
                pi.setApplyStatus("APPROVE");
            } else {
                // 推进下一步
                pi.setCurrentStep(pi.getCurrentStep() + 1);
                pi.setApplyStatus("PENDING_APPROVAL");
            }
        } else {
            // 审批拒绝
            pi.setCurrentStep(0); // 重置
            pi.setApplyStatus("REJECT");
        }


        // 4. 更新 PI 信息表（只更新状态和步骤）
        professionalGroupMapper.updatePiInfo(pi);


//        // 5. 写入审批日志表

        PiApprovalLogVO log =new PiApprovalLogVO();
        log.setPiInfoId(pi_info_id);
        log.setPiId(piId);
        log.setApproverId(approverId);
        log.setRole(role);
        log.setCurrentStep(pi.getCurrentStep()); // 审批前的步骤
        log.setApplyStatus(approve ? "APPROVE" : "REJECT");
        log.setComment(comment);
        log.setApproveTime(new Date());
        professionalGroupMapper.insertApprovalLog(log);

        return true;
    }

}
