package com.example.treningapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistracijaScreen(
    onRegistracija: (email: String, korisnickoIme: String, lozinka: String) -> Unit,
    onNavigateToPrijava: () -> Unit,
    greska: String? = null
) {
    var email by remember { mutableStateOf("") }
    var korisnickoIme by remember { mutableStateOf("") }
    var lozinka by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3D3D3D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Color(0xFF2B2B2B))
                .padding(horizontal = 28.dp)
                .padding(top = 96.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registriraj se", color = Color.White, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = poljeBoje(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = korisnickoIme,
                onValueChange = { korisnickoIme = it },
                label = { Text("Korisničko ime") },
                singleLine = true,
                colors = poljeBoje(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = lozinka,
                onValueChange = { lozinka = it },
                label = { Text("Lozinka") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = poljeBoje(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onRegistracija(email, korisnickoIme, lozinka) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A9BD5)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Registriraj se", fontSize = 16.sp)
            }
            if (greska != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(greska, color = Color(0xFFE05555), fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Prijavi se",
                color = Color(0xFF9A9A9A),
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToPrijava() }
            )
        }
    }
}

@Composable
private fun poljeBoje() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
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
fun RegistracijaScreenPreview() {
    RegistracijaScreen(onRegistracija = { _, _, _ -> }, onNavigateToPrijava = {})
}