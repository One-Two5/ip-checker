package org.example.ipchecker.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ip_logs")
public class GeoIpLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String requestIp;
    private String country;
    private String countryCode;
    private String city;
    private Double latitude;
    private Double longitude;
    private String asnOrganization;
    private Long asdNumber;
    private String status;
    private String message;
    private LocalDateTime createdAt;

    public GeoIpLog(){}

    public GeoIpLog(String requestIp, String country, String countryCode,
                    String city, Double latitude, Double longitude,
                    String asnOrganization, Long asdNumber, String status, String message) {
        this.requestIp = requestIp;
        this.country = country;
        this.countryCode = countryCode;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.asnOrganization = asnOrganization;
        this.asdNumber = asdNumber;
        this.status = status;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCity() {
        return city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
