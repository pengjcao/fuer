package org.example.fuer_xitong.controller.user;


import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.UserLoginVO;
import org.example.fuer_xitong.service.DepartmentService;
import org.example.fuer_xitong.service.ProfessionalGroupService;
import org.example.fuer_xitong.service.Userservice;
import org.example.fuer_xitong.utils.JwtProperties;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user/upload")
@Slf4j
public class InformationUploadController {

    @Autowired
    private ProfessionalGroupService professionalGroupService;

    @PostMapping("/zhuanyezu")
    public Result ProfessionalGroupAdd(
            @RequestParam("recordTypes") List<String> recordTypes,
            @RequestParam("recordNames") List<String> recordNames,
            @RequestParam("hospitalAreas") List<String> hospitalAreas,
            @RequestParam("file") MultipartFile file)

    {

        ProfessionalGroupAddDTO dto = new ProfessionalGroupAddDTO();
        dto.setRecordTypes(recordTypes);
        dto.setRecordNames(recordNames);
        dto.setHospitalAreas(hospitalAreas);
        dto.setSelfAssessmentReport(file);

        professionalGroupService.addProfessionalGroup(dto);

        return Result.success();
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result addPiInfo(@ModelAttribute PiInfoDTO dto) {
        // 获取当前用户工号
        String userId = BaseContext.getCurrentId();
        dto.setId(userId);

        professionalGroupService.addPiInfo(dto);
        return Result.success("PI信息提交成功");
    }







}
