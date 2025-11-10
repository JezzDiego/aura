package com.example.aura.presentation.ui.feature_settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel? = null) {
    // Support calling without a ViewModel (eg. Preview) by falling back to local states.

    val darkThemeEnabled by if (viewModel != null) viewModel.isDarkMode.collectAsState() else remember { mutableStateOf(false) }
    val cloudBackupEnabled by if (viewModel != null) viewModel.isCloudBackupEnabled.collectAsState() else remember { mutableStateOf(true) }
    val isRefreshing by if (viewModel != null) viewModel.isRefreshing.collectAsState() else remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // pull-to-refresh state
            val pullRefreshState = rememberPullRefreshState(if (viewModel != null) isRefreshing else false, onRefresh = { if (viewModel != null) viewModel.refresh() })

            val scrollState = rememberScrollState()

            Box(modifier = Modifier
                .fillMaxWidth()
                .pullRefresh(pullRefreshState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .border(
                                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                            CircleShape
                                        )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Jessé Oliveira",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Informações Pessoais",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fields
                    ReadOnlyField(label = "Nome Completo", value = "Ana Carolina Silva")
                    Spacer(modifier = Modifier.height(12.dp))
                    ReadOnlyField(label = "Data de Nascimento", value = "25/08/1990")
                    Spacer(modifier = Modifier.height(12.dp))
                    ReadOnlyField(label = "Gênero", value = "Feminino")
                    Spacer(modifier = Modifier.height(12.dp))
                    ReadOnlyField(label = "Convênio Médico", value = "Unimed")

                    Spacer(modifier = Modifier.height(36.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Configurações",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)) {

                            SettingRow(
                                icon = { Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                title = "Gerenciar Assinatura Premium",
                                trailing = {
                                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                                }
                            )

                            HorizontalSpaceDivider()

                            SettingRow(
                                icon = { Icon(Icons.Default.CloudUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                title = "Backup Automático",
                                trailing = {
                                    Switch(
                                        checked = cloudBackupEnabled,
                                        onCheckedChange = { if (viewModel != null) viewModel.setCloudBackup(it) },
                                    )
                                }
                            )

                            HorizontalSpaceDivider()

                            SettingRow(
                                icon = { Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                title = "Tema Escuro",
                                trailing = {
                                    Switch(
                                        checked = darkThemeEnabled,
                                        onCheckedChange = { if (viewModel != null) viewModel.setDarkMode(it) },
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)) {

                            SettingRow(
                                icon = { Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                title = "Notificações",
                                trailing = {
                                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                                }
                            )

                            HorizontalSpaceDivider()

                            SettingRow(
                                icon = { Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                title = "Segurança e Biometria",
                                trailing = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) }
                            )

                            HorizontalSpaceDivider()

                            SettingRow(
                                icon = { Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                title = "Termos e Privacidade",
                                trailing = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(18.dp))

                    Button(
                        onClick = { /* sair da conta */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Sair da Conta", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(58.dp)) // espaço para o FAB
                }

                PullRefreshIndicator(
                    refreshing = if (viewModel != null) isRefreshing else false,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

            FloatingActionButton(
                onClick = { /* editar perfil */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
            }
        }
    }
}

@Composable
fun HorizontalSpaceDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = DividerDefaults.Thickness,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

@Composable
private fun ReadOnlyField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: @Composable () -> Unit,
    title: String,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                )

                Box(modifier = Modifier.size(35.dp), contentAlignment = Alignment.Center) {
                    icon()
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }

        if (trailing != null) {
            Box(contentAlignment = Alignment.Center) {
                trailing()
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    SettingsScreen()
}