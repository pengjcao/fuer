package org.example.fuer_xitong.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangeRoute {

    @Autowired
    private FilePathUtil filePathUtil;

    public String toFileUrl(String physicalPath) {
        return filePathUtil.toFileUrl(physicalPath);
    }

}
