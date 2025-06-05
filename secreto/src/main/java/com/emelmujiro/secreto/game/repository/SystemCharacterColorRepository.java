package com.emelmujiro.secreto.game.repository;

import com.emelmujiro.secreto.game.entity.SystemCharacterColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SystemCharacterColorRepository extends JpaRepository<SystemCharacterColor, Long> {

    @Query("select scc from SystemCharacterColor scc where scc.clothesColor = :clothesColor and scc.skinColor = :skinColor")
    Optional<SystemCharacterColor> findByClothesColorAndSkinColor(@Param("clothesColor") String clothesColorRgb, @Param("skinColor") String skinColor);
}
