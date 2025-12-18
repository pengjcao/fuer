package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.exception.AccountNotFoundException;
import org.example.fuer_xitong.exception.PasswordErrorException;
import org.example.fuer_xitong.mapper.AdminMapper;
import org.example.fuer_xitong.pojo.dto.AdminLoginDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;
import org.example.fuer_xitong.service.Adminservice;
import org.example.fuer_xitong.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AdminServiceImpl implements Adminservice {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public AdminLoginDTO login(AdminLoginDTO adminLoginDTO) {
        String Id=adminLoginDTO.getID();
        String password=adminLoginDTO.getPassword();
        AdminLoginDTO adminLoginDTO1=adminMapper.getAdminById(Id);

        if(adminLoginDTO1 == null)
        {
            throw new AccountNotFoundException("账号不存在");
        }

        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(DigestUtils.md5DigestAsHex(adminLoginDTO1.getPassword().getBytes()))) {
            //密码错误
            throw new PasswordErrorException("密码错误");
        }
        return adminLoginDTO1;

    }
}
