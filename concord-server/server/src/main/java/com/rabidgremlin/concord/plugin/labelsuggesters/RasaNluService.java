package com.rabidgremlin.concord.plugin.labelsuggesters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RasaNluService {
	@POST("/parse")
	Call<RasaNluResponse> getSuggestions(@Body RasaNluQuery query);
	}