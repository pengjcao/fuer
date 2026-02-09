package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.InstitutionFileSystemCreateDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO;

import java.util.List;

public interface InstitutionFileSystemService {
    void create(InstitutionFileSystemCreateDTO dto,String keshi,String groupPath);

    List<InstitutionFileSystemVO> list(String keshi ,String groupPath);
    void deleteSystem(Long systemId);
}
