package com.example.doevida.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.doevida.model.DoacaoManual
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.AzureStorageService
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import com.example.doevida.util.UserDataManager
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private fun createTempImageFile(context: Context): Uri {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaRegistrarDoacao(navController: NavController) {
    var hospitais by remember { mutableStateOf<List<HospitaisCards>>(emptyList()) }
    var selectedHospital by remember { mutableStateOf<HospitaisCards?>(null) }
    var donationDate by rememberSaveable { mutableStateOf("") }
    var observacao by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var tempImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    var isLoadingHospitais by remember { mutableStateOf(true) }
    var isProcessing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // Busca o ID do usuário logado
    val userId = UserDataManager.getUserId(context)

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitFactory(context).getHospitalService().getHospitais()
            if (response.isSuccessful) {
                hospitais = response.body()?.hospitais ?: emptyList()
            } else {
                Toast.makeText(context, "Erro ao carregar hospitais.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoadingHospitais = false
        }
    }

    fun uploadImage(uri: Uri) {
        coroutineScope.launch {
            if (isProcessing) return@launch
            isProcessing = true
            imageUri = uri
            Toast.makeText(context, "Enviando imagem...", Toast.LENGTH_SHORT).show()

            val resultUrl = AzureStorageService.uploadImageToAzure(context, uri)

            if (resultUrl != null) {
                uploadedImageUrl = resultUrl
                println("✅ URL da imagem no Azure: $resultUrl")
                Toast.makeText(context, "Imagem enviada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                imageUri = null
                Toast.makeText(context, "Falha no upload da imagem.", Toast.LENGTH_SHORT).show()
            }
            isProcessing = false
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { uploadImage(it) }
        showSheet = false
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            tempImageUri?.let { uploadImage(it) }
        }
        showSheet = false
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            val newUri = createTempImageFile(context)
            tempImageUri = newUri
            cameraLauncher.launch(newUri)
        } else {
            Toast.makeText(context, "Permissão da câmera negada.", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleCamera() {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                val newUri = createTempImageFile(context)
                tempImageUri = newUri
                cameraLauncher.launch(newUri)
            }
            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Escolha uma opção", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { if (!isProcessing) galleryLauncher.launch("image/*") }, enabled = !isProcessing) {
                        Text("Galeria")
                    }
                    Button(onClick = { if (!isProcessing) handleCamera() }, enabled = !isProcessing) {
                        Text("Câmera")
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = { TopBarRegistrarDoacao(navController) },
        bottomBar = {
            BottomBarRegistrarDoacao(
                isEnabled = selectedHospital != null && donationDate.isNotBlank() && uploadedImageUrl != null && !isLoadingHospitais,
                isBusy = isProcessing
            ) {
                // Inclui o userId ao criar a doação manual
                val donation = DoacaoManual(
                    userId = userId,
                    hospitalName = selectedHospital!!.nomeHospital,
                    donationDate = donationDate,
                    proofImageUrl = uploadedImageUrl!!,
                    observacao = observacao.takeIf { it.isNotBlank() }
                )
                SharedPreferencesUtils.saveManualDonation(context, donation)
                Toast.makeText(context, "Doação registrada com sucesso!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        },
        containerColor = Color(0xFFF7F7F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            RegistrarDoacaoStep(1, "Selecione o local da doação") {
                if (isLoadingHospitais) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 50.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF990410))
                    }
                } else {
                    HospitalSelection(hospitais = hospitais, selected = selectedHospital) { hospital ->
                        selectedHospital = hospital
                    }
                }
            }

            RegistrarDoacaoStep(2, "Informe os detalhes da doação", isVisible = selectedHospital != null) {
                DateField(date = donationDate) { updatedDate -> donationDate = updatedDate }
                Spacer(modifier = Modifier.height(16.dp))
                ImageUpload(imageUri = imageUri, isUploading = isProcessing) {
                    if (!isProcessing) showSheet = true
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it },
                    label = { Text("Observação (opcional)") },
                    placeholder = { Text("Como você se sentiu? Alguma nota especial?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
fun RegistrarDoacaoStep(stepNumber: Int, title: String, isVisible: Boolean = true, content: @Composable () -> Unit) {
    if (!isVisible) return
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Passo $stepNumber", color = Color(0xFF990410), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun HospitalSelection(hospitais: List<HospitaisCards>, selected: HospitaisCards?, onSelect: (HospitaisCards) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(hospitais) { hospital ->
            val isSelected = selected?.id == hospital.id
            Card(
                modifier = Modifier.width(280.dp).clickable { onSelect(hospital) },
                shape = RoundedCornerShape(12.dp),
                border = if (isSelected) BorderStroke(2.dp, Color(0xFF990410)) else null,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(hospital.nomeHospital, fontWeight = FontWeight.Bold)
                    Text(hospital.endereco, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(Icons.Default.CheckCircle, "Selecionado", tint = Color(0xFF990410), modifier = Modifier.align(Alignment.End))
                    }
                }
            }
        }
    }
}

@Composable
fun DateField(date: String, onDateChange: (String) -> Unit) {
    OutlinedTextField(
        value = date,
        onValueChange = onDateChange,
        label = { Text("Data da Doação (DD/MM/AAAA)") },
        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF990410))
    )
}

@Composable
fun ImageUpload(imageUri: Uri?, isUploading: Boolean, onImageClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .clickable(enabled = !isUploading, onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {
        if (isUploading) {
            CircularProgressIndicator(color = Color(0xFF990410))
        } else if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Comprovante",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                Icon(
                    Icons.Default.CameraAlt, 
                    contentDescription = "Trocar Imagem", 
                    tint = Color.White,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape).padding(6.dp)
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Anexar Comprovante", fontWeight = FontWeight.SemiBold, color = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarRegistrarDoacao(navController: NavController) {
    TopAppBar(
        title = { Text("Registrar Doação Manual", fontWeight = FontWeight.Bold, maxLines = 1) },
        navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color(0xFF990410))
    )
}

@Composable
fun BottomBarRegistrarDoacao(isEnabled: Boolean, isBusy: Boolean, onRegister: () -> Unit) {
    Surface(shadowElevation = 8.dp, color = Color.White) {
        Button(
            onClick = onRegister,
            enabled = isEnabled && !isBusy,
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isBusy) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp)
            } else {
                Text("Registrar Doação", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaRegistrarDoacaoPreview() {
    TelaRegistrarDoacao(rememberNavController())
}
