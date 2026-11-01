package com.vehicle.vehicleDetails.DTO;

import lombok.Data;

@Data
public class RtoDetailsResponse {

    private String vehicleNo;
    private String rtoCode;
    private String registeredRto;
    private String district;
    private String state;
    private String pincode;
    private String website;

    private String ownerName;
    private String makeAndModel;

}