package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProfessionalGroupService {
    void addProfessionalGroup(ProfessionalGroupAddDTO dto);

    void  addPiInfo(PiInfoDTO dto);

    PiInfoVO getRejectedPiInfoForResubmit(Integer piInfoId);

    void resubmitRejectedPiInfo(Integer piInfoId, PiInfoDTO dto);

    List<PiInfoVO> getPendingApprovalList();

    List<PiInfoVO> getApprovedPiList();

    Map<String, List<PiInfoVO>> groupByProfessional(List<PiInfoVO> piList);

    void fillDrugAdminRecordTime(Integer piInfoId, LocalDateTime recordTime);

}
