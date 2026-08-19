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

@Composable
fun OnboardingBrojDanaScreen(
    onNastavi: (brojDana: Int) -> Unit
) {
    var odabranBrojDana by remember { mutableStateOf<Int?>(null) }

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
            Text("Korak 4 od 5", color = Color(0xFF9A9A9A), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Koliko dana tjedno planiraš trenirati?", color = Color.White, fontSize = 26.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            listOf(2, 3, 4, 5).forEach { broj ->
                BrojDanaKartica(
                    broj = broj,
                    odabran = odabranBrojDana == broj,
                    onClick = { odabranBrojDana = broj }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { odabranBrojDana?.let { onNastavi(it) } },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A9BD5)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Dalje", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun BrojDanaKartica(broj: Int, odabran: Boolean, onClick: () -> Unit) {
    Text(
        "$broj dana tjedno",
        color = Color.White,
        fontSize = 17.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (odabran) Color(0xFF5A9BD5) else Color(0xFF4A4A4A))
            .clickable { onClick() }
            .padding(vertical = 18.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun OnboardingBrojDanaScreenPreview() {
    OnboardingBrojDanaScreen(onNastavi = {})
}
