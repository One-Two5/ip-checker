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
        int maxAttempts = 3;

        log.info("Скачивание: {}", sourceUrl);

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try (InputStream in = URI.create(sourceUrl).toURL().openStream();
                 ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream(targetPath)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                log.info("Файл успешно сохранен -> {}", targetPath);
                return;
            }catch (Exception e) {
                log.error("Ошибка при скачивании файла {}: {}", sourceUrl, e.getMessage(), e);

                if (attempt == maxAttempts) {
                    log.error("Не удалось скачать файл после {} попыток.", maxAttempts);
                }
            }
        }
    }
}
