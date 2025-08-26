package com.example.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Disabled("병렬 실행 증명용, 수동 실행 전용")
class ParallelExecutionTest {

    @Test
    @DisplayName("병렬 실행 테스트 1 - 3초 지연")
    void test01(TestInfo testInfo) throws InterruptedException {
        logTestStart(testInfo);
        
        // 3초 지연
        Thread.sleep(3000);
        
        logTestEnd(testInfo);
    }

    @Test
    @DisplayName("병렬 실행 테스트 2 - 2초 지연")
    void test02(TestInfo testInfo) throws InterruptedException {
        logTestStart(testInfo);
        
        // 2초 지연
        Thread.sleep(2000);
        
        logTestEnd(testInfo);
    }

    @Test
    @DisplayName("병렬 실행 테스트 3 - 1초 지연")
    void test03(TestInfo testInfo) throws InterruptedException {
        logTestStart(testInfo);
        
        // 1초 지연
        Thread.sleep(1000);
        
        logTestEnd(testInfo);
    }

    @Test
    @DisplayName("병렬 실행 테스트 4 - 4초 지연")
    void test04(TestInfo testInfo) throws InterruptedException {
        logTestStart(testInfo);
        
        // 4초 지연
        Thread.sleep(4000);
        
        logTestEnd(testInfo);
    }

    @Test
    @DisplayName("병렬 실행 테스트 5 - 2.5초 지연")
    void test05(TestInfo testInfo) throws InterruptedException {
        logTestStart(testInfo);
        
        // 2.5초 지연
        Thread.sleep(2500);
        
        logTestEnd(testInfo);
    }

    private void logTestStart(TestInfo testInfo) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        System.out.printf("[%s] [%s] [Thread-%d] 테스트 시작%n", 
            timestamp, testInfo.getDisplayName(), Thread.currentThread().getId());
    }

    private void logTestEnd(TestInfo testInfo) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        System.out.printf("[%s] [%s] [Thread-%d] 테스트 완료%n", 
            timestamp, testInfo.getDisplayName(), Thread.currentThread().getId());
    }
} 