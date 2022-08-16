package com.axreng.backend;

import com.axreng.backend.utility.CommonUtils;
import com.axreng.backend.crawler.WebCrawlingApplication;

import java.time.Duration;

import static spark.Spark.init;
import static spark.Spark.stop;

public class Main {

    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();
        try {
            Config config = Config.getInstance();
            if (CommonUtils.isAbsoluteUriWithValidScheme(config.getBaseUrl())) {
                init();
                new WebCrawlingApplication(config).exec();
                stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            stop();
        }
        Duration tempoFinal = Duration.ofMillis(System.currentTimeMillis() - tempoInicial);
        System.out.printf("---- Runtime: %d:%d:%d sec ---- \n", tempoFinal.toHoursPart(), tempoFinal.toMinutesPart(), tempoFinal.toSecondsPart());
    }

}
