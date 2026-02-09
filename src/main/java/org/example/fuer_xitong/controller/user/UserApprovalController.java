package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.impl.ApprovalServiceImpl;
import org.example.fuer_xitong.service.impl.ProfessionalGroupServiceImpl;
import org.example.fuer_xitong.service.impl.UserServiceImpl;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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


    @GetMapping("/approvedPiListGroup")
    public Result<Map<String, List<PiInfoVO>>> getApprovedPiListGroup() {
        // 1️⃣ 查询审批完成的 PI 列表
        List<PiInfoVO> list = professionalGroupService.getApprovedPiList();

        // 2️⃣ 调用 Service 分组
        Map<String, List<PiInfoVO>> groupedResult =
                professionalGroupService.groupByProfessional(list);

        return Result.success(groupedResult);
    }



    @PostMapping("/drug-admin-record-time")
    public Result fillDrugAdminRecordTime(
            @RequestParam int piInfoId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime recordTime
    ) {
        int role =BaseContext.getCurrentRole();
        // 1. 权限校验：只能机构办秘书
        if (role != 2 ) {
            return Result.error("只有机构办秘书才能填写药监局备案时间");
        }


        professionalGroupService.fillDrugAdminRecordTime(piInfoId, recordTime);
        return Result.success("药监局备案时间填写成功");
    }


    @PostMapping("/shenpi")
    public Result Review(@RequestParam String userId,/*也是ID,这个ID应该从审批者看到*/
                                  @RequestParam int pi_info_id,
                                  @RequestParam Boolean approve,
                                  @RequestParam(required = false) String comment) {
        String approverId = BaseContext.getCurrentId();   // 从 BaseContext 获取当前登录用户ID
        Integer role = BaseContext.getCurrentRole();     // 从 BaseContext 获取当前登录用户 role
        boolean success = approvalService.handleApproval(userId,pi_info_id, approverId, role, approve, comment);
        return success ? Result.success("审批成功") : Result.error("审批失败");

    }







}
