package com.example.treningapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun TreningDetaljiScreen(treningId: Int, onZavrsen: () -> Unit) {
    var trening by remember { mutableStateOf<TreningDetaljiOdgovor?>(null) }
    var ucitavanje by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    suspend fun ucitajTrening() {
        val odgovor = ApiClient.instance.dohvatiDetaljeTreninga(treningId)
        if (odgovor.isSuccessful) {
            trening = odgovor.body()
        }
        ucitavanje = false
    }

    LaunchedEffect(treningId) {
        ucitajTrening()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF2B2B2B))
    ) {
        if (ucitavanje) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF5A9BD5))
            }
            return@Column
        }

        val podaci = trening ?: return@Column

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(56.dp))
            Text(podaci.tip_treninga, color = Color.White, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(podaci.datum, color = Color(0xFF9A9A9A), fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF5A9BD5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Točno odrađeno", color = Color(0xFF9A9A9A), fontSize = 13.sp)

                Spacer(modifier = Modifier.width(20.dp))

                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE05555)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Odrađeno manje", color = Color(0xFF9A9A9A), fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        val prvaNeoznacena = podaci.stavke.indexOfFirst { it.ishod == null }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            podaci.stavke.forEachIndexed { index, stavka ->
                val otkljucano = index == 0 || podaci.stavke.getOrNull(index - 1)?.ishod != null

                VjezbaRedak(
                    stavka = stavka,
                    otkljucano = otkljucano,
                    onOznaci = { ishod ->
                        scope.launch {
                            ApiClient.instance.postaviIshod(stavka.id, IshodZahtjev(ishod))
                            ucitajTrening()
                        }
                    }
                )

                if (index < podaci.stavke.size - 1) {
                    androidx.compose.foundation.Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)) {
                        drawLine(
                            color = Color(0xFF4A4A4A),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(size.width, 0f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            val sveOznacene = podaci.stavke.all { it.ishod != null }

            androidx.compose.material3.Button(
                onClick = {
                    scope.launch {
                        val odgovor = ApiClient.instance.zavrsiTrening(treningId)
                        if (odgovor.isSuccessful) {
                            onZavrsen()
                        }
                    }
                },
                enabled = sveOznacene,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5A9BD5),
                    disabledContainerColor = Color(0xFF4A4A4A)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(if (sveOznacene) "Završi trening" else "Označi sve vježbe prvo", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun VjezbaRedak(
    stavka: StavkaOdgovor,
    otkljucano: Boolean,
    onOznaci: (String) -> Unit
) {
    val prvaSerija = stavka.serije.firstOrNull()
    val brojSerija = stavka.serije.size
    val ponavljanja = prvaSerija?.planirana_ponavljanja ?: 0
    val tezina = prvaSerija?.planirana_tezina

    val tekstBoja = if (otkljucano) Color.White else Color(0xFF707070)
    val sivaBoja = Color(0xFF707070)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF3D3D3D))
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(stavka.naziv_vjezbe, color = tekstBoja, fontSize = 17.sp)
            Spacer(modifier = Modifier.height(4.dp))
            val opisTezine = if (tezina != null) " · ${tezina}kg" else ""
            Text("${brojSerija}x${ponavljanja}$opisTezine", color = if (otkljucano) sivaBoja else Color(0xFF5A5A5A), fontSize = 17.sp)
        }

        if (otkljucano) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (stavka.ishod == "tocno") Color(0xFF5A9BD5) else Color(0xFF3D3D3D))
                        .clickable { onOznaci("tocno") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Točno", tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (stavka.ishod == "manje") Color(0xFFE05555) else Color(0xFF3D3D3D))
                        .clickable { onOznaci("manje") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Manje", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}