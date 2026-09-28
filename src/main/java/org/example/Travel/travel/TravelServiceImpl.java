package org.example.Travel.travel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TravelServiceImpl implements TravelService {
    @Autowired
    TravelMapper travelMapper;




    @Override
    public int insertOK(TravelVO vo) {
        log.info("insertOK()....");
        log.info(vo.toString());



        return travelMapper.insertOK(vo);
    }

    @Override
    public int updateOK(TravelVO vo) {
        log.info("updateOK()....");
        log.info(vo.toString());



        return travelMapper.updateOK(vo);
    }

    @Override
    public int deleteOK(TravelVO vo) {
        log.info("deleteOK()....");
        log.info(vo.toString());
        return travelMapper.deleteOK(vo);
    }

    @Override
    public TravelVO selectOne(TravelVO vo) {
        log.info("selectOne()....");
        log.info(vo.toString());

        return travelMapper.selectOne(vo);
    }


    @Override
    public List<TravelVO> selectAll(int cpage, int limit) {
        log.info("selectAll()....");
        log.info("cpage: " + cpage);//3
        log.info("limit: " + limit);//5
        //cpage,limit를 이용해서 시작행을 구합니다.
        int startRow = (cpage - 1) * limit;//0,5,10,,,,
        return travelMapper.selectAll(startRow,limit);
    }

    @Override
    public List<TravelVO> searchList(int cpage, int limit, String searchKey, String searchWord) {
        log.info("searchList()....");
        log.info("cpage: " + cpage);
        log.info("limit: " + limit);
        log.info("searchKey: " + searchKey);
        log.info("searchWord: " + searchWord);

        //cpage,limit를 이용해서 시작행을 구합니다.
        int startRow = (cpage - 1) * limit;//0,5,10,,,,

        if(searchKey.equals("district")) {
            return travelMapper.searchListDistrict(startRow,limit,"%"+searchWord+"%");
        }else{
            return travelMapper.searchListAddress(startRow,limit,"%"+searchWord+"%");
        }
    }

    @Override
    public int getTotalRecords() {
        return travelMapper.getTotalRecords();
    }

    @Override
    public int getSearchDistrictRecords(String searchWord) {
        return travelMapper.getSearchDistrictRecords("%"+searchWord+"%");
    }

    @Override
    public int getSearchAddressRecords(String searchWord) {
        return travelMapper.getSearchAddressRecords("%"+searchWord+"%");
    }


}//end class
