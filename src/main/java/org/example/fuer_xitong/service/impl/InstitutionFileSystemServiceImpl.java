package org.example.fuer_xitong.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.example.fuer_xitong.mapper.InstitutionFileSystemMapper;
import org.example.fuer_xitong.mapper.InstitutionSystemFileHistoryMapper;
import org.example.fuer_xitong.mapper.InstitutionSystemFileMapper;
import org.example.fuer_xitong.mapper.UserMapper;
import org.example.fuer_xitong.pojo.dto.InstitutionFileSystemCreateDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileVO;
import org.example.fuer_xitong.service.InstitutionFileSystemService;
import org.example.fuer_xitong.utils.DeletePhysicalFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class InstitutionFileSystemServiceImpl implements InstitutionFileSystemService {
    @Autowired
    private InstitutionFileSystemMapper institutionFileSystemMapper;
    @Autowired
    private InstitutionSystemFileMapper institutionSystemFileMapper;
    @Autowired
    private InstitutionSystemFileHistoryMapper institutionSystemFileHistoryMapper;

    @Autowired
    private UserMapper userMapper;

    @Value("${file.upload-path}")
    private String uploadPath;
    @Value("${file.base-url}")
    private String baseUrl;
    @Override
    @Transactional
    public void create(InstitutionFileSystemCreateDTO dto,String keshi,String groupPath) {
        String operatorId=BaseContext.getCurrentId();
        keshi = resolveWritableKeshi(keshi, operatorId);


//        String keshi=userMapper.selectKeshiByJobNumber(operatorId);
        dto.setKeshi(keshi);
        dto.setGroupPath(trimToNull(groupPath));
        dto.setOperatorId(operatorId);
        // 后续如果要加唯一性校验，可以在这里做
        institutionFileSystemMapper.insertFileSystem(dto);
    }

    @Override
    public List<InstitutionFileSystemVO> list(String keshi,String GroupPath) {
        String operatorID = BaseContext.getCurrentId();
        Integer role = BaseContext.getCurrentRole();

        if (!hasText(keshi) && !hasText(GroupPath)) {
            return institutionFileSystemMapper.selectInstitutionReadonlyList();
        }

        if (role != null && role > 1) {
            if(keshi != null && !keshi.trim().isEmpty())
            {
                List<InstitutionFileSystemVO> aaa= institutionFileSystemMapper.selectByKeshiAndGroupPath(keshi.trim(), trimToNull(GroupPath));
                return aaa;
            }
            String keshi1 = userMapper.selectKeshiByJobNumber(operatorID);

            return institutionFileSystemMapper.selectByKeshi(keshi1);
        }

        String currentKeshi = resolveReadableKeshi(keshi, operatorID);
        if (GroupPath != null && !GroupPath.trim().isEmpty()) {
            return institutionFileSystemMapper.selectByKeshiAndGroupPath(currentKeshi, GroupPath.trim());
        }
        return institutionFileSystemMapper.selectByKeshiForCreator(currentKeshi, operatorID);
    }

    @Override
    @Transactional
    public  void deleteSystem(Long systemId){

        if (systemId == null) {
            throw new IllegalArgumentException("systemId 不能为空");
        }

        InstitutionFileSystemVO system = institutionFileSystemMapper.selectById(systemId);
        if (system == null) {
            throw new RuntimeException("文件体系不存在");
        }
        assertCanManageSystem(system);

        // 1️⃣ 查询体系下所有文件
        List<InstitutionSystemFileVO> files =
                institutionSystemFileMapper.selectBySystemId(systemId);

        // 2️⃣ 删除物理文件
        for (InstitutionSystemFileVO file : files) {
            DeletePhysicalFile.deleteFile(file.getCurrentPath());
        }

        // 3️⃣ 删除历史记录
        institutionSystemFileHistoryMapper.deleteBySystemId(systemId);

        // 4️⃣ 删除主表文件记录
        institutionSystemFileMapper.deleteBySystemId(systemId);

        // 5️⃣ 删除文件体系记录
        institutionFileSystemMapper.deleteById(systemId);

        DeletePhysicalFile.deleteDirectoryQuietly(
                uploadPath+"upload/InstitutionSystemFile/" + systemId + "/"
        );

//        // 5️⃣ 删除体系目录（可选，不影响主流程）
//        try {
//            File systemDir = new File("D:/yan/upload/InstitutionSystemFile/" + systemId + "/");
//            if (systemDir.exists()) {
//                FileUtils.deleteDirectory(systemDir);
//            }
//        } catch (IOException e) {
//            // 只记录日志，绝不抛出
//            log.warn("删除体系物理目录失败，systemId={}", systemId, e);
//        }
    }


    private String resolveWritableKeshi(String keshi, String operatorId) {
        Integer role = BaseContext.getCurrentRole();
        String currentKeshi = userMapper.selectKeshiByJobNumber(operatorId);

        if (role != null && role > 1) {
            return hasText(keshi) ? keshi.trim() : null;
        }

        if (hasText(keshi) && !keshi.trim().equals(currentKeshi)) {
            throw new RuntimeException("无权限在其他科室新建文件体系");
        }

        return currentKeshi;
    }

    private String resolveReadableKeshi(String keshi, String operatorId) {
        String currentKeshi = userMapper.selectKeshiByJobNumber(operatorId);
        if (hasText(keshi) && !keshi.trim().equals(currentKeshi)) {
            throw new RuntimeException("无权限查看其他科室文件体系");
        }
        return currentKeshi;
    }

    private void assertCanManageSystem(InstitutionFileSystemVO system) {
        Integer role = BaseContext.getCurrentRole();
        if (role != null && role > 1) {
            return;
        }

        String operatorId = BaseContext.getCurrentId();
        if (isResearcherProfessionalGroupSystem(system, operatorId)) {
            return;
        }
        if (system.getCreatedBy() == null || !system.getCreatedBy().equals(operatorId)) {
            throw new RuntimeException("只能管理自己创建的文件体系");
        }
    }

    private boolean isResearcherProfessionalGroupSystem(InstitutionFileSystemVO system, String operatorId) {
        if (system == null || !hasText(system.getKeshi()) || !hasText(system.getGroupPath())) {
            return false;
        }

        String currentKeshi = userMapper.selectKeshiByJobNumber(operatorId);
        return hasText(currentKeshi) && system.getKeshi().trim().equals(currentKeshi.trim());
    }

    private String trimToNull(String value) {
        return hasText(value) ? value.trim() : null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }


}



