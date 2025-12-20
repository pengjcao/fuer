package org.example.fuer_xitong.service.impl;

import org.apache.catalina.User;
import org.example.fuer_xitong.exception.AccountNotFoundException;
import org.example.fuer_xitong.exception.PasswordErrorException;
import org.example.fuer_xitong.mapper.UserMapper;
import org.example.fuer_xitong.pojo.dto.UserInformationDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.entity.UserFile;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;
import org.example.fuer_xitong.pojo.vo.UserLoginVO;
import org.example.fuer_xitong.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UserServiceImpl implements Userservice {
    @Autowired
    private UserMapper userMapper;

    @Value("D:/yan")
    private String uploadPath;


    @Override
    public UserLoginDTO login(UserLoginDTO userLoginDTO) {
        String Id = userLoginDTO.getID();
        String password = userLoginDTO.getPassword();
        UserLoginDTO userLoginDTO1=userMapper.getUserById(Id);
        if(userLoginDTO1 == null)
        {
            throw new AccountNotFoundException("账号不存在");
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(DigestUtils.md5DigestAsHex(userLoginDTO1.getPassword().getBytes()))) {
            //密码错误
            throw new PasswordErrorException("密码错误");
        }
        return userLoginDTO1;
    }

    @Override

    public Integer getRole(String ID) {
        if (ID != null) {
            ID = ID.trim();  // 去掉前后空格和换行符
        }
        return userMapper.getRoleBy(ID);
    }


    @Override
    public void setUserInformationById(UserInformationDTO userInformationDTO)
    {
        int hangshu;
        hangshu=userMapper.setUserInformationById(userInformationDTO);
    }




//    @Override
//    public void upload(MultipartFile file, String userId)
//    {
//        if (file == null || file.isEmpty()) {
//            throw new RuntimeException("上传文件不能为空");
//        }
//
//        // 1. 原始文件名
//        String originalFilename = file.getOriginalFilename();
//
//        // 2. 文件后缀
//        String suffix = originalFilename.substring(
//                originalFilename.lastIndexOf(".") + 1);
//
//        // 3. 校验文件类型（示例）
//        if (!suffix.equalsIgnoreCase("pdf")
//                && !suffix.equalsIgnoreCase("jpg")
//                && !suffix.equalsIgnoreCase("png")) {
//            throw new RuntimeException("不支持的文件类型");
//        }
//
//        // 4. 创建用户目录
//        String userDirPath = uploadPath + "/user/" + userId;
//        File userDir = new File(userDirPath);
//        if (!userDir.exists()) {
//            userDir.mkdirs();
//        }
//
//
//        // 5. 防止重名
//        String newFileName = UUID.randomUUID() + "_" + originalFilename;
//        File destFile = new File(userDir, newFileName);
//
//        // 6. 保存文件
//        try {
//            file.transferTo(destFile);
//        } catch (IOException e) {
//            throw new RuntimeException("文件保存失败");
//        }
//
//        // 7. 保存数据库
//        UserFile userFile = new UserFile();
//        userFile.setUserId(userId);
//        userFile.setFileName(originalFilename);
//        userFile.setFileSuffix(suffix);
//        userFile.setFilePath(destFile.getAbsolutePath());
//
//    }

}
