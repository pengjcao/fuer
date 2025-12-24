package org.example.fuer_xitong.controller.user;


import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.pojo.vo.UserLoginVO;
import org.example.fuer_xitong.service.DepartmentService;
import org.example.fuer_xitong.service.ProfessionalGroupService;
import org.example.fuer_xitong.service.Userservice;
import org.example.fuer_xitong.utils.JwtProperties;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/user/upload")
@Slf4j
public class InformationUploadController {

    @Autowired
    private ProfessionalGroupService professionalGroupService;

    @Autowired
    private ProfessionalGroupMapper professionalGroupMapper;


    @PostMapping(value = "/zhuanyezu" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> addProfessionalGroup(@ModelAttribute PiInfoDTO dto) {

        // 调用服务方法统一处理：新增 PI + 专业组
        professionalGroupService.addPiInfo(dto);

        return Result.success("新增专业上传成功");
    }


    @PostMapping(value = "/piinfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result addPiInfo(@ModelAttribute PiInfoDTO dto) {
        String id = BaseContext.getCurrentId();
        dto.setId(id);
        // ================== 1. 根据 ID + professional 查已有 PI/专业组记录 ==================
        List<PiInfoVO> existingList = professionalGroupMapper.getByIdAndProfessionalList(id, dto.getProfessional());
        PiInfoVO existing = existingList.get(0);
        dto.setRecordTypes(existing.getRecordTypes() != null ?
                Arrays.asList(existing.getRecordTypes().split(",")) : null);
        dto.setHospitalAreas(existing.getHospitalAreas() != null ?
                Arrays.asList(existing.getHospitalAreas().split(",")) : null);

        if (existing.getReportFilePath() != null) {
            File file = new File(existing.getReportFilePath());
            if (file.exists()) {
                try (InputStream is = new FileInputStream(file)) {
                    MultipartFile mf = new MockMultipartFile(
                            file.getName(),
                            file.getName(),
                            "application/octet-stream",
                            is
                    );
                    dto.setSelfAssessmentReport(mf);
                } catch (IOException e) {
                    throw new RuntimeException("生成 MultipartFile 失败", e);
                }
            }
        }

        professionalGroupService.addPiInfo(dto);

        return Result.success("PI信息提交成功");
    }
}
