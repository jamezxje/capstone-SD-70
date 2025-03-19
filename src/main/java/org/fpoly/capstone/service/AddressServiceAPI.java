package org.fpoly.capstone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceAPI {

    private final String TOKEN_API_GHN = "7d67a984-b5fe-11ef-b166-4205c1d15e61";
    private final String URL_PROVINCE = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
    private final String URL_DISTRICT = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
    private final String URL_WARD = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";

    private final RestTemplate restTemplate;

    @Autowired
    public AddressServiceAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getProvinces() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", TOKEN_API_GHN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(URL_PROVINCE, HttpMethod.GET, entity, Map.class);

        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    public List<Map<String, Object>> getDistricts(Integer provinceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", TOKEN_API_GHN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = URL_DISTRICT + "?province_id=" + provinceId;

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    public List<Map<String, Object>> getWards(Integer districtId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", TOKEN_API_GHN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = URL_WARD + "?district_id=" + districtId;

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return (List<Map<String, Object>>) response.getBody().get("data");
    }
}
