package com.example.websocket.games.backgammon.service;

import com.example.websocket.games.backgammon.domain.BoardState;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

@Mapper(mappingControl = DeepClone.class, componentModel = "spring")
public interface CloningMapper {

    CloningMapper INSTANCE = Mappers.getMapper(CloningMapper.class);

    BoardState cloneBoardState(BoardState in);

}
