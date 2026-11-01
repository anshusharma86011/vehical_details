package com.vehicle.vehicleDetails.controller;

import com.vehicle.vehicleDetails.service.VehicleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleServiceImpl vehicleService;

    @GetMapping("/{vehicleNo}")
    public ResponseEntity<?> getRtoDetails(
            @PathVariable String vehicleNo) {

        return ResponseEntity.ok(
                vehicleService.getRtoDetails(vehicleNo)
        );
    }
}