package com.example.aura.presentation.ui.feature_add_exam

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.Category
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExamScreen(container: AppContainer, navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var date by remember { mutableStateOf("") }
    var attachedFileUri by remember { mutableStateOf<Uri?>(null) }
    var attachedFileName by remember { mutableStateOf<String?>(null) }
    var attachedFileSize by remember { mutableStateOf<String?>(null) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    val factory = AddExamViewModelFactory(container)
    val viewModel: AddExamViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val labs by viewModel.labs.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var selectedLab by remember { mutableStateOf<com.example.aura.domain.model.Laboratory?>(null) }
    var labMenuExpanded by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            attachedFileUri = it
            context.contentResolver.query(it, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                attachedFileName = cursor.getString(nameIndex)
                val size = cursor.getLong(sizeIndex)
                attachedFileSize = "${String.format(Locale.getDefault(), "%.1f", size / (1024.0 * 1024.0))} MB"
            }
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AddExamUiState.Success -> {
                showSuccessMessage = true
                delay(2000) // Tempo para exibir a mensagem de sucesso
                navController.popBackStack()
            }
            is AddExamUiState.Error -> snackbarHostState.showSnackbar(message = state.throwable.localizedMessage ?: "Ocorreu um erro")
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Adicionar Exame", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {

            // Botão para abrir o seletor de arquivos
            TextButton(
                onClick = { filePickerLauncher.launch(arrayOf("application/pdf", "image/*")) },
                modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.FileUpload, contentDescription = "Upload", modifier = Modifier.padding(end = 8.dp))
                Text(text = "Selecionar arquivo (PDF/Imagem)")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Nome do exame") },
                placeholder = { Text("Ex: Hemograma completo") },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is AddExamUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Dropdown para selecionar o tipo de exame
                ExposedDropdownMenuBox(
                    expanded = categoryMenuExpanded,
                    onExpandedChange = { categoryMenuExpanded = !categoryMenuExpanded },
                    modifier = Modifier.weight(1.2f)
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.displayName ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        placeholder = { Text("Selecione") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = categoryMenuExpanded, onDismissRequest = { categoryMenuExpanded = false }) {
                        Category.entries.sortedBy { it.displayName }.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    selectedCategory = category
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Campo de data que abre o calendário
                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Data") },
                    placeholder = { Text("DD/MM/AAAA") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Selecionar data")
                        }
                    },
                    modifier = Modifier.weight(0.9f),
                    enabled = uiState !is AddExamUiState.Loading
                )
            }

            // Dropdown para selecionar laboratório
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(
                expanded = labMenuExpanded,
                onExpandedChange = { labMenuExpanded = !labMenuExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedLab?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Laboratório") },
                    placeholder = { Text("Selecione um laboratório (opcional)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = labMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    enabled = uiState !is AddExamUiState.Loading
                )
                ExposedDropdownMenu(expanded = labMenuExpanded, onDismissRequest = { labMenuExpanded = false }) {
                    // Mostrar item vazio para "nenhum"
                    DropdownMenuItem(text = { Text("Nenhum") }, onClick = {
                        selectedLab = null
                        labMenuExpanded = false
                    })
                    labs.forEach { lab ->
                        DropdownMenuItem(text = { Text(lab.name) }, onClick = {
                            selectedLab = lab
                            labMenuExpanded = false
                        })
                    }
                }
            }

            // Calendário para selecionar a data
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                                        timeZone = TimeZone.getTimeZone("UTC")
                                    }
                                    date = sdf.format(Date(millis))
                                }
                                showDatePicker = false
                            }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // Exibição do arquivo anexado
            if (attachedFileName != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = attachedFileName!!, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                            attachedFileSize?.let { size -> Text(text = size, style = MaterialTheme.typography.bodySmall) }
                        }
                        IconButton(onClick = {
                            attachedFileUri = null
                            attachedFileName = null
                            attachedFileSize = null
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Remover arquivo")
                        }
                    }
                }
            }

            // Espaço central que empurra a mensagem e o botão para baixo
            Spacer(Modifier.weight(1f))

            // Mensagem de sucesso
            AnimatedVisibility(visible = showSuccessMessage) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(
                        text = "Exame salvo com sucesso!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Botão de Salvar
            Button(
                onClick = { if (selectedCategory != null) { viewModel.createExam(title, selectedCategory!!, date, selectedLab) } },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = uiState !is AddExamUiState.Loading && title.isNotBlank() && selectedCategory != null && date.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (uiState is AddExamUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(text = "Salvar exame", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}