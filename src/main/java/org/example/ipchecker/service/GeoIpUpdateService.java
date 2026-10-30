package org.example.ipchecker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Service
public class GeoIpUpdateService {

    private static final Logger log = LoggerFactory.getLogger(GeoIpUpdateService.class);
    @Value("${geoip.database.city-path}")
    private String cityPath;

    @Value("${geoip.database.asn-path}")
    private String asnPath;

    @Value("${geoip.download-urls.city}")
    private String cityUrl;

    @Value("${geoip.download-urls.asn}")
    private String asnUrl;

    @Scheduled(cron = "0 0 4 * * *", zone = "Europe/Moscow")
    public void autoUpdateAllDatabases() {
        log.info("Запуск ежедневного обновления справочников GeoLite2 с GitHub...");
        runUpdate();
    }

    public void forceUpdateAllDatabases() {
        log.info("Запуск ПРИНУДИТЕЛЬНОГО обновления справочников GeoLite2 с GitHub...");
        runUpdate();
    }

    private void runUpdate() {
        downloadFile(cityUrl, cityPath);
        downloadFile(asnUrl, asnPath);
        log.info(">>> Все справочники с GitHub успешно обновлены.");
    }

    private void downloadFile(String sourceUrl, String targetPath) {
        System.out.println("Скачивание: " + sourceUrl);

        int maxAttempts = 3;
        long waitTimeMillis = 2000;

        for (int attempt = 0; attempt <= maxAttempts; attempt++) {
            try {
                URL url = URI.create(sourceUrl).toURL();
                File targetFile = new File(targetPath);

                File parentDir = targetFile.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }

                try (InputStream in = url.openStream();
                     ReadableByteChannel rbc = Channels.newChannel(in);
                     FileOutputStream fos = new FileOutputStream(targetFile)) {

                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    System.out.println("Файл успешно сохранен -> " + targetPath);
                    return;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), "Ошибка при скачивании файла %s:");

                if (attempt == maxAttempts) {
                    log.error("Не удалось скачать файл после {} попыток.", maxAttempts);
                }

                try{
                    Thread.sleep(waitTimeMillis);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
