package group.demo.dataAccess;

import group.demo.logger.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MusicRepository extends Repository {
    private final String ARTIST_TABLE_NAME = "Artist";
    private final String TRACK_TABLE_NAME = "Track";
    private final String GENRE_TABLE_NAME = "Genre";

    public MusicRepository(Logger logger) {
        super(logger);
    }

    public ArrayList<String> randomArtistsNames(int amountOfArtists) {
        ArrayList<String> artistNames = null;
        if (amountOfArtists < 1) {
            logger.errorToConsole("invalid amount of artists: " + amountOfArtists);
            return null;
        }
        try {
            artistNames = getNamesFromDatabase(ARTIST_TABLE_NAME, amountOfArtists);
            logger.logToConsole("\t" + artistNames.size() + " artist names returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return artistNames;
    }

    public ArrayList<String> randomSongNames(int amountOfSongs) {
        ArrayList<String> songNames = null;
        if (amountOfSongs < 1) {
            logger.errorToConsole("invalid amount of songs: " + amountOfSongs);
            return null;
        }
        try {
            songNames = getNamesFromDatabase(TRACK_TABLE_NAME, amountOfSongs);
            logger.logToConsole("\t" + songNames.size() + " song names returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return songNames;
    }

    public ArrayList<String> randomGenres(int amountOfGenres) {
        ArrayList<String> genres = null;
        if (amountOfGenres < 1) {
            logger.errorToConsole("invalid amount of genres: " + amountOfGenres);
            return null;
        }
        try {
            genres = getNamesFromDatabase(GENRE_TABLE_NAME, amountOfGenres);
            logger.logToConsole("\t" + genres.size() + " genres returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return genres;
    }

    //public Song getSongByName(Song song)

    private ArrayList<String> getNamesFromDatabase(String table, int amount) throws Exception {
        openConnectionAndLog();
        PreparedStatement preparedStatement =
                prepareQuery("""
                        select Name
                        from ?
                        order by random()
                        limit ?;
                        """);
        preparedStatement.setString(1, table);
        preparedStatement.setInt(2, amount);
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
