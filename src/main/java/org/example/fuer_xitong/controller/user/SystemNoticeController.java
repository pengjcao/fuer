package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.SystemNoticePublishDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.SystemNoticeVO;
import org.example.fuer_xitong.service.SystemNoticeService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class SystemNoticeController {

    @Autowired
    private SystemNoticeService systemNoticeService;
    /**
     * 发布通知（管理员 / 审批者 role=2/3/4）
     */
    @PostMapping("/publishnotice")
    public Result publish(@ModelAttribute @Validated SystemNoticePublishDTO dto) {

        // DTO直接传给Service
        systemNoticeService.publish(dto);

        return Result.success("发布成功");
    }

    /**
     * 查询通知列表（所有用户可见）
     */
    @GetMapping("/noticelist")
    public Result list() {
        return Result.success(systemNoticeService.listAll());
    }


    /**
     * 查询当前用户所属分组可见的通知
     */
    @GetMapping("/noticelist/group")
    public Result listByGroup() {
        String currentUserId = BaseContext.getCurrentId();
        List<SystemNoticeVO> notices = systemNoticeService.listByUserGroups(currentUserId);
        return Result.success(notices);
    }


    /**
     * 查询通知详情
     * @param noticeId 通知ID
     */
    @GetMapping("/detail/{noticeId}")
    public Result detail(@PathVariable Integer noticeId) {
        SystemNoticeVO vo = systemNoticeService.getById(noticeId);
        return Result.success(vo);
    }

}
