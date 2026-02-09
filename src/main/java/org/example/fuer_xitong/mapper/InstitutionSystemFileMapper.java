package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileVO;

import java.util.List;

@Mapper
public interface InstitutionSystemFileMapper {

    void insert(@Param("systemId") Long systemId,
                @Param("fileName") String fileName,
                @Param("filePath") String filePath,
                @Param("operatorId") String operatorId,
                @Param("keshi") String keshi,
                @Param("groupPath") String groupPath);


    /**
     * 根据文件体系ID查询文件列表
     */

    // 原有：只按 systemId 查
    List<InstitutionSystemFileVO> selectBySystemId(
            @Param("systemId") Long systemId
    );
    List<InstitutionSystemFileVO> selectByCondition(@Param("systemId") Long systemId,    @Param("keshi") String keshi,
                                                   @Param("groupPath") String groupPath);

    InstitutionSystemFileVO selectById(@Param("id") Long id);

    void updateCurrentPath(@Param("id") Long id,
                           @Param("currentPath") String currentPath);


    /**
     * 更新当前文件路径和文件名（覆盖上传时用）
     */
    void updateFilePathAndName(
            @Param("id") Long id,
            @Param("currentPath") String currentPath,
            @Param("fileName") String fileName
    );

    void deleteBySystemId(@Param("systemId") Long systemId);


    void deleteById(@Param("id") Long id);



    /**
     * 将文件标记为失效
     */
    void markAsInactive(@Param("fileId") Long fileId);
}
