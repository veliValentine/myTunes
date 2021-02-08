package group.demo.dataAccess;

import group.demo.logger.Logger;
import group.demo.models.Song;

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
            logger.logToConsole("artist names returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
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
            logger.logToConsole("song names returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
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
            logger.logToConsole("genres returned from database");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return genres;
    }

    public boolean searchSongsByName(String name, ArrayList<Song> songs) {
        songs.clear();
        boolean success = false;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement =
                    prepareQuery("" +
                            "select TrackId as ID, Track.Name as Name, Track.Composer, A.Title, g.Name as Genre from Track\n" +
                            "   join Album A on A.AlbumId = Track.AlbumId\n" +
                            "   join Genre G on Track.GenreId = G.GenreId\n" +
                            "where Track.Name like ?;");
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            songs.addAll(parseSongSearchResultSet(resultSet));
            if (songs.size() > 0) {
                success = true;
            }
            logger.logToConsole("searched songs successful");
        } catch (Exception e) {
            logger.logToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return success;
    }

    private ArrayList<Song> parseSongSearchResultSet(ResultSet resultSet) throws Exception {
        ArrayList<Song> songs = new ArrayList<>();
        while (resultSet.next()) {
            songs.add(parseSongResultSet(resultSet));
        }
        return songs;
    }

    public Song getSongById(String id) {
        Song song = null;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement =
                    prepareQuery("" +
                            "select TrackId as ID, Track.Name as Name, Track.Composer, A.Title, g.Name as Genre\n" +
                            "from Track\n" +
                            "   join Album A on A.AlbumId = Track.AlbumId\n" +
                            "   join Genre G on Track.GenreId = G.GenreId\n" +
                            "where TrackId = ?;"
                    );
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            song = parseSongResultSet(resultSet);
            logger.logToConsole("get song by id success");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return song;
    }

    private Song parseSongResultSet(ResultSet resultSet) throws Exception {
        return new Song(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("composer"),
                resultSet.getString("title"),
                resultSet.getString("genre")
        );
    }

    private ArrayList<String> getNamesFromDatabase(String table, int amount) throws Exception {
        openConnectionAndLog();
        PreparedStatement preparedStatement =
                prepareQuery("" +
                        "select Name\n" +
                        "from " + table + "\n" +
                        "order by random()\n" +
                        "limit ?;");
        preparedStatement.setInt(1, amount);
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
