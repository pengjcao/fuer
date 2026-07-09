package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.PiApprovalLogMapper;
import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;
import org.example.fuer_xitong.pojo.vo.PiApprovalProgressVO;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.ApprovalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApprovalLogServiceImpl implements ApprovalLogService {

    @Autowired
    private PiApprovalLogMapper piApprovalLogMapper;

    @Autowired
    private ProfessionalGroupMapper professionalGroupMapper;

    @Override
    public List<PiApprovalProgressVO> getApprovalLogsByPiId(String piId) {

        if (piId == null || piId.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<PiInfoVO> piInfoList = professionalGroupMapper.selectPiInfoListByPiId(piId);

        // 查询该研究者所有审批日志（按 pi_info_id DESC, current_step ASC 排序）
        List<PiApprovalLogVO> allLogs = piApprovalLogMapper.selectLogsByPiId(piId);
        if ((piInfoList == null || piInfoList.isEmpty()) && allLogs.isEmpty()) {
            return Collections.emptyList();
        }

        // 按 pi_info_id 分组
        Map<Integer, List<PiApprovalLogVO>> logMap = allLogs.stream()
                .collect(Collectors.groupingBy(PiApprovalLogVO::getPiInfoId, LinkedHashMap::new, Collectors.toList()));

        // 组装返回结构
        List<PiApprovalProgressVO> result = new ArrayList<>();

        if (piInfoList != null && !piInfoList.isEmpty()) {
            for (PiInfoVO piInfo : piInfoList) {
                PiApprovalProgressVO vo = new PiApprovalProgressVO();
                vo.setPiInfoId(piInfo.getPiInfoId());
                vo.setCurrentStep(piInfo.getCurrentStep());
                vo.setApplyStatus(piInfo.getApplyStatus());
                vo.setDrugAdminRecordTime(piInfo.getDrugAdminRecordTime());
                vo.setLogs(logMap.getOrDefault(piInfo.getPiInfoId(), Collections.emptyList()));
                result.add(vo);
            }
            return result;
        }

        for (Map.Entry<Integer, List<PiApprovalLogVO>> entry : logMap.entrySet()) {
            Integer piInfoId = entry.getKey();
            List<PiApprovalLogVO> logs = entry.getValue();

            PiApprovalProgressVO vo = new PiApprovalProgressVO();
            vo.setPiInfoId(piInfoId);
            vo.setLogs(logs);

            // 顶层状态取 pi_info 的真实当前状态，避免中间节点同意被误判为整体通过。
            PiApprovalLogVO latestLog = logs.get(logs.size() - 1);
            PiInfoVO piInfo = professionalGroupMapper.selectPiinfoById(piInfoId);
            if (piInfo != null) {
                vo.setCurrentStep(piInfo.getCurrentStep());
                vo.setApplyStatus(piInfo.getApplyStatus());
                vo.setDrugAdminRecordTime(piInfo.getDrugAdminRecordTime());
            } else {
                vo.setCurrentStep(latestLog.getCurrentStep());
                vo.setApplyStatus(latestLog.getApplyStatus());
            }

            result.add(vo);
        }

        return result;
    }

}
