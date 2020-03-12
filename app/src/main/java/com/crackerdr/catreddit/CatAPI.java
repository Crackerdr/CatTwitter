package com.crackerdr.catreddit;


import models.Feed;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CatAPI {

    String BASE_URL = "https://www.reddit.com/r/";

    @GET("Catloaf+CatsAreAssholes+CatsInSinks+CatsStandingUp+Catswithjobs+TheCatTrapIsWorking+cats+curledfeetsies+lolcats.rss?limit=1000")
    Call<Feed> getFeed();

}
