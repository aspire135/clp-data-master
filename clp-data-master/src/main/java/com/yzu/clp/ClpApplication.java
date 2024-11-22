package com.yzu.clp;

import com.yzu.clp.task.BulletinTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan(basePackages = "com.yzu")
@SpringBootApplication
@EnableSwagger2
public class ClpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClpApplication.class, args);
        BulletinTask.run();
    }

}
