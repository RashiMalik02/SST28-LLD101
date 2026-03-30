package models.entitities;

import java.util.List;

public class Movie {
    private String id;
    private String title;
    private String language;
    private String genre;
    private int durationMinutes;
    private String description;
    private String posterUrl;
    private List<String> castIds;

    public Movie(String id, String title , String language, String genre, int durationMinutes, String description, String posterUrl, List<String> castIds) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.description = description;
        this.posterUrl = posterUrl;
        this.castIds = castIds;
    }
 
    public static Movie create(String id,String title, String language, String genre, int durationMinutes, String description) {
        return Movie.builder()
                .id(id)
                .title(title)
                .language(language)
                .genre(genre)
                .durationMinutes(durationMinutes)
                .description(description)
                .build();
    }
}


