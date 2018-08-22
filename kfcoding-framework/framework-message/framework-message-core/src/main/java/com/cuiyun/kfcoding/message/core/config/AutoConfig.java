package com.cuiyun.kfcoding.message.core.config;

import lombok.Data;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 11:32
 **/
@Data
public class AutoConfig {

    /**
     * 资源后缀  此参数请填写  关于是事务存储路径
     * 1 如果是表存储 这个就是表名后缀，其他方式存储一样
     * 2 如果此参数不填写，那么会默认获取应用的applicationName.
     */
    private String repositorySuffix;

    /**
     * 提供不同的序列化对象. {@linkplain com.cuiyun.kfcoding.message.core.enums.SerializeEnum}
     */
    private String serializer = "kryo";

    /**
     * 补偿存储类型. {@linkplain com.cuiyun.kfcoding.message.core.enums.RepositorySupportEnum}
     */
    private String repositorySupport = "db";

    /**
     * 是否需要自动恢复
     * 1 注意 当为事务发起方的时候（调用方/消费方），这里需要填true，
     * 默认为false，为了节省资源，不开启线程池调度.
     */
    private Boolean needRecover = false;

    /**
     * 任务调度线程大小.
     */
    private int scheduledThreadMax = Runtime.getRuntime().availableProcessors() << 1;

    /**
     * 调度时间周期单位秒.
     */
    private int scheduledDelay = 60;

    /**
     * 最大重试次数.
     */
    private int retryMax = 3;

    /**
     * 事务恢复间隔时间 单位秒（注意 此时间表示本地事务创建的时间多少秒以后才会执行）.
     */
    private int recoverDelayTime = 60;

    /**
     * disruptor  bufferSize.
     */
    private int bufferSize = 1024;

    /**
     * db配置.
     */
    private DbConfig dbConfig;

    /**
     * mongo配置.
     */
    private MongoConfig mongoConfig;

    /**
     * redis配置.
     */
    private RedisConfig redisConfig;

    /**
     * zookeeper配置.
     */
    private ZookeeperConfig zookeeperConfig;

    /**
     * file配置.
     */
    private FileConfig fileConfig;

    public AutoConfig() {
    }

    public AutoConfig(final Builder builder) {
        builder(builder);
    }

    public static Builder create() {
        return new Builder();
    }

    public void builder(final Builder builder) {
        this.serializer = builder.serializer;
        this.repositorySuffix = builder.repositorySuffix;
        this.repositorySupport = builder.repositorySupport;
        this.needRecover = builder.needRecover;
        this.scheduledThreadMax = builder.scheduledThreadMax;
        this.scheduledDelay = builder.scheduledDelay;
        this.retryMax = builder.retryMax;
        this.recoverDelayTime = builder.recoverDelayTime;
        this.bufferSize = builder.bufferSize;
        this.dbConfig = builder.dbConfig;
        this.mongoConfig = builder.mongoConfig;
        this.redisConfig = builder.redisConfig;
        this.zookeeperConfig = builder.zookeeperConfig;
        this.fileConfig = builder.fileConfig;
    }

    public static class Builder {

        private String repositorySuffix;

        private String serializer = "kryo";

        private String repositorySupport = "db";

        private Boolean needRecover = false;

        private int scheduledThreadMax = Runtime.getRuntime().availableProcessors() << 1;

        private int scheduledDelay = 60;

        private int retryMax = 3;

        private int recoverDelayTime = 60;

        private int bufferSize = 1024;

        private DbConfig dbConfig;

        private MongoConfig mongoConfig;

        private RedisConfig redisConfig;

        private ZookeeperConfig zookeeperConfig;

        private FileConfig fileConfig;

        public Builder setRepositorySuffix(String repositorySuffix) {
            this.repositorySuffix = repositorySuffix;
            return this;
        }

        public Builder setSerializer(String serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder setRepositorySupport(String repositorySupport) {
            this.repositorySupport = repositorySupport;
            return this;
        }

        public Builder setNeedRecover(Boolean needRecover) {
            this.needRecover = needRecover;
            return this;
        }

        public Builder setScheduledThreadMax(int scheduledThreadMax) {
            this.scheduledThreadMax = scheduledThreadMax;
            return this;
        }

        public Builder setScheduledDelay(int scheduledDelay) {
            this.scheduledDelay = scheduledDelay;
            return this;
        }

        public Builder setRetryMax(int retryMax) {
            this.retryMax = retryMax;
            return this;
        }

        public Builder setRecoverDelayTime(int recoverDelayTime) {
            this.recoverDelayTime = recoverDelayTime;
            return this;
        }

        public Builder setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder setDbConfig(DbConfig dbConfig) {
            this.dbConfig = dbConfig;
            return this;
        }

        public Builder setMongoConfig(MongoConfig mongoConfig) {
            this.mongoConfig = mongoConfig;
            return this;
        }

        public Builder setRedisConfig(RedisConfig redisConfig) {
            this.redisConfig = redisConfig;
            return this;
        }

        public Builder setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
            this.zookeeperConfig = zookeeperConfig;
            return this;
        }

        public Builder setFileConfig(FileConfig fileConfig) {
            this.fileConfig = fileConfig;
            return this;
        }

        public String getRepositorySuffix() {
            return repositorySuffix;
        }

        public String getSerializer() {
            return serializer;
        }

        public String getRepositorySupport() {
            return repositorySupport;
        }

        public Boolean getNeedRecover() {
            return needRecover;
        }

        public int getScheduledThreadMax() {
            return scheduledThreadMax;
        }

        public int getScheduledDelay() {
            return scheduledDelay;
        }

        public int getRetryMax() {
            return retryMax;
        }

        public int getRecoverDelayTime() {
            return recoverDelayTime;
        }

        public int getBufferSize() {
            return bufferSize;
        }

        public DbConfig getDbConfig() {
            return dbConfig;
        }

        public MongoConfig getMongoConfig() {
            return mongoConfig;
        }

        public RedisConfig getRedisConfig() {
            return redisConfig;
        }

        public ZookeeperConfig getZookeeperConfig() {
            return zookeeperConfig;
        }

        public FileConfig getFileConfig() {
            return fileConfig;
        }

        public AutoConfig build() {
            return new AutoConfig(this);
        }
    }
}
