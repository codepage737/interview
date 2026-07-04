package com.shahrabi.interview;

import org.springframework.boot.SpringApplication;

public class TestInterviewApplication {

    public static void main(String[] args) {
        SpringApplication.from(InterviewApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
