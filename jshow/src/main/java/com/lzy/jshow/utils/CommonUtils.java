package com.lzy.jshow.utils;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class CommonUtils {
    public static  <T> T getFirst(List<T> list){
        if (list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }
}
