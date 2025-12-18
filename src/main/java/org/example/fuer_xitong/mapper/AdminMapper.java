package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.fuer_xitong.pojo.dto.AdminLoginDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;

@Mapper
public interface AdminMapper {
    /*login*/
    @Select("select * from admin where  ID= #{id}#")
    AdminLoginDTO getAdminById(String id);


}
