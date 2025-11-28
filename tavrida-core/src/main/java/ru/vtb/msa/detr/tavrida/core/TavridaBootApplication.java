package ru.vtb.msa.detr.tavrida.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class TavridaBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TavridaBootApplication.class, args);
    }

}
