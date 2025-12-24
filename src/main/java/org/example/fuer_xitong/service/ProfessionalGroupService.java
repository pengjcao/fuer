package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;

import java.util.List;

public interface ProfessionalGroupService {
    void addProfessionalGroup(ProfessionalGroupAddDTO dto);

    void  addPiInfo(PiInfoDTO dto);

    List<PiInfoVO> getPendingApprovalList();

    List<PiInfoVO> getApprovedPiList();
}
