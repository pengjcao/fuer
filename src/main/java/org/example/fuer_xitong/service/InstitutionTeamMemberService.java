package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.InstitutionTeamMemberDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionTeamMemberVO;

import java.util.List;

public interface InstitutionTeamMemberService {
    void saveOrUpdate(InstitutionTeamMemberDTO dto);

    List<InstitutionTeamMemberVO> listAll();
//    void delete(String institutionId, String institutionMemberId);
//
//    InstitutionTeamMemberVO get(String institutionId, String institutionMemberId);
//
//    List<InstitutionTeamMemberVO> list(String institutionId);
}
