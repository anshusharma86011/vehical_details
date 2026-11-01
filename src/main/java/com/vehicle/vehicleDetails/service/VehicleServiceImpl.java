package com.vehicle.vehicleDetails.service;

import com.vehicle.vehicleDetails.DTO.RtoDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl {

    private final RestTemplate restTemplate;

    public RtoDetailsResponse getRtoDetails(String vehicleNo) {

        try {

            String url =
                    "https://www.carinfo.app/rto-vehicle-registration-detail/rto-details/"
                            + vehicleNo;

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            String html = response.getBody();

            System.out.println("===== JSOUP CODE RUNNING =====");
            System.out.println("STATUS = " + response.getStatusCode());

            if (html == null || html.isEmpty()) {
                throw new RuntimeException("Empty HTML received");
            }

            Document document = Jsoup.parse(html);

            Elements titles =
                    document.select("p.expand_component_itemText__cbigB");

            Elements values =
                    document.select("p.expand_component_itemSubTitle__ElsYf");

            System.out.println("TITLES FOUND = " + titles.size());
            System.out.println("VALUES FOUND = " + values.size());

            RtoDetailsResponse dto = new RtoDetailsResponse();
            dto.setVehicleNo(vehicleNo);

            for (int i = 0; i < Math.min(titles.size(), values.size()); i++) {

                String title = titles.get(i).text().trim();
                String value = values.get(i).text().trim();

                System.out.println(title + " = " + value);

                switch (title) {

                    case "Number":
                        dto.setRtoCode(value);
                        break;

                    case "Registered RTO":
                        dto.setRegisteredRto(value);

                        String[] parts = value.split(",");

                        if (parts.length > 0) {
                            dto.setDistrict(parts[0].trim());
                        }

                        if (parts.length > 1) {
                            String[] statePin = parts[1].split("-");

                            dto.setState(statePin[0].trim());

                            if (statePin.length > 1) {
                                dto.setPincode(statePin[1].trim());
                            }
                        }
                        break;

                    case "State":
                        dto.setState(value);
                        break;

                    case "Website":
                        dto.setWebsite(value);
                        break;
                }
            }
            if (dto.getRtoCode() == null || dto.getRtoCode().isBlank()) {
                throw new RuntimeException(
                        "No RTO details found for vehicle number : " + vehicleNo
                );
            }
            dto.setOwnerName(
                    document.select(
                                    "p.input_vehical_layout_ownerName__NHkpi")
                            .text());

            dto.setMakeAndModel(
                    document.select(
                                    "p.input_vehical_layout_vehicalModel__1ABTF")
                            .text());


            return dto;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to fetch RTO details : " + e.getMessage(),
                    e
            );
        }
    }
}