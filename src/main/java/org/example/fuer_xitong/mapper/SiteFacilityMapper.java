package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.dto.*;
import org.example.fuer_xitong.pojo.vo.*;

import java.util.List;

@Mapper
public interface SiteFacilityMapper {

    /**
     * 新增受试者接待室
     */
    void insertReceptionRoom(@Param("dto") FacilityReceptionRoomDTO dto,
                             @Param("photoPath") String photoPath,
                             @Param("createBy") String createBy);


    List<FacilityReceptionRoomVO> selectReceptionRoomByKeshi(@Param("keshi") String keshi);
    void updateReceptionRoomPhoto(@Param("id") Long id,
                                  @Param("photoPath") String photoPath);



    void insertManagementRoom(@Param("dto") FacilityManagementRoomDTO dto,
                              @Param("photoPath") String photoPath,
                              @Param("createBy") String createBy);

    /**
     * 根据科室查询资料管理室信息
     */
    List<FacilityManagementRoomVO> selectManagementRoomByKeshi(@Param("keshi") String keshi);
    void updateManagementRoomPhoto(@Param("id") Long id,
                                   @Param("photoPath") String photoPath);



    /**
     * 药品保管室填报插入
     */
    void insertDrugStorageRoom(@Param("dto") FacilityDrugStorageRoomDTO dto,
                               @Param("photoPath") String photoPath,
                               @Param("createBy") String createBy);
    List<FacilityDrugStorageRoomVO> selectDrugStorageRoomByKeshi(@Param("keshi") String keshi);
    void updateDrugStorageRoomPhoto(@Param("id") Long id,
                                    @Param("photoPath") String photoPath);
    /**
     * 器械保管室填报插入
     */
    void insertEquipmentStorageRoom(@Param("dto") FacilityEquipmentStorageRoomDTO dto,
                                    @Param("photoPath") String photoPath,
                                    @Param("createBy") String createBy);
    List<FacilityEquipmentStorageRoomVO> selectEquipmentStorageRoomByKeshi(@Param("keshi") String keshi);
    void updateEquipmentStorageRoomPhoto(@Param("id") Long id,
                                         @Param("photoPath") String photoPath);
    /**
     * 样本处理及储存区插入
     */
    void insertSampleStorageRoom(@Param("dto") FacilitySampleStorageRoomDTO dto,
                                 @Param("photoPath") String photoPath,
                                 @Param("createBy") String createBy);
    List<FacilitySampleStorageRoomVO> selectSampleStorageRoomByKeshi(@Param("keshi") String keshi);
    void updateSampleStorageRoomPhoto(@Param("id") Long id,
                                      @Param("photoPath") String photoPath);
    /**
     * 插入抢救设施设备
     */
    int insertEmergencyEquipment(@Param("dto") FacilityEmergencyEquipmentDTO dto,
                                 @Param("createBy") String createBy);

    List<FacilityEmergencyEquipmentVO> selectEmergencyEquipmentByKeshi(@Param("keshi") String keshi);
}
