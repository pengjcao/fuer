package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.InstitutionSystemFileHistoryMapper;
import org.example.fuer_xitong.mapper.InstitutionSystemFileMapper;
import org.example.fuer_xitong.mapper.UserMapper;
import org.example.fuer_xitong.pojo.dto.InstitutionSystemFileHistoryDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileHistoryVO;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileVO;
import org.example.fuer_xitong.service.InstitutionSystemFileService;
import org.example.fuer_xitong.utils.DeletePhysicalFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstitutionSystemFileServiceImpl implements InstitutionSystemFileService {

    @Autowired
    private InstitutionSystemFileMapper institutionSystemFileMapper;

    @Autowired
    private InstitutionSystemFileHistoryMapper institutionSystemFileHistoryMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void uploadFiles(Long systemId, MultipartFile[] files,String keshi,String Grouppath) {

        // 1️⃣ 当前操作人
        String operatorId = BaseContext.getCurrentId();

        if (files == null || files.length == 0) {
            return;
        }

        // 2️⃣ 计算最终使用的科室
        String finalKeshi;
        if (keshi != null && !keshi.trim().isEmpty()) {
            finalKeshi = keshi.trim();
        } else {
            finalKeshi = userMapper.selectKeshiByJobNumber(operatorId);
        }


        // 3️⃣ 计算最终使用的专业组（可选）
        String finalGroupPath = null;
        if (Grouppath != null && !Grouppath.trim().isEmpty()) {
            finalGroupPath = Grouppath.trim();
        }
        // 2️⃣ 体系级目录（不会再用自增ID作为目录）
        String baseDir = "D:/yan/upload/"+ finalKeshi  +"/"+ systemId + "/"+finalGroupPath +"/";

        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        }

        // 3️⃣ 遍历文件：一个文件 = 一条记录
        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                continue;
            }

            // 3.1 保存文件
            String filePath = saveFile(file, baseDir);

            // 3.2 插入数据库记录
            institutionSystemFileMapper.insert(
                    systemId,
                    file.getOriginalFilename(),
                    filePath,
                    operatorId,
                    finalKeshi,
                    finalGroupPath
            );
        }
    }


//    @Override
//    public List<InstitutionSystemFileVO> queryBySystemId(Long systemId) {
//
//        if (systemId == null) {
//            throw new IllegalArgumentException("systemId 不能为空");
//        }
//
//        return institutionSystemFileMapper.selectBySystemId(systemId);
//    }

    @Override
    public List<InstitutionSystemFileVO> queryBySystemId(Long systemId,String keshi,String Grouppath) {

        if (systemId == null) {
            throw new IllegalArgumentException("systemId 不能为空");
        }
        if(keshi!=null && !keshi.trim().isEmpty()) {
            // 查询数据库
            List<InstitutionSystemFileVO> aaa = institutionSystemFileMapper.selectByCondition(systemId,keshi,Grouppath);
            return aaa;
        }

        // 查询数据库
        List<InstitutionSystemFileVO> list = institutionSystemFileMapper.selectBySystemId(systemId);

        // 转成前端可访问路径
        return convertFilePaths(list);
    }



    @Override
    @Transactional
    public void overwriteFile(Long fileId, MultipartFile file, String remark) {

        if (fileId == null) {
            throw new IllegalArgumentException("fileId 不能为空");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String operatorId = BaseContext.getCurrentId();

        // 1️⃣ 查询当前文件（主表）
        InstitutionSystemFileVO current =
                institutionSystemFileMapper.selectById(fileId);

        if (current == null) {
            throw new RuntimeException("文件不存在");
        }

        // 2️⃣ 写入历史表（保留旧文件名和路径）
        InstitutionSystemFileHistoryDTO historyDTO = new InstitutionSystemFileHistoryDTO();
        historyDTO.setFileId(current.getId());
        historyDTO.setSystemId(current.getSystemId());
        historyDTO.setFileName(current.getFileName()); // ⚠️ 旧文件名
        historyDTO.setFilePath(current.getCurrentPath()); // ⚠️ 旧路径
        historyDTO.setOperatedBy(operatorId);
        historyDTO.setRemark(remark);
        institutionSystemFileHistoryMapper.insertHistory(historyDTO);


        String keshi = userMapper.selectKeshiByJobNumber(operatorId);
        // 3️⃣ 保存新文件（复用你的 saveFile）
        String baseDir = "D:/yan/upload/"+keshi +"/"+ current.getSystemId() + "/";

        File baseFolder = new File(baseDir);
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        }

        String newPath = saveFile(file, baseDir);
        String newFileName = file.getOriginalFilename(); // ⚠️ 新文件名

        // 4️⃣ 更新主表（current_path + file_name）
        institutionSystemFileMapper.updateFilePathAndName(
                current.getId(),
                newPath,
                newFileName
        );
    }

    @Override
    @Transactional
    public void invalidateFile(Long fileId) {

        if (fileId == null) {
            throw new IllegalArgumentException("fileId 不能为空");
        }

        InstitutionSystemFileVO file = institutionSystemFileMapper.selectById(fileId);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }

        // 标记为失效
        institutionSystemFileMapper.markAsInactive(fileId);
    }



    @Override
    public List<InstitutionSystemFileHistoryVO> queryFileHistory(Long fileId) {

        if (fileId == null) {
            throw new IllegalArgumentException("fileId 不能为空");
        }

        // 查询历史记录列表
        List<InstitutionSystemFileHistoryVO> list =
                institutionSystemFileHistoryMapper.selectByFileId(fileId);

        // 转换路径成前端可访问 URL
        return list.stream()
                .map(this::convertFilePath)
                .collect(Collectors.toList());
    }

    private InstitutionSystemFileHistoryVO convertFilePath(InstitutionSystemFileHistoryVO vo) {
        if (vo == null) return null;
        vo.setCurrentPath(toFileUrl(vo.getCurrentPath()));
        return vo;
    }


    @Override
    @Transactional
    public void deleteFile(Long fileId) {

        if (fileId == null) {
            throw new IllegalArgumentException("fileId 不能为空");
        }

        // 1️⃣ 查询文件
        InstitutionSystemFileVO file =
                institutionSystemFileMapper.selectById(fileId);

        if (file == null) {
            throw new RuntimeException("文件不存在");
        }

        // 2️⃣ 删除物理文件
        DeletePhysicalFile.deleteFile(file.getCurrentPath());

        // 3️⃣ 删除历史记录
        institutionSystemFileHistoryMapper.deleteByFileId(fileId);

        // 4️⃣ 删除主表记录
        institutionSystemFileMapper.deleteById(fileId);
    }


    /**
     * 列表 VO 转换，将数据库存的路径转换成前端可访问 URL
     */
    private List<InstitutionSystemFileVO> convertFilePaths(List<InstitutionSystemFileVO> list) {
        if (list == null || list.isEmpty()) return list;
        for (InstitutionSystemFileVO vo : list) {
            convertFilePath(vo);
        }
        return list;
    }

    /**
     * 单条 VO 转换
     */
    private InstitutionSystemFileVO convertFilePath(InstitutionSystemFileVO vo) {
        if (vo == null) return null;
        vo.setCurrentPath(toFileUrl(vo.getCurrentPath()));
        return vo;
    }

    /**
     * 将数据库存储路径转换成前端可访问 URL
     */
    private String toFileUrl(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) return null;

        // 1. 把 Windows 路径分隔符 \ 替换成 /
        String normalizedPath = dbPath.replace("\\", "/");

        // 2. 去掉本地磁盘路径前缀
        String urlPath = normalizedPath.replace("D:/yan/upload/", "");

        // 3. 拼成前端 URL
        return "http://localhost:8080/files/" + urlPath;
    }



    /**
     * 保存单个文件，返回存储路径
     */
    private String saveFile(MultipartFile file, String baseDir) {

        try {
            String originalName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis()
                    + "_" + originalName;

            File dest = new File(baseDir + fileName);
            file.transferTo(dest);

            return dest.getAbsolutePath();

        } catch (Exception e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }

}
