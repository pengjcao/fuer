package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
        SELECT
            ID,
            professional,
            pi_photo_path            AS piPhotoPath,
            senior_title_certificate_path AS seniorTitleCertificatePath,
            senior_title_appointment_path AS seniorTitleAppointmentPath,
            signed_resume_path       AS signedResumePath,
            qualification_certificate_path AS qualificationCertificatePath,
            practice_certificate_path AS practiceCertificatePath,
            gcp_certificate_path     AS gcpCertificatePath,
            shanchang,
            clinical_participation   AS clinicalParticipation,
            clinical_reason          AS clinicalReason,
            apply_status             AS applyStatus,
            current_step             AS currentStep,
            submit_time              AS submitTime,
            pi_info_id              AS piInfoId
        FROM pi_info
        WHERE apply_status = 'PENDING_APPROVAL'
        ORDER BY submit_time DESC
    """)
    List<PiInfoVO> selectPendingApprovalVO();


    PiInfoVO selectPiinfoById(@Param("id") String id ,@Param("piInfoId") int pi_info_id);



}
