package com.example.treningapp

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

data class PrijavaZahtjev(val email: String, val lozinka: String)
data class RegistracijaZahtjev(val email: String, val korisnicko_ime: String, val lozinka: String)
data class KorisnikOdgovor(val id: Int, val email: String, val ime: String)
data class GreskaOdgovor(val greska: String)
data class TreningStavka(val id: Int, val tip_treninga: String, val datum: String, val status: String, val prioritetna_skupina: String?)
data class SerijaOdgovor(val id: Int, val redni_broj: Int, val planirana_tezina: Double?, val planirana_ponavljanja: Int)
data class StavkaOdgovor(val id: Int, val redni_broj: Int, val ishod: String?, val vjezba_id: Int, val naziv_vjezbe: String, val serije: List<SerijaOdgovor>)
data class TreningDetaljiOdgovor(val id: Int, val tip_treninga: String, val datum: String, val status: String, val stavke: List<StavkaOdgovor>)
data class IshodZahtjev(val ishod: String)

data class ZavrsiOdgovor(val zavrseni_trening: TreningStavka?, val sljedeci_trening: TreningStavka?)

data class ProfilZahtjev(
    val cilj: String,
    val visina_cm: Int,
    val tezina_kg: Double,
    val tip_tijela: Int,
    val broj_dana_tjedno: Int
)
data class PrioritetiZahtjev(val misicna_skupina_id: List<Int>)

data class ProfilOdgovor(
    val cilj: String,
    val visina_cm: Int,
    val tezina_kg: Double,
    val tip_tijela: Int,
    val broj_dana_tjedno: Int
)
data class PrioritetOdgovor(val id: Int, val naziv: String)

data class VjezbaPovijestOdgovor(val id: Int, val naziv: String, val misicna_skupina: String)
data class NapredakTocka(val datum: String, val planirana_tezina: Double?, val planirana_ponavljanja: Int)

interface ApiService {
    @POST("api/korisnici/prijava")
    suspend fun prijava(@Body zahtjev: PrijavaZahtjev): Response<KorisnikOdgovor>

    @POST("api/korisnici/registracija")
    suspend fun registracija(@Body zahtjev: RegistracijaZahtjev): Response<KorisnikOdgovor>

    @POST("api/korisnici/{id}/profil")
    suspend fun spremiProfil(@Path("id") id: Int, @Body zahtjev: ProfilZahtjev): Response<Unit>

    @POST("api/korisnici/{id}/prioriteti")
    suspend fun spremiPrioritete(@Path("id") id: Int, @Body zahtjev: PrioritetiZahtjev): Response<Unit>

    @POST("api/korisnici/{id}/generiraj-ciklus")
    suspend fun generirajCiklus(@Path("id") id: Int): Response<Unit>

    @retrofit2.http.GET("api/korisnici/{id}/treninzi")
    suspend fun dohvatiTreninge(@Path("id") id: Int): Response<List<TreningStavka>>

    @retrofit2.http.GET("api/korisnici/{id}/profil")
    suspend fun dohvatiProfil(@Path("id") id: Int): Response<Unit>

    @retrofit2.http.GET("api/treninzi/{id}")
    suspend fun dohvatiDetaljeTreninga(@Path("id") id: Int): Response<TreningDetaljiOdgovor>

    @retrofit2.http.PUT("api/stavke-treninga/{id}/ishod")
    suspend fun postaviIshod(@Path("id") id: Int, @Body zahtjev: IshodZahtjev): Response<Unit>

    @retrofit2.http.POST("api/treninzi/{id}/zavrsi")
    suspend fun zavrsiTrening(@Path("id") id: Int): Response<ZavrsiOdgovor>

    @retrofit2.http.GET("api/korisnici/{id}/profil")
    suspend fun dohvatiProfilPodatke(@Path("id") id: Int): Response<ProfilOdgovor>

    @retrofit2.http.GET("api/korisnici/{id}/prioriteti")
    suspend fun dohvatiPrioritete(@Path("id") id: Int): Response<List<PrioritetOdgovor>>

    @retrofit2.http.GET("api/korisnici/{id}/vjezbe-povijest")
    suspend fun dohvatiVjezbePovijest(@Path("id") id: Int): Response<List<VjezbaPovijestOdgovor>>

    @retrofit2.http.GET("api/korisnici/{id}/napredak/{vjezbaId}")
    suspend fun dohvatiNapredak(@Path("id") id: Int, @Path("vjezbaId") vjezbaId: Int): Response<List<NapredakTocka>>
}

object ApiClient {
    private const val BASE_URL = "http://192.168.1.212:3000/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}