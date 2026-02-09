package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.NoticeGroupCreateDTO;
import org.example.fuer_xitong.service.NoticeGroupService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class NoticeGroupController {

    @Autowired
    private  NoticeGroupService noticeGroupService;

    /**
     * 1️⃣ 创建分组（只建组名）
     */
    @PostMapping("/creategroup")
    public Result createGroup(@RequestBody @Validated NoticeGroupCreateDTO dto) {
        Integer groupId = noticeGroupService.createGroup(dto);
        return Result.success(groupId);
    }

    /**
     * 获取当前用户创建的所有分组
     */
    @GetMapping("/getallGroups")
    public Result myGroups() {
        // 返回 DTO 列表，不用 VO
        List<NoticeGroupCreateDTO> groups = noticeGroupService.getallGroups();
        return Result.success(groups);
    }

    /**
     * 给分组添加研究者
     */
    @PostMapping("/group/{groupId}/addUsers")
    public Result addUsersToGroup(@PathVariable Integer groupId,
                                  @RequestBody List<String> userIds) {
        noticeGroupService.addUsersToGroup(groupId, userIds);
        return Result.success("用户绑定成功");
    }


    /**
     * 删除分组（同时删除分组与用户关系）
     */
    @DeleteMapping("/group/{groupId}")
    public Result deleteGroup(@PathVariable Integer groupId) {
        noticeGroupService.deleteGroup(groupId);
        return Result.success("分组删除成功");
    }


    /**
     * 查询分组下的所有研究者
     */
    @GetMapping("/group/{groupId}/users")
    public Result getUsersByGroup(@PathVariable Integer groupId) {
        List<String> userIds = noticeGroupService.getUsersByGroup(groupId);
        return Result.success(userIds);
    }

}
