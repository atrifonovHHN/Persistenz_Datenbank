package com.example.persistenz_datenbanken.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

// Beispiel für ein einfaches Farbschema
private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF6200EE),
    secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    background = androidx.compose.ui.graphics.Color(0xFFFAFAFA)
)

@Composable
fun PersistenzDatenbankenTheme(
    content: @Composable () -> Unit
) {
    // MaterialTheme mit dem Farbschema und anderen Theme-Parametern
    MaterialTheme(
        colorScheme = LightColors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PersistenzDatenbankenTheme {
        // Hier kannst du deine UI-Komponenten testen
    }
}
