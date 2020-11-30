package com.qinglianyun.buildsrc;

import java.util.Properties;

/**
 * SDK 配置相关信息
 * 通过字节码插桩，修改项目中ChargePileSDK配置信息
 * Created by tang_xqing on 2020/8/21.
 */
public class SDKConfig {
    /**
     * SDK版本号
     */
    private String sdkVersion;

    /**
     * 包名
     */
    private String companyPn;

    /**
     * 云充名称
     */
    private String companyCn;

    /**
     * ip地址
     */
    private String dispatchIP;

    /**
     * 端口号
     */
    private String dispatchPort;

    private String packageName;

    private String sdkClassName;

    private String packagePath;

    private String applicationName;

    public SDKConfig(Properties properties) {
        sdkVersion = properties.getProperty("SDKVersion");
        companyCn = properties.getProperty("");
        companyPn = properties.getProperty("");
        dispatchIP = properties.getProperty("");
        dispatchPort = properties.getProperty("");
        packageName = properties.getProperty("PackageName");
        sdkClassName = properties.getProperty("SDKClassName");
        packagePath = properties.getProperty("PackagePath");
        applicationName = properties.getProperty("ApplicationName");
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public String getCompanyPn() {
        return companyPn;
    }

    public String getCompanyCn() {
        return companyCn;
    }

    public String getDispatchIP() {
        return dispatchIP;
    }

    public String getDispatchPort() {
        return dispatchPort;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSdkClassName() {
        return sdkClassName;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public String getApplicationName() {
        return applicationName;
    }
}
