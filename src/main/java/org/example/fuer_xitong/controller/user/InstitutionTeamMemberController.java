package org.example.fuer_xitong.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.fuer_xitong.pojo.dto.InstitutionTeamMemberDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionTeamMemberVO;
import org.example.fuer_xitong.service.InstitutionTeamMemberService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/institution")
@RequiredArgsConstructor
public class InstitutionTeamMemberController {
    @Autowired
    private InstitutionTeamMemberService institutionTeamMemberService;

    @PostMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Result save(@ModelAttribute InstitutionTeamMemberDTO dto) {
        institutionTeamMemberService.saveOrUpdate(dto);
        return Result.success();
    }


    @GetMapping("/list")
    public Result<List<InstitutionTeamMemberVO>> listAll() {
        List<InstitutionTeamMemberVO> list =
                institutionTeamMemberService.listAll();
        return Result.success(list);
    }
}
