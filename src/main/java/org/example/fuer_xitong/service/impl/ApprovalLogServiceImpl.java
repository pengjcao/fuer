package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.PiApprovalLogMapper;
import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;
import org.example.fuer_xitong.pojo.vo.PiApprovalProgressVO;
import org.example.fuer_xitong.service.ApprovalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApprovalLogServiceImpl implements ApprovalLogService {

    @Autowired
    private PiApprovalLogMapper piApprovalLogMapper;

    @Override
    public List<PiApprovalProgressVO> getApprovalLogsByPiId(String piId) {

        if (piId == null || piId.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 查询该研究者所有审批日志（按 pi_info_id DESC, current_step ASC 排序）
        List<PiApprovalLogVO> allLogs = piApprovalLogMapper.selectLogsByPiId(piId);
        if (allLogs.isEmpty()) {
            return Collections.emptyList();
        }

        // 按 pi_info_id 分组
        Map<Integer, List<PiApprovalLogVO>> logMap = allLogs.stream()
                .collect(Collectors.groupingBy(PiApprovalLogVO::getPiInfoId, LinkedHashMap::new, Collectors.toList()));

        // 组装返回结构
        List<PiApprovalProgressVO> result = new ArrayList<>();

        for (Map.Entry<Integer, List<PiApprovalLogVO>> entry : logMap.entrySet()) {
            Integer piInfoId = entry.getKey();
            List<PiApprovalLogVO> logs = entry.getValue();

            PiApprovalProgressVO vo = new PiApprovalProgressVO();
            vo.setPiInfoId(piInfoId);
            vo.setLogs(logs);

            // 取最新一条日志的 currentStep 和 applyStatus 作为申请的当前状态
            PiApprovalLogVO latestLog = logs.get(logs.size() - 1);
            vo.setCurrentStep(latestLog.getCurrentStep());
            vo.setApplyStatus(latestLog.getApplyStatus());

            result.add(vo);
        }

        return result;
    }

}
