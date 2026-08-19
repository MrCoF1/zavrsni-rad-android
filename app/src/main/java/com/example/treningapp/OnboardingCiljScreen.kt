package com.example.treningapp

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.treningapp.R

@Composable
fun OnboardingCiljScreen(onCiljOdabran: (String) -> Unit) {
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
                .padding(top = 96.dp, start = 32.dp, end = 24.dp, bottom = 32.dp)
        ) {
            Text("Korak 1 od 5", color = Color(0xFF9A9A9A), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Koji je tvoj glavni cilj?", color = Color.White, fontSize = 28.sp, fontFamily = FontFamily.SansSerif)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CiljKartica(
                naslov = "Mršavljenje",
                opis = "Definicija i gubitak masti",
                slika = R.drawable.cilj_mrsavljenje,
                onClick = { onCiljOdabran("mrsavljenje") }
            )
            Spacer(modifier = Modifier.height(28.dp))
            CiljKartica(
                naslov = "Clean bulk",
                opis = "Postupna izgradnja mišića",
                slika = R.drawable.cilj_cleanbulk,
                onClick = { onCiljOdabran("clean_bulk") }
            )
            Spacer(modifier = Modifier.height(28.dp))
            CiljKartica(
                naslov = "Bulk",
                opis = "Maksimalan rast snage i mase",
                slika = R.drawable.cilj_bulk,
                onClick = { onCiljOdabran("bulk") }
            )
        }
    }
}

@Composable
fun CiljKartica(naslov: String, opis: String, slika: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF4A4A4A))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = slika),
            contentDescription = naslov,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp, 120.dp)
                .clip(RoundedCornerShape(14.dp))
        )
        Spacer(modifier = Modifier.width(18.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(naslov, fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(opis, fontSize = 14.sp, color = Color(0xFFB0B0B0))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingCiljScreenPreview() {
    OnboardingCiljScreen(onCiljOdabran = {})
}