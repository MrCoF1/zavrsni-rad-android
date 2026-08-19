package com.example.treningapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PovijestScreen(korisnikId: Int) {
    var vjezbe by remember { mutableStateOf<List<VjezbaPovijestOdgovor>>(emptyList()) }
    var odabranaVjezba by remember { mutableStateOf<VjezbaPovijestOdgovor?>(null) }
    var napredak by remember { mutableStateOf<List<NapredakTocka>>(emptyList()) }
    var ucitavanjeVjezbi by remember { mutableStateOf(true) }
    var ucitavanjeNapretka by remember { mutableStateOf(false) }

    LaunchedEffect(korisnikId) {
        val odgovor = ApiClient.instance.dohvatiVjezbePovijest(korisnikId)
        if (odgovor.isSuccessful) {
            vjezbe = odgovor.body() ?: emptyList()
        }
        ucitavanjeVjezbi = false
    }

    LaunchedEffect(odabranaVjezba) {
        val vjezba = odabranaVjezba ?: return@LaunchedEffect
        ucitavanjeNapretka = true
        val odgovor = ApiClient.instance.dohvatiNapredak(korisnikId, vjezba.id)
        if (odgovor.isSuccessful) {
            napredak = odgovor.body() ?: emptyList()
        }
        ucitavanjeNapretka = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text("Tvoj napredak", color = Color.White, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(24.dp))

        if (ucitavanjeVjezbi) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF5A9BD5))
            }
            return@Column
        }

        if (vjezbe.isEmpty()) {
            Text("Još nemaš odrađenih treninga.", color = Color(0xFF9A9A9A))
            return@Column
        }

        val grupirano = vjezbe.groupBy { it.misicna_skupina }

        Column(modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
            grupirano.forEach { (skupina, vjezbeSkupine) ->
                Text(skupina.replaceFirstChar { it.uppercase() }, color = Color(0xFF9A9A9A), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    vjezbeSkupine.forEach { vjezba ->
                        val odabrana = odabranaVjezba?.id == vjezba.id
                        Text(
                            vjezba.naziv,
                            color = if (odabrana) Color.White else Color(0xFF9A9A9A),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (odabrana) Color(0xFF5A9BD5) else Color(0xFF3D3D3D))
                                .clickable { odabranaVjezba = vjezba }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            when {
                odabranaVjezba == null -> {
                    Text("Odaberi vježbu iznad da vidiš graf napretka.", color = Color(0xFF707070), fontSize = 13.sp)
                }
                ucitavanjeNapretka -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF5A9BD5))
                    }
                }
                napredak.size < 2 -> {
                    Text("Treba barem 2 odrađena treninga za graf.", color = Color(0xFF707070), fontSize = 13.sp)
                }
                else -> {
                    GrafNapretka(napredak)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun GrafNapretka(tocke: List<NapredakTocka>) {
    val imaTezinu = tocke.any { it.planirana_tezina != null }

    val vrijednosti: List<Double>
    val naslov: String
    val jedinica: String

    if (imaTezinu) {
        vrijednosti = tocke.mapNotNull { it.planirana_tezina }
        naslov = "Težina kroz vrijeme (kg)"
        jedinica = "kg"
    } else {
        vrijednosti = tocke.map { it.planirana_ponavljanja.toDouble() }
        naslov = "Broj ponavljanja kroz vrijeme"
        jedinica = "ponavljanja"
    }

    if (vrijednosti.size < 2) {
        Text("Nema dovoljno podataka za graf.", color = Color(0xFF707070), fontSize = 13.sp)
        return
    }

    val minVrijednost = vrijednosti.min()
    val maxVrijednost = vrijednosti.max()
    val raspon = (maxVrijednost - minVrijednost).takeIf { it > 0 } ?: 1.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF3D3D3D))
            .padding(20.dp)
    ) {
        Text(naslov, color = Color.White, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            val sirinaKoraka = size.width / (vrijednosti.size - 1)
            val putanja = androidx.compose.ui.graphics.Path()

            vrijednosti.forEachIndexed { index, vr ->
                val x = index * sirinaKoraka
                val normalizirano = ((vr - minVrijednost) / raspon).toFloat()
                val y = size.height - (normalizirano * size.height)
                if (index == 0) putanja.moveTo(x, y) else putanja.lineTo(x, y)
                drawCircle(color = Color(0xFF5A9BD5), radius = 6f, center = Offset(x, y))
            }

            drawPath(putanja, color = Color(0xFF5A9BD5), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f, cap = StrokeCap.Round))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${tocke.first().datum}", color = Color(0xFF9A9A9A), fontSize = 12.sp)
            Text("${tocke.last().datum}", color = Color(0xFF9A9A9A), fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("${minVrijednost.toInt()} → ${maxVrijednost.toInt()} $jedinica", color = Color(0xFF5A9BD5), fontSize = 13.sp)
    }
}