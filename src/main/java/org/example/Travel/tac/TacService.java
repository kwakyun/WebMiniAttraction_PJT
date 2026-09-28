package org.example.Travel.tac;


import java.util.List;

public interface TacService {

    public List<TacVO> searchList(String searchWord);
    public String formatRelationTitle(String relationTitle);


}
