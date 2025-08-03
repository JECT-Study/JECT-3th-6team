package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 비동기 메서드(@Async) 실행용 스레드풀 설정.
 * Java 21의 가상 스레드를 사용하여 I/O 바운드 작업의 효율을 높인다.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("Async-Email-VT-");
        executor.setVirtualThreads(true);
        return executor;
    }
}
