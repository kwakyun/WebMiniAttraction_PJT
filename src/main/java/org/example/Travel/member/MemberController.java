package org.example.Travel.member;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService service;

    @Autowired
    private HttpSession session;


    @GetMapping("/insert")
    public String insert() {
        log.info("/member/insert");
        return "member/insert";
    }

    @GetMapping("/update")
    public String update(MemberVO vo, Model model) {
        log.info("/member/update");

        MemberVO vo2 = service.selectOne(vo);
        model.addAttribute("vo2", vo2);

        return "member/update";
    }

    @GetMapping("/delete")
    public String delete() {
        log.info("/member/delete");

        return "member/delete";
    }

    @GetMapping("/selectOne")
    public String selectOne(MemberVO vo, Model model) {
        log.info("/member/selectOne");
        MemberVO vo2 = service.selectOne(vo);
        model.addAttribute("vo2", vo2);
        return "member/selectOne";


    }

    @GetMapping("/selectAll")
    public String selectAll(
            @RequestParam(defaultValue = "1") int cpage,
            @RequestParam(defaultValue = "5") int limit, Model model) {
        log.info("/member/selectAll");
        log.info("cpage: {}", cpage);
        log.info("limit: {}", limit);

        List<MemberVO> vos = service.selectAll(cpage, limit);
        model.addAttribute("vos", vos);


        return "member/selectAll";
    }

    @GetMapping("/searchList")
    public String searchList(
            @RequestParam(defaultValue = "1") int cpage,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "name") String searchKey,
            @RequestParam(defaultValue = "") String searchWord,
            Model model) {
        log.info("/member/searchList");
        log.info("cpage: {}", cpage);
        log.info("limit: {}", limit);
        log.info("searchKey: {}", searchKey);
        log.info("searchWord: {}", searchWord);

        List<MemberVO> vos = service.searchList(cpage, limit, searchKey, searchWord);
        model.addAttribute("vos", vos);

        return "member/selectAll";
    }

    @PostMapping("/insertOK")
    public String insertOK(MemberVO vo) throws IOException {
        log.info("/member/insertOK");


        int result = service.insertOK(vo);
        log.info("result : {}", result);

        if (result > 0) {
            return "redirect:/member/selectAll";
        } else {
            return "redirect:/member/insert";
        }
    }

    @PostMapping("/updateOK")
    public String updateOK(MemberVO vo) throws IOException {
        log.info("/member/updateOK");


        int result = service.updateOK(vo);
        log.info("result : {}", result);

        if (result > 0) {
            return "redirect:/member/selectAll";
        } else {
            return "redirect:/member/update";
        }
    }

    @GetMapping("/deleteOK")
    public String deleteOK(MemberVO vo) {
        log.info("/member/deleteOK");

        int result = service.deleteOK(vo);
        log.info("result : {}", result);

        if (result > 0) {
            return "redirect:/member/selectAll";
        } else {
            return "redirect:/member/delete?num=" + vo.getNum();
        }
    }

    @GetMapping("/login_form")
    public String login_form() {
        log.info("/member/login_form");

        return "member/login_form";
    }

    @GetMapping("/required_login")
    public String required_login() {
        log.info("/member/required_login");

        return "member/login_form";
    }

    //주의 : 로그인시 POST전송으로 제출되기때문에 실패시에도 그대로 @PostMapping으로 처리해야함
    @PostMapping("/login_fail")
    public String login_fail() {
        log.info("/member/login_fail");

        return "member/login_fail";
    }

    //주의 : 로그인시 POST전송으로 제출되기때문에 성공시에도 그대로 @PostMapping으로 처리해야함
    @PostMapping("/login_success")
    public String login_success() {
        log.info("/member/login_success");
        //로그인 성공 후 보여질 페이지
        return "redirect:/home";
    }

    @GetMapping("/denied")
    public String denied() {
        log.info("/member/denied");
        return "member/denied";
    }

    @GetMapping("/expired")
    public String expired() {
        log.info("/member/expired");
        return "member/expired";
    }


    @GetMapping("/selectOneMyPage")
    public String selectOneMyPage(Model model) {
        log.info("/member/selectOneMyPage");
        //마이페이지로부터는 회원번호가 넘어오지 않기때문에 세션으로부터 아이디를 얻어서 검색.
        String user_id = session.getAttribute("user_id").toString();
        MemberVO vo2 = service.selectOneMyPage(user_id);

        model.addAttribute("vo2", vo2);

        return "member/selectOne";
    }


}//end class
