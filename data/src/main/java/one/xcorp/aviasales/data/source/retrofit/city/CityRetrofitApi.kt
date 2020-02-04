package one.xcorp.aviasales.data.source.retrofit.city

import io.reactivex.Single
import one.xcorp.aviasales.data.source.retrofit.city.response.AutocompleteResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CityRetrofitApi {

    @GET(METHOD_AUTOCOMPLETE)
    fun autocomplete(
        @Query("term") term: String,
        @Query("lang") lang: String
    ): Single<AutocompleteResponse>

    companion object {

        private const val METHOD_AUTOCOMPLETE = "autocomplete"
    }
}
