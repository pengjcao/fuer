package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.*;
import org.example.fuer_xitong.pojo.minimal.PiInfoMinimalDTO;
import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;
import org.example.fuer_xitong.pojo.vo.PiInfoHistoryVO;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ProfessionalGroupMapper {
    @Insert("INSERT INTO professional_group(id,record_types, record_names, hospital_areas, report_file_path) " +
            "VALUES(#{Id},#{recordTypes}, #{recordNames}, #{hospitalAreas}, #{filePath})")
    void insertProfessionalGroup(@Param("Id") String Id,
                                @Param("recordTypes") String recordTypes,
                                 @Param("recordNames") String recordNames,
                                 @Param("hospitalAreas") String hospitalAreas,
                                 @Param("filePath") String filePath);


    void insertPiInfo(
            @Param("Id") String Id,
            @Param("professional") String professional,
            @Param("piPhotoPath") String piPhotoPath,
            @Param("seniorTitleCertificatePath") String seniorTitleCertificatePath,
            @Param("seniorTitleAppointmentPath") String seniorTitleAppointmentPath,
            @Param("signedResumePath") String signedResumePath,
            @Param("qualificationCertificatePath") String qualificationCertificatePath,
            @Param("practiceCertificatePath") String practiceCertificatePath,
            @Param("gcpCertificatePath") String gcpCertificatePath,
            @Param("shanchang") String shanchang,
            @Param("clinicalParticipation") Integer clinicalParticipation,
            @Param("clinicalReason") String clinicalReason,
            @Param("clinicalRootPath") String clinicalRootPath
    );

//下面这三个是为了插入的时候好区分文件
    void insertPiInfoMinimal(PiInfoMinimalDTO dto);


    Integer selectLastInsertId(@Param("id") String id);

    void updatePiInfoFiles(
            @Param("piInfoId") Integer piInfoId,
            @Param("piPhotoPath") String piPhotoPath,
            @Param("seniorTitleCertificatePath") String seniorTitleCertificatePath,
            @Param("seniorTitleAppointmentPath") String seniorTitleAppointmentPath,
            @Param("signedResumePath") String signedResumePath,
            @Param("qualificationCertificatePath") String qualificationCertificatePath,
            @Param("practiceCertificatePath") String practiceCertificatePath,
            @Param("gcpCertificatePath") String gcpCertificatePath,
            @Param("clinicalParticipation") Integer clinicalParticipation,
            @Param("clinicalReason") String clinicalReason,
            @Param("clinicalRootPath") String clinicalRootPath,
            @Param("selfAssessmentReportPath") String selfAssessmentReportPath, // 新增
            @Param("recordTypes") String recordTypes,                             // 新增
            @Param("hospitalAreas") String hospitalAreas                          // 新增
    );




    @Select("""
        SELECT *
        FROM pi_info
        WHERE apply_status = 'PENDING_APPROVAL'
        ORDER BY submit_time DESC
    """)
    List<PiInfoVO> selectPendingApprovalVO();


    @Select("""
        SELECT *
        FROM pi_info
        WHERE current_step = 4 AND drug_admin_record_time IS NOT NULL
        ORDER BY submit_time DESC
    """)
    List<PiInfoVO> selectApprovedPiVO();

//    @Select("""
//        SELECT *
//        FROM pi_info
//        WHERE apply_status = "APPROVE"
//        ORDER BY submit_time DESC
//    """)
//    List<PiInfoVO> selectApprovedPiVO();


    PiInfoVO selectPiinfoById(@Param("piInfoId") int pi_info_id);


    int updatePiInfo(PiInfoVO pi);


    int insertApprovalLog(PiApprovalLogVO log);


    List<PiInfoVO> getByIdAndProfessionalList(
            @Param("id") String id,
            @Param("professional") String professional
    );



    /**
     * 填写 / 更新 药监局备案时间
     *
     * @param piInfoId PI 信息表主键
     * @param recordTime 药监局备案时间（前端传）
     * @return 受影响行数
     */
    @Update("""
        UPDATE pi_info
        SET drug_admin_record_time = #{recordTime}
        WHERE pi_info_id = #{piInfoId}
    """)
    int updateDrugAdminRecordTime(
            @Param("piInfoId") Integer piInfoId,
            @Param("recordTime") LocalDateTime recordTime
    );

    /**
     * 根据 piInfoId 查询当前审批步骤
     *
     * @param piInfoId PI 信息表主键
     * @return 当前步骤
     */
    @Select("""
        SELECT current_step
        FROM pi_info
        WHERE pi_info_id = #{piInfoId}
    """)
    Integer selectCurrentStep(@Param("piInfoId") Integer piInfoId);



    PiInfoHistoryVO selectHistoryById(@Param("piInfoId") int piInfoId);

}
