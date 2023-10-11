package com.example.demo.dao;

import com.example.demo.dto.MovieDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MovieMapper {
    public List<MovieDTO> readinfo ();
    public List<MovieDTO> insertShow(MovieDTO movieDTO);
    public void addGrade(MovieDTO movieDTO);
    public void incAddCount(MovieDTO movieDTO);
    public List<MovieDTO> readSpeGrade(MovieDTO movieDTO);
    public List<MovieDTO> readSpeGrade2(String str);
    public void increment_view(String str, String id);
    public void increment_review(String str, String id);
    public void updateGrade(String grade, String infoid);
}
