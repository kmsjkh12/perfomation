package com.example.demo.dao;

import com.example.demo.dto.FavoritesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FavoritesMapper {
    public List<FavoritesDTO> readFavorites (FavoritesDTO favoritesDTO);
    public void insertFavorites (FavoritesDTO favoritesDTO);
    public void deleteFavorites (FavoritesDTO favoritesDTO);
}
