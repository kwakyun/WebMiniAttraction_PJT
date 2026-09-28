package org.example.Travel.map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class RestMapController {
    private final String javascriptKey;

    public RestMapController(@Value("${kakao.maps.javascript-key:}") String javascriptKey) {
        this.javascriptKey = javascriptKey;
    }

    @GetMapping("/kakaoMap_do")
    public String kakaoMap_do(Model model) {
        // Only this public browser key enters the view; server credentials never do.
        model.addAttribute("kakaoMapJavascriptKey", javascriptKey);
        return "map/kakao_map";
    }
}
