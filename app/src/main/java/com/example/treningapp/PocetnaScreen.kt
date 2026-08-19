package com.example.treningapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun PocetnaScreen(
    korisnikId: Int,
    onOtvoriTrening: (treningId: Int) -> Unit,
    onPromjeniCiljeve: () -> Unit
) {
    var aktivniTab by remember { mutableStateOf("pocetna") }

    Scaffold(
        containerColor = Color(0xFF2B2B2B),
        bottomBar = {
            DonjaNavigacija(aktivniTab = aktivniTab, onOdaberi = { aktivniTab = it })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (aktivniTab) {
                "pocetna" -> ListaTreningaSadrzaj(korisnikId, onOtvoriTrening)
                "povijest" -> PovijestScreen(korisnikId = korisnikId)
                "profil" -> ProfilScreen(korisnikId = korisnikId, onPromjeniCiljeve = onPromjeniCiljeve)
            }
        }
    }
}

@Composable
private fun ListaTreningaSadrzaj(korisnikId: Int, onOtvoriTrening: (Int) -> Unit) {
    var treninzi by remember { mutableStateOf<List<TreningStavka>>(emptyList()) }
    var ucitavanje by remember { mutableStateOf(true) }
    var greska by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(korisnikId) {
        try {
            val odgovor = ApiClient.instance.dohvatiTreninge(korisnikId)
            if (odgovor.isSuccessful) {
                treninzi = odgovor.body() ?: emptyList()
            } else {
                greska = "Greška pri dohvatu treninga."
            }
        } catch (e: Exception) {
            greska = "Greška u komunikaciji sa serverom."
        }
        ucitavanje = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(54.dp))
        Text("Dobrodošao natrag", color = Color(0xFF9A9A9A), fontSize = 15.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Tvoji treninzi", color = Color.White, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(40.dp))

        when {
            ucitavanje -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF5A9BD5))
                }
            }
            greska != null -> {
                Text(greska ?: "", color = Color(0xFFE05555))
            }
            treninzi.isEmpty() -> {
                Text("Nema planiranih treninga.", color = Color(0xFF9A9A9A))
            }
            else -> {
                treninzi.forEachIndexed { index, trening ->
                    TreningKartica(
                        trening = trening,
                        aktivan = index == 0,
                        onClick = { if (index == 0) onOtvoriTrening(trening.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun TreningKartica(trening: TreningStavka, aktivan: Boolean, onClick: () -> Unit) {
    val pozadinaBoja = if (aktivan) Color(0xFF3D3D3D) else Color(0xFF353535)
    val tekstBoja = if (aktivan) Color.White else Color(0xFF707070)
    val ikonaBoja = if (aktivan) Color(0xFF5A9BD5) else Color(0xFF5A5A5A)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(pozadinaBoja)
            .clickable(enabled = aktivan) { onClick() }
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(ikonaBoja.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = null, tint = ikonaBoja)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(trening.tip_treninga, color = tekstBoja, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text("${danUTjednu(trening.datum)}, ${trening.datum}", color = Color(0xFF707070), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(opisTreninga(trening.tip_treninga), color = Color(0xFF808080), fontSize = 12.sp)
            if (trening.prioritetna_skupina != null) {
                Text("+1 vježba za ${trening.prioritetna_skupina}", color = Color(0xFF5A9BD5), fontSize = 12.sp)
            }
        }
        if (aktivan) {
            Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF9A9A9A))
        } else {
            Text("Zaključano", color = Color(0xFF707070), fontSize = 12.sp)
        }
    }
}

@Composable
private fun PlaceholderSadrzaj(naslov: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(naslov, color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Uskoro dostupno", color = Color(0xFF9A9A9A), fontSize = 14.sp)
        }
    }
}

@Composable
private fun DonjaNavigacija(aktivniTab: String, onOdaberi: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2B2B2B))
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NavStavka("pocetna", Icons.Filled.Home, "Početna", aktivniTab, onOdaberi)
        NavStavka("povijest", Icons.Filled.History, "Povijest", aktivniTab, onOdaberi)
        NavStavka("profil", Icons.Filled.Person, "Profil", aktivniTab, onOdaberi)
    }
}

@Composable
private fun NavStavka(
    kljuc: String,
    ikona: androidx.compose.ui.graphics.vector.ImageVector,
    naziv: String,
    aktivniTab: String,
    onOdaberi: (String) -> Unit
) {
    val aktivan = aktivniTab == kljuc
    val boja = if (aktivan) Color(0xFF5A9BD5) else Color(0xFF9A9A9A)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onOdaberi(kljuc) }
    ) {
        Icon(imageVector = ikona, contentDescription = naziv, tint = boja)
        Spacer(modifier = Modifier.height(4.dp))
        Text(naziv, color = boja, fontSize = 11.sp)
    }
}
private fun danUTjednu(datumStr: String): String {
    return try {
        val (godina, mjesec, dan) = datumStr.split("-").map { it.toInt() }
        val kalendar = java.util.GregorianCalendar(godina, mjesec - 1, dan)
        val nazivi = listOf("Nedjelja", "Ponedjeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota")
        nazivi[kalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
    } catch (e: Exception) {
        ""
    }
}
private fun opisTreninga(tipTreninga: String): String = when (tipTreninga) {
    "Upper" -> "Trening gornjeg dijela tijela koji uključuje prsa, leđa, ramena i bicepse."
    "Lower" -> "Trening donjeg dijela tijela koji uključuje kvadricepse, zadnju ložu i listove."
    "Push" -> "Trening svih mišića koji sudjeluju u pokretima guranja: prsa, ramena i triceps."
    "Pull" -> "Trening svih mišića koji sudjeluju u povlačenju: leđa, biceps i stražnje rame."
    "Legs" -> "Trening fokusiran na kompletne noge: kvadricepse, zadnju ložu i listove."
    "Full Body" -> "Trening cijelog tijela u jednoj sesiji, kombinirajući vježbe za gornji i donji dio tijela."
    else -> ""
}