package group.demo.dataAccess;

import group.demo.logger.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RandomMusicRepository extends Repository {

    public RandomMusicRepository(Logger logger) {
        super(logger);
    }

    public ArrayList<String> randomArtistsNames(int amountOfArtists) {
        ArrayList<String> artistNames = null;
        if (amountOfArtists < 1) {
            logger.errorToConsole("invalid amount of artists: " + amountOfArtists);
            return artistNames;
        }
        try {
            artistNames = getNamesFromDatabase("Artist", amountOfArtists);
            logger.logToConsole("\t" + artistNames.size() + " artist names returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return artistNames;
    }

    public ArrayList<String> randomSongNames(int amountOfSongs){
        ArrayList<String> songNames = null;
        if (amountOfSongs < 1) {
            logger.errorToConsole("invalid amount of songs: " + amountOfSongs);
            return songNames;
        }
        try {
            songNames = getNamesFromDatabase("Track", amountOfSongs);
            logger.logToConsole("\t" + songNames.size() + " song names returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return songNames;
    }

    public ArrayList<String> randomGenres(int amountOfGenres){
        ArrayList<String> genres = null;
        if (amountOfGenres < 1) {
            logger.errorToConsole("invalid amount of genres: " + amountOfGenres);
            return genres;
        }
        try {
            genres = getNamesFromDatabase("Genre", amountOfGenres);
            logger.logToConsole("\t" + genres.size() + " genres returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return genres;
    }

    private ArrayList<String> getNamesFromDatabase(String table, int number) throws Exception {
        openConnectionAndLog();
        PreparedStatement preparedStatement =
                prepareQuery("" +
                        "select Name from "
                        + table +
                        "order by random() limit ?;");
        preparedStatement.setInt(1, number);
        ResultSet resultSet = preparedStatement.executeQuery();
        return parseNames(resultSet);
    }

    private ArrayList<String> parseNames(ResultSet resultSet) throws Exception {
        ArrayList<String> artistNames = new ArrayList<>();
        while (resultSet.next()) {
            artistNames.add(resultSet.getString("Name"));
        }
        return artistNames;
    }
}
