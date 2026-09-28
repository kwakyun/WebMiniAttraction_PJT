package org.example.Travel.location_image;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class PublicDataService {

    @Value("${public.api.url}")
    private String apiUrl;

    @Value("${public.api.serviceKey}")
    private String serviceKey;

    public String getImageUrl(String title) {
        if (serviceKey == null || serviceKey.isBlank()) {
            throw new IllegalStateException("PUBLIC_DATA_SERVICE_KEY is not configured");
        }
        log.info("getImageUrl()....keyword: {}", title);

        // ✅ 키워드 안전하게 인코딩
        String encodedKeyword;
        try {
            encodedKeyword = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("URL Encoding Error", e);
            throw new RuntimeException("Keyword Encoding Failed", e);
        }

        // ✅ API 요청 URL 생성
        String requestUrl = apiUrl + "&serviceKey=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + "&keyword=" + encodedKeyword;

        // ✅ API 요청 및 응답 수집
        String responseJson = fetchApiResponse(requestUrl);

        // ✅ JSON 파싱 및 이미지 URL 추출
        return parseImageUrl(responseJson);
    }

    private String fetchApiResponse(String requestUrl) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);

            try (InputStream is = connection.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            log.warn("Public data API request failed");
            // IOException messages can include the URL and its service key.
            throw new RuntimeException("Failed to fetch API response");
        }
        return responseBuilder.toString();
    }

    private String parseImageUrl(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            // ✅ JSON 구조 검증 및 예외 방지
            if (itemsNode.isMissingNode() || !itemsNode.isArray() || itemsNode.size() == 0) {
                log.warn("No image found in API response.");
                return "No image available";
            }

            // ✅ 이미지 URL 추출
            String imageUrl = itemsNode.get(0).path("galWebImageUrl").asText();
            log.info("Extracted Image URL: {}", imageUrl);
            return imageUrl.isEmpty() ? "No image available" : imageUrl;
        } catch (JsonProcessingException e) {
            log.warn("Public data API returned invalid JSON");
            throw new RuntimeException("Failed to parse API response");
        }
    }
}
