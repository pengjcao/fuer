package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (!role.equals(pi.getCurrentStep())) {
            return false;
        }

//        // 3. 审批逻辑
//        if (approve) {
//            // 审批通过
//            if (pi.getCurrentStep() == 5) {
//                pi.setApplyStatus("已通过"); // 流程完成
//            } else {
//                pi.setCurrentStep(pi.getCurrentStep() + 1); // 推进下一步
//                pi.setApplyStatus("待审批");
//            }
//        } else {
//            // 审批拒绝
//            pi.setCurrentStep(2); // 重置到机构办秘书
//            pi.setApplyStatus("待审批");
//        }
//
//        // 4. 更新 PI 信息表
//        pi.setLastApproverId(approverId);
//        pi.setLastComment(comment);
//        pi.setLastApproveTime(new Date());
//        piInfoMapper.updatePiInfo(pi);
//
//        // 5. 写入审批日志表
//        PiApprovalLog log = new PiApprovalLog();
//        log.setPiId(piId);
//        log.setApproverId(approverId);
//        log.setRole(role);
//        log.setStep(role); // 当前审批步骤
//        log.setStatus(approve ? 1 : 2); // 1=通过, 2=拒绝
//        log.setComment(comment);
//        log.setApproveTime(new Date());
//        piApprovalLogMapper.insert(log);

        return true;
    }

}
