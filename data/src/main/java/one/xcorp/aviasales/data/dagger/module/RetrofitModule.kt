package one.xcorp.aviasales.data.dagger.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import one.xcorp.aviasales.data.CityApiConfiguration
import one.xcorp.aviasales.data.dagger.scope.DataScope
import one.xcorp.aviasales.data.source.retrofit.city.CityRetrofitApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal class RetrofitModule {

    @Provides
    @DataScope
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @DataScope
    fun rxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    fun retrofitBuilder(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(rxJava2CallAdapterFactory)

    @Provides
    @DataScope
    fun cityRetrofitApi(
        configuration: CityApiConfiguration,
        httpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): CityRetrofitApi {
        retrofitBuilder.baseUrl(configuration.url)
        retrofitBuilder.client(httpClientBuilder.build())

        return retrofitBuilder.build().create(CityRetrofitApi::class.java)
    }
}
