package one.xcorp.aviasales.data.dagger.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import one.xcorp.aviasales.data.dagger.scope.DataScope

@Module
internal class NetworkModule {

    @Provides
    @DataScope
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
}
