package com.niu.elasticjob;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.niu.elasticjob.job.MyDataFlowJob;
import com.niu.elasticjob.job.MySampleJob;

/**
 * 启动类
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello!");
//        new JobScheduler(zkCenter(), configuration()).init();
//        new JobScheduler(zkCenter(), configurationDataFlow()).init();
        new JobScheduler(zkCenter(), configurationScrpit()).init();
    }

    /**
     * 配置JOB
     * 1. job创建核心配置
     * 2. job类型配置
     * 3. job根配置(Lite)
     *
     * @return {@link LiteJobConfiguration}
     */
    public static LiteJobConfiguration configuration() {

        // job创建核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("MySimpleJob", "0/5 * * * * ?", 2)
                .build();

        // job类型配置
        JobTypeConfiguration jtc = new SimpleJobConfiguration(jcc, MySampleJob.class.getCanonicalName());

        // job根配置(Lite)
        return LiteJobConfiguration
                .newBuilder(jtc)
                .overwrite(true)
                .build();
    }

    /**
     * 配置协调中心(zookeeper)
     *
     * @return {@link CoordinatorRegistryCenter}
     */
    public static CoordinatorRegistryCenter zkCenter() {

        ZookeeperConfiguration zc = new ZookeeperConfiguration("localhost:2181", "java-simple-job");

        CoordinatorRegistryCenter crc = new ZookeeperRegistryCenter(zc);

        // 初始化
        crc.init();

        return crc;
    }

    /**
     * 配置 DataFlow JOB
     * 1. job创建核心配置
     * 2. job类型配置
     * 3. job根配置(Lite)
     *
     * @return {@link LiteJobConfiguration}
     */
    public static LiteJobConfiguration configurationDataFlow() {

        // job创建核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("MyDataFlowJob", "0/10 * * * * ?", 2)
                .build();

        // job类型配置, getCanonicalName() 获取包路径
        // streamingProcess: 是否开启循环抓取
        JobTypeConfiguration jtc = new DataflowJobConfiguration(jcc,
                MyDataFlowJob.class.getCanonicalName(),
                true);

        // job根配置(Lite)
        return LiteJobConfiguration
                .newBuilder(jtc)
                .overwrite(true)
                .build();
    }


    /**
     * 配置 Script JOB
     * 1. job创建核心配置
     * 2. job类型配置
     * 3. job根配置(Lite)
     *
     * @return {@link LiteJobConfiguration}
     */
    public static LiteJobConfiguration configurationScrpit() {

        // 脚本文件地址
        String scriptFile = "E:\\code\\elastic-job-demo\\test.cmd";

        // job创建核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("MyScriptJob", "0/10 * * * * ?", 2)
                .build();

        // job类型配置, getCanonicalName() 获取包路径
        // streamingProcess: 是否开启循环抓取
        JobTypeConfiguration jtc = new ScriptJobConfiguration(jcc, scriptFile);

        // job根配置(Lite)
        return LiteJobConfiguration
                .newBuilder(jtc)
                .overwrite(true)
                .build();
    }
}
