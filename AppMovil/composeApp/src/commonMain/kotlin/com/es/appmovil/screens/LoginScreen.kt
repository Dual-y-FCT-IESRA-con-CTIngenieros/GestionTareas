package com.es.appmovil.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.es.appmovil.viewmodel.UserViewmodel
import ctingenierosappmovil.composeapp.generated.resources.LogoCT
import ctingenierosappmovil.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

/**
 * Pantalla del login que permite iniciar sesion a los trabajadores.
 *
 * @param userViewmodel:UserViewmodel viewmodel que guarda los datos del usuario para iniciar sesión.
 */
class LoginScreen(private val userViewmodel: UserViewmodel): Screen {
    @Composable
    override fun Content() {
        // Generamos la navegación actual
        val navigator = LocalNavigator.current
        // Creamos las variables necesarias desde el viewmodel
        val username by userViewmodel.username.collectAsState("")
        val password by userViewmodel.passwordText.collectAsState("")
        val visibility by userViewmodel.visibility.collectAsState(false)
        val login by userViewmodel.login.collectAsState(false)
        val loginError by userViewmodel.loginError.collectAsState(false)
        val loginErrorMesssage by userViewmodel.loginErrorMessage.collectAsState("")

        if (loginError) {
            DialogError(loginErrorMesssage) {userViewmodel.resetError()}
        }

        // Montamos la estructura
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Generamos la imagen del logo de CT
            Image(
                painterResource(Res.drawable.LogoCT),
                contentDescription = "Logo de la empresa"
            )

            // Añadimos los campos a rellenar del usuario y la contraseña
            PedirLogin(username, password, visibility,
                onChangeValue = { user, pass ->
                    userViewmodel.onChangeValue(user, pass)
                },
                onClickIcon = {userViewmodel.onChangeVisibility()}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Si la navegación no es nula, esto es para evitarnos de problemas, aparece el botón y
            // comprueba los campos
            if (navigator != null) {
                Botones {userViewmodel.checkLogin()}
                if (login) {
                    navigator.push(ResumeScreen())
                    userViewmodel.resetVar()
                    userViewmodel.resetError()
                }
            }



        }
    }

    /**
     * Muestra los campos a rellenar del usuario y la contraseña y lo muestra por pantalla.
     *
     * @param username:String el nombre de usuario para iniciar sesión.
     * @param password: String la contraseña del usuario para iniciar sesión.
     * @param visibility:Boolean variable que permite cambiar la visibilidad de la contraseña.
     * @param onChangeValue:(String, String) -> Unit actualiza los campos al escribir.
     * @param onClickIcon:() -> Unit Cambia visualmente el icono de la visibilidad.
     */
    @Composable
    fun PedirLogin(
        username:String,
        password:String,
        visibility:Boolean,
        onChangeValue:(String, String) -> Unit,
        onClickIcon:() -> Unit
    ) {
        // TextField para el username
        OutlinedTextField(
            value = username,
            onValueChange = { onChangeValue(it, password) },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp)) // Separador entre ambos campos
        // TextField para la password
        OutlinedTextField(
            value = password,
            onValueChange = { onChangeValue(username, it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if(!visibility) PasswordVisualTransformation() else VisualTransformation.None, // Ponemos los puntos o mostrarmos el texto
            trailingIcon = { // Dependiendo de si está visible o no cambia el icono
                IconButton(onClick = onClickIcon) {
                    Icon(
                        imageVector = if (visibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Visibility"
                    )
                }
            }
        )
    }

    /**
     * Dialog de error que nos indica el error al iniciar sesión
     *
     * @param errorMessage:String mensaje con el error.
     * @param onDismiss:() -> Unit función lambda que cierra el dialog
     */
    @Composable
    fun DialogError(errorMessage:String,onDismiss:() -> Unit) {
        Dialog(onDismissRequest = onDismiss) { // Creamos el Dialog con la funcionalidad de cerrase
            Card( // Creamos y le damos un estilo bonito a la card
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column( // Creamos la columna donde irán el icono y el mensaje del texto
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon( // Icono de error
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text( // Texto de título de error
                        text = "Error al iniciar sesión",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text( // Texto con el mensaje de error
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = onDismiss) { // Botón para cerrar el Dialog
                        Text("Aceptar")
                    }
                }
            }
        }
    }

    /**
     * Muestra el botón de iniciar sesión y ejecuta la lógica para la comprobación
     * de los campos rellenados.
     *
     * @param onCheckLogin: () -> Boolean función lambda para comprobar los campos y
     * que devuelve true o false.
     */
    @Composable
    fun Botones(onCheckLogin: () -> Unit) {
        // Fila para poner los dos botones
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onCheckLogin() }) {
                Text("Iniciar sesión")
            }
        }
    }


}