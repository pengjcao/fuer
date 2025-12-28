package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.minimal.DrugTrialSopFileMinimalDTO;

@Mapper
public interface DrugTrialSopFileMapper {

    /**
     * 插入 minimal 数据，获取自增 ID
     */
    int insertMinimal(DrugTrialSopFileMinimalDTO dto);

    /**
     * 作废当前生效记录
     */
    int invalidateCurrent(@Param("institutionFileId") Integer institutionFileId);

    /**
     * 根据主键更新当前记录（写入 file_path）
     */
    int updateCurrentById(
            @Param("id") Integer id,
            @Param("filePath") String filePath
    );
}