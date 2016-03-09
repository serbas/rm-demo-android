package my.apps.demo.russianmediademo.customview.helper;

import java.util.List;

import my.apps.demo.russianmediademo.customview.PlaylistItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlaylistService {
    @GET("users/{user}/repos")
    Call<List<PlaylistItem>> listRepos(@Path("user") String user);
}