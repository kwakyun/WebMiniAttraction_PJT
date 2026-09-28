package org.example.Travel.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberPageRestController {


    @Autowired
    private MemberService service;  // 전체 데이터 개수를 제공하는 서비스

    @GetMapping("/getTotalPages")
    public Map<String, Integer> getTotalPages(@RequestParam int limit) {
        log.info("/member/getTotalPages()...");
        log.info("limit: {}", limit);
        // 전체 레코드 수를 조회 (예: DB 쿼리 호출)
        int totalRecords = service.getTotalRecords_m();
        log.info("totalRecords: {}", totalRecords);

        int totalPages = (int) Math.ceil((double) totalRecords / limit);
        log.info("totalPages: {}", totalPages);


        Map<String, Integer> result = new HashMap<>();
        result.put("totalPages", totalPages);
        return result;
    }//end getTotalPages()...

    @GetMapping("/getSearchListPages")
    public Map<String, Integer> getSearchListPages(@RequestParam int limit
            , @RequestParam(defaultValue = "name") String searchKey
            , @RequestParam(defaultValue = "") String searchWord) {
        log.info("/travel/getSearchListPages()...");
        log.info("limit: {}", limit);
        log.info("searchKey: {}", searchKey);
        log.info("searchWord: {}", searchWord);
        Map<String, Integer> result = new HashMap<>();
        if (searchKey.equals("name")) {
            double temp = service.getSearchNameRecords(searchWord);
            Integer searchEndPage = (int) Math.ceil(temp/(double) limit);
            log.info("searchEndPage: {}", searchEndPage);
            result.put("searchEndPage", searchEndPage);
            return result;
        } else {
            double temp = service.getSearchTelRecords(searchWord);
            Integer searchEndPage = (int) Math.ceil(temp/(double) limit);
            log.info("searchEndPage: {}", searchEndPage);
            result.put("searchEndPage", searchEndPage);
            return result;
        }

        }//end getSearchListPages()...


    }//end class
