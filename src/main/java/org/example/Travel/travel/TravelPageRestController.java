package org.example.Travel.travel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/travel")
public class TravelPageRestController {


    @Autowired
    private TravelService service;  // 전체 데이터 개수를 제공하는 서비스

    @GetMapping("/getTotalPages")
    public Map<String, Integer> getTotalPages(@RequestParam int limit) {
        log.info("/travel/getTotalPages()...");
        log.info("limit: {}", limit);
        // 전체 레코드 수를 조회 (예: DB 쿼리 호출)
        int totalRecords = service.getTotalRecords();
        log.info("totalRecords: {}", totalRecords);

        int totalPages = (int) Math.ceil((double) totalRecords / limit);
        log.info("totalPages: {}", totalPages);


        Map<String, Integer> result = new HashMap<>();
        result.put("totalPages", totalPages);
        return result;
    }//end getTotalPages()...

    @GetMapping("/getSearchListPages")
    public Map<String, Integer> getSearchListPages(@RequestParam int limit
            , @RequestParam(defaultValue = "district") String searchKey
            , @RequestParam(defaultValue = "") String searchWord) {
        log.info("/travel/getSearchListPages()...");
        log.info("limit: {}", limit);
        log.info("searchKey: {}", searchKey);
        log.info("searchWord: {}", searchWord);
        Map<String, Integer> result = new HashMap<>();
        if (searchKey.equals("district")) {
            double temp = service.getSearchDistrictRecords(searchWord);
            Integer searchEndPage = (int) Math.ceil(temp/(double) limit);
            log.info("searchEndPage: {}", searchEndPage);
            result.put("searchEndPage", searchEndPage);
            return result;
        } else {
            double temp = service.getSearchAddressRecords(searchWord);
            Integer searchEndPage = (int) Math.ceil(temp/(double) limit);
            log.info("searchEndPage: {}", searchEndPage);
            result.put("searchEndPage", searchEndPage);
            return result;
        }

        }//end getSearchListPages()...


    }//end class
