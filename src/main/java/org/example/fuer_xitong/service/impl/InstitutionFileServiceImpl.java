package org.example.fuer_xitong.service.impl;

import jakarta.transaction.Transactional;

import org.example.fuer_xitong.mapper.DrugTrialSopFileMapper;
import org.example.fuer_xitong.mapper.InstitutionFileMapper;
import org.example.fuer_xitong.mapper.InstitutionTeamMemberMapper;
import org.example.fuer_xitong.mapper.TrialManagementFileMapper;
import org.example.fuer_xitong.pojo.dto.InstitutionFileDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.entity.InstitutionFile;
import org.example.fuer_xitong.pojo.minimal.DrugTrialSopFileMinimalDTO;
import org.example.fuer_xitong.pojo.minimal.InstitutionFileMinimalDTO;
import org.example.fuer_xitong.pojo.minimal.TrialManagementFileMinimalDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionFileVO;
import org.example.fuer_xitong.pojo.vo.InstitutionTeamMemberVO;
import org.example.fuer_xitong.pojo.vo.InstitutionTrialManagementFileVO;
import org.example.fuer_xitong.service.InstitutionFileService;
import org.example.fuer_xitong.utils.ChangeRoute;
import org.example.fuer_xitong.utils.FilePathUtil;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InstitutionFileServiceImpl implements InstitutionFileService {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.base-url}")
    private String baseUrl;
    @Autowired
    private InstitutionFileMapper institutionFileMapper;

    @Autowired
    private FilePathUtil filePathUtil;



    @Autowired
    private TrialManagementFileMapper trialManagementFileMapper;

    @Autowired
    private DrugTrialSopFileMapper drugTrialSopFileMapper;

    @Override
    @Transactional
    public void saveOrUpdate(InstitutionFileDTO dto) {


        // ================== 1. 获取当前操作人 ==================
        String operatorId = BaseContext.getCurrentId();

        dto.setInstitutionId("科研处");
        // ================== 2. 插入 minimal 数据，获取自增 institution_file_id ==================
        InstitutionFileMinimalDTO minimalDTO = new InstitutionFileMinimalDTO();
        minimalDTO.setInstitutionId(dto.getInstitutionId());
        minimalDTO.setCreatedBy(operatorId);


        // 插入 minimal 记录
        institutionFileMapper.insertInstitutionFileMinimal(minimalDTO);

        Integer institutionFileId = minimalDTO.getInstitutionFileId();
        if (institutionFileId == null) {
            throw new RuntimeException("获取自增 institution_file_id 失败");
        }

        // ================== 3. 构建统一文件存储路径 ==================
        String baseDir = filePathUtil.buildUploadDir(
                "InstitutionFile",
                dto.getInstitutionId(),
                String.valueOf(institutionFileId)
        );

        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        }

        // ================== 4. 保存文件 ==================
        String trialManagementPath =
                saveFile(dto.getTrialManagementFile(), baseDir);

        String standardOperationPath =
                saveFile(dto.getStandardOperationFile(), baseDir);

        String emergencyPlanPath =
                saveFile(dto.getEmergencyPlanFile(), baseDir);

        String trainingPlanPath =
                saveFile(dto.getTrainingPlanFile(), baseDir);

        String qualityPlanPath =
                saveFile(dto.getQualityPlanFile(), baseDir);

        String otherFilePath =
                saveFile(dto.getOtherFile(), baseDir);

        // ================== 5. 回写文件路径 ==================
        institutionFileMapper.updateInstitutionFilePaths(
                institutionFileId,
                trialManagementPath,
                standardOperationPath,
                emergencyPlanPath,
                trainingPlanPath,
                qualityPlanPath,
                otherFilePath
        );

    }


    /**
     * 查询 institution_file 表的第一条记录
     */
    public InstitutionFileVO getInstitutionFile() {
        InstitutionFileVO vo = institutionFileMapper.selectFirst();
        if (vo == null) {
            throw new RuntimeException("institution_file 表为空，请先初始化数据");
        }
        return InstitutionFileConvertFilePaths(vo);

    }


    @Override
    @Transactional
    /*药物临床试验管理制度*/
    public void uploadTrialManagementFile(MultipartFile file) {

        // ================== 1. 校验 ==================
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        String operatorId = BaseContext.getCurrentId();
        String institutionId = "科研处"; // 当前阶段系统内定

        // ================== 2. 查询 institution_file（只会有一条） ==================
        InstitutionFile institutionFile =
                institutionFileMapper.selectByInstitutionId(institutionId);

        if (institutionFile == null) {
            throw new RuntimeException("institution_file 主表不存在，请先初始化");
        }

        Integer institutionFileId = institutionFile.getInstitutionFileId();

        // ================== 2. 插入 minimal 数据，获取自增 trial_file_id ==================
        TrialManagementFileMinimalDTO minimalDTO = new TrialManagementFileMinimalDTO();
        minimalDTO.setInstitutionId(institutionId);
        minimalDTO.setInstitutionFileId(institutionFileId);
        minimalDTO.setCreatedBy(operatorId);

        trialManagementFileMapper.insertMinimal(minimalDTO);
        Integer trialFileId = minimalDTO.getInstitutionTrialManagementFileId(); // 回填自增 ID

        // ================== 3. 构建存储目录 ==================
        String baseDir = filePathUtil.buildUploadDir(
                "InstitutionFile",
                institutionId,
                String.valueOf(institutionFileId),
                "trialManagement",
                String.valueOf(trialFileId)
        ); // 用自增 ID 分目录

        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ================== 4. 保存文件 ==================
        String filePath = saveFile(file, baseDir);


        // ================== 5. 作废旧历史记录（把 is_invalid = 1 → 0） ==================
        trialManagementFileMapper.invalidateCurrent(institutionFileId);

        // 5. 更新当前这条（而不是 insert）
        trialManagementFileMapper.updateCurrentById(trialFileId, filePath);

        // ================== 7. 更新主表最新路径 ==================
        institutionFileMapper.updateTrialManagementPath(
                institutionFileId,
                filePath
        );
    }


    /**
     * 查询临床试验管理制度历史记录，并转换文件URL
     */
    public List<InstitutionTrialManagementFileVO> listTrialManagementHistory(Integer institutionFileId) {

        List<InstitutionTrialManagementFileVO> list =
                trialManagementFileMapper.selectAllByInstitutionFileId(institutionFileId);

        List<InstitutionTrialManagementFileVO> result = new ArrayList<>();
        for (InstitutionTrialManagementFileVO vo : list) {
            result.add(trialManagementconvertFilePaths(vo));
        }

        return result;
    }


    /*药物临床试验标准操作规程*/
    @Override
    @Transactional
    public void uploadDrugTrialSopFile(MultipartFile file) {

        // ================== 1. 校验 ==================
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        String operatorId = BaseContext.getCurrentId();
        String institutionId = "科研处"; // 当前阶段系统内定

        // ================== 2. 查询 institution_file（只会有一条） ==================
        InstitutionFile institutionFile =
                institutionFileMapper.selectByInstitutionId(institutionId);

        if (institutionFile == null) {
            throw new RuntimeException("institution_file 主表不存在，请先初始化");
        }

        Integer institutionFileId = institutionFile.getInstitutionFileId();

        // ================== 3. 插入 minimal 数据，获取自增 sop_file_id ==================
        DrugTrialSopFileMinimalDTO minimalDTO = new DrugTrialSopFileMinimalDTO();
        minimalDTO.setInstitutionId(institutionId);
        minimalDTO.setInstitutionFileId(institutionFileId);
        minimalDTO.setCreatedBy(operatorId);

        drugTrialSopFileMapper.insertMinimal(minimalDTO);

        Integer sopFileId =
                minimalDTO.getInstitutionDrugTrialSopFileId(); // 回填自增 ID

        // ================== 4. 构建存储目录 ==================
        String baseDir = filePathUtil.buildUploadDir(
                "InstitutionFile",
                institutionId,
                String.valueOf(institutionFileId),
                "drugTrialSop",
                String.valueOf(sopFileId)
        );   // 用自增 ID 分目录

        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ================== 5. 保存文件 ==================
        String filePath = saveFile(file, baseDir);

        // ================== 6. 作废旧历史记录 ==================
        drugTrialSopFileMapper.invalidateCurrent(institutionFileId);

        // ================== 7. 更新当前这条记录（写入 file_path） ==================
        drugTrialSopFileMapper.updateCurrentById(
                sopFileId,
                filePath
        );

        // ================== 8. 更新主表最新路径 ==================
        institutionFileMapper.updateDrugTrialSopPath(
                institutionFileId,
                filePath
        );
    }




    private InstitutionTeamMemberVO convertFilePaths(InstitutionTeamMemberVO  vo) {
        vo.setResumePath(toFileUrl(vo.getResumePath()));
        vo.setGcpPath(toFileUrl(vo.getGcpPath()));
        vo.setLicensePath(toFileUrl(vo.getLicensePath()));

        return vo;
    }

    private InstitutionFileVO InstitutionFileConvertFilePaths(InstitutionFileVO vo) {

        vo.setTrialManagementPath(toFileUrl(vo.getTrialManagementPath()));
        vo.setStandardOperationPath(toFileUrl(vo.getStandardOperationPath()));
        vo.setEmergencyPlanPath(toFileUrl(vo.getEmergencyPlanPath()));
        vo.setTrainingPlanPath(toFileUrl(vo.getTrainingPlanPath()));
        vo.setQualityPlanPath(toFileUrl(vo.getQualityPlanPath()));
        vo.setOtherFilePath(toFileUrl(vo.getOtherFilePath()));

        return vo;
    }

    /** 接收 VO 的 convertFilePaths 方法 */
    private InstitutionTrialManagementFileVO trialManagementconvertFilePaths(InstitutionTrialManagementFileVO vo) {
        vo.setFileUrl(toFileUrl(vo.getFilePath()));
        return vo;
    }
    /**
     * 将数据库路径转换为前端可访问 URL
     */
//    private InstitutionTrialManagementFileVO trialconvertFilePaths(InstitutionTrialManagementFile record) {
//        InstitutionTrialManagementFileVO vo = new InstitutionTrialManagementFileVO();
//        BeanUtils.copyProperties(record, vo);
//
//        // 转换 filePath 为可访问 URL
//        vo.setFileUrl(toFileUrl(record.getFilePath()));
//
//        return vo;
//    }

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
