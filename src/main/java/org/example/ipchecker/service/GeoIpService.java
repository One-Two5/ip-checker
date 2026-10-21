package org.example.ipchecker.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.transaction.Transactional;
import org.example.ipchecker.dto.GeoIpResponse;
import org.example.ipchecker.entity.GeoIpLog;
import org.example.ipchecker.repository.GeoIpLogRepository;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Optional;

@Service
public class GeoIpService {

    private final DatabaseReader cityReader;
    private final DatabaseReader asnReader;
    private final GeoIpLogRepository logRepository;

    public GeoIpService(DatabaseReader cityReader, DatabaseReader asnReader, GeoIpLogRepository logRepository) {
        this.cityReader = cityReader;
        this.asnReader = asnReader;
        this.logRepository = logRepository;
    }

    @Transactional
    public GeoIpResponse lookupAndLog(String ipAddress) {
        String country = null;
        String countryCode = null;
        String city = null;
        Double latitude = null;
        Double longitude = null;
        String asnOrganization = null;
        Long asnNumber = null;
        String status = "SUCCESS";
        String errorMessage = null;

        try{
            InetAddress ip = InetAddress.getByName(ipAddress);

            Optional<CityResponse> cityOptions = cityReader.tryCity(ip);
            if (cityOptions.isPresent()) {
                CityResponse cityResponse = cityOptions.get();
                country = cityResponse.getCountry().getNames().getOrDefault("ru", cityResponse.getCountry().getName());
                countryCode = cityResponse.getCountry().getIsoCode();
                city = cityResponse.getCity().getNames().getOrDefault("ru", cityResponse.getCity().getName());
                latitude = cityResponse.getLocation().getLatitude();
                longitude = cityResponse.getLocation().getLongitude();
            }

            Optional<AsnResponse> asnOptions = asnReader.tryAsn(ip);
            if (asnOptions.isPresent()) {
                AsnResponse asnResponse = asnOptions.get();
                asnOrganization = asnResponse.getAutonomousSystemOrganization();
                asnNumber = asnResponse.getAutonomousSystemNumber();
            }

            if (cityOptions.isEmpty() && asnOptions.isEmpty()) {
                status = "NOT_FOUND";
                errorMessage = "IP-адрес отсутствует в базах данных";
            }
        } catch (Exception e) {
            status = "ERROR";
            errorMessage = "Ошибка обработки IP: " + e.getMessage();
        }

        GeoIpLog logEntry = new GeoIpLog(
                ipAddress, country, countryCode, city, latitude, longitude, asnOrganization, asnNumber,
                status, errorMessage
        );
        logRepository.save(logEntry);

        return new GeoIpResponse(
                ipAddress, country, countryCode, city, latitude, longitude, asnOrganization, asnNumber,
                status, errorMessage
        );
    }
}
