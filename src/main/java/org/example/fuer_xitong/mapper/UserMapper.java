package org.example.fuer_xitong.mapper;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.fuer_xitong.pojo.dto.UserInformationDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;

@Mapper
public interface UserMapper {
    /*login*/
    @Select("select * from user where  ID= #{id}#")
    UserLoginDTO getUserById(String id);

    @Insert("INSERT INTO user_information (ID, username, keshi, shanchang) " +
            "VALUES (#{ID}, #{username}, #{keshi}, #{shanchang}) " +
            "ON DUPLICATE KEY UPDATE " +
            "username = VALUES(username), " +
            "keshi = VALUES(keshi), " +
            "shanchang = VALUES(shanchang)")
    int setUserInformationById(UserInformationDTO userInformationDTO);

}
