package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.*;
import org.example.fuer_xitong.pojo.vo.*;

import java.util.List;

public interface SiteFacilityService {

    /**
     * 受试者接待室填报
     */
    void reportReceptionRoom(FacilityReceptionRoomDTO dto);
    List<FacilityReceptionRoomVO> getReceptionRoomDetail(String keshi);

    /**
     * 资料管理室填报
     */
    void reportManagementRoom(FacilityManagementRoomDTO dto);
    List<FacilityManagementRoomVO> getManagementRoomDetail(String keshi);

    /**
     * 药品保管室填报
     */
    void reportDrugStorageRoom(FacilityDrugStorageRoomDTO dto);
    List<FacilityDrugStorageRoomVO> getDrugStorageRoomDetail(String keshi);

    /**
     * 器械保管室填报
     */
    void reportEquipmentStorageRoom(FacilityEquipmentStorageRoomDTO dto);
    List<FacilityEquipmentStorageRoomVO> getEquipmentStorageRoomDetail(String keshi);

    /**
     * 样本处理及储存区填报
     */
    void reportSampleStorageRoom(FacilitySampleStorageRoomDTO dto);
    List<FacilitySampleStorageRoomVO> getSampleStorageRoomDetail(String keshi);


    /**
     * 急救
     */
    void reportEmergencyEquipment(FacilityEmergencyEquipmentDTO dto);
    List<FacilityEmergencyEquipmentVO> getEmergencyEquipmentDetail(String keshi);
}