package com.qinglianyun.buildsrc;

/**
 * Created by tang_xqing on 2020/8/21.
 */
public class Ad2 {
    private String adName;
    private String adVersion;

    private String propertiesPath;

    private String srcClassDir;

    private String annotationClass;

    public Ad2() {
    }

    public Ad2(String adName, String adVersion) {
        this.adName = adName;
        this.adVersion = adVersion;
    }

    public Ad2(String adName, String adVersion, String srcClassDir, String annotationClass) {
        this.adName = adName;
        this.adVersion = adVersion;
        this.srcClassDir = srcClassDir;
        this.annotationClass = annotationClass;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdVersion() {
        return adVersion;
    }

    public void setAdVersion(String adVersion) {
        this.adVersion = adVersion;
    }

    public String getSrcClassDir() {
        return srcClassDir;
    }

    public void setSrcClassDir(String srcClassDir) {
        this.srcClassDir = srcClassDir;
    }

    public String getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(String annotationClass) {
        this.annotationClass = annotationClass;
    }

    public String getPropertiesPath() {
        return propertiesPath;
    }

    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    @Override
    public String toString() {
        return "Ad2{" +
                "adName='" + adName + '\'' +
                ", adVersion='" + adVersion + '\'' +
                ", propertiesPath='" + propertiesPath + '\'' +
                ", srcClassDir='" + srcClassDir + '\'' +
                ", annotationClass='" + annotationClass + '\'' +
                '}';
    }
}
