package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.impl.ApprovalServiceImpl;
import org.example.fuer_xitong.service.impl.ProfessionalGroupServiceImpl;
import org.example.fuer_xitong.service.impl.UserServiceImpl;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserApprovalController {
    @Autowired
    private ProfessionalGroupServiceImpl professionalGroupService;

    @Autowired
    private ApprovalServiceImpl approvalService;

    @GetMapping("/pendingPiList")
    public Result<List<PiInfoVO>> getPendingPiList() {
        List<PiInfoVO> list = professionalGroupService.getPendingApprovalList();
        return Result.success(list);
    }

    /** 机构办秘书审批（步骤2） */
    @PostMapping("/secretaryReview")
    public Result secretaryReview(
                                  @RequestParam Boolean approve,
                                  @RequestParam(required = false) String comment) {
        String approverId = BaseContext.getCurrentId();   // 从 BaseContext 获取当前登录用户ID
        Integer role = BaseContext.getCurrentRole();     // 从 BaseContext 获取当前登录用户 role

        int pi_info_id=1;
        boolean success = approvalService.handleApproval("U001",pi_info_id, approverId, role, approve, comment);
        return success ? Result.success("审批成功") : Result.error("审批失败");
    }
}
