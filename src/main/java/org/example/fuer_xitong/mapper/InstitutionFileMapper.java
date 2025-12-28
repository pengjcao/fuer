package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.*;
import org.example.fuer_xitong.pojo.dto.InstitutionFileDTO;
import org.example.fuer_xitong.pojo.entity.InstitutionFile;
import org.example.fuer_xitong.pojo.minimal.InstitutionFileMinimalDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionFileVO;

@Mapper
public interface InstitutionFileMapper {
    /**
     * 插入 minimal 数据，获取自增 institution_file_id
     */
    void insertInstitutionFileMinimal(InstitutionFileMinimalDTO minimalDTO);

    /**
     * 回写各类制度文件路径
     */
    void updateInstitutionFilePaths(
            @Param("institutionFileId") Integer institutionFileId,

            @Param("trialManagementPath") String trialManagementPath,
            @Param("standardOperationPath") String standardOperationPath,
            @Param("emergencyPlanPath") String emergencyPlanPath,

            @Param("trainingPlanPath") String trainingPlanPath,
            @Param("qualityPlanPath") String qualityPlanPath,

            @Param("otherFilePath") String otherFilePath
    );


    /**
     * 根据 institution_id 查询（主表只会有一条）
     */
    @Select("""
        SELECT *
        FROM institution_file
        WHERE institution_id = #{institutionId}
        LIMIT 1
    """)
    InstitutionFile selectByInstitutionId(
            @Param("institutionId") String institutionId
    );


    /**
     * 查询 institution_file 表的所有记录（这里只有一条）
     */

    @Select("""
    SELECT
        trial_management_path,
        standard_operation_path,
        emergency_plan_path,
        training_plan_path,
        quality_plan_path,
        other_file_path
    FROM institution_file
    LIMIT 1
""")
    InstitutionFileVO selectFirst();


    /**
     * 更新临床试验管理制度最新路径
     *
     * @param institutionFileId 主表自增ID
     * @param filePath 新文件路径
     * @return 受影响行数
     */
    @Update("""
        UPDATE institution_file
        SET trial_management_path = #{filePath}
    """)
    int updateTrialManagementPath(
            @Param("institutionFileId") Integer institutionFileId,
            @Param("filePath") String filePath
    );

    /**
     * 更新【药物临床试验标准操作规程（SOP）】最新路径
     *
     * @param institutionFileId 主表自增ID
     * @param filePath 新文件路径
     * @return 受影响行数
     */
    @Update("""
    UPDATE institution_file
    SET standard_operation_path = #{filePath}
""")
    int updateDrugTrialSopPath(
            @Param("institutionFileId") Integer institutionFileId,
            @Param("filePath") String filePath
    );



}
