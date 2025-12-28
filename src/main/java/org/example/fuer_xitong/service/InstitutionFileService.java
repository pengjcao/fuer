package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.InstitutionFileDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionFileVO;
import org.example.fuer_xitong.pojo.vo.InstitutionTrialManagementFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InstitutionFileService {
    void saveOrUpdate(InstitutionFileDTO dto);

    void uploadTrialManagementFile(MultipartFile file);

    List<InstitutionTrialManagementFileVO> listTrialManagementHistory(Integer institutionFileId);

    InstitutionFileVO getInstitutionFile();

    void uploadDrugTrialSopFile(MultipartFile file);
}
