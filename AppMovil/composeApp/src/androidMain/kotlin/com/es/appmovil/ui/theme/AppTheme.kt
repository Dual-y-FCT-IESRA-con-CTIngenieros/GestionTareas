package com.es.appmovil.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────
// Colores de la marca
// ─────────────────────────────────────────

/** Naranja corporativo */
val BrandOrange = Color(0xFFF2A900)

/** Blanco (modo claro) */
val BrandLight = Color(0xFFFFFFFF)

/** Gris oscuro (reemplaza blanco en modo oscuro) */
val BrandDark = Color(0xFF707372)

/** Gris pie de página */
val BrandFooter = Color(0xFFA3A4A4)

/** Fondo claro de tarjetas / superficies */
val BrandSurface = Color(0xFFFAFAFA)

/** Gris claro para bordes / placeholders */
val BrandOutline = Color(0xFFBDBDBD)

// ─────────────────────────────────────────
// Paletas de colores Material 3
// ─────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary            = BrandOrange,
    onPrimary          = BrandLight,
    primaryContainer   = Color(0xFFFFE082),   // variante suave del naranja
    onPrimaryContainer = Color(0xFF3E2800),

    secondary          = BrandDark,
    onSecondary        = BrandLight,
    secondaryContainer = Color(0xFFE0E0E0),
    onSecondaryContainer = Color(0xFF1C1C1C),

    background         = BrandLight,
    onBackground       = BrandDark,

    surface            = BrandSurface,
    onSurface          = BrandDark,
    surfaceVariant     = Color(0xFFF5F5F5),
    onSurfaceVariant   = BrandDark,

    outline            = BrandOutline,
    error              = Color(0xFFB00020),
    onError            = BrandLight
)

private val DarkColorScheme = darkColorScheme(
    primary            = BrandOrange,
    onPrimary          = Color(0xFF3E2800),
    primaryContainer   = Color(0xFF5A3D00),
    onPrimaryContainer = Color(0xFFFFDEA0),

    secondary          = BrandDark,
    onSecondary        = BrandLight,
    secondaryContainer = Color(0xFF4A4A4A),
    onSecondaryContainer = BrandLight,

    background         = Color(0xFF1C1C1C),
    onBackground       = BrandLight,

    surface            = Color(0xFF2A2A2A),
    onSurface          = BrandLight,
    surfaceVariant     = Color(0xFF3A3A3A),
    onSurfaceVariant   = Color(0xFFD0D0D0),

    outline            = Color(0xFF707372),
    error              = Color(0xFFCF6679),
    onError            = Color(0xFF690018)
)

// ─────────────────────────────────────────
// Tipografía (Arial → sans-serif nativo)
// ─────────────────────────────────────────
// Android no incluye la fuente "Arial" como recurso embebido (es propiedad de Microsoft).
// FontFamily.SansSerif mapea al sans-serif del sistema (Roboto), que tiene la misma
// métrica y aspecto que Arial en pantallas de alta densidad.

val ArialFontFamily = FontFamily.SansSerif

val AppTypography = Typography(
    // ── Título principal ── (47 sp, naranja)
    displayLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 47.sp,
        color      = BrandOrange
    ),

    // ── Encabezado de sección ── (20 sp, naranja)
    headlineMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 20.sp,
        color      = BrandOrange
    ),
    headlineSmall = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp,
        color      = BrandOrange
    ),
    titleLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp,
        color      = BrandOrange
    ),
    titleMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 18.sp,
        color      = BrandOrange
    ),
    titleSmall = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        color      = BrandOrange
    ),

    // ── Cuerpo ──
    bodyLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp
    ),

    // ── Etiquetas ──
    labelLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp
    ),

    // ── Pie de página ── (8 sp, gris #a3a4a4)
    labelSmall = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 8.sp,
        color      = BrandFooter
    )
)

// ─────────────────────────────────────────
// Tema principal de la app
// ─────────────────────────────────────────

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography,
        content     = content
    )
}

