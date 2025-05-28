package com.emelmujiro.secreto.game.service.impl;

import com.emelmujiro.secreto.game.dto.CreateSystemCharacterRequestDto;
import com.emelmujiro.secreto.game.entity.SystemCharacterColor;
import com.emelmujiro.secreto.game.repository.SystemCharacterColorRepository;
import com.emelmujiro.secreto.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class GameServiceImpl implements GameService {

    private final SystemCharacterColorRepository systemCharacterColorRepository;

    @Override
    public void createSystemCharacter(CreateSystemCharacterRequestDto params) {

        SystemCharacterColor newSystemCharacterColor = SystemCharacterColor.builder()
                .clothesRgbCode(params.getClothesRgbCode())
                .skinRgbCode(params.getSkinRgbCode())
                .url(params.getUrl())
                .build();

        systemCharacterColorRepository.save(newSystemCharacterColor);
    }
}
