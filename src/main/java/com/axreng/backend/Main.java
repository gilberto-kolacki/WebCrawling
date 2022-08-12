package com.axreng.backend;

import com.axreng.backend.utility.CommonUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static spark.Spark.init;
import static spark.Spark.stop;

public class Main {

    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();
        try {
            Config config = new Config();
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
