package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;
import org.example.fuer_xitong.pojo.vo.PiApprovalProgressVO;
import org.example.fuer_xitong.service.ApprovalLogService;
import org.example.fuer_xitong.service.ProfessionalGroupService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class PiApprovalLogsController {
    @Autowired
    private ApprovalLogService approvalLogService;


    /**
     * 研究者查看自己的 PI 审批日志
     */
    @GetMapping("/piApprovalLogs")
    public Result<List<PiApprovalProgressVO>> getApprovalLogs() {

        // 当前登录研究者工号
        String piId = BaseContext.getCurrentId();

        log.info("查询研究者审批日志，piId={}", piId);

        // 调用 Service 获取分组后的审批日志
        List<PiApprovalProgressVO> logs = approvalLogService.getApprovalLogsByPiId(piId);

        return Result.success(logs);
    }
}
