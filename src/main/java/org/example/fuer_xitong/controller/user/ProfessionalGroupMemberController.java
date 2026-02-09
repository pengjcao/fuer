package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupMemberCreateDTO;
import org.example.fuer_xitong.pojo.vo.ProfessionalGroupMemberVO;
import org.example.fuer_xitong.service.ProfessionalGroupMemberService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user/professional-group-members")
@Slf4j
public class ProfessionalGroupMemberController {

    @Autowired
    private ProfessionalGroupMemberService professionalGroupMemberService;

    @PostMapping("/add-member")
    public Result addProfessionalGroupMember(
            @ModelAttribute ProfessionalGroupMemberCreateDTO dto
    ) {
        professionalGroupMemberService.createMember(dto);
        return Result.success("研究团队上传成功");
    }



    /**
     * 查询专业组成员信息
     */
    @GetMapping("/query-members")
    public Result<List<ProfessionalGroupMemberVO>> queryMembers(
            @RequestParam Integer departmentId,
            @RequestParam String groupPath
    ) {
        return Result.success(
                professionalGroupMemberService.queryMembers(departmentId, groupPath)
        );
    }


}
