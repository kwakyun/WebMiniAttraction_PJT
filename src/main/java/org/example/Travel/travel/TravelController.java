package org.example.Travel.travel;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.Travel.location_image.PublicDataService;
import org.example.Travel.location_relation_title.PublicRelatedDataService;
import org.example.Travel.tac.TacService;
import org.example.Travel.tac.TacVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/travel")
public class TravelController {
    @Value("${kakao.maps.javascript-key:}")
    private String kakaoMapJavascriptKey;

    @ModelAttribute("kakaoMapJavascriptKey")
    public String kakaoMapJavascriptKey() {
        return kakaoMapJavascriptKey;
    }

    @Autowired
    private PublicRelatedDataService publicRelatedDataService;

    @Autowired
    private TravelService service;

    @Autowired
    private PublicDataService publicDataService;

    @Autowired
    private TacService tacService;

    @Value("${file.dir}")
    private String realPath;


    @Autowired
    private HttpSession session;

    @GetMapping("/insert")
    public String insert() {
        log.info("/travel/insert");
        return "travel/insert";
    }

    @GetMapping("/update")
    public String update(TravelVO vo, Model model ) {
        log.info("/travel/update");
        log.info("vo : {}",vo);

        TravelVO vo2 = service.selectOne(vo);
        model.addAttribute("vo2", vo2);

        return "travel/update";
    }

    @GetMapping("/delete")
    public String delete() {
        log.info("/travel/delete");

        return "travel/delete";
    }

    @GetMapping("/selectOne")
    public String selectOne(TravelVO vo, Model model , String title) {
        log.info("/travel/selectOne");
        log.info("vo : {}",vo);

        TravelVO vo2 = service.selectOne(vo);
        log.info("vo2 : {}",vo2);
        model.addAttribute("vo2", vo2);

        //주소와 관련된 관광명소 코드 VO들을 얻어와서 데이터를 얻어올 준비하기
        List<TacVO> vos = tacService.searchList(vo2.getAddress());
        log.info("vos: {}",vos);
        Map<String, String> temp = publicRelatedDataService.getTitle(vos);
        String titles = temp.get("titles");
        String relationaddr = temp.get("addresses");

        String relationTitle = tacService.formatRelationTitle(titles);
        log.info("relationTitle:" + relationTitle);

        //관련명소(relationTitle)을 얻어와서 모델에 넣기
        model.addAttribute("relationTitle", relationTitle);
        log.info("vo2.getTitle(): {}", vo2.getTitle());
        String imageUrl = publicDataService.getImageUrl(vo2.getTitle());
        log.info("imageUrl: {}", imageUrl);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("relationaddr", relationaddr);


        return "travel/selectOne";
    }

    @GetMapping("/selectAll")
    public String selectAll(
            @RequestParam(defaultValue = "1")int cpage,
            @RequestParam(defaultValue = "5")int limit, Model model) {
        log.info("/travel/selectAll");
        log.info("cpage: {}",cpage);
        log.info("limit: {}",limit);

        List<TravelVO> vos = service.selectAll(cpage, limit);
        model.addAttribute("vos", vos);


        return "travel/selectAll";
    }

    @GetMapping("/searchList")
    public String searchList(
            @RequestParam(defaultValue = "1")int cpage,
            @RequestParam(defaultValue = "5")int limit,
            @RequestParam(defaultValue = "district")String searchKey,
            @RequestParam(defaultValue = "")String searchWord,
            Model model ) {
        log.info("Controller :  /travel/searchList");
        log.info("cpage: {}",cpage);
        log.info("limit: {}",limit);
        log.info("searchKey: {}",searchKey);
        log.info("searchWord: {}",searchWord);


        List<TravelVO> vos = service.searchList(cpage, limit, searchKey, searchWord);
        log.info("vos: {}",vos);
        model.addAttribute("vos", vos);


        return "travel/selectAll";
    }

    @PostMapping("/insertOK")
    public String insertOK(TravelVO vo)  {
        log.info("/travel/insertOK");
        log.info("vo : {}",vo);

        int result = service.insertOK(vo);
        log.info("result : {}",result);

        if(result  > 0) {
            return "redirect:/travel/selectAll";
        }else{
            return "redirect:/travel/insert";
        }
    }

    @PostMapping("/updateOK")
    public String updateOK(TravelVO vo)  {
        log.info("/travel/updateOK");
        log.info("vo : {}",vo);



        int result = service.updateOK(vo);
        log.info("result : {}",result);

        if(result  > 0) {
            return "redirect:/travel/selectOne?no="+vo.getNo();
        }else{
            return "redirect:/travel/update?no="+vo.getNo();
        }
    }

    @GetMapping("/deleteOK")
    public String deleteOK(TravelVO vo) {
        log.info("/travel/deleteOK");
        log.info("vo : {}",vo);

        int result = service.deleteOK(vo);
        log.info("result : {}",result);

        if(result  > 0) {
            return "redirect:/travel/selectAll";
        }else{
            return "redirect:/travel/delete?no="+vo.getNo();
        }
    }



}//end class