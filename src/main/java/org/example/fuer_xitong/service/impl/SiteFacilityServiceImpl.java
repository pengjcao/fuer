package org.example.fuer_xitong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.mapper.SiteFacilityMapper;
import org.example.fuer_xitong.mapper.UserMapper;
import org.example.fuer_xitong.pojo.dto.*;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.vo.*;
import org.example.fuer_xitong.service.SiteFacilityService;
import org.springframework.beans.BeanUtils;
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
@Slf4j
public class SiteFacilityServiceImpl implements SiteFacilityService {
    @Value("${file.upload-path}")
    private String uploadPath;
    @Value("${file.base-url}")
    private String baseUrl;

    @Autowired
    private SiteFacilityMapper siteFacilityMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public void reportReceptionRoom(FacilityReceptionRoomDTO dto) {
        log.info("受试者接待室填报service入参: {}", dto);

        String createBy = BaseContext.getCurrentId();

        // ================== 1. 先插入基本信息，获取自增 id ==================
        siteFacilityMapper.insertReceptionRoom(dto, null, createBy);

        Long receptionRoomId = dto.getId();
        if (receptionRoomId == null) {
            throw new RuntimeException("获取受试者接待室自增ID失败");
        }

        // ================== 2. 根据自增 id 构建文件路径 ==================
        MultipartFile photoFile = dto.getPhoto();
        String photoPath = null;

        if (photoFile != null && !photoFile.isEmpty()) {
            String baseDir = uploadPath
                    + "/siteFacility/receptionRoom/"
                    + dto.getKeshi()
                    + "/"
                    + receptionRoomId
                    + "/";

            File baseFolder = new File(baseDir);
            if (!baseFolder.exists()) {
                baseFolder.mkdirs();
            }

            photoPath = saveFile(photoFile, baseDir);

            // ================== 3. 更新数据库中的图片路径 ==================
            siteFacilityMapper.updateReceptionRoomPhoto(receptionRoomId, photoPath);
        }
    }



    @Override
    public List<FacilityReceptionRoomVO> getReceptionRoomDetail(String keshi) {
        // 1. 当前登录用户工号
        String currentUserId = BaseContext.getCurrentId();

        // 2. 当前用户所属科室
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        // 3. 如果前端没传keshi，默认查自己的科室
        if (keshi == null || keshi.trim().isEmpty()) {
            keshi = currentKeshi;
        }

        // 4. 权限判断：不是自己科室且不是科研处，则无权限
        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        // 5. 查询详情，返回List
        List<FacilityReceptionRoomVO> list = siteFacilityMapper.selectReceptionRoomByKeshi(keshi);

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
            // 或者 throw new RuntimeException("该科室暂无接待室信息");
        }

        // 6. 处理图片路径
        for (FacilityReceptionRoomVO vo : list) {
            vo.setPhoto(toFileUrl(vo.getPhoto()));
        }

        return list;
    }
    @Transactional
    @Override
    public void reportManagementRoom(FacilityManagementRoomDTO dto) {
        log.info("资料管理室填报service入参: {}", dto);

        String createBy = BaseContext.getCurrentId();

        // 1. 先插入基础数据，photo 暂时为空
        siteFacilityMapper.insertManagementRoom(dto, null, createBy);

        // 2. 获取数据库自增 id
        Long managementRoomId = dto.getId();
        if (managementRoomId == null) {
            throw new RuntimeException("获取资料管理室自增ID失败");
        }

        // 3. 根据自增 id 保存文件
        MultipartFile photoFile = dto.getPhoto();

        if (photoFile != null && !photoFile.isEmpty()) {
            String baseDir = uploadPath
                    + "/siteFacility/managementRoom/"
                    + dto.getKeshi()
                    + "/"
                    + managementRoomId
                    + "/";

            File baseFolder = new File(baseDir);
            if (!baseFolder.exists()) {
                baseFolder.mkdirs();
            }

            String photoPath = saveFile(photoFile, baseDir);

            // 4. 更新图片路径
            siteFacilityMapper.updateManagementRoomPhoto(managementRoomId, photoPath);
        }
    }


    /**
     * 查询资料管理室详情
     */
    @Override
    public List<FacilityManagementRoomVO> getManagementRoomDetail(String keshi) {
        // 1. 当前登录用户工号
        String currentUserId = BaseContext.getCurrentId();

        // 2. 当前用户所属科室
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        // 3. 如果前端没传keshi，默认查自己的科室
        if (keshi == null || keshi.trim().isEmpty()) {
            keshi = currentKeshi;
        }

        // 4. 权限判断：不是自己科室且不是科研处，则无权限
        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        // 5. 查询详情，直接返回List<VO>
        List<FacilityManagementRoomVO> list = siteFacilityMapper.selectManagementRoomByKeshi(keshi);

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
            // 或者 throw new RuntimeException("该科室暂无资料管理室信息");
        }

        // 6. 处理图片路径
        for (FacilityManagementRoomVO vo : list) {
            vo.setPhoto(toFileUrl(vo.getPhoto()));
        }

        return list;
    }



    @Transactional
    @Override
    public void reportDrugStorageRoom(FacilityDrugStorageRoomDTO dto) {
        log.info("药品保管室填报service入参: {}", dto);

        String createBy = BaseContext.getCurrentId();

        // 1. 先插入基础数据，photo 暂时为空
        siteFacilityMapper.insertDrugStorageRoom(dto, null, createBy);

        // 2. 获取数据库自增 id
        Long drugStorageRoomId = dto.getId();
        if (drugStorageRoomId == null) {
            throw new RuntimeException("获取药品保管室自增ID失败");
        }

        // 3. 根据自增 id 保存文件
        MultipartFile photoFile = dto.getPhoto();

        if (photoFile != null && !photoFile.isEmpty()) {
            String baseDir = uploadPath
                    + "/siteFacility/drugStorageRoom/"
                    + dto.getKeshi()
                    + "/"
                    + drugStorageRoomId
                    + "/";

            File baseFolder = new File(baseDir);
            if (!baseFolder.exists()) {
                baseFolder.mkdirs();
            }

            String photoPath = saveFile(photoFile, baseDir);

            // 4. 更新图片路径
            siteFacilityMapper.updateDrugStorageRoomPhoto(drugStorageRoomId, photoPath);
        }
    }

    @Override
    public List<FacilityDrugStorageRoomVO> getDrugStorageRoomDetail(String keshi) {
        // 1. 当前登录用户工号
        String currentUserId = BaseContext.getCurrentId();

        // 2. 当前用户所属科室
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        // 3. 如果前端没传keshi，默认查自己的科室
        if (keshi == null || keshi.trim().isEmpty()) {
            keshi = currentKeshi;
        }

        // 4. 权限判断：不是自己科室且不是科研处，则无权限
        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        // 5. 查询详情
        List<FacilityDrugStorageRoomVO> list = siteFacilityMapper.selectDrugStorageRoomByKeshi(keshi);

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 6. 处理图片路径
        for (FacilityDrugStorageRoomVO vo : list) {
            vo.setPhoto(toFileUrl(vo.getPhoto()));
        }

        return list;
    }




    @Transactional
    @Override
    public void reportEquipmentStorageRoom(FacilityEquipmentStorageRoomDTO dto) {
        log.info("器械保管室填报service入参: {}", dto);

        String createBy = BaseContext.getCurrentId();

        // 1. 先插入基础数据，photo 暂时为空
        siteFacilityMapper.insertEquipmentStorageRoom(dto, null, createBy);

        // 2. 获取数据库自增 id
        Long equipmentStorageRoomId = dto.getId();
        if (equipmentStorageRoomId == null) {
            throw new RuntimeException("获取器械保管室自增ID失败");
        }

        // 3. 根据自增 id 保存文件
        MultipartFile photoFile = dto.getPhoto();

        if (photoFile != null && !photoFile.isEmpty()) {
            String baseDir = uploadPath
                    + "/siteFacility/equipmentStorageRoom/"
                    + dto.getKeshi()
                    + "/"
                    + equipmentStorageRoomId
                    + "/";

            File baseFolder = new File(baseDir);
            if (!baseFolder.exists()) {
                baseFolder.mkdirs();
            }

            String photoPath = saveFile(photoFile, baseDir);

            // 4. 更新图片路径
            siteFacilityMapper.updateEquipmentStorageRoomPhoto(equipmentStorageRoomId, photoPath);
        }
    }

    @Override
    public List<FacilityEquipmentStorageRoomVO> getEquipmentStorageRoomDetail(String keshi) {
        // 1. 当前登录用户工号
        String currentUserId = BaseContext.getCurrentId();

        // 2. 当前用户所属科室
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        // 3. 如果前端没传keshi，默认查自己的科室
        if (keshi == null || keshi.trim().isEmpty()) {
            keshi = currentKeshi;
        }

        // 4. 权限判断：不是自己科室且不是科研处，则无权限
        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        // 5. 查询详情
        List<FacilityEquipmentStorageRoomVO> list = siteFacilityMapper.selectEquipmentStorageRoomByKeshi(keshi);

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 6. 处理图片路径
        for (FacilityEquipmentStorageRoomVO vo : list) {
            vo.setPhoto(toFileUrl(vo.getPhoto()));
        }

        return list;
    }

    @Transactional
    @Override
    public void reportSampleStorageRoom(FacilitySampleStorageRoomDTO dto) {
        log.info("样本处理及储存区填报service入参: {}", dto);

        String createBy = BaseContext.getCurrentId();

        // 1. 先插入基础数据，photo 暂时为空
        siteFacilityMapper.insertSampleStorageRoom(dto, null, createBy);

        // 2. 获取数据库自增 id
        Long sampleStorageRoomId = dto.getId();
        if (sampleStorageRoomId == null) {
            throw new RuntimeException("获取样本处理及储存区自增ID失败");
        }

        // 3. 根据自增 id 保存文件
        MultipartFile photoFile = dto.getPhoto();

        if (photoFile != null && !photoFile.isEmpty()) {
            String baseDir = uploadPath
                    + "/siteFacility/sampleStorageRoom/"
                    + dto.getKeshi()
                    + "/"
                    + sampleStorageRoomId
                    + "/";

            File baseFolder = new File(baseDir);
            if (!baseFolder.exists()) {
                baseFolder.mkdirs();
            }

            String photoPath = saveFile(photoFile, baseDir);

            // 4. 更新图片路径
            siteFacilityMapper.updateSampleStorageRoomPhoto(sampleStorageRoomId, photoPath);
        }
    }

    @Override
    public List<FacilitySampleStorageRoomVO> getSampleStorageRoomDetail(String keshi) {
        // 1. 当前登录用户工号
        String currentUserId = BaseContext.getCurrentId();

        // 2. 当前用户所属科室
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        // 3. 如果前端没传keshi，默认查自己的科室
        if (keshi == null || keshi.trim().isEmpty()) {
            keshi = currentKeshi;
        }

        // 4. 权限判断：不是自己科室且不是科研处，则无权限
        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        // 5. 查询详情
        List<FacilitySampleStorageRoomVO> list = siteFacilityMapper.selectSampleStorageRoomByKeshi(keshi);

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 6. 处理图片路径
        for (FacilitySampleStorageRoomVO vo : list) {
            vo.setPhoto(toFileUrl(vo.getPhoto()));
        }

        return list;
    }

    @Override
    public void reportEmergencyEquipment(FacilityEmergencyEquipmentDTO dto) {
        log.info("抢救设施设备填报service入参: {}", dto);

        String createBy = BaseContext.getCurrentId();

        siteFacilityMapper.insertEmergencyEquipment(dto, createBy);
    }

    @Override
    public List<FacilityEmergencyEquipmentVO> getEmergencyEquipmentDetail(String keshi) {

        String currentUserId = BaseContext.getCurrentId();
        String currentKeshi = userMapper.selectKeshiByJobNumber(currentUserId);

        if (keshi == null || keshi.trim().isEmpty()) {
            keshi = currentKeshi;
        }

        if (!keshi.equals(currentKeshi) && !"科研处".equals(currentKeshi)) {
            throw new RuntimeException("无权限查看该科室数据");
        }

        List<FacilityEmergencyEquipmentVO> list = siteFacilityMapper.selectEmergencyEquipmentByKeshi(keshi);

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        return list;
    }

    private String toFileUrl(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) {
            return null;
        }
        return baseUrl + "/files/" + dbPath;
    }

    private String saveFile(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) return null;

        try {

            File folder = new File(path);
            if (!folder.exists()) folder.mkdirs();

            // 文件名加时间戳，避免覆盖
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = path + fileName;

            // 保存文件到磁盘
            file.transferTo(new File(filePath));

            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }
}