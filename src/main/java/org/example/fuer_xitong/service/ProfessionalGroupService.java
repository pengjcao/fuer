package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;

public interface ProfessionalGroupService {
    void addProfessionalGroup(ProfessionalGroupAddDTO dto);

    void  addPiInfo(PiInfoDTO dto);

}
