package org.example.ipchecker.controller;

import org.example.ipchecker.dto.GeoIpResponse;
import org.example.ipchecker.service.GeoIpService;
import org.example.ipchecker.service.GeoIpUpdateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geoip")
public class GeoIpController {

    private final GeoIpService geoIpService;
    private final GeoIpUpdateService updateService;

    public GeoIpController(GeoIpService geoIpService, GeoIpUpdateService updateService) {
        this.geoIpService = geoIpService;
        this.updateService = updateService;
    }

    @GetMapping("/ip")
    public GeoIpResponse getIpInfo(@RequestParam String ip) {
        return geoIpService.lookupAndLog(ip);
    }

    @GetMapping("/update")
    public String triggerUpdate() {
        updateService.forceUpdateAllDatabases();
        return "Процесс обновления запущен! Проверьте логи консоли.";
    }
}
