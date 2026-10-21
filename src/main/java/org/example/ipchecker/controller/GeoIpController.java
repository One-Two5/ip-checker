package org.example.ipchecker.controller;

import org.example.ipchecker.dto.GeoIpResponse;
import org.example.ipchecker.service.GeoIpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geoip")
public class GeoIpController {

    private final GeoIpService geoIpService;

    public GeoIpController(GeoIpService geoIpService) {
        this.geoIpService = geoIpService;
    }

    @GetMapping("/ip")
    public GeoIpResponse getIpInfo(@RequestParam String ip) {
        return geoIpService.lookupAndLog(ip);
    }
}
