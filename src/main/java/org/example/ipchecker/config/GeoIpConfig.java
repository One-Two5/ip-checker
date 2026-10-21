package org.example.ipchecker.config;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GeoIpConfig {

    @Value("${geoip.database.city-path}")
    private String cityPath;
    @Value("${geoip.database.asn-path}")
    private String asnPath;

    @Bean
    public DatabaseReader cityReader() throws IOException {
        File database =  new File(cityPath);
        if (!database.exists()) {
            throw new IllegalStateException("База городов не найдена по пути %s".formatted(cityPath));
        }
        return new DatabaseReader.Builder(database)
                .withCache(new CHMCache())
                .build();
    }

    @Bean
    public DatabaseReader asnReader() throws IOException {
        File database =  new File(asnPath);
        if (!database.exists()) {
            throw new IllegalStateException("База данных ASN не найденп по пути %s".formatted(asnPath));
        }
        return new DatabaseReader.Builder(database)
                .withCache(new CHMCache())
                .build();
    }

}
