package group.demo.controllers;

import group.demo.dataAccess.CustomerRepository;
import group.demo.dataAccess.MusicRepository;
import group.demo.logger.Logger;
import group.demo.models.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class HomeController {
    private final Logger logger = new Logger();
    private final CustomerRepository customerRepository = new CustomerRepository(logger);
    private final MusicRepository musicRepository = new MusicRepository(logger);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("randomSongs", musicRepository.randomSongNames(5));
        model.addAttribute("randomArtists", musicRepository.randomArtistsNames(5));
        model.addAttribute("randomGenres", musicRepository.randomGenres(5));
        // using song object to fill search parameters.
        // This way it should be easier to extend searching by artist or/and composer
        model.addAttribute("searchSong", new Song());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String search(Model model, @ModelAttribute Song searchSong) {
        return "redirect:/result?songName=" + searchSong.getTrackName();
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String searchPage(Model model, @RequestParam String songName) {
        ArrayList<Song> searchSongsResult = new ArrayList<>();
        boolean success = musicRepository.searchSongsByName(songName.trim(), searchSongsResult);
        if(success && searchSongsResult.size() == 1){
            return "redirect:/song/" + searchSongsResult.get(0).getId();
        }
        model.addAttribute("success", success);
        model.addAttribute("searchSongs", searchSongsResult);
        model.addAttribute("searchSong", new Song());
        model.addAttribute("searchParam", songName);
        return "searchPage";
    }

    @RequestMapping(value = "/song/{id}", method = RequestMethod.GET)
    public String song(Model model, @PathVariable String id){
        Song song = musicRepository.getSongById(id);
        model.addAttribute("song", song);
        return "songPage";
    }
}
