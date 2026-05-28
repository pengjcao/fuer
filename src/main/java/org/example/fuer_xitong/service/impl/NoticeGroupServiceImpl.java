package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.NoticeGroupMapper;
import org.example.fuer_xitong.pojo.dto.NoticeGroupCreateDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.service.NoticeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeGroupServiceImpl implements NoticeGroupService {
    @Value("${file.base-url}")
    private String baseUrl;
    @Autowired
    private NoticeGroupMapper noticeGroupMapper;

    @Override
    @Transactional
    public Integer createGroup(NoticeGroupCreateDTO dto) {

        // ================== 1. 获取当前用户 ==================
        String currentUserId = BaseContext.getCurrentId();
        Integer currentRole = BaseContext.getCurrentRole();

        // ================== 2. 权限校验 ==================
        // 只有审批者（2/3/4）可以建组
        if (currentRole == null || currentRole < 2) {
            throw new RuntimeException("当前用户无创建分组权限");
        }

        // ================== 3. 参数补全 ==================
        // 前端只传 groupName
        // creatorId 由后端补
        dto.setCreatorId(currentUserId);

        // ================== 4. 插入分组 ==================
        noticeGroupMapper.insertGroup(dto);

        // ================== 5. 拿回填的自增 ID ==================
        Integer groupId = dto.getGroupId();
        if (groupId == null) {
            throw new RuntimeException("创建分组失败，未生成分组ID");
        }

        return groupId;
    }


    @Override
    @Transactional
    public void addUsersToGroup(Integer groupId, List<String> userIds) {

        // ================== 2. 插入用户 ==================
        for (String userId : userIds) {

                noticeGroupMapper.insertGroupUser(groupId, userId);
            }
        }


    @Override
    public List<NoticeGroupCreateDTO> getallGroups() {
        // 不区分创建者，直接返回所有分组
        return noticeGroupMapper.selectAllGroups();
    }


    @Override
    @Transactional
    public void deleteGroup(Integer groupId) {

        noticeGroupMapper.deleteUsersByGroupId(groupId);
        // 再删分组
        noticeGroupMapper.deleteById(groupId);
    }


    @Override
    public List<String> getUsersByGroup(Integer groupId) {

        return noticeGroupMapper.selectUserIdsByGroupId(groupId);
    }


}


