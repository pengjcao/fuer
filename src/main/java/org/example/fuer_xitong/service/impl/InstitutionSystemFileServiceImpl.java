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
import org.example.fuer_xitong.utils.FilePathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstitutionSystemFileServiceImpl implements InstitutionSystemFileService {
    @Value("${file.upload-path}")
    private String uploadPath;
    @Value("${file.base-url}")
    private String baseUrl;

    @Autowired
    private InstitutionSystemFileMapper institutionSystemFileMapper;

    @Autowired
    private org.example.fuer_xitong.mapper.InstitutionFileSystemMapper institutionFileSystemMapper;

    @Autowired
    private InstitutionSystemFileHistoryMapper institutionSystemFileHistoryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FilePathUtil filePathUtil;

    @Override
    @Transactional
    public void uploadFiles(Long systemId, MultipartFile[] files,String keshi,String Grouppath) {

        // 1️⃣ 当前操作人
        String operatorId = BaseContext.getCurrentId();

        if (files == null || files.length == 0) {
            return;
        }

        org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO system = institutionFileSystemMapper.selectById(systemId);
        if (system == null) {
            throw new RuntimeException("文件体系不存在");
        }
        assertCanUploadToSystem(system, keshi, Grouppath, operatorId);

        // 2️⃣ 计算最终使用的科室
        String finalKeshi = resolveActionKeshi(keshi, system.getKeshi(), operatorId);


        // 3️⃣ 计算最终使用的专业组（可选）
        String finalGroupPath = firstText(Grouppath, system.getGroupPath());
        // 2️⃣ 体系级目录（不会再用自增ID作为目录）
        String baseDir = filePathUtil.buildUploadDir(
                finalKeshi,
                String.valueOf(systemId),
                finalGroupPath
        );

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
        org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO system = institutionFileSystemMapper.selectById(systemId);
        if (system == null) {
            throw new RuntimeException("文件体系不存在");
        }
        String operatorId = BaseContext.getCurrentId();
        if (!canUseSystemInProfessionalGroup(system, keshi, Grouppath, operatorId)) {
            assertCanReadSystem(system);
        }

        boolean institutionReadonlySystem = isInstitutionReadonlySystem(system);
        if (!hasText(keshi) && institutionReadonlySystem) {
            // 机构文件体系入口不按研究者科室过滤，否则看不到机构级文件。
            List<InstitutionSystemFileVO> list = institutionSystemFileMapper.selectBySystemId(systemId);
            return convertFilePaths(list);
        }

        keshi = resolveActionKeshi(keshi, system.getKeshi(), operatorId);
        Grouppath = firstText(Grouppath, system.getGroupPath());

        if(keshi!=null && !keshi.trim().isEmpty()) {
            // 查询数据库
            List<InstitutionSystemFileVO> aaa = institutionSystemFileMapper.selectByCondition(systemId,keshi,Grouppath);
            return convertFilePaths(aaa);
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
        assertCanManageFile(current);

        // 2️⃣ 写入历史表（保留旧文件名和路径）
        InstitutionSystemFileHistoryDTO historyDTO = new InstitutionSystemFileHistoryDTO();
        historyDTO.setFileId(current.getId());
        historyDTO.setSystemId(current.getSystemId());
        historyDTO.setFileName(current.getFileName()); // ⚠️ 旧文件名
        historyDTO.setFilePath(current.getCurrentPath()); // ⚠️ 旧路径
        historyDTO.setOperatedBy(operatorId);
        historyDTO.setRemark(remark);
        institutionSystemFileHistoryMapper.insertHistory(historyDTO);


        String keshi = hasText(current.getKeshi())
                ? current.getKeshi()
                : userMapper.selectKeshiByJobNumber(operatorId);
        String groupPath = trimToNull(current.getGroupPath());
        // 3️⃣ 保存新文件（复用你的 saveFile）
        String baseDir = filePathUtil.buildUploadDir(keshi, String.valueOf(current.getSystemId()), groupPath);

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
        assertCanManageFile(file);

        // 标记为失效
        institutionSystemFileMapper.markAsInactive(fileId);
    }



    @Override
    public List<InstitutionSystemFileHistoryVO> queryFileHistory(Long fileId) {

        if (fileId == null) {
            throw new IllegalArgumentException("fileId 不能为空");
        }

        InstitutionSystemFileVO current = institutionSystemFileMapper.selectById(fileId);
        if (current == null) {
            throw new RuntimeException("文件不存在");
        }
        assertCanReadFile(current);

        // 查询历史记录列表
        List<InstitutionSystemFileHistoryVO> list =
                institutionSystemFileHistoryMapper.selectByFileId(fileId);

        // 转换路径成前端可访问 URL
        List<InstitutionSystemFileHistoryVO> result = new ArrayList<>();
        result.add(buildCurrentHistoryVO(current));
        result.addAll(list.stream()
                .map(this::convertFilePath)
                .collect(Collectors.toList()));
        return result;
    }

    private InstitutionSystemFileHistoryVO buildCurrentHistoryVO(InstitutionSystemFileVO current) {
        InstitutionSystemFileHistoryVO vo = new InstitutionSystemFileHistoryVO();
        vo.setId(0L);
        vo.setFileName(current.getFileName());
        vo.setCurrentPath(toFileUrl(current.getCurrentPath()));
        vo.setOperatedBy(current.getCreatedBy());
        vo.setRemark("当前版本");
        vo.setVersionType("当前版本");
        vo.setCreatedTime(current.getUpdatedTime() != null ? current.getUpdatedTime() : current.getCreatedTime());
        return vo;
    }

    private InstitutionSystemFileHistoryVO convertFilePath(InstitutionSystemFileHistoryVO vo) {
        if (vo == null) return null;
        vo.setCurrentPath(toFileUrl(vo.getCurrentPath()));
        vo.setVersionType("历史版本");
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
        assertCanManageFile(file);

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
//    private String toFileUrl(String dbPath) {
//        if (dbPath == null || dbPath.isEmpty()) return null;
//
//        // 1. 把 Windows 路径分隔符 \ 替换成 /
//        String normalizedPath = dbPath.replace("\\", "/");
//
//        // 2. 去掉本地磁盘路径前缀
//        String urlPath = normalizedPath.replace("upload/", "");
//
//        // 3. 拼成前端 URL
//        return "http://localhost:8080/files/" + urlPath;
//    }
//    private String toFileUrl(String dbPath) {
//        if (dbPath == null || dbPath.isEmpty()) {
//            return null;
//        }
//        return baseUrl + "/files/" + dbPath;
//    }

    private String toFileUrl(String physicalPath) {
        return filePathUtil.toFileUrl(physicalPath);
    }

    private void assertCanUploadToSystem(org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO system,
                                         String requestKeshi,
                                         String requestGroupPath,
                                         String operatorId) {
        Integer role = BaseContext.getCurrentRole();
        if (role != null && role > 1) {
            return;
        }

        if (canUseSystemInProfessionalGroup(system, requestKeshi, requestGroupPath, operatorId)) {
            return;
        }

        if (system.getCreatedBy() == null || !system.getCreatedBy().equals(operatorId)) {
            throw new RuntimeException("只能上传到自己创建的文件体系");
        }
    }

    private void assertCanReadSystem(org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO system) {
        Integer role = BaseContext.getCurrentRole();
        if (role != null && role > 1) {
            return;
        }

        if (isInstitutionReadonlySystem(system)) {
            return;
        }

        String operatorId = BaseContext.getCurrentId();
        if (system.getCreatedBy() == null || !system.getCreatedBy().equals(operatorId)) {
            throw new RuntimeException("无权限查看该文件体系");
        }
    }

    private void assertCanManageFile(InstitutionSystemFileVO file) {
        Integer role = BaseContext.getCurrentRole();
        if (role != null && role > 1) {
            return;
        }

        String operatorId = BaseContext.getCurrentId();
        if (isResearcherProfessionalGroupFile(file, operatorId)) {
            return;
        }

        if (file.getCreatedBy() == null || !file.getCreatedBy().equals(operatorId)) {
            throw new RuntimeException("只能管理自己上传的文件");
        }
    }

    private void assertCanReadFile(InstitutionSystemFileVO file) {
        Integer role = BaseContext.getCurrentRole();
        if (role != null && role > 1) {
            return;
        }

        org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO system =
                institutionFileSystemMapper.selectById(file.getSystemId());
        if (system != null && isInstitutionReadonlySystem(system)) {
            return;
        }

        String operatorId = BaseContext.getCurrentId();
        if (isResearcherProfessionalGroupFile(file, operatorId)) {
            return;
        }

        if (file.getCreatedBy() == null || !file.getCreatedBy().equals(operatorId)) {
            throw new RuntimeException("无权限查看该文件历史");
        }
    }

    private boolean canUseSystemInProfessionalGroup(org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO system,
                                                    String requestKeshi,
                                                    String requestGroupPath,
                                                    String operatorId) {
        Integer role = BaseContext.getCurrentRole();
        if (role != null && role > 1) {
            return true;
        }
        if (system == null || !hasText(requestKeshi) || !hasText(requestGroupPath)) {
            return false;
        }

        String currentKeshi = userMapper.selectKeshiByJobNumber(operatorId);
        if (!hasText(currentKeshi) || !requestKeshi.trim().equals(currentKeshi.trim())) {
            return false;
        }

        String systemKeshi = trimToNull(system.getKeshi());
        String systemGroupPath = trimToNull(system.getGroupPath());
        if (systemKeshi == null) {
            return true;
        }
        if (!systemKeshi.equals(currentKeshi.trim())) {
            return false;
        }
        return systemGroupPath == null || systemGroupPath.equals(requestGroupPath.trim());
    }

    private boolean isResearcherProfessionalGroupFile(InstitutionSystemFileVO file, String operatorId) {
        if (file == null || !hasText(file.getKeshi()) || !hasText(file.getGroupPath())) {
            return false;
        }

        String currentKeshi = userMapper.selectKeshiByJobNumber(operatorId);
        return hasText(currentKeshi) && file.getKeshi().trim().equals(currentKeshi.trim());
    }

    private boolean isInstitutionReadonlySystem(org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO system) {
        if (system == null) {
            return false;
        }
        if (Boolean.TRUE.equals(system.getIsFixed()) || !hasText(system.getKeshi())) {
            return true;
        }
        if (hasText(system.getGroupPath())) {
            return false;
        }
        Integer creatorRole = system.getCreatedBy() == null ? null : userMapper.getRoleBy(system.getCreatedBy());
        return creatorRole != null && creatorRole > 1;
    }

    private String resolveActionKeshi(String requestKeshi, String systemKeshi, String operatorId) {
        Integer role = BaseContext.getCurrentRole();
        String currentKeshi = userMapper.selectKeshiByJobNumber(operatorId);

        if (role != null && role > 1) {
            String resolved = firstText(requestKeshi, systemKeshi);
            return hasText(resolved) ? resolved : currentKeshi;
        }

        if (hasText(requestKeshi) && !requestKeshi.trim().equals(currentKeshi)) {
            throw new RuntimeException("无权限操作其他科室文件");
        }

        return currentKeshi;
    }

    private String firstText(String first, String second) {
        if (hasText(first)) {
            return first.trim();
        }
        if (hasText(second)) {
            return second.trim();
        }
        return null;
    }

    private String trimToNull(String value) {
        return hasText(value) ? value.trim() : null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }




    /**
     * 保存单个文件，返回存储路径
     */
    private String saveFile(MultipartFile file, String baseDir) {
        return filePathUtil.saveFile(file, baseDir);
    }

}
