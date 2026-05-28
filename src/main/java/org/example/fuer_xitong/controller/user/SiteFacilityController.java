package org.example.fuer_xitong.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.*;
import org.example.fuer_xitong.pojo.vo.*;
import org.example.fuer_xitong.service.SiteFacilityService;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/siteFacility")
@Slf4j
public class SiteFacilityController {

    @Autowired
    private SiteFacilityService siteFacilityService;

    /**
     * 受试者接待室填报
     */
    @PostMapping("/receptionRoom/report")
    public Result reportReceptionRoom(FacilityReceptionRoomDTO dto) {
        log.info("受试者接待室填报入参: {}", dto);
        siteFacilityService.reportReceptionRoom(dto);
        return Result.success("受试者接待室填报成功");
    }

    @GetMapping("/receptionRoom/detail")
    public  Result<List<FacilityReceptionRoomVO>> getReceptionRoomDetail(@RequestParam String keshi) {
        log.info("查询受试者接待室详情，keshi={}", keshi);
        List<FacilityReceptionRoomVO> list = siteFacilityService.getReceptionRoomDetail(keshi);
        return Result.success(list);
    }


    /**
     * 资料管理室填报
     */
    @PostMapping("/managementRoom/report")
    public Result reportFacilityManagementRoom(FacilityManagementRoomDTO dto) {
        log.info("资料管理室填报入参: {}", dto);

        siteFacilityService.reportManagementRoom(dto);

        return Result.success("资料管理室填报成功");
    }


    @GetMapping("/managementRoom/detail")
    public Result<List<FacilityManagementRoomVO>> getManagementRoomDetail(@RequestParam String keshi) {
        log.info("查询资料管理室详情，keshi={}", keshi);
        List<FacilityManagementRoomVO> list = siteFacilityService.getManagementRoomDetail(keshi);
        return Result.success(list);
    }



    /**
     * 药品保管室填报
     */
    @PostMapping("/drugStorageRoom/report")
    public Result reportDrugStorageRoom(FacilityDrugStorageRoomDTO dto) {
        log.info("药品保管室填报入参: {}", dto);
        siteFacilityService.reportDrugStorageRoom(dto);
        return Result.success("药品保管室填报成功");
    }


    /**
     * 查询药品保管室详情列表
     */
    @GetMapping("/drugStorageRoom/detail")
    public Result<List<FacilityDrugStorageRoomVO>> getDrugStorageRoomDetail(@RequestParam(required = false) String keshi) {
        log.info("查询药品保管室详情，keshi={}", keshi);
        List<FacilityDrugStorageRoomVO> list = siteFacilityService.getDrugStorageRoomDetail(keshi);
        return Result.success(list);
    }


    /**
     * 器械保管室填报
     */
    @PostMapping("/equipmentStorageRoom/report")
    public Result reportEquipmentStorageRoom(FacilityEquipmentStorageRoomDTO dto) {
        log.info("器械保管室填报入参: {}", dto);
        siteFacilityService.reportEquipmentStorageRoom(dto);
        return Result.success("器械保管室填报成功");
    }

    @GetMapping("/equipmentStorageRoom/detail")
    public Result<List<FacilityEquipmentStorageRoomVO>> getEquipmentStorageRoomDetail(@RequestParam(required = false) String keshi) {
        log.info("查询器械保管室详情，keshi={}", keshi);
        List<FacilityEquipmentStorageRoomVO> list = siteFacilityService.getEquipmentStorageRoomDetail(keshi);
        return Result.success(list);
    }

    /**
     * 样本处理及储存区填报
     */
    @PostMapping("/sampleStorageRoom/report")
    public Result reportSampleStorageRoom(FacilitySampleStorageRoomDTO dto) {
        log.info("样本处理及储存区填报入参: {}", dto);
        siteFacilityService.reportSampleStorageRoom(dto);
        return Result.success("样本处理及储存区填报成功");
    }

    /**
     * 查询样本处理及储存区详情列表
     */
    @GetMapping("/sampleStorageRoom/detail")
    public Result<List<FacilitySampleStorageRoomVO>> getSampleStorageRoomDetail(@RequestParam(required = false) String keshi) {
        log.info("查询样本处理及储存区详情，keshi={}", keshi);
        List<FacilitySampleStorageRoomVO> list = siteFacilityService.getSampleStorageRoomDetail(keshi);
        return Result.success(list);
    }

    /**
     * 抢救设施设备填报
     */
    @PostMapping("/emergencyEquipment/report")
    public Result reportEmergencyEquipment(FacilityEmergencyEquipmentDTO dto) {
        log.info("抢救设施设备填报入参: {}", dto);
        siteFacilityService.reportEmergencyEquipment(dto);
        return Result.success("抢救设施设备填报成功");
    }
    /**
     * 查询抢救设施设备详情列表
     */
    @GetMapping("/emergencyEquipment/detail")
    public Result<List<FacilityEmergencyEquipmentVO>> getEmergencyEquipmentDetail(@RequestParam(required = false) String keshi) {
        log.info("查询抢救设施设备详情，keshi={}", keshi);
        List<FacilityEmergencyEquipmentVO> list = siteFacilityService.getEmergencyEquipmentDetail(keshi);
        return Result.success(list);
    }
}