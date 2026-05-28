package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.ProfessionalGroupMemberMapper;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupMemberCreateDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.ProfessionalGroupMemberVO;
import org.example.fuer_xitong.service.NoticeGroupService;
import org.example.fuer_xitong.service.ProfessionalGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProfessionalGroupMemberServiceImpl implements ProfessionalGroupMemberService {
    @Value("${file.upload-path}")
    private String uploadPath;
    @Value("${file.base-url}")
    private String baseUrl;
    @Autowired
    private ProfessionalGroupMemberMapper professionalGroupMemberMapper;

    @Override
    @Transactional
    public void createMember(ProfessionalGroupMemberCreateDTO dto) {

        // 1️⃣ 当前操作人
        String operatorId = BaseContext.getCurrentId();

        // 2️⃣ 校验必填
        if (dto.getDepartmentId() == null) {
            throw new RuntimeException("科室ID不能为空");
        }
        if (dto.getKeshi() == null || dto.getKeshi().trim().isEmpty()) {
            throw new RuntimeException("科室名称不能为空");
        }
        if (dto.getGroupPath() == null || dto.getGroupPath().trim().isEmpty()) {
            throw new RuntimeException("专业组不能为空");
        }

        // 3️⃣ 专业组任职列表转字符串
        String rolesStr = null;
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            rolesStr = String.join(",", dto.getRoles());
        }

        // 4️⃣ 基础路径（每个专业组成员独立文件夹）
        String baseDir = uploadPath+"upload/ProfessionalGroupMember/"
                + dto.getDepartmentId() + "/"
                + dto.getGroupPath() + "/";

        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        }

        // 5️⃣ 遍历文件上传（每个文件 = 一条记录）
        MultipartFile[] files = new MultipartFile[]{
                dto.getResumeFile(),
                dto.getGcpCertFile(),
                dto.getPracticeCertFile()
        };

        String[] fileTypes = new String[]{"resume", "gcp", "practice"};
        String[] fileNames = new String[]{"resumeFilePath", "gcpCertPath", "practiceCertPath"};
        String[] dbPaths = new String[3];

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file == null || file.isEmpty()) {
                dbPaths[i] = null;
                continue;
            }

            String filePath = saveFile(file, baseDir);
            dbPaths[i] = filePath;
            dbPaths[i] = filePath.replace("\\", "/");
        }


        // 6️⃣ 插入数据库
        professionalGroupMemberMapper.insertMember(
                dto.getDepartmentId(),
                dto.getKeshi(),
                dto.getGroupPath(),
                dto.getPersonType(),
                dto.getName(),
                rolesStr,
                dto.getResumeText(),
                dbPaths[0], // resumeFilePath
                dbPaths[1], // gcpCertPath
                dbPaths[2], // practiceCertPath
                operatorId
        );
    }




    @Override
    public List<ProfessionalGroupMemberVO> queryMembers(Integer departmentId, String groupPath) {

        if (departmentId == null) {
            throw new RuntimeException("科室ID不能为空");
        }
        if (groupPath == null || groupPath.trim().isEmpty()) {
            throw new RuntimeException("专业组不能为空");
        }

        List<ProfessionalGroupMemberVO> list =
                professionalGroupMemberMapper.selectByDepartmentAndGroup(
                        departmentId, groupPath
                );

        if (list == null || list.isEmpty()) {
            return list;
        }

        for (ProfessionalGroupMemberVO vo : list) {

            // ✅ roles 字符串 → List<String>
            if (vo.getRoles() != null && !vo.getRoles().trim().isEmpty()) {
                vo.setRolesList(Arrays.asList(vo.getRoles().split(",")));
            } else {
                vo.setRolesList(new ArrayList<>());
            }


            // 2️⃣ 文件路径 → 前端 URL
            vo.setResumeFileUrl(toFileUrl(vo.getResumeFileUrl()));
            vo.setGcpCertUrl(toFileUrl(vo.getGcpCertUrl()));
            vo.setPracticeCertUrl(toFileUrl(vo.getPracticeCertUrl()));
        }

        return list;
    }



//    private String toFileUrl(String dbPath) {
//        if (dbPath == null || dbPath.isEmpty()) return null;
//        return "http://localhost:8080/files/" + dbPath.replace("upload/", "");
//    }
private String toFileUrl(String dbPath) {
    if (dbPath == null || dbPath.isEmpty()) {
        return null;
    }
    return baseUrl + "/files/" + dbPath;
}



    /**
     * 保存单个文件，返回存储路径（沿用你原来的写法）
     */
    private String saveFile(MultipartFile file, String baseDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String originalName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalName;

            File dest = new File(baseDir + fileName);
            dest.getParentFile().mkdirs();

            file.transferTo(dest);
            return dest.getAbsolutePath();

        } catch (Exception e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }

}
