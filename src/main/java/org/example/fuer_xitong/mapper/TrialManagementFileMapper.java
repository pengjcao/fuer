package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.*;
import org.example.fuer_xitong.pojo.minimal.TrialManagementFileMinimalDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionTrialManagementFileVO;

import java.util.List;
import java.util.Map;

@Mapper
public interface TrialManagementFileMapper {

    /**
     * 1️⃣ 将当前 institution_file_id 下的“最新记录”作废
     * （把 1 → 0）
     */
    @Update("""
        UPDATE institution_trial_management_file
        SET is_invalid = 0
        WHERE institution_file_id = #{institutionFileId}
          AND is_invalid = 1
    """)
    int invalidateCurrent(
            @Param("institutionFileId") Integer institutionFileId
    );



    @Update("""
    UPDATE institution_trial_management_file
    SET 
        file_path = #{filePath},
        is_invalid = 1
    WHERE institution_trial_management_file_id = #{trialFileId}
""")
    int updateCurrentById(
            @Param("trialFileId") Integer trialFileId,
            @Param("filePath") String filePath
    );


    /**
     * 插入 minimal 数据，同时回填自增主键 institution_trial_management_file_id
     */
    @Insert("INSERT INTO institution_trial_management_file " +
            "(institution_id, institution_file_id, created_by) " +
            "VALUES (#{institutionId}, #{institutionFileId}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "institutionTrialManagementFileId")
    void insertMinimal(TrialManagementFileMinimalDTO minimalDTO);


    @Select("""
        SELECT institution_trial_management_file_id,
               institution_id,
               institution_file_id,
               file_path,
               is_invalid,
               created_by,
               created_time
        FROM institution_trial_management_file
        WHERE institution_file_id = #{institutionFileId}
        ORDER BY created_time DESC
    """)
    List<InstitutionTrialManagementFileVO> selectAllByInstitutionFileId(
            @Param("institutionFileId") Integer institutionFileId
    );
}