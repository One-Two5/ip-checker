package org.example.ipchecker.dto;

public class GeoIpResponse {

    private String ip;
    private String country;
    private String countryCode;
    private String city;
    private Double latitude;
    private Double longitude;
    private String asnOrganization;
    private Long asdNumber;
    private String status;
    private String message;

    public GeoIpResponse(String ip, String country, String countryCode, String city,
                         Double latitude, Double longitude, String asnOrganization,
                         Long asdNumber, String status, String message) {
        this.ip = ip;
        this.country = country;
        this.countryCode = countryCode;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.asnOrganization = asnOrganization;
        this.asdNumber = asdNumber;
        this.status = status;
        this.message = message;
    }

    public String getIp() {
        return ip;
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

    public String getAsnOrganization() {
        return asnOrganization;
    }

    public Long getAsdNumber() {
        return asdNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
