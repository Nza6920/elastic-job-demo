package com.niu.elasticjob.job;

        import com.dangdang.ddframe.job.api.ShardingContext;
        import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * Sample Job 实现类
 *
 * @author [nza]
 * @version 1.0 2020/12/8
 * @createTime 22:22
 */
public class MySampleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext context) {
        System.out.printf("我是分片项: %s, 总分片项: %s", context.getShardingItem(), context.getShardingTotalCount());
        System.out.println();
    }
}
