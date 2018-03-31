package com.example.liuxuanchi.project;

/**
 * Created by liuxuanchi on 2017/12/23.
 */

public enum Department{

    LOGISTICS, PRODUCTION, MARKET, FINANCE, UNKNOWN;

    public static int departmentToInt(Department dep){
        switch(dep){
            case PRODUCTION:
                return 0;
            case MARKET:
                return 1;
            case LOGISTICS:
                return 2;
            case FINANCE:
                return 3;
            default:
                return -1;
        }
    }

    public static Department intToDepartment(int num){
        switch (num){
            case 0:
                return PRODUCTION;
            case 1:
                return MARKET;
            case 2:
                return LOGISTICS;
            case 3:
                return FINANCE;
            default:
                return UNKNOWN;
        }
    }

    public static String intToHanzi(int num) {
        switch (num){
            case 0:
                return "生产部";
            case 1:
                return "市场部";
            case 2:
                return "后勤部";
            case 3:
                return "财务部";
            default:
                return "未知";
        }
    }
}


