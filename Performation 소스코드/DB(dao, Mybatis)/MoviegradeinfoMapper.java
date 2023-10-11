package com.example.demo.dao;

import com.example.demo.dto.MovieDTO;
import com.example.demo.dto.MoviegradeinfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MoviegradeinfoMapper {
    public void InsertUser (MovieDTO movieDTO);
    public List<MoviegradeinfoDTO> readMovieGradeInfo(MoviegradeinfoDTO moviegradeinfoDTO);
    public List<MoviegradeinfoDTO> readMovieGradeInfo2(String appusers_id, String info_id);
    public void updateCurrentUserGrade (MoviegradeinfoDTO moviegradeinfoDTO);
    public void DeleteUserGrade(String id);
}
