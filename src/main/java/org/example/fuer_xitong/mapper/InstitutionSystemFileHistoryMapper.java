package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.dto.InstitutionSystemFileHistoryDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionSystemFileHistoryVO;

import java.util.List;

@Mapper
public interface InstitutionSystemFileHistoryMapper {

    void insertHistory(InstitutionSystemFileHistoryDTO dto);

    /**
     * 查询某个文件的历史记录列表
     */
    List<InstitutionSystemFileHistoryVO> selectByFileId(@Param("fileId") Long fileId);


    void deleteBySystemId(@Param("systemId") Long systemId);

    void deleteByFileId(@Param("fileId") Long fileId);

}
