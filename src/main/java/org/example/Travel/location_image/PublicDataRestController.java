package org.example.Travel.location_image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/location_image")
public class PublicDataRestController {

    @Autowired
    private PublicDataService service;

    @GetMapping("/image")
    public Map<String, String> image(String title) {
        log.info("/image");
        log.info(title);

        //String imageUrl = "http://tong.visitkorea.or.kr/cms2/website/66/3023466.jpg";
        String imageUrl = service.getImageUrl(title);
        log.info(imageUrl);
        Map<String, String> map = new HashMap<>();
        map.put("imageUrl", imageUrl);
        log.info(map.toString());

        return map;
    }
}
