package org.example.fuer_xitong.service.impl;

import jakarta.transaction.Transactional;
import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.dto.ClinicalMaterialDTO;
import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.minimal.PiInfoMinimalDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;
import org.example.fuer_xitong.pojo.vo.ClinicalMaterialVO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;
import org.example.fuer_xitong.service.ProfessionalGroupService;
import org.example.fuer_xitong.utils.FilePathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProfessionalGroupServiceImpl implements ProfessionalGroupService {
    @Value("${file.upload-path}")
    private String uploadPath;
    @Value("${file.base-url}")
    private String baseUrl;
    @Autowired
    private ProfessionalGroupMapper professionalGroupMapper;

    @Autowired
    private FilePathUtil filePathUtil;



    @Override
    public void addProfessionalGroup(ProfessionalGroupAddDTO dto) {

        MultipartFile file = dto.getSelfAssessmentReport();
        String filePath = saveFile(file, filePathUtil.buildUploadDir("professional-group"));
        String Id=BaseContext.getCurrentId();
        professionalGroupMapper.insertProfessionalGroup(
                Id,
                String.join(",", dto.getRecordTypes()),
                String.join(",", dto.getRecordNames()),
                String.join(",", dto.getHospitalAreas()),
                filePath);
    }

    @Transactional
    @Override
    public void addPiInfo(PiInfoDTO dto) {
        String id = BaseContext.getCurrentId();
        dto.setId(id);

        // ================== 1. 插入 minimal 数据，获取自增 pi_info_id ==================
        PiInfoMinimalDTO minimalDTO = new PiInfoMinimalDTO();
        minimalDTO.setId(id);
        minimalDTO.setProfessional(dto.getProfessional());
        minimalDTO.setApplyType(dto.getApplyType());


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
        String baseDir = filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId));
        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) baseFolder.mkdirs();

        // ================== 3. 保存 PI 基本文件 ==================
        String piPhotoPath = saveFile(dto.getPiPhoto(), baseDir);
        String idCardCopyPath = saveFile(dto.getIdCardCopy(), baseDir);
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
        List<ClinicalMaterialVO> clinicalMaterialList = new ArrayList<>();
        if (Boolean.TRUE.equals(dto.getClinicalParticipation())
                && dto.getClinicalMaterials() != null
                && !dto.getClinicalMaterials().isEmpty()) {

            clinicalRootPath = filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical");
            int materialIndex = 1;
            for (ClinicalMaterialDTO cm : dto.getClinicalMaterials()) {
                if (!hasClinicalMaterial(cm)) {
                    materialIndex++;
                    continue;
                }

                String projectDirName = "project-" + materialIndex;
                ClinicalMaterialVO material = new ClinicalMaterialVO();
                material.setProjectName(cm.getProjectName());
                material.setNmpaApprovalPath(saveFiles(
                        cm.getNmpaApproval(),
                        filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "nmpaApproval")
                ));
                material.setDelegationTablePath(saveFiles(
                        cm.getDelegationTable(),
                        filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "delegationTable")
                ));
                material.setTrainingRecordPath(saveFiles(
                        cm.getTrainingRecord(),
                        filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "trainingRecord")
                ));
                material.setProcessFilesPath(saveFiles(
                        cm.getProcessFiles(),
                        filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "processFiles")
                ));
                material.setCompletionFilesPath(saveFiles(
                        cm.getCompletionFiles(),
                        filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "completionFiles")
                ));
                material.setOtherFilesPath(saveFiles(
                        cm.getOtherFiles(),
                        filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "otherFiles")
                ));

                clinicalMaterialList.add(material);
                materialIndex++;
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
                idCardCopyPath,
                Boolean.TRUE.equals(dto.getClinicalParticipation()) ? 1 : 0,
                dto.getClinicalReason(),
                clinicalRootPath,
                dto.getShanchang(),
                selfAssessmentReportPath,  // 自评报告路径
                recordTypesStr,            // 专业组备案类型
                hospitalAreasStr           // 所属院区
        );

        for (ClinicalMaterialVO material : clinicalMaterialList) {
            professionalGroupMapper.insertClinicalMaterial(piInfoId, material);
        }
    }

    @Override
    public PiInfoVO getRejectedPiInfoForResubmit(Integer piInfoId) {
        PiInfoVO pi = getRejectedPiInfo(piInfoId);
        PiInfoVO result = convertFilePaths(pi);
        fillClinicalMaterials(Collections.singletonList(result));
        return result;
    }

    @Transactional
    @Override
    public void resubmitRejectedPiInfo(Integer piInfoId, PiInfoDTO dto) {
        PiInfoVO existing = getRejectedPiInfo(piInfoId);
        String id = BaseContext.getCurrentId();
        dto.setId(id);

        String baseDir = filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId));
        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) baseFolder.mkdirs();

        String piPhotoPath = saveOrKeep(dto.getPiPhoto(), baseDir, dto.getExistingPiPhotoPath());
        String idCardCopyPath = saveOrKeep(dto.getIdCardCopy(), baseDir, dto.getExistingIdCardCopyPath());
        String seniorTitleCertificatePath = saveOrKeep(dto.getSeniorTitleCertificate(), baseDir, dto.getExistingSeniorTitleCertificatePath());
        String seniorTitleAppointmentPath = saveOrKeep(dto.getSeniorTitleAppointment(), baseDir, dto.getExistingSeniorTitleAppointmentPath());
        String signedResumePath = saveOrKeep(dto.getSignedResume(), baseDir, dto.getExistingSignedResumePath());
        String qualificationCertificatePath = saveOrKeep(dto.getQualificationCertificate(), baseDir, dto.getExistingQualificationCertificatePath());
        String practiceCertificatePath = saveOrKeep(dto.getPracticeCertificate(), baseDir, dto.getExistingPracticeCertificatePath());
        String gcpCertificatePath = saveOrKeep(dto.getGcpCertificate(), baseDir, dto.getExistingGcpCertificatePath());
        String selfAssessmentReportPath = saveOrKeep(dto.getSelfAssessmentReport(), baseDir, dto.getExistingSelfAssessmentReportPath());

        String recordTypesStr = dto.getRecordTypes() != null ? String.join(",", dto.getRecordTypes()) : existing.getRecordTypes();
        String hospitalAreasStr = dto.getHospitalAreas() != null ? String.join(",", dto.getHospitalAreas()) : existing.getHospitalAreas();
        String professional = StringUtils.hasText(dto.getProfessional()) ? dto.getProfessional() : existing.getProfessional();
        String shanchang = StringUtils.hasText(dto.getShanchang()) ? dto.getShanchang() : existing.getShanchang();
        Integer applyType = dto.getApplyType() != null ? dto.getApplyType() : existing.getApplyType();

        List<ClinicalMaterialVO> clinicalMaterialList = buildResubmitClinicalMaterials(dto, id, piInfoId);
        boolean hasClinicalMaterials = !clinicalMaterialList.isEmpty();
        String clinicalRootPath = hasClinicalMaterials
                ? filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical")
                : null;

        professionalGroupMapper.updatePiInfoForResubmit(
                piInfoId,
                id,
                professional,
                applyType,
                piPhotoPath,
                seniorTitleCertificatePath,
                seniorTitleAppointmentPath,
                signedResumePath,
                qualificationCertificatePath,
                practiceCertificatePath,
                gcpCertificatePath,
                idCardCopyPath,
                hasClinicalMaterials ? 1 : 0,
                dto.getClinicalReason(),
                clinicalRootPath,
                shanchang,
                selfAssessmentReportPath,
                recordTypesStr,
                hospitalAreasStr
        );

        professionalGroupMapper.deleteClinicalMaterialsByPiInfoId(piInfoId);
        for (ClinicalMaterialVO material : clinicalMaterialList) {
            professionalGroupMapper.insertClinicalMaterial(piInfoId, material);
        }
    }





    public List<PiInfoVO> getPendingApprovalList() {
        List<PiInfoVO> list = professionalGroupMapper.selectPendingApprovalVO();

        // 2. 遍历列表，转换每个 VO 的文件路径
        List<PiInfoVO> result = new ArrayList<>();
        for (PiInfoVO vo : list) {
            result.add(convertFilePaths(vo));
        }

        fillClinicalMaterials(result);
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

        fillClinicalMaterials(result);
        return result;
    }



    @Override
    public Map<String, List<PiInfoVO>> groupByProfessional(List<PiInfoVO> piList) {

        if (CollectionUtils.isEmpty(piList)) {
            return Collections.emptyMap();
        }

        Map<String, List<PiInfoVO>> result = new LinkedHashMap<>();

        for (PiInfoVO pi : piList) {
            String group = pi.getProfessional();
            if (group == null) continue;

            result.computeIfAbsent(group, k -> new ArrayList<>()).add(pi);
        }

        return result;
    }




    @Override
    public void fillDrugAdminRecordTime(Integer piInfoId, LocalDateTime recordTime) {


        // 1. 查询当前审批步骤
        PiInfoVO pi = professionalGroupMapper.selectPiinfoById(piInfoId);

        if (pi == null) {
            throw new RuntimeException("PI 信息不存在");
        }
        Integer  currentStep=pi.getCurrentStep();
        if (currentStep == null) {
            throw new RuntimeException("PI 审批步骤不存在");
        }

        // 2. 校验：必须是机构主任已完成审批（step = 4）
        if (currentStep != 4) {
            throw new RuntimeException("当前审批流程未完成，不能填写药监局备案时间");
        }

        // 3. 填写备案时间
        professionalGroupMapper.updateDrugAdminRecordTime(piInfoId, recordTime);
    }

    private PiInfoVO getRejectedPiInfo(Integer piInfoId) {
        if (piInfoId == null) {
            throw new RuntimeException("PI 信息ID不能为空");
        }
        PiInfoVO pi = professionalGroupMapper.selectPiinfoById(piInfoId);
        if (pi == null) {
            throw new RuntimeException("PI 信息不存在");
        }

        String currentId = BaseContext.getCurrentId();
        if (!Objects.equals(currentId, pi.getId())) {
            throw new RuntimeException("无权限修改该 PI 备案申请");
        }

        String status = pi.getApplyStatus() == null ? "" : pi.getApplyStatus().trim().toUpperCase();
        if (pi.getCurrentStep() != 0 && !"REJECT".equals(status) && !"REJECTED".equals(status)) {
            throw new RuntimeException("只有已驳回的申请才能回填修改");
        }
        return pi;
    }

    private String saveOrKeep(MultipartFile file, String baseDir, String oldPath) {
        String newPath = saveFile(file, baseDir);
        return StringUtils.hasText(newPath) ? newPath : filePathUtil.toStoragePath(oldPath);
    }

    private List<ClinicalMaterialVO> buildResubmitClinicalMaterials(PiInfoDTO dto, String id, Integer piInfoId) {
        if (dto.getClinicalMaterials() == null || dto.getClinicalMaterials().isEmpty()) {
            return Collections.emptyList();
        }

        List<ClinicalMaterialVO> result = new ArrayList<>();
        int materialIndex = 1;
        for (ClinicalMaterialDTO cm : dto.getClinicalMaterials()) {
            if (!hasClinicalMaterialForResubmit(cm)) {
                materialIndex++;
                continue;
            }

            String projectDirName = "project-" + materialIndex;
            ClinicalMaterialVO material = new ClinicalMaterialVO();
            material.setProjectName(cm.getProjectName());
            material.setNmpaApprovalPath(mergeExistingAndNewFiles(
                    cm.getExistingNmpaApprovalPaths(),
                    cm.getNmpaApproval(),
                    filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "nmpaApproval")
            ));
            material.setDelegationTablePath(mergeExistingAndNewFiles(
                    cm.getExistingDelegationTablePaths(),
                    cm.getDelegationTable(),
                    filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "delegationTable")
            ));
            material.setTrainingRecordPath(mergeExistingAndNewFiles(
                    cm.getExistingTrainingRecordPaths(),
                    cm.getTrainingRecord(),
                    filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "trainingRecord")
            ));
            material.setProcessFilesPath(mergeExistingAndNewFiles(
                    cm.getExistingProcessFilesPaths(),
                    cm.getProcessFiles(),
                    filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "processFiles")
            ));
            material.setCompletionFilesPath(mergeExistingAndNewFiles(
                    cm.getExistingCompletionFilesPaths(),
                    cm.getCompletionFiles(),
                    filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "completionFiles")
            ));
            material.setOtherFilesPath(mergeExistingAndNewFiles(
                    cm.getExistingOtherFilesPaths(),
                    cm.getOtherFiles(),
                    filePathUtil.buildUploadDir("Pi", id, String.valueOf(piInfoId), "clinical", projectDirName, "otherFiles")
            ));

            result.add(material);
            materialIndex++;
        }
        return result;
    }

    private String mergeExistingAndNewFiles(List<String> existingPaths, List<MultipartFile> files, String path) {
        List<String> mergedPaths = new ArrayList<>();
        if (existingPaths != null) {
            existingPaths.stream()
                    .filter(StringUtils::hasText)
                    .map(filePathUtil::toStoragePath)
                    .filter(StringUtils::hasText)
                    .forEach(mergedPaths::add);
        }

        String savedFiles = saveFiles(files, path);
        if (StringUtils.hasText(savedFiles)) {
            Arrays.stream(savedFiles.split("\\R"))
                    .filter(StringUtils::hasText)
                    .forEach(mergedPaths::add);
        }

        if (mergedPaths.isEmpty()) {
            return null;
        }
        return String.join("\n", mergedPaths);
    }

    private boolean hasClinicalMaterialForResubmit(ClinicalMaterialDTO cm) {
        if (cm == null) {
            return false;
        }
        return StringUtils.hasText(cm.getProjectName())
                || hasFiles(cm.getNmpaApproval())
                || hasFiles(cm.getDelegationTable())
                || hasFiles(cm.getTrainingRecord())
                || hasFiles(cm.getProcessFiles())
                || hasFiles(cm.getCompletionFiles())
                || hasFiles(cm.getOtherFiles())
                || hasTextItems(cm.getExistingNmpaApprovalPaths())
                || hasTextItems(cm.getExistingDelegationTablePaths())
                || hasTextItems(cm.getExistingTrainingRecordPaths())
                || hasTextItems(cm.getExistingProcessFilesPaths())
                || hasTextItems(cm.getExistingCompletionFilesPaths())
                || hasTextItems(cm.getExistingOtherFilesPaths());
    }

    private boolean hasTextItems(List<String> items) {
        return items != null && items.stream().anyMatch(StringUtils::hasText);
    }



    private PiInfoVO convertFilePaths(PiInfoVO vo) {
        vo.setPiPhotoPath(toFileUrl(vo.getPiPhotoPath()));
        vo.setIdCardCopyPath(toFileUrl(vo.getIdCardCopyPath()));
        vo.setSeniorTitleCertificatePath(toFileUrl(vo.getSeniorTitleCertificatePath()));
        vo.setSeniorTitleAppointmentPath(toFileUrl(vo.getSeniorTitleAppointmentPath()));
        vo.setSignedResumePath(toFileUrl(vo.getSignedResumePath()));
        vo.setQualificationCertificatePath(toFileUrl(vo.getQualificationCertificatePath()));
        vo.setPracticeCertificatePath(toFileUrl(vo.getPracticeCertificatePath()));
        vo.setGcpCertificatePath(toFileUrl(vo.getGcpCertificatePath()));
        vo.setReportFilePath(toFileUrl(vo.getReportFilePath()));
        return vo;
    }

    private void fillClinicalMaterials(List<PiInfoVO> piList) {
        if (CollectionUtils.isEmpty(piList)) {
            return;
        }

        List<Integer> piInfoIds = piList.stream()
                .map(PiInfoVO::getPiInfoId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (piInfoIds.isEmpty()) {
            return;
        }

        List<ClinicalMaterialVO> materials =
                professionalGroupMapper.selectClinicalMaterialsByPiInfoIds(piInfoIds);
        if (CollectionUtils.isEmpty(materials)) {
            for (PiInfoVO pi : piList) {
                pi.setClinicalMaterials(Collections.emptyList());
            }
            return;
        }

        Map<Integer, List<ClinicalMaterialVO>> materialMap = materials.stream()
                .map(this::convertClinicalMaterialPaths)
                .collect(Collectors.groupingBy(
                        ClinicalMaterialVO::getPiInfoId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        for (PiInfoVO pi : piList) {
            pi.setClinicalMaterials(
                    materialMap.getOrDefault(pi.getPiInfoId(), Collections.emptyList())
            );
        }
    }

    private ClinicalMaterialVO convertClinicalMaterialPaths(ClinicalMaterialVO material) {
        material.setNmpaApprovalPaths(toFileUrls(material.getNmpaApprovalPath()));
        material.setDelegationTablePaths(toFileUrls(material.getDelegationTablePath()));
        material.setTrainingRecordPaths(toFileUrls(material.getTrainingRecordPath()));
        material.setProcessFilesPaths(toFileUrls(material.getProcessFilesPath()));
        material.setCompletionFilesPaths(toFileUrls(material.getCompletionFilesPath()));
        material.setOtherFilesPaths(toFileUrls(material.getOtherFilesPath()));
        return material;
    }

    private List<String> toFileUrls(String pathText) {
        if (!StringUtils.hasText(pathText)) {
            return Collections.emptyList();
        }

        return Arrays.stream(pathText.split("\\R"))
                .filter(StringUtils::hasText)
                .map(this::toFileUrl)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    private boolean hasClinicalMaterial(ClinicalMaterialDTO cm) {
        if (cm == null) {
            return false;
        }
        return StringUtils.hasText(cm.getProjectName())
                || hasFiles(cm.getNmpaApproval())
                || hasFiles(cm.getDelegationTable())
                || hasFiles(cm.getTrainingRecord())
                || hasFiles(cm.getProcessFiles())
                || hasFiles(cm.getCompletionFiles())
                || hasFiles(cm.getOtherFiles());
    }

    private boolean hasFiles(List<MultipartFile> files) {
        if (CollectionUtils.isEmpty(files)) {
            return false;
        }
        return files.stream().anyMatch(file -> file != null && !file.isEmpty());
    }

    private String saveFiles(List<MultipartFile> files, String path) {
        if (!hasFiles(files)) {
            return null;
        }

        List<String> savedPaths = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(file -> saveFile(file, path))
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        if (savedPaths.isEmpty()) {
            return null;
        }
        return String.join("\n", savedPaths);
    }
    /**
     * 磁盘路径 -> 前端可访问 URL
     */
//    private String toFileUrl(String dbPath) {
//        if (dbPath == null || dbPath.isEmpty()) return null;
//        return "http://localhost:8080/files/" + dbPath.replace("upload/", "");
//    }
    private String toFileUrl(String dbPath) {
        return filePathUtil.toFileUrl(dbPath);
    }

    /**
     * 从磁盘路径获取文件名
     */
    private String getFileName(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) return null;
        return Paths.get(dbPath).getFileName().toString();
    }



    private String saveFile(MultipartFile file, String path) {
        return filePathUtil.saveFile(file, path);
    }
}
