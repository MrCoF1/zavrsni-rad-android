package com.example.treningapp

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.treningapp.R

@Composable
fun OnboardingTipTijelaScreen(
    onNastavi: (tipTijela: Int) -> Unit
) {
    var odabranTip by remember { mutableStateOf<Int?>(null) }

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
            Text("Korak 3 od 5", color = Color(0xFF9A9A9A), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Koji je tvoj tip građe tijela?", color = Color.White, fontSize = 26.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TipTijelaKartica(
                naziv = "Tip 1",
                slika = R.drawable.tip_tijela_1,
                odabran = odabranTip == 1,
                onClick = { odabranTip = 1 }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TipTijelaKartica(
                naziv = "Tip 2",
                slika = R.drawable.tip_tijela_2,
                odabran = odabranTip == 2,
                onClick = { odabranTip = 2 }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TipTijelaKartica(
                naziv = "Tip 3",
                slika = R.drawable.tip_tijela_3,
                odabran = odabranTip == 3,
                onClick = { odabranTip = 3 }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TipTijelaKartica(
                naziv = "Tip 4",
                slika = R.drawable.tip_tijela_4,
                odabran = odabranTip == 4,
                onClick = { odabranTip = 4 }
            )
            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { odabranTip?.let { onNastavi(it) } },
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
private fun TipTijelaKartica(naziv: String, slika: Int, odabran: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (odabran) Color(0xFF5A9BD5) else Color(0xFF4A4A4A))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = slika),
            contentDescription = naziv,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp, 80.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(naziv, fontSize = 17.sp, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingTipTijelaScreenPreview() {
    OnboardingTipTijelaScreen(onNastavi = {})
}