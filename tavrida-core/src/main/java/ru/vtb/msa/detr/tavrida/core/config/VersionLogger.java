package ru.vtb.msa.detr.tavrida.core.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VersionLogger {

    @Value("${info.app.version:?.?.?}")
    private String version;

    @Value("${info.app.name:Tavrida}")
    private String appName;

    private static final Logger logger = LoggerFactory.getLogger(VersionLogger.class);


    @PostConstruct
    public void logVersion() {
        String banner = """
                
                ===========================================
                %s v%s is starting...
                ===========================================
                """.formatted(appName, version);

        logger.info(banner);
    }
}