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

    @GetMapping("/approvedPiList")
    public Result<List<PiInfoVO>> getApprovedPiList() {
        List<PiInfoVO> list = professionalGroupService.getApprovedPiList();
        return Result.success(list);
    }



    @PostMapping("/shenpi")
    public Result secretaryReview(@RequestParam String userId,/*也是ID,这个ID应该从审批者看到*/
                                  @RequestParam int pi_info_id,
                                  @RequestParam Boolean approve,
                                  @RequestParam(required = false) String comment) {
        String approverId = BaseContext.getCurrentId();   // 从 BaseContext 获取当前登录用户ID
        Integer role = BaseContext.getCurrentRole();     // 从 BaseContext 获取当前登录用户 role
        boolean success = approvalService.handleApproval(userId,pi_info_id, approverId, role, approve, comment);
        return success ? Result.success("审批成功") : Result.error("审批失败");

    }


}
