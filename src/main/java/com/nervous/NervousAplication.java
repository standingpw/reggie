package com.nervous;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@ServletComponentScan
@EnableTransactionManagement
public class NervousAplication {
    public static void main(String[] args) {
        SpringApplication.run(NervousAplication.class,args);
        log.info("项目启动成功！");
    }
}
