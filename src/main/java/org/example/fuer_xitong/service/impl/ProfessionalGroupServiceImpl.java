package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.dto.ClinicalMaterialDTO;
import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.dto.PiInfoMinimalDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.ProfessionalGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProfessionalGroupServiceImpl implements ProfessionalGroupService {

    @Autowired
    private ProfessionalGroupMapper professionalGroupMapper;



    @Override
    public void addProfessionalGroup(ProfessionalGroupAddDTO dto) {

        MultipartFile file = dto.getSelfAssessmentReport();
        String filePath = saveFile(file,"professional-group");
        String Id=BaseContext.getCurrentId();
        professionalGroupMapper.insertProfessionalGroup(
                Id,
                String.join(",", dto.getRecordTypes()),
                String.join(",", dto.getRecordNames()),
                String.join(",", dto.getHospitalAreas()),
                filePath);
    }

    @Override
    public void addPiInfo(PiInfoDTO dto) {
        String id = BaseContext.getCurrentId();
        dto.setId(id);

        // ================== 1. 插入 minimal 数据，获取自增 pi_info_id ==================
        PiInfoMinimalDTO minimalDTO = new PiInfoMinimalDTO();
        minimalDTO.setId(id);
        minimalDTO.setProfessional(dto.getProfessional());

        // recordTypes / hospitalAreas 逗号拼接
        String recordTypesStr = dto.getRecordTypes() != null ? String.join(",", dto.getRecordTypes()) : null;
        String hospitalAreasStr = dto.getHospitalAreas() != null ? String.join(",", dto.getHospitalAreas()) : null;

        minimalDTO.setRecordTypes(recordTypesStr);
        minimalDTO.setHospitalAreas(hospitalAreasStr);

        // 先插入 minimalDTO 获取自增 pi_info_id
        professionalGroupMapper.insertPiInfoMinimal(minimalDTO);
        Integer piInfoId = minimalDTO.getPiInfoId();
        if (piInfoId == null) {
            throw new RuntimeException("获取自增 pi_info_id 失败");
        }

        // ================== 2. 构建统一 PI 文件存储路径 ==================
        String baseDir = "D:/yan/upload/Pi/" + id + "/" + piInfoId + "/";
        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) baseFolder.mkdirs();

        // ================== 3. 保存 PI 基本文件 ==================
        String piPhotoPath = saveFile(dto.getPiPhoto(), baseDir);
        String seniorTitleCertificatePath = saveFile(dto.getSeniorTitleCertificate(), baseDir);
        String seniorTitleAppointmentPath = saveFile(dto.getSeniorTitleAppointment(), baseDir);
        String signedResumePath = saveFile(dto.getSignedResume(), baseDir);
        String qualificationCertificatePath = saveFile(dto.getQualificationCertificate(), baseDir);
        String practiceCertificatePath = saveFile(dto.getPracticeCertificate(), baseDir);
        String gcpCertificatePath = saveFile(dto.getGcpCertificate(), baseDir);

        // ================== 4. 保存专业组自评报告文件 ==================
        String selfAssessmentReportPath = saveFile(dto.getSelfAssessmentReport(), baseDir);

        // ================== 5. 保存临床材料 ==================
        String clinicalRootPath = null;
        if (Boolean.TRUE.equals(dto.getClinicalParticipation())
                && dto.getClinicalMaterials() != null
                && !dto.getClinicalMaterials().isEmpty()) {

            clinicalRootPath = baseDir + "clinical/";
            for (ClinicalMaterialDTO cm : dto.getClinicalMaterials()) {
                saveFile(cm.getNmpaApproval(), clinicalRootPath);
                saveFile(cm.getDelegationTable(), clinicalRootPath);
                saveFile(cm.getTrainingRecord(), clinicalRootPath);
                saveFile(cm.getProcessFiles(), clinicalRootPath);
                saveFile(cm.getCompletionFiles(), clinicalRootPath);
                saveFile(cm.getOtherFiles(), clinicalRootPath);
            }
        }

        // ================== 6. 更新数据库，把文件路径和专业组字段写回 ==================
        professionalGroupMapper.updatePiInfoFiles(
                piInfoId,
                piPhotoPath,
                seniorTitleCertificatePath,
                seniorTitleAppointmentPath,
                signedResumePath,
                qualificationCertificatePath,
                practiceCertificatePath,
                gcpCertificatePath,
                Boolean.TRUE.equals(dto.getClinicalParticipation()) ? 1 : 0,
                dto.getClinicalReason(),
                clinicalRootPath,
                selfAssessmentReportPath,  // 自评报告路径
                recordTypesStr,            // 专业组备案类型
                hospitalAreasStr           // 所属院区
        );
    }






    public List<PiInfoVO> getPendingApprovalList() {
        List<PiInfoVO> list = professionalGroupMapper.selectPendingApprovalVO();

        // 2. 遍历列表，转换每个 VO 的文件路径
        List<PiInfoVO> result = new ArrayList<>();
        for (PiInfoVO vo : list) {
            result.add(convertFilePaths(vo));
        }

        return result;
    }
//
//        return list;
//    }

    public List<PiInfoVO> getApprovedPiList() {
        // 1. 查询已审批完成的 PI 列表
        List<PiInfoVO> list = professionalGroupMapper.selectApprovedPiVO();

        // 2. 遍历列表，转换每个 VO 的文件路径
        List<PiInfoVO> result = new ArrayList<>();
        for (PiInfoVO vo : list) {
            result.add(convertFilePaths(vo));
        }

        return result;
    }




    private PiInfoVO convertFilePaths(PiInfoVO vo) {
        vo.setPiPhotoPath(toFileUrl(vo.getPiPhotoPath()));
        vo.setSeniorTitleCertificatePath(toFileUrl(vo.getSeniorTitleCertificatePath()));
        vo.setSeniorTitleAppointmentPath(toFileUrl(vo.getSeniorTitleAppointmentPath()));
        vo.setSignedResumePath(toFileUrl(vo.getSignedResumePath()));
        vo.setQualificationCertificatePath(toFileUrl(vo.getQualificationCertificatePath()));
        vo.setPracticeCertificatePath(toFileUrl(vo.getPracticeCertificatePath()));
        vo.setGcpCertificatePath(toFileUrl(vo.getGcpCertificatePath()));
        vo.setReportFilePath(toFileUrl(vo.getReportFilePath()));
        return vo;
    }
    /**
     * 磁盘路径 -> 前端可访问 URL
     */
    private String toFileUrl(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) return null;
        return "http://localhost:8080/files/" + dbPath.replace("D:/yan/upload/", "");
    }

    /**
     * 从磁盘路径获取文件名
     */
    private String getFileName(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) return null;
        return Paths.get(dbPath).getFileName().toString();
    }



    private String saveFile(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) return null;

        try {

            File folder = new File(path);
            if (!folder.exists()) folder.mkdirs();

            // 文件名加时间戳，避免覆盖
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = path + fileName;

            // 保存文件到磁盘
            file.transferTo(new File(filePath));

            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }
}
