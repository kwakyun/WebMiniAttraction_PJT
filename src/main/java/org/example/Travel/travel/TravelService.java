package org.example.Travel.travel;

import java.util.List;


public interface TravelService {
    public int insertOK(TravelVO vo);
    public int updateOK(TravelVO vo);
    public int deleteOK(TravelVO vo);
    public TravelVO selectOne(TravelVO vo);
    public List<TravelVO> selectAll(int cpage, int limit);
    public List<TravelVO> searchList(int cpage, int limit, String searchKey,String searchWord);

    public int getTotalRecords();
    public int getSearchDistrictRecords(String searchWord);
    public int getSearchAddressRecords(String searchWord);


}
