package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.ProfessionalGroupMapper;
import org.example.fuer_xitong.pojo.dto.PiInfoDTO;
import org.example.fuer_xitong.pojo.dto.ProfessionalGroupAddDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.service.ProfessionalGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ProfessionalGroupServiceImpl implements ProfessionalGroupService {

    @Autowired
    private ProfessionalGroupMapper professionalGroupMapper;

    @Override
    public void addProfessionalGroup(ProfessionalGroupAddDTO dto) {

        MultipartFile file = dto.getSelfAssessmentReport();
        String filePath = saveFile(file);
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

    }



    private String saveFile(MultipartFile file) {
        try {
            // 获取当前用户 ID
            String userId = BaseContext.getCurrentId(); // 假设返回 String 类型

            // 每个用户单独一个文件夹
            String dir = "D:/yan/upload/professional-group/" + userId + "/";
            File folder = new File(dir);
            if (!folder.exists()) folder.mkdirs();

            // 文件名加时间戳，保证同名文件不会覆盖
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = dir + fileName;

            // 保存文件到磁盘
            file.transferTo(new File(filePath));

            return filePath; // 返回完整路径，存数据库
        } catch (Exception e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }
}
