package com.bookcode.worksprotectingsystem.utils;

import java.util.UUID;

public class Util {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}

