package com.example.treningapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingVisinaTezinaScreen(
    onNastavi: (visinaCm: Int, tezinaKg: Double) -> Unit
) {
    var visina by remember { mutableStateOf("") }
    var tezina by remember { mutableStateOf("") }
    var odabranSpol by remember { mutableStateOf("musko") }

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
            Text("Korak 2 od 5", color = Color(0xFF9A9A9A), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Reci nam nešto o sebi", color = Color.White, fontSize = 28.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpolGumb(
                    tekst = "Muško",
                    odabran = odabranSpol == "musko",
                    onClick = { odabranSpol = "musko" },
                    modifier = Modifier.weight(1f)
                )
                SpolGumb(
                    tekst = "Žensko",
                    odabran = odabranSpol == "zensko",
                    onClick = { odabranSpol = "zensko" },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = visina,
                onValueChange = { visina = it.filter { c -> c.isDigit() } },
                label = { Text("Visina (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = poljeBojeVisinaTezina(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = tezina,
                onValueChange = { tezina = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Težina (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = poljeBojeVisinaTezina(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    val visinaInt = visina.toIntOrNull()
                    val tezinaDouble = tezina.toDoubleOrNull()
                    if (visinaInt != null && tezinaDouble != null) {
                        onNastavi(visinaInt, tezinaDouble)
                    }
                },
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
private fun SpolGumb(tekst: String, odabran: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        tekst,
        color = Color.White,
        fontSize = 15.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (odabran) Color(0xFF5A9BD5) else Color(0xFF4A4A4A))
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    )
}

@Composable
private fun poljeBojeVisinaTezina() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color(0xFF9A9A9A),
    unfocusedBorderColor = Color(0xFF5A5A5A),
    focusedLabelColor = Color(0xFF9A9A9A),
    unfocusedLabelColor = Color(0xFF7A7A7A),
    cursorColor = Color.White
)

@Preview(showBackground = true)
@Composable
fun OnboardingVisinaTezinaScreenPreview() {
    OnboardingVisinaTezinaScreen(onNastavi = { _, _ -> })
}