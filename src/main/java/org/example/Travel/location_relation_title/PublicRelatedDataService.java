package org.example.Travel.location_relation_title;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.Travel.tac.TacVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PublicRelatedDataService {

    @Value("${public.related_attractions_api.url}")
    private String relatedAttractionsApiUrl;

    @Value("${public.api.serviceKey}")
    private String serviceKey;


    public Map<String,String> getTitle(List<TacVO> tacVOList) {
        if (serviceKey == null || serviceKey.isBlank()) {
            throw new IllegalStateException("PUBLIC_DATA_SERVICE_KEY is not configured");
        }
        log.info("getTitle()....keyword:{}", tacVOList);

        StringBuilder result = new StringBuilder();
        StringBuilder addressResult = new StringBuilder();

        tacVOList.forEach(tacVO -> {
            String areaCd = String.valueOf(tacVO.getAreaCd());
            String sigunguCd = String.valueOf(tacVO.getSignguCd());

            String temp = tacVO.getAreaNm();
            String areaNm = temp.substring(0,2);
            log.info("areaNm:" + areaNm);

            try {
                String keyword = URLEncoder.encode(areaNm, "UTF-8");
                String requestUrl = buildRequestUrl(areaCd, sigunguCd, keyword);
                String apiResponse = callApi(requestUrl);

                // parseRelatedTitle을 Map으로 받아오기
                Map<String, List<String>> parsedData = parseRelatedTitle(apiResponse);
                List<String> relatedTitles = parsedData.get("titles");
                List<String> relatedAddresses = parsedData.get("addresses");

                // 리스트를 문자열로 변환 후 추가
                result.append(String.join("\n", relatedTitles)).append("\n");
                addressResult.append(String.join("\n", relatedAddresses)).append("\n");

            } catch (Exception e) {
                log.warn("Related attraction lookup failed");
            }
        });

        log.info("Titles: {}", result.toString());
        log.info("Addresses: {}", addressResult.toString());

        Map<String, String> finalresult = new HashMap<>();
        finalresult.put("titles", result.toString());
        finalresult.put("addresses", addressResult.toString());

        return finalresult;
    }

    private String buildRequestUrl(String areaCd, String sigunguCd, String keyword) {
        // URL을 생성하는 부분을 수정하여, serviceKey를 쿼리 파라미터 앞부분에 정확히 추가

        String strUrl = relatedAttractionsApiUrl
                + "?serviceKey=" + URLEncoder.encode(serviceKey, java.nio.charset.StandardCharsets.UTF_8)   // serviceKey는 맨 앞에 위치해야 함
                + "&pageNo=1"
                + "&numOfRows=10"
                + "&MobileOS=ETC"
                + "&MobileApp=AppTest"
                + "&baseYm=202408"
                + "&areaCd=" + areaCd
                + "&signguCd=" + sigunguCd
                + "&keyword=" + keyword   // URL 인코딩된 키워드
                + "&_type=json";         // _type=json 파라미터 추가
        return strUrl;
    }

    private String callApi(String requestUrl) {
        // REST API 호출을 위한 코드
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        StringBuilder sb = new StringBuilder(); // API 응답을 저장할 StringBuilder

        try {
            // URL 객체 생성 및 HttpURLConnection 연결
            URL url = new URL(requestUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);

            is = urlConnection.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid public data API URL");
        } catch (IOException e) {
            throw new RuntimeException("Error reading API response");
        } finally {
            closeQuietly(br);
            closeQuietly(isr);
            closeQuietly(is);
        }
        return sb.toString();
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                log.error("Error closing stream", e);
            }
        }
    }


    private Map<String, List<String>> parseRelatedTitle(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> relatedTitles = new ArrayList<>();
        List<String> relatedAddresses = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode items = rootNode.path("response").path("body").path("items").path("item");

            for (JsonNode item : items) {
                String areaNm = item.path("areaNm").asText();
                String signguNm = item.path("signguNm").asText();
                String rlteTatsNm = item.path("rlteTatsNm").asText();
                String rlteBsicAdres = item.path("rlteBsicAdres").asText();

                relatedTitles.add(areaNm + " " + signguNm + " " + rlteTatsNm);
                relatedAddresses.add(rlteBsicAdres);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON response");
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("titles", relatedTitles);
        result.put("addresses", relatedAddresses);
        return result;
    }
}
