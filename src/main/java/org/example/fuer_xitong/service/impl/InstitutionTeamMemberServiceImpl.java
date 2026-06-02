package org.example.fuer_xitong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fuer_xitong.mapper.InstitutionTeamMemberMapper;
import org.example.fuer_xitong.pojo.dto.InstitutionTeamMemberDTO;
import org.example.fuer_xitong.pojo.minimal.InstitutionTeamMemberMinimalDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.InstitutionTeamMemberVO;
import org.example.fuer_xitong.service.InstitutionTeamMemberService;
import org.example.fuer_xitong.utils.FilePathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionTeamMemberServiceImpl implements InstitutionTeamMemberService {
    @Value("${file.upload-path}")
    private String uploadPath;
    @Value("${file.base-url}")
    private String baseUrl;
    @Autowired
    private InstitutionTeamMemberMapper institutionTeamMemberMapper;

    @Autowired
    private FilePathUtil filePathUtil;

    @Override
    public void saveOrUpdate(InstitutionTeamMemberDTO dto) {
        // 1. 获取当前操作人 ID
        String operatorId = BaseContext.getCurrentId();
        // ================== 1. 插入 minimal 数据，获取自增 zizi_id ==================
        InstitutionTeamMemberMinimalDTO minimalDTO = new InstitutionTeamMemberMinimalDTO();
        minimalDTO.setInstitutionMemberId(dto.getInstitutionMemberId());
        minimalDTO.setInstitutionId(dto.getInstitutionId());
        minimalDTO.setCreateById(operatorId);
        minimalDTO.setName(dto.getName());

        institutionTeamMemberMapper.insertInstitutionTeamMemberMinimal(minimalDTO);
        Integer ziziId = minimalDTO.getZiziId();
        if (ziziId == null) {
            throw new RuntimeException("获取自增 zizi_id 失败");
        }

        // ================== 2. 构建统一文件存储路径 ==================
        String baseDir = filePathUtil.buildUploadDir(
                "InstitutionTeamMember",
                dto.getInstitutionId(),
                dto.getInstitutionMemberId(),
                String.valueOf(ziziId)
        );

        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        }

        // ================== 3. 保存文件 ==================
        String resumePath = saveFile(dto.getResumeFile(), baseDir);
        String gcpPath = saveFile(dto.getGcpFile(), baseDir);
        String licensePath = saveFile(dto.getLicenseFile(), baseDir);

        // ================== 4. 拼接 positions ==================
        String positionsStr = dto.getPositions() != null
                ? String.join(",", dto.getPositions())
                : null;

        // ================== 5. 回写文件路径和业务字段 ==================
        institutionTeamMemberMapper.updateInstitutionTeamMemberFiles(
                ziziId,
                dto.getName(),
                positionsStr,
                resumePath,
                gcpPath,
                licensePath
        );

    }


    @Override
    public List<InstitutionTeamMemberVO> listAll() {
        List<InstitutionTeamMemberVO> list =
                institutionTeamMemberMapper.selectAll();

        for (InstitutionTeamMemberVO vo : list) {

            // 1. positions：字符串 → List
            if (StringUtils.hasText(vo.getPositionsStr())) {
                vo.setPositions(
                        Arrays.asList(vo.getPositionsStr().split(","))
                );
            }

            // 2. 文件路径转为可访问 URL
            convertFilePaths(vo);
        }

        return list;
    }


    @Override
    public void deleteById(Integer ziziId) {

        if (ziziId == null) {
            throw new IllegalArgumentException("ziziId 不能为空");
        }

        int rows = institutionTeamMemberMapper.deleteById(ziziId);

        if (rows == 0) {
            throw new RuntimeException("删除失败：记录不存在");
        }
    }

    private InstitutionTeamMemberVO  convertFilePaths(InstitutionTeamMemberVO  vo) {
        vo.setResumePath(toFileUrl(vo.getResumePath()));
        vo.setGcpPath(toFileUrl(vo.getGcpPath()));
        vo.setLicensePath(toFileUrl(vo.getLicensePath()));

        return vo;
    }

//    private String toFileUrl(String dbPath) {
//        if (dbPath == null || dbPath.isEmpty()) return null;
//        return "http://localhost:8080/files/" + dbPath.replace("yan/upload/", "");
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
