package group.demo.models;

public class Song {
    private String id;
    private String trackName;
    private String composer;
    private String albumName;
    private String genre;

    public Song() {
    }

    public Song(String id, String trackName, String artist, String album, String genre) {
        this.id = id;
        this.trackName = trackName;
        this.composer = artist;
        this.albumName = album;
        this.genre = genre;
    }

    // Getters and setters
    public String getTrackName() {
        if(trackName == null){
            return "---";
        }
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getComposer() {
        if(composer == null){
            return "---";
        }
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbumName() {
        if(albumName == null){
            return "---";
        }
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getGenre() {
        if(genre == null){
            return "---";
        }
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
