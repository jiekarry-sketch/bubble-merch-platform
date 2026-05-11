package ltd.newbee.mall;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 */
//使用 MyBatis 操作数据库，所有的数据库接口（Mapper）都放在 ltd.newbee.mall.dao 包下。
// 有了它，就不需要在每一个 Mapper 接口上都写 @Mapper 注解了。
@MapperScan(basePackages = "ltd.newbee.mall.dao")
@Slf4j
@EnableTransactionManagement //开启事务管理
@EnableCaching  //开启缓存
@EnableScheduling //spring task 定时任务，开启任务调度
@SpringBootApplication
public class NewBeeMallPlusApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewBeeMallPlusApplication.class, args);
    }
}
