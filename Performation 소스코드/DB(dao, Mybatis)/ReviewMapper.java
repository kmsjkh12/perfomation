package com.example.demo.dao;

import com.example.demo.dto.ApiDTO;
import com.example.demo.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ReviewMapper {
    public void insertreview(ReviewDTO reviewDTO);
    public void updatereview(ReviewDTO reviewDTO);
    public List<ReviewDTO> readreview(ReviewDTO reviewDTO);
    public List<ReviewDTO> SpecificReview(String str);
    public void Push_Appusers_id(String str, String id);
    public void Delete_review(ReviewDTO reviewDTO);
    public void CountUpPush(String id,String push);
    public List<ReviewDTO> allreadreview();
}
