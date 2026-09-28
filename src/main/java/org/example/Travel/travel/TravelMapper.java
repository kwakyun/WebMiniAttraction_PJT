package org.example.Travel.travel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelMapper {
    public int insertOK(TravelVO vo);

    public int updateOK(TravelVO vo);

    public int deleteOK(TravelVO vo);

    public TravelVO selectOne(TravelVO vo);

    public List<TravelVO> selectAll(int startRow, int limit);

    public List<TravelVO> searchListDistrict(int startRow, int limit, String searchWord);

    public List<TravelVO> searchListAddress(int startRow, int limit, String searchWord);

    public int getTotalRecords();

    public int getSearchDistrictRecords(String searchWord);

    public int getSearchAddressRecords(String searchWord);
}
