package com.gameflix.controller;

import com.gameflix.model.Game;
import com.gameflix.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames(Authentication authentication) {
        return ResponseEntity.ok(gameService.getAllGames(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody Game game, Authentication authentication) {
        try {
            return ResponseEntity.ok(gameService.createGame(game, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGame(@PathVariable Long id, @RequestBody Game game, Authentication authentication) {
        try {
            return ResponseEntity.ok(gameService.updateGame(id, game, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id, Authentication authentication) {
        try {
            gameService.deleteGame(id, authentication.getName());
            return ResponseEntity.ok(Map.of("message", "Game deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
