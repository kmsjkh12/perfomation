package com.example.demo.dao;

import com.example.demo.dto.ApiDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ApiMapper {
    public List<ApiDTO> readuser(ApiDTO apiDTO);
    public List<ApiDTO> readappuser(String id);
    public List<ApiDTO> readuserAll();
    public void userInsert(ApiDTO apiDTO);
    public List<ApiDTO> searchUser(ApiDTO apiDTO);
    public void changeusername(ApiDTO apiDTO);
    public void changepassword(ApiDTO apiDTO);
    public void decPrice(String price, String id);
}
