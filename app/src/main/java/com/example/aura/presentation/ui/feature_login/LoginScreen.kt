package com.example.aura.presentation.ui.feature_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.core.ResultWrapper
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.presentation.navigation.LocalAppActions
import com.example.aura.presentation.navigation.destinations.navigateToMainPagerScreenWithPopUpToLoginScreen
import com.example.aura.presentation.ui.components.AuraLogo
import com.example.aura.presentation.ui.components.Pill
import com.example.aura.presentation.ui.components.PrimaryButton
import com.example.aura.presentation.ui.components.SectionCard
import com.example.aura.presentation.ui.components.SquareIconButton
import com.example.aura.presentation.ui.components.icon

/**
 * Boas-vindas: cria um perfil local (offline) com nome, e-mail opcional e perfil de acessibilidade.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(container: AppContainer, navController: NavHostController) {
    val factory = LoginViewModelFactory(container.userUseCases)
    val viewModel: LoginViewModel = viewModel(factory = factory)
    val form by viewModel.form.collectAsState()
    val loginState by viewModel.uiState.collectAsState()
    val actions = LocalAppActions.current

    LaunchedEffect(loginState) {
        if (loginState is ResultWrapper.Success) {
            navController.navigateToMainPagerScreenWithPopUpToLoginScreen()
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerLow,
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.fillMaxWidth().height(64.dp), verticalAlignment = Alignment.CenterVertically) {
            AuraLogo(height = 40.dp)
            Spacer(Modifier.weight(1f))
            SquareIconButton(
                icon = Icons.Filled.AccessibilityNew,
                contentDescription = "Ajustes de acessibilidade e contraste",
                onClick = actions.openAccessibilitySettings,
                iconSize = 24.dp,
            )
        }

        SectionCard {
            Pill("Bem-vindo(a)", uppercase = true)
            Text(
                "Explore a cidade com autonomia",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                "Crie seu perfil para receber recomendações de locais com acessibilidade verificada pela comunidade.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = form.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Como podemos te chamar?") },
                leadingIcon = { Icon(Icons.Outlined.Person, null) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("E-mail (opcional)") },
                leadingIcon = { Icon(Icons.Outlined.Email, null) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        SectionCard {
            Text(
                "Qual o seu perfil ou recurso principal?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                "Opcional. Usamos para sugerir filtros prioritários — você pode mudar depois no Perfil.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ProfileChips(selected = form.profile, onSelect = viewModel::onProfileChange)
        }

        (loginState as? ResultWrapper.Error)?.let {
            Text(
                it.throwable.message ?: "Não foi possível continuar.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        PrimaryButton(
            text = "Começar a explorar",
            trailingIcon = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = viewModel::login,
            loading = loginState is ResultWrapper.Loading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
        )
        TextButton(onClick = viewModel::continueAsGuest, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Continuar sem cadastro", style = MaterialTheme.typography.labelLarge)
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.WifiOff, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
            Text(
                "Seus dados ficam salvos apenas neste aparelho.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileChips(selected: AccessibilityProfile?, onSelect: (AccessibilityProfile) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        AccessibilityProfile.entries.forEach { profile ->
            FilterChip(
                selected = profile == selected,
                onClick = { onSelect(profile) },
                label = { Text(profile.displayName, style = MaterialTheme.typography.labelLarge) },
                leadingIcon = { Icon(profile.icon, null, modifier = Modifier.size(18.dp)) },
                shape = RoundedCornerShape(8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                    iconColor = MaterialTheme.colorScheme.primary,
                ),
                border = null,
                modifier = Modifier.height(48.dp),
            )
        }
    }
}
