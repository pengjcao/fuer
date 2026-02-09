package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.dto.InstitutionFileSystemCreateDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionFileSystemVO;

import java.util.List;

@Mapper
public interface InstitutionFileSystemMapper {

    int insertFileSystem(InstitutionFileSystemCreateDTO dto);

    List<InstitutionFileSystemVO> selectList();


    /**
     * 根据 systemId 删除文件体系
     */
    void deleteById(@Param("systemId") Long systemId);


    List<InstitutionFileSystemVO> selectByKeshi(@Param("keshi") String keshi);

    List<InstitutionFileSystemVO> selectByKeshiAndGroupPath(@Param("keshi") String keshi , @Param("GroupPath") String GroupPath);
}
