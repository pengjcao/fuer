package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.AdminLoginDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;

public interface Adminservice {
    AdminLoginDTO login(AdminLoginDTO adminLoginDTO);
}
