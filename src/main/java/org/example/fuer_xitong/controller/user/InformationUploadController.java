package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.PiInfoHistoryVO;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.ProfessionalGroupService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user/upload")
@Slf4j
public class InformationUploadController {

    @Autowired
    private ProfessionalGroupService professionalGroupService;

    @Autowired
    private ProfessionalGroupMapper professionalGroupMapper;

    @PostMapping(value = "/zhuanyezu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> addProfessionalGroup(@ModelAttribute PiInfoDTO dto) {
        professionalGroupService.addPiInfo(dto);
        return Result.success("新增专业组上传成功");
    }

    @PostMapping(value = "/piinfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> addPiAndZhuanyezuInfo(@ModelAttribute PiInfoDTO dto) {
        String id = BaseContext.getCurrentId();
        dto.setId(id);

        inheritProfessionalGroupInfoIfExists(id, dto);
        professionalGroupService.addPiInfo(dto);

        return Result.success("PI信息提交成功");
    }

    private void inheritProfessionalGroupInfoIfExists(String id, PiInfoDTO dto) {
        if (dto == null || dto.getProfessional() == null || dto.getProfessional().trim().isEmpty()) {
            return;
        }

        List<PiInfoVO> existingList =
                professionalGroupMapper.getByIdAndProfessionalList(id, dto.getProfessional());
        PiInfoVO existing = findProfessionalGroupRecord(existingList);
        if (existing == null) {
            return;
        }

        dto.setRecordTypes(existing.getRecordTypes() != null
                ? Arrays.asList(existing.getRecordTypes().split(","))
                : null);
        dto.setHospitalAreas(existing.getHospitalAreas() != null
                ? Arrays.asList(existing.getHospitalAreas().split(","))
                : null);

        if (existing.getReportFilePath() != null) {
            copySelfAssessmentReport(existing.getReportFilePath(), dto);
        }
    }

    private PiInfoVO findProfessionalGroupRecord(List<PiInfoVO> existingList) {
        if (existingList == null || existingList.isEmpty()) {
            return null;
        }

        for (int i = existingList.size() - 1; i >= 0; i--) {
            PiInfoVO item = existingList.get(i);
            if (item.getRecordTypes() != null
                    || item.getHospitalAreas() != null
                    || item.getReportFilePath() != null) {
                return item;
            }
        }
        return null;
    }

    private void copySelfAssessmentReport(String reportFilePath, PiInfoDTO dto) {
        File file = new File(reportFilePath);
        if (!file.exists()) {
            return;
        }

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

    @GetMapping("/piinfo/resubmit/{piInfoId}")
    public Result<PiInfoVO> getRejectedPiInfoForResubmit(@PathVariable Integer piInfoId) {
        PiInfoVO vo = professionalGroupService.getRejectedPiInfoForResubmit(piInfoId);
        return Result.success(vo);
    }

    @PostMapping(value = "/piinfo/resubmit/{piInfoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> resubmitRejectedPiInfo(@PathVariable Integer piInfoId, @ModelAttribute PiInfoDTO dto) {
        professionalGroupService.resubmitRejectedPiInfo(piInfoId, dto);
        return Result.success("PI备案申请已重新提交");
    }

    @GetMapping("/history/{piInfoId}")
    public Result<PiInfoHistoryVO> history(@PathVariable int piInfoId) {
        PiInfoHistoryVO vo = professionalGroupMapper.selectHistoryById(piInfoId);
        if (vo == null) {
            return Result.error("记录不存在");
        }
        if (vo.getCurrentStep() != 0) {
            return Result.error("当前记录不允许回退");
        }
        return Result.success(vo);
    }
}
