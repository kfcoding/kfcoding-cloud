package com.cuiyun.kfcoding.generator.config;


import cn.hutool.core.util.StrUtil;

/**
 * 全局配置
 *
 * @author maple
 * @date 2018-05-08 20:21
 */
public class ContextConfig {

    private String templatePrefixPath = "kfcodingTemplate/advanced";
    private String projectPath = "D:\\ideaSpace\\kfcoding";//模板输出的项目目录
    private String bizChName;   //业务名称
    private String bizEnName;   //业务英文名称
    private String bizEnBigName;//业务英文名称(大写)
    private String moduleName = "system";  //模块名称

    private String proPackage = "com.cuiyun.kfcoding.admin";
    private String coreBasePackage = "com.cuiyun.kfcoding.core";
    private String modelPackageName = "com.cuiyun.kfcoding.modular.system.model";        //model的包名
    private String modelMapperPackageName = "com.cuiyun.kfcoding.modular.system.dao";    //model的dao
    private String entityName;              //实体的名称

    private Boolean daoSwitch = true;           //dao
    private Boolean serviceSwitch = true;       //service
    private Boolean entitySwitch = true;        //生成实体的开关

    public void init() {
        if (entityName == null) {
            entityName = bizEnBigName;
        }
        modelPackageName = proPackage + "." + "modular.system.model";
        modelMapperPackageName = proPackage + "." + "modular.system.dao";
    }

    public String getBizEnBigName() {
        return bizEnBigName;
    }

    public void setBizEnBigName(String bizEnBigName) {
        this.bizEnBigName = bizEnBigName;
    }

    public String getBizChName() {
        return bizChName;
    }

    public void setBizChName(String bizChName) {
        this.bizChName = bizChName;
    }

    public String getBizEnName() {
        return bizEnName;
    }

    public void setBizEnName(String bizEnName) {
        this.bizEnName = bizEnName;
        this.bizEnBigName = StrUtil.upperFirst(this.bizEnName);
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Boolean getDaoSwitch() {
        return daoSwitch;
    }

    public void setDaoSwitch(Boolean daoSwitch) {
        this.daoSwitch = daoSwitch;
    }

    public Boolean getServiceSwitch() {
        return serviceSwitch;
    }

    public void setServiceSwitch(Boolean serviceSwitch) {
        this.serviceSwitch = serviceSwitch;
    }

    public String getTemplatePrefixPath() {
        return templatePrefixPath;
    }

    public void setTemplatePrefixPath(String templatePrefixPath) {
        this.templatePrefixPath = templatePrefixPath;
    }

    public String getModelPackageName() {
        return modelPackageName;
    }

    public void setModelPackageName(String modelPackageName) {
        this.modelPackageName = modelPackageName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getModelMapperPackageName() {
        return modelMapperPackageName;
    }

    public void setModelMapperPackageName(String modelMapperPackageName) {
        this.modelMapperPackageName = modelMapperPackageName;
    }

    public Boolean getEntitySwitch() {
        return entitySwitch;
    }

    public void setEntitySwitch(Boolean entitySwitch) {
        this.entitySwitch = entitySwitch;
    }

    public String getProPackage() {
        return proPackage;
    }

    public void setProPackage(String proPackage) {
        this.proPackage = proPackage;
    }

    public String getCoreBasePackage() {
        return coreBasePackage;
    }

    public void setCoreBasePackage(String coreBasePackage) {
        this.coreBasePackage = coreBasePackage;
    }
}
