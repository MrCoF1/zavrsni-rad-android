package com.example.treningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.treningapp.ui.theme.TreningAppTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TreningAppTheme {
                AppNavigacija()
            }
        }
    }
}

@Composable
fun AppNavigacija() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var greska by remember { mutableStateOf<String?>(null) }
    var korisnikId by remember { mutableStateOf<Int?>(null) }
    var odabraniCilj by remember { mutableStateOf<String?>(null) }
    var visinaCm by remember { mutableStateOf<Int?>(null) }
    var tezinaKg by remember { mutableStateOf<Double?>(null) }
    var tipTijela by remember { mutableStateOf<Int?>(null) }
    var brojDanaTjedno by remember { mutableStateOf<Int?>(null) }

    NavHost(navController = navController, startDestination = "prijava") {

        composable("prijava") {
            AuthScreen(
                onPrijava = { email, lozinka ->
                    scope.launch {
                        try {
                            val odgovor = ApiClient.instance.prijava(PrijavaZahtjev(email, lozinka))
                            if (odgovor.isSuccessful) {
                                korisnikId = odgovor.body()?.id
                                greska = null
                                val id = korisnikId
                                if (id != null) {
                                    val profilOdgovor = ApiClient.instance.dohvatiProfil(id)
                                    if (profilOdgovor.isSuccessful) {
                                        navController.navigate("pocetna")
                                    } else {
                                        navController.navigate("onboarding_cilj")
                                    }
                                }
                            } else {
                                greska = "Neispravan email ili lozinka."
                            }
                        } catch (e: Exception) {
                            greska = "Greška u komunikaciji sa serverom: ${e.message}"
                        }
                    }
                },
                onGooglePrijava = { },
                onNavigateToRegistracija = { navController.navigate("registracija") },
                greska = greska
            )
        }

        composable("registracija") {
            RegistracijaScreen(
                onRegistracija = { email, korisnickoIme, lozinka ->
                    scope.launch {
                        try {
                            val odgovor = ApiClient.instance.registracija(
                                RegistracijaZahtjev(email, korisnickoIme, lozinka)
                            )
                            if (odgovor.isSuccessful) {
                                korisnikId = odgovor.body()?.id
                                greska = null
                                navController.navigate("onboarding_cilj")
                            } else {
                                greska = "Registracija nije uspjela."
                            }
                        } catch (e: Exception) {
                            greska = "Greška u komunikaciji sa serverom: ${e.message}"
                        }
                    }
                },
                onNavigateToPrijava = { navController.navigate("prijava") },
                greska = greska
            )
        }

        composable("onboarding_cilj") {
            OnboardingCiljScreen(
                onCiljOdabran = { cilj ->
                    odabraniCilj = cilj
                    navController.navigate("onboarding_visina_tezina")
                }
            )
        }

        composable("onboarding_visina_tezina") {
            OnboardingVisinaTezinaScreen(
                onNastavi = { visina, tezina ->
                    visinaCm = visina
                    tezinaKg = tezina
                    navController.navigate("onboarding_tip_tijela")
                }
            )
        }

        composable("onboarding_tip_tijela") {
            OnboardingTipTijelaScreen(
                onNastavi = { tip ->
                    tipTijela = tip
                    navController.navigate("onboarding_broj_dana")
                }
            )
        }

        composable("onboarding_broj_dana") {
            OnboardingBrojDanaScreen(
                onNastavi = { brojDana ->
                    brojDanaTjedno = brojDana
                    navController.navigate("onboarding_prioriteti")
                }
            )
        }
        composable("onboarding_prioriteti") {
            OnboardingPrioritetiScreen(
                onZavrsi = { prioriteti ->
                    scope.launch {
                        try {
                            val id = korisnikId ?: return@launch
                            val cilj = odabraniCilj ?: return@launch
                            val visina = visinaCm ?: return@launch
                            val tezina = tezinaKg ?: return@launch
                            val tip = tipTijela ?: return@launch
                            val dana = brojDanaTjedno ?: return@launch

                            val profilOdgovor = ApiClient.instance.spremiProfil(
                                id,
                                ProfilZahtjev(cilj, visina, tezina, tip, dana)
                            )
                            if (!profilOdgovor.isSuccessful) {
                                greska = "Greška pri spremanju profila."
                                return@launch
                            }

                            if (prioriteti.isNotEmpty()) {
                                ApiClient.instance.spremiPrioritete(id, PrioritetiZahtjev(prioriteti))
                            }

                            ApiClient.instance.generirajCiklus(id)

                            navController.navigate("pocetna")
                        } catch (e: Exception) {
                            greska = "Greška u komunikaciji sa serverom: ${e.message}"
                        }
                    }
                }
            )
        }

        composable("pocetna") {
            korisnikId?.let { id ->
                PocetnaScreen(
                    korisnikId = id,
                    onOtvoriTrening = { treningId ->
                        navController.navigate("trening_detalji/$treningId")
                    },
                    onPromjeniCiljeve = {
                        navController.navigate("onboarding_cilj")
                    }
                )
            }
        }

        composable(
            "trening_detalji/{treningId}",
            arguments = listOf(navArgument("treningId") { type = NavType.IntType })
        ) { backStackEntry ->
            val treningId = backStackEntry.arguments?.getInt("treningId") ?: return@composable
            TreningDetaljiScreen(
                treningId = treningId,
                onZavrsen = { navController.navigate("pocetna") }
            )
        }
    }
}