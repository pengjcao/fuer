package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.fuer_xitong.pojo.vo.PiApprovalLogVO;
import org.example.fuer_xitong.pojo.vo.PiInfoVO;

import java.util.List;

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



    @Select("""
        SELECT *
        FROM pi_info
        WHERE apply_status = 'PENDING_APPROVAL'
        ORDER BY submit_time DESC
    """)
    List<PiInfoVO> selectPendingApprovalVO();


    PiInfoVO selectPiinfoById(@Param("id") String id ,@Param("piInfoId") int pi_info_id);


    int updatePiInfo(PiInfoVO pi);


    int insertApprovalLog(PiApprovalLogVO log);


}
