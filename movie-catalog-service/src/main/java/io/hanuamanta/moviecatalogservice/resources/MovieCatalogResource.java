package io.hanuamanta.moviecatalogservice.resources;


import io.hanuamanta.moviecatalogservice.models.CatalogItem;
import io.hanuamanta.moviecatalogservice.models.Movie;
import io.hanuamanta.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        //Fetch all the movies rated by user.
        UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);
        return userRating.getUserRatings().stream().map(
                rating -> {
                    //For each movieId get the movie info.
                    Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
                    //Put them all together.
                    return new CatalogItem(movie.getName(), "desc", rating.getRating());
                })
                .collect(Collectors.toList());
    }
}

//      webClientBuilder.build()
//              .get()
//                .uri("")
//                .retrieve()
//                .bodyToMono(Movie.class)
//                .block();