package org.example.ipchecker.service;

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
        System.out.println("Запуск ежедневного обновления справочников GeoLite2 с GitHub...");
        runUpdate();
    }

    public void forceUpdateAllDatabases() {
        System.out.println("Запуск ПРИНУДИТЕЛЬНОГО обновления справочников GeoLite2 с GitHub...");
        runUpdate();
    }

    private void runUpdate() {
        downloadFile(cityUrl, cityPath);
        downloadFile(asnUrl, asnPath);
        System.out.println(">>> Все справочники с GitHub успешно обновлены.");
    }

    private void downloadFile(String sourceUrl, String targetPath) {
        System.out.println("Скачивание: " + sourceUrl);
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
            }
        } catch (Exception e) {
            System.err.println("Ошибка при скачивании файла " + sourceUrl + ": " + e.getMessage());
        }
    }
}
