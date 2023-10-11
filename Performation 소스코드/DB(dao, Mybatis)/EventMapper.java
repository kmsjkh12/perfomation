package com.example.demo.dao;

import com.example.demo.dto.EventDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EventMapper {
    public void addEvent(EventDTO eventDTO);
    public List<EventDTO> readEventAll();
}
