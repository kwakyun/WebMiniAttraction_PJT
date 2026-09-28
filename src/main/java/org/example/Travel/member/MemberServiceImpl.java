package org.example.Travel.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int insertOK(MemberVO vo) {
        log.info("insertOK()....");
        vo.setPw(passwordEncoder.encode(vo.getPw()));

        return mapper.insertOK(vo);
    }

    @Override
    public int updateOK(MemberVO vo) {
        log.info("updateOK()....");
        vo.setPw(passwordEncoder.encode(vo.getPw()));

        return mapper.updateOK(vo);
    }

    @Override
    public int deleteOK(MemberVO vo) {
        log.info("deleteOK()....");
        return mapper.deleteOK(vo);
    }

    @Override
    public MemberVO selectOne(MemberVO vo) {
        log.info("selectOne()....");
        return mapper.selectOne(vo);
    }

    @Override
    public List<MemberVO> selectAll(int cpage, int limit) {
        log.info("selectAll()....");
        log.info("cpage: " + cpage);//3
        log.info("limit: " + limit);//5
        //cpage,limit를 이용해서 시작행을 구합니다.
        int startRow = (cpage - 1) * limit;//0,5,10,,,,
        return mapper.selectAll(startRow,limit);
    }

    @Override
    public List<MemberVO> searchList(int cpage, int limit, String searchKey, String searchWord) {
        log.info("searchList()....");
        log.info("cpage: " + cpage);
        log.info("limit: " + limit);
        log.info("searchKey: " + searchKey);
        log.info("searchWord: " + searchWord);

        //cpage,limit를 이용해서 시작행을 구합니다.
        int startRow = (cpage - 1) * limit;//0,5,10,,,,

        if(searchKey.equals("name")) {
            return mapper.searchListName(startRow,limit,"%"+searchWord+"%");
        }else{
            return mapper.searchListTel(startRow,limit,"%"+searchWord+"%");
        }
    }

    @Override
    public MemberVO  selectOneMyPage(String user_id) {
        return mapper.findById(user_id);
    }

    @Override
    public int getTotalRecords_m() {
        return mapper.getTotalRecords_m();
    }

    @Override
    public int getSearchNameRecords(String searchWord) {
        return mapper.getSearchNameRecords("%"+searchWord+"%");
    }

    @Override
    public int getSearchTelRecords(String searchWord) {
        return mapper.getSearchTelRecords("%"+searchWord+"%");
    }


}