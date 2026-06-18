package com.example.more.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class ApiBean {
    private String baseUrl;

    private String functionName;

    private ArrayList<String> paramsMaps;

    public ApiBean(String baseUrl,String functionName, ArrayList<String> paramsMaps) {
        this.baseUrl = baseUrl;
        this.paramsMaps = paramsMaps;
        this.functionName = functionName;
    }
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public ArrayList<String> getParamsMaps() {
        return paramsMaps;
    }

    public void setParamsMaps(ArrayList<String> paramsMaps) {
        this.paramsMaps = paramsMaps;
    }
}
