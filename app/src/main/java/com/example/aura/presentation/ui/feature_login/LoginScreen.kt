package com.example.aura.presentation.ui.feature_login


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.core.ResultWrapper
import com.example.aura.di.AppContainer
import com.example.aura.presentation.navigation.destinations.navigateToMainPagerScreenWithPopUpToLoginScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    container: AppContainer,
    navController: NavHostController
){
    val factory = LoginViewModelFactory(container.userUseCases)
    val viewModel: LoginViewModel = viewModel(factory = factory)

    val loginState by viewModel.uiState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is ResultWrapper.Success){
            navController.navigateToMainPagerScreenWithPopUpToLoginScreen()
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollState()
                )
            ,
        ) {
            Text(
                text = "AURA",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 2.dp),
                text = "Acompanhamento Unificado de Resultados e Análises",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(45.dp))

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp))
                    .padding(horizontal = 40.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Digite suas credenciais para Entrar",
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            EmailInput(
                email = email,
                onEmailChange = { email = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            PasswordInput(
                password = password,
                onPasswordChange = { password = it }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {viewModel.login(email, password)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                enabled = !(email.isBlank() || password.isBlank())
            ) {
                Text("Entrar", fontSize = 18.sp)
            }

            when(loginState){
                is ResultWrapper.Loading -> Text("")
                is ResultWrapper.Success -> Text("Bem-vindo")
                is ResultWrapper.Error -> Text("Erro")
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(email: String, onEmailChange: (String) -> Unit){

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = {Text("E-mail")},
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(999.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(password: String, onPasswordChange: (String) -> Unit){
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {Text("Senha")},
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff

            IconButton(onClick = {passwordVisible = !passwordVisible}) {
                Icon(imageVector = image, contentDescription = null)
            }
        },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(999.dp)
    )
}