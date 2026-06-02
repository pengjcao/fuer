package org.example.fuer_xitong.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.mapper.BasicConditionMapper;
import org.example.fuer_xitong.mapper.UserMapper;
import org.example.fuer_xitong.pojo.dto.BasicConditionDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.service.BasicConditionService;
import org.example.fuer_xitong.utils.FilePathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicConditionServiceImpl implements BasicConditionService {

    @Autowired
    private  BasicConditionMapper basicConditionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FilePathUtil filePathUtil;

    @Override
    @Transactional
    public void reportBasicCondition(BasicConditionDTO dto) {
        log.info("基础条件填报入参: {}", dto);

        String createBy = BaseContext.getCurrentId();
        dto.setCreateBy(createBy);

        String baseDir = filePathUtil.buildUploadDir(
                "basicCondition",
                dto.getKeshi(),
                createBy
        );
        dto.setDepartmentPhotoPath(saveFile(dto.getDepartmentPhoto(), baseDir));
        dto.setDepartmentIntroductionPath(saveFile(dto.getDepartmentIntroduction(), baseDir));

        basicConditionMapper.insertBasicCondition(dto);
    }


    @Override
    public List<BasicConditionDTO> getBasicConditionDetail(String keshi) {
        keshi = resolveReadableKeshi(keshi);

        // 查询该科室数据
        List<BasicConditionDTO> list = basicConditionMapper.selectByKeshi(keshi);
        if (list != null) {
            for (BasicConditionDTO dto : list) {
                dto.setDepartmentPhotoPath(toFileUrl(dto.getDepartmentPhotoPath()));
                dto.setDepartmentIntroductionPath(toFileUrl(dto.getDepartmentIntroductionPath()));
            }
        }
        return list;
    }

    private String saveFile(MultipartFile file, String path) {
        return filePathUtil.saveFile(file, path);
    }

    private String toFileUrl(String dbPath) {
        return filePathUtil.toFileUrl(dbPath);
    }

    private String resolveReadableKeshi(String keshi) {
        String currentUserId = BaseContext.getCurrentId();
        Integer currentRole = BaseContext.getCurrentRole();
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        if (keshi == null || keshi.trim().isEmpty()) {
            return currentKeshi;
        }

        keshi = keshi.trim();

        // role > 1 是管理员/审批人员，可以查看任意科室
        if (currentRole != null && currentRole > 1) {
            return keshi;
        }

        // role = 1 研究者只能查看自己科室；保留原来“科研处”可查看全部的逻辑
        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        return keshi;
    }
}
