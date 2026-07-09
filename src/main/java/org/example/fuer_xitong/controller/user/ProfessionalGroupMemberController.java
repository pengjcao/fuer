package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupMemberCreateDTO;
import org.example.fuer_xitong.pojo.vo.ProfessionalGroupMemberVO;
import org.example.fuer_xitong.service.ProfessionalGroupMemberService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/user/professional-group-members")
@Slf4j
public class ProfessionalGroupMemberController {

    @Autowired
    private ProfessionalGroupMemberService professionalGroupMemberService;

    @PostMapping(value = "/add-member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result addProfessionalGroupMember(
            @ModelAttribute ProfessionalGroupMemberCreateDTO dto,
            @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestParam(value = "gcpCertFile", required = false) MultipartFile gcpCertFile,
            @RequestParam(value = "practiceCertFile", required = false) MultipartFile practiceCertFile,
            HttpServletRequest request
    ) {
        dto.setResumeFile(resolveMultipartFile(resumeFile, request, "resumeFile", "resume"));
        dto.setGcpCertFile(resolveMultipartFile(gcpCertFile, request, "gcpCertFile", "gcpFile"));
        dto.setPracticeCertFile(resolveMultipartFile(practiceCertFile, request, "practiceCertFile", "practiceFile", "licenseFile"));
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

    @DeleteMapping("/delete")
    public Result deleteMember(@RequestParam Integer id) {
        professionalGroupMemberService.deleteMember(id);
        return Result.success("研究团队成员删除成功");
    }

    private MultipartFile resolveMultipartFile(
            MultipartFile boundFile,
            HttpServletRequest request,
            String... names
    ) {
        if (boundFile != null && !boundFile.isEmpty()) {
            return boundFile;
        }

        if (request instanceof MultipartHttpServletRequest multipartRequest) {
            for (String name : names) {
                MultipartFile file = multipartRequest.getFile(name);
                if (file != null && !file.isEmpty()) {
                    return file;
                }
            }
        }

        return boundFile;
    }


}
