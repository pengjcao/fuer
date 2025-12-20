package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.dto.ClinicalMaterialDTO;
import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
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
        String Id = BaseContext.getCurrentId();
        dto.setId(Id);


        String dir ="D:/yan/upload/" + "Pi/" + Id + "/";
        /* ========== 1. 保存 PI 基本文件 ========== */
        String piPhotoPath = saveFile(dto.getPiPhoto(), "Pi");
        String seniorTitleCertificatePath = saveFile(dto.getSeniorTitleCertificate(), dir);
        String seniorTitleAppointmentPath = saveFile(dto.getSeniorTitleAppointment(), dir);
        String signedResumePath = saveFile(dto.getSignedResume(), dir);
        String qualificationCertificatePath = saveFile(dto.getQualificationCertificate(), dir);
        String practiceCertificatePath = saveFile(dto.getPracticeCertificate(), dir);
        String gcpCertificatePath = saveFile(dto.getGcpCertificate(), dir);

        /* ========== 2. 保存临床试验材料（列表） ========== */
        String clinicalRootPath = null;

        if (Boolean.TRUE.equals(dto.getClinicalParticipation())
                && dto.getClinicalMaterials() != null
                && !dto.getClinicalMaterials().isEmpty()
        ) {

            // 数据库存一个“根路径”即可
            clinicalRootPath = "D:/yan/upload/Pi/" + Id + "/clinical/";

            for (int i = 0; i < dto.getClinicalMaterials().size(); i++) {
                ClinicalMaterialDTO cm = dto.getClinicalMaterials().get(i);

                // 每一个项目一个子目录：Pi/clinical/1、Pi/clinical/2 ...

                saveFile(cm.getNmpaApproval(), clinicalRootPath);
                saveFile(cm.getDelegationTable(), clinicalRootPath);
                saveFile(cm.getTrainingRecord(), clinicalRootPath);
                saveFile(cm.getProcessFiles(), clinicalRootPath);
                saveFile(cm.getCompletionFiles(), clinicalRootPath);
                saveFile(cm.getOtherFiles(), clinicalRootPath);
            }
        }

        /* ========== 3. 直接 Mapper 入库（不建 Entity） ========== */
        professionalGroupMapper.insertPiInfo(
                Id,
                dto.getProfessional(),
                piPhotoPath,
                seniorTitleCertificatePath,
                seniorTitleAppointmentPath,
                signedResumePath,
                qualificationCertificatePath,
                practiceCertificatePath,
                gcpCertificatePath,
                dto.getShanchang(),
                Boolean.TRUE.equals(dto.getClinicalParticipation()) ? 1 : 0,
                dto.getClinicalReason(),
                clinicalRootPath
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




    private PiInfoVO convertFilePaths(PiInfoVO vo) {
        vo.setPiPhotoPath(toFileUrl(vo.getPiPhotoPath()));
        vo.setSeniorTitleCertificatePath(toFileUrl(vo.getSeniorTitleCertificatePath()));
        vo.setSeniorTitleAppointmentPath(toFileUrl(vo.getSeniorTitleAppointmentPath()));
        vo.setSignedResumePath(toFileUrl(vo.getSignedResumePath()));
        vo.setQualificationCertificatePath(toFileUrl(vo.getQualificationCertificatePath()));
        vo.setPracticeCertificatePath(toFileUrl(vo.getPracticeCertificatePath()));
        vo.setGcpCertificatePath(toFileUrl(vo.getGcpCertificatePath()));

//        // 临床材料列表
//        if (vo.getClinicalMaterials() != null) {
//            vo.getClinicalMaterials().forEach(material -> {
//                material.setFileUrl(toFileUrl(material.getFilePath()));
//                material.setFileName(getFileName(material.getFilePath()));
//            });
//        }

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
//            String userId = BaseContext.getCurrentId(); // 获取当前用户工号

            // 根据 type 决定基础目录
//            String baseDir = "D:/yan/upload/" + type + "/" + userId + "/";
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
