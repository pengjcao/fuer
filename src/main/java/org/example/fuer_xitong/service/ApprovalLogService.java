package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;
import org.example.fuer_xitong.pojo.vo.PiApprovalProgressVO;

import java.util.List;

public interface ApprovalLogService {

    /**
     * 查询研究者某条 PI 的审批日志
     */
    List<PiApprovalProgressVO> getApprovalLogsByPiId(String piId);
}
