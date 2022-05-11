package ru.zav.storedbooksinfo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zav.storedbooksinfo.controller.dto.GenreDto;
import ru.zav.storedbooksinfo.controller.dto.GenreMapper;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.service.GenreService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/genre")
public class GenreController {

    private final GenreService genreService;

    @GetMapping(path = {"", "/all"})
    String getGenres(Model model){
        final List<Genre> genres = genreService.getAll();
        model.addAttribute("genres", genres);
        return "genres_list";
    }

    @GetMapping(path = "/edit")
    String editGenre(@RequestParam String id, Model model){
        final Optional<Genre> byId = genreService.findById(id);
        byId.map(g-> model.addAttribute("genre", g));
        return "genre_edit";
    }

    @PostMapping("/edit")
    public String saveGenre(@ModelAttribute("genre") GenreDto genreDto, Model model) {
        genreService.save(GenreMapper.INSTANCE.toEntity(genreDto));
        return "redirect:/genre/all";
    }
}
