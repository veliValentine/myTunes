package group.demo.controllers;

import group.demo.dataAccess.MusicRepository;
import group.demo.logger.Logger;
import group.demo.models.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/song")
public class SongController {
    private final MusicRepository musicRepository = new MusicRepository(new Logger());

    @RequestMapping(method = RequestMethod.GET)
    public String searchPage(Model model, @RequestParam String songName) {
        ArrayList<Song> searchSongsResult = new ArrayList<>();
        boolean success = musicRepository.searchSongsByName(songName.trim(), searchSongsResult);
        if (success && searchSongsResult.size() == 1) {
            return "redirect:/song/" + searchSongsResult.get(0).getId();
        }
        model.addAttribute("success", success);
        model.addAttribute("searchSongs", searchSongsResult);
        model.addAttribute("searchParam", songName);
        model.addAttribute("searchSong", new Song());
        return "searchPage";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String song(Model model, @PathVariable String id) {
        Song song = musicRepository.getSongById(id);
        model.addAttribute("song", song);
        return "songPage";
    }
}
