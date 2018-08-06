package com.cuiyun.kfcoding.generator;


import com.cuiyun.kfcoding.generator.config.KfCodingGeneratorConfig;

/**
 * 代码生成器,可以生成实体,dao,service
 *
 * @author maple
 * @Date 2018/8/4 12:38
 */
public class KfCodingCodeGenerator {

    public static void main(String[] args) {

        /**
         *  生成mapper service model
         */
        KfCodingGeneratorConfig kfCodingGeneratorConfig = new KfCodingGeneratorConfig();
        kfCodingGeneratorConfig.doMpGeneration();
    }


}