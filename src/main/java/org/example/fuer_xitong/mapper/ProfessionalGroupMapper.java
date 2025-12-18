package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ProfessionalGroupMapper {
    @Insert("INSERT INTO professional_group(id,record_types, record_names, hospital_areas, report_file_path) " +
            "VALUES(#{Id},#{recordTypes}, #{recordNames}, #{hospitalAreas}, #{filePath})")
    void insertProfessionalGroup(@Param("Id") String Id,
                                @Param("recordTypes") String recordTypes,
                                 @Param("recordNames") String recordNames,
                                 @Param("hospitalAreas") String hospitalAreas,
                                 @Param("filePath") String filePath);




}
