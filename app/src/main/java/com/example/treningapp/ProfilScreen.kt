package com.example.treningapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun nazivCilja(cilj: String): String = when (cilj) {
    "mrsavljenje" -> "Mršavljenje"
    "bulk" -> "Bulk"
    "clean_bulk" -> "Clean bulk"
    else -> cilj
}

@Composable
fun ProfilScreen(
    korisnikId: Int,
    onPromjeniCiljeve: () -> Unit
) {
    var profil by remember { mutableStateOf<ProfilOdgovor?>(null) }
    var prioriteti by remember { mutableStateOf<List<PrioritetOdgovor>>(emptyList()) }
    var ucitavanje by remember { mutableStateOf(true) }

    LaunchedEffect(korisnikId) {
        try {
            val profilOdgovor = ApiClient.instance.dohvatiProfilPodatke(korisnikId)
            if (profilOdgovor.isSuccessful) {
                profil = profilOdgovor.body()
            }
            val prioritetiOdgovor = ApiClient.instance.dohvatiPrioritete(korisnikId)
            if (prioritetiOdgovor.isSuccessful) {
                prioriteti = prioritetiOdgovor.body() ?: emptyList()
            }
        } catch (e: Exception) {
            // ostavi profil = null, prikazat ćemo prazno stanje
        }
        ucitavanje = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text("Tvoj profil", color = Color.White, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(28.dp))

        if (ucitavanje) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF5A9BD5))
            }
            return@Column
        }

        val p = profil
        if (p == null) {
            Text("Profil nije pronađen.", color = Color(0xFF9A9A9A))
            return@Column
        }

        PodatakRedak("Cilj", nazivCilja(p.cilj))
        PodatakRedak("Visina", "${p.visina_cm} cm")
        PodatakRedak("Težina", "${p.tezina_kg} kg")
        PodatakRedak("Dana treninga tjedno", "${p.broj_dana_tjedno}")

        if (prioriteti.isNotEmpty()) {
            val nazivi = prioriteti.joinToString(", ") { it.naziv }
            PodatakRedak("Prioritetne skupine", nazivi)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onPromjeniCiljeve,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A9BD5)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Promijeni ciljeve", fontSize = 16.sp)
        }
    }
}

@Composable
private fun PodatakRedak(naziv: String, vrijednost: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF3D3D3D))
            .padding(16.dp)
    ) {
        Text(naziv, color = Color(0xFF9A9A9A), fontSize = 13.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(vrijednost, color = Color.White, fontSize = 17.sp)
    }
    Spacer(modifier = Modifier.height(12.dp))
}