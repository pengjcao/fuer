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
        if (keshi == null || keshi.trim().isEmpty()) {
            keshi = userMapper.selectKeshiByJobNumber(operatorId);
        }


//        String keshi=userMapper.selectKeshiByJobNumber(operatorId);
        dto.setKeshi(keshi);
        dto.setGroupPath(groupPath);
        dto.setOperatorId(operatorId);
        // 后续如果要加唯一性校验，可以在这里做
        institutionFileSystemMapper.insertFileSystem(dto);
    }

    @Override
    public List<InstitutionFileSystemVO> list(String keshi,String GroupPath) {
        String operatorID = BaseContext.getCurrentId();
        Integer role = BaseContext.getCurrentRole();
        if(keshi != null && !keshi.trim().isEmpty())
        {
            List<InstitutionFileSystemVO> aaa= institutionFileSystemMapper.selectByKeshiAndGroupPath(keshi,GroupPath);
            return aaa;
        }
        String keshi1 = userMapper.selectKeshiByJobNumber(operatorID);

        return institutionFileSystemMapper.selectByKeshi(keshi1);
    }

    @Override
    public  void deleteSystem(Long systemId){

        if (systemId == null) {
            throw new IllegalArgumentException("systemId 不能为空");
        }

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




}



