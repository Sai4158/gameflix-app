package com.gameflix.service;

import com.gameflix.model.Game;
import com.gameflix.model.User;
import com.gameflix.repository.GameRepository;
import com.gameflix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Game> getAllGames(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return gameRepository.findByUserId(user.getId());
    }

    public Game createGame(Game game, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        game.setUser(user);
        if (game.getPurchaseDate() == null) {
            game.setPurchaseDate(java.time.LocalDate.now().toString());
        }
        return gameRepository.save(game);
    }

    public Game updateGame(Long id, Game gameDetails, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!game.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to game");
        }

        game.setTitle(gameDetails.getTitle());
        game.setGenre(gameDetails.getGenre());
        game.setPlatform(gameDetails.getPlatform());
        game.setStatus(gameDetails.getStatus());

        return gameRepository.save(game);
    }

    public void deleteGame(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!game.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to game");
        }

        gameRepository.delete(game);
    }
}
