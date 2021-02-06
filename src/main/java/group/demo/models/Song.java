package group.demo.models;

public class Song {
    private String id;
    private String trackName;
    private String artist;
    private String album;
    private String genre;

    public Song() {
    }

    public Song(String id, String trackName, String artist, String album, String genre) {
        this.id = id;
        this.trackName = trackName;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    // Getters and setters
    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
