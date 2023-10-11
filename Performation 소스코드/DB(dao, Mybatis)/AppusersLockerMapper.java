package com.example.demo.dao;


import com.example.demo.dto.AppusersLockerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppusersLockerMapper {
    public void insertTicket(AppusersLockerDTO appusersLockerDTO);
    public List<AppusersLockerDTO> returnAppusers();
    public List<AppusersLockerDTO> SpeAppusersLocker(AppusersLockerDTO appusersLockerDTO);
    public void deleteUserTicket(AppusersLockerDTO appusersLockerDTO);
}
