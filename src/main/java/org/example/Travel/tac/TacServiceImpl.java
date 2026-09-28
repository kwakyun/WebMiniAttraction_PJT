package org.example.Travel.tac;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class TacServiceImpl implements TacService {
    @Autowired
    TacMapper tacMapper;


    @Override
    public List<TacVO> searchList(String searchWord) {

        // 공백을 기준으로 문자열 분리
        String[] parts = searchWord.split(" ");

        // 예를 들어, "서울특별시"와 "종로구"를 각각 뽑아내기
        String searchWord1 = parts[0];  // 서울특별시
        String searchWord2 = parts[1];  // 종로구
        log.info("searchWord1:" + searchWord1);
        log.info("searchWord2:" + searchWord2);



        return tacMapper.searchList(searchWord1, searchWord2);
    }
    public String formatRelationTitle(String relationTitle) {
        String[] lines = relationTitle.split("\n");
        StringBuilder formattedTitle = new StringBuilder();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(" ");
            if (parts.length > 2) {
                String attraction = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                formattedTitle.append(attraction).append("\n");
            }
        }

        return formattedTitle.toString().trim();
    }

}//end class
