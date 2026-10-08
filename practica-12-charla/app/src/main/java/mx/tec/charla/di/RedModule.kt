package mx.tec.charla.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object RedModule {

    /**
     * Un solo cliente para el WebSocket y para Retrofit.
     *
     * `pingInterval`: cada 20 s OkHttp manda un ping por el WebSocket. Si no
     * vuelve el pong, da la conexión por muerta y avisa con `onFailure`.
     * Sin pings, una conexión que se cayó en silencio —el wifi cambió, el
     * túnel la cerró por inactiva— parecería viva para siempre.
     */
    @Provides
    @Singleton
    fun cliente(): OkHttpClient = OkHttpClient.Builder()
        .pingInterval(20, TimeUnit.SECONDS)
        .build()
}
