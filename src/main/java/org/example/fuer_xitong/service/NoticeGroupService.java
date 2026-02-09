package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.NoticeGroupCreateDTO;

import java.util.List;

public interface NoticeGroupService {
    Integer createGroup(NoticeGroupCreateDTO dto);

    void addUsersToGroup(Integer groupId, List<String> userIds);

    List<NoticeGroupCreateDTO> getallGroups();

    void deleteGroup(Integer groupId);

    List<String> getUsersByGroup(Integer groupId);
}
