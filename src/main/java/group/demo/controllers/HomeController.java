package group.demo.controllers;

import group.demo.dataAccess.MusicRepository;
import group.demo.logger.Logger;
import group.demo.models.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/")
public class HomeController {
    private final MusicRepository musicRepository = new MusicRepository(new Logger());

    //Homepage
    @RequestMapping(method = RequestMethod.GET)
    public String homepage(Model model) {
        // add 5 random song, artist and genres to homepage
        model.addAttribute("randomSongs", musicRepository.randomSongNames(5));
        model.addAttribute("randomArtists", musicRepository.randomArtistsNames(5));
        model.addAttribute("randomGenres", musicRepository.randomGenres(5));
        // using song object to fill search parameters.
        // This way it should be easier to extend searching by artist and/or composer
        model.addAttribute("searchSong", new Song());
        return "index";
    }

    // Search song by name
    @RequestMapping(method = RequestMethod.POST)
    public String searchSongByName(@ModelAttribute Song searchSong) {
        return "redirect:/song?songName=" + searchSong.getTrackName();
    }
}
