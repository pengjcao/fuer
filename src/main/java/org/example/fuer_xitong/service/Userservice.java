package org.example.fuer_xitong.service;


import org.example.fuer_xitong.pojo.dto.UserInformationDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;
import org.springframework.web.multipart.MultipartFile;

public interface Userservice {

    UserLoginDTO login(UserLoginDTO userLoginDTO);

    void setUserInformationById(UserInformationDTO userInformationDTO);

     Integer getRole(String ID);
//    void upload(MultipartFile file ,String userId);

}
