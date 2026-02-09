package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.SystemNoticePublishDTO;
import org.example.fuer_xitong.pojo.vo.SystemNoticeVO;

import java.util.List;

public interface SystemNoticeService {
    void publish(SystemNoticePublishDTO dto);
    List<SystemNoticeVO> listAll();
    SystemNoticeVO getById(Integer noticeId);

    List<SystemNoticeVO> listByUserGroups(String userId);
}
