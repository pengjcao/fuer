package org.example.fuer_xitong.pojo.entity;

import lombok.Data;

@Data
public class Department {

    /**
     * 科室编码
     */
    private String keshi;

    /**
     * 科室中文名称（如 传染科、心内科）
     */
    private String keshi_mingcheng;
}
