package com.axreng.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.init;
import static spark.Spark.stop;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        init();
        try {
            new WebCrawlingApplication(new Config()).exec();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            stop();
        }
    }
}
