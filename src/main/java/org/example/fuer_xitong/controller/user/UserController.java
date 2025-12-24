package org.example.fuer_xitong.controller.user;


import lombok.extern.slf4j.Slf4j;
import org.example.fuer_xitong.pojo.dto.UserInformationDTO;
import org.example.fuer_xitong.pojo.dto.UserLoginDTO;

import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.entity.JwtClaimsConstant;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;
import org.example.fuer_xitong.pojo.vo.UserLoginVO;
import org.example.fuer_xitong.service.DepartmentService;
import org.example.fuer_xitong.service.Userservice;
import org.example.fuer_xitong.utils.JwtProperties;
import org.example.fuer_xitong.utils.JwtUtil;
import org.example.fuer_xitong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private Userservice userservice;

    @Autowired
    private DepartmentService departmentService;


    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("员工登录：{}", userLoginDTO);
        UserLoginDTO user=userservice.login(userLoginDTO);
        Integer juese = userservice.getRole(userLoginDTO.getID());
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getID());
        claims.put("role", juese);


        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        log.info("--------------{}",jwtProperties.getUserTokenName());
        UserLoginVO userLoginVO=UserLoginVO.builder().id(user.getID())
                .password(user.getPassword())
                .role(juese)
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @GetMapping("/role")
    public Result<Integer> getRoleByID(@RequestParam String ID) {
        log.info("查询用户 {} 的角色", ID);
        Integer role = userservice.getRole(ID);


        if (role == null) {
            return Result.error("用户不存在");
        }
        return Result.success(role);
    }

    /*用户要注册自己基础信息的话就用这个接口，这个接口是往数据库进行插入*/
    @PostMapping("/information")
    public Result submitInformation(@RequestBody UserInformationDTO userInformationDTO) {
        log.info("我来了哦");
        userservice.setUserInformationById(userInformationDTO);
        return Result.success();
    }


    /*这个接口是用户端一登陆就能够看到部门的信息和admin那种一样，是查询所有部门*/
    @GetMapping("/department")
    public Result<List<String>> listDepartment() {
        List<Department> departments = departmentService.listAll();

        List<String> keshiNames = departments.stream()
                .map(Department::getKeshiMingcheng)
                .toList();
        return Result.success(keshiNames);
    }

    /*点了部门信息后会弹出这个部门的人*/
    @GetMapping("/departmentuser")
    public Result<List<UserInformationVO>> listByDepartment(@RequestParam String keshi) {
        List<UserInformationVO> users = departmentService.listByDepartmentName(keshi);
        return Result.success(users);
    }

//    /*这个接口不用管，只是自己测试文件如何存储*/
//    @PostMapping("/upload")
//    public Result upload(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("userId") String userId) {
//
//        userservice.upload(file, userId);
//        return Result.success("上传成功");
//    }


}
