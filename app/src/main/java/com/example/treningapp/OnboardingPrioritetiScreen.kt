package com.example.treningapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MisicnaSkupinaOpcija(val id: Int, val naziv: String)

private val SKUPINE = listOf(
    MisicnaSkupinaOpcija(1, "Prsa"),
    MisicnaSkupinaOpcija(2, "Leđa"),
    MisicnaSkupinaOpcija(3, "Ramena"),
    MisicnaSkupinaOpcija(4, "Biceps"),
    MisicnaSkupinaOpcija(5, "Triceps"),
    MisicnaSkupinaOpcija(6, "Quads"),
    MisicnaSkupinaOpcija(7, "Hamstring"),
    MisicnaSkupinaOpcija(8, "Calves")
)

@Composable
fun OnboardingPrioritetiScreen(
    onZavrsi: (prioriteti: List<Int>) -> Unit
) {
    var odabrani by remember { mutableStateOf<List<Int>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3D3D3D))
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Color(0xFF2B2B2B))
                .padding(top = 96.dp, start = 24.dp, end = 24.dp, bottom = 32.dp)
        ) {
            Text("Korak 5 od 5", color = Color(0xFF9A9A9A), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Želiš li dodatno raditi na nekoj skupini?", color = Color.White, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Opcionalno, odaberi do 2", color = Color(0xFF9A9A9A), fontSize = 13.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SKUPINE.chunked(2).forEach { par ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    par.forEach { skupina ->
                        val jeOdabrana = odabrani.contains(skupina.id)
                        Text(
                            skupina.naziv,
                            color = Color.White,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (jeOdabrana) Color(0xFF5A9BD5) else Color(0xFF4A4A4A))
                                .clickable {
                                    odabrani = when {
                                        jeOdabrana -> odabrani - skupina.id
                                        odabrani.size >= 2 -> odabrani
                                        else -> odabrani + skupina.id
                                    }
                                }
                                .padding(vertical = 14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onZavrsi(odabrani) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A9BD5)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(if (odabrani.isEmpty()) "Preskoči" else "Dalje", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPrioritetiScreenPreview() {
    OnboardingPrioritetiScreen(onZavrsi = {})
}
