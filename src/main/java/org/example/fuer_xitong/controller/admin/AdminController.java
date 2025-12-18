package org.example.fuer_xitong.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.AdminLoginDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;
import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.entity.JwtClaimsConstant;
import org.example.fuer_xitong.pojo.vo.AdminLoginVO;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;
import org.example.fuer_xitong.pojo.vo.UserLoginVO;
import org.example.fuer_xitong.service.Adminservice;
import org.example.fuer_xitong.service.DepartmentService;
import org.example.fuer_xitong.utils.JwtProperties;
import org.example.fuer_xitong.utils.JwtUtil;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private Adminservice adminservice;

    @Autowired
    private DepartmentService departmentService;;

    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("管理员登录：{}", adminLoginDTO);

        AdminLoginDTO adminLoginDTO1=adminservice.login(adminLoginDTO);


        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,adminLoginDTO1.getID());


        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        log.info("--------------{}",jwtProperties.getAdminTokenName());
       AdminLoginVO adminLoginVO=AdminLoginVO.builder().id(adminLoginDTO1.getID())
                .password(adminLoginDTO1.getPassword())
                .token(token)
                .build();

        return Result.success(adminLoginVO);
    }

    /*首页展示部门信息*/
    @GetMapping("/department")
    public Result<List<String>> listDepartment() {
        List<Department> departments = departmentService.listAll();
        List<String> keshiNames = departments.stream()
                .map(Department::getKeshi_mingcheng)
                .toList();
        return Result.success(keshiNames);
    }




//    @GetMapping("/department")
//    public Result<List<Department>> listDepartment() {
//        List<Department> departments = departmentService.listAll();
//        List<String> keshiNames = departments.stream()
//                .map(Department::getKeshi_mingcheng)
//                .toList();
//        return Result.success(departments);
//    }


    /*点了部门信息后会弹出这个部门的人*/
    @GetMapping("/departmentuser")
    public Result<List<UserInformationVO>> listByDepartment(@RequestParam String keshi) {
        List<UserInformationVO> users = departmentService.listByDepartmentName(keshi);
        return Result.success(users);
    }

}
