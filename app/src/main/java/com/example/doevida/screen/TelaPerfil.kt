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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.doevida.R
import com.example.doevida.components.InfoRow
import com.example.doevida.components.MenuInferior
import com.example.doevida.service.AzureStorageService
import com.example.doevida.service.SharedPreferencesUtils
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
fun TelaPerfil(navController: NavController) {
    val userName = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }
    val context = LocalContext.current
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) } // URI para exibição imediata
    var tempImageUri by rememberSaveable { mutableStateOf<Uri?>(null) } // URI temporária da câmera
    var showSheet by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun uploadImage(uri: Uri) {
        coroutineScope.launch {
            if (isUploading) return@launch
            isUploading = true
            imageUri = uri // Mostra a imagem selecionada imediatamente
            Toast.makeText(context, "Enviando imagem...", Toast.LENGTH_SHORT).show()

            val uploadedUrl = AzureStorageService.uploadImageToAzure(context, uri)

            if (uploadedUrl != null) {
                SharedPreferencesUtils.saveUserProfileImage(context, uploadedUrl)
                imageUri = uploadedUrl.toUri() // Garante que a URI exibida seja a da nuvem
                println("✅ URL da imagem salva: $uploadedUrl")
                Toast.makeText(context, "Imagem de perfil atualizada!", Toast.LENGTH_SHORT).show()
            } else {
                // Se o upload falhar, reverte para a imagem antiga (se houver)
                val savedUrl = SharedPreferencesUtils.getUserProfileImage(context)
                imageUri = savedUrl?.toUri()
                Toast.makeText(context, "Falha no upload para Azure.", Toast.LENGTH_SHORT).show()
            }
            isUploading = false
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

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
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

    // Carrega dados do usuário, incluindo a imagem, ao iniciar a tela
    LaunchedEffect(Unit) {
        userName.value = SharedPreferencesUtils.getUserName(context)
        userEmail.value = SharedPreferencesUtils.getUserEmail(context)
        val savedImageUrl = SharedPreferencesUtils.getUserProfileImage(context)
        if (savedImageUrl != null) {
            imageUri = savedImageUrl.toUri()
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
                    Button(onClick = { if (!isUploading) galleryLauncher.launch("image/*") }, enabled = !isUploading) {
                        Text("Galeria")
                    }
                    Button(onClick = { if (!isUploading) handleCamera() }, enabled = !isUploading) {
                        Text("Câmera")
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = { MenuInferior(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F7F7))
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(userName.value, imageUri, isUploading) {
                if (!isUploading) showSheet = true
            }
            Spacer(modifier = Modifier.height(24.dp))
            UserInfoCard(name = userName.value, email = userEmail.value)
            Spacer(modifier = Modifier.height(24.dp))
            OptionsMenu(navController = navController)
            Spacer(modifier = Modifier.height(24.dp))
            LogoutButton {
                navController.navigate("tela_inicial") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileHeader(userName: String, imageUri: Uri?, isUploading: Boolean = false, onImageClick: () -> Unit) {
    val primaryColor = Color(0xFF990410)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryColor)
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            if (isUploading) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                AsyncImage(
                    model = imageUri ?: R.drawable.logologin,
                    contentDescription = "Avatar do Usuário",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .clickable(enabled = !isUploading) { onImageClick() },
                    contentScale = ContentScale.Crop
                )
            }
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Trocar foto",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .background(primaryColor, CircleShape)
                    .padding(6.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = userName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Doador de Sangue",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun UserInfoCard(name: String, email: String) {
    // TODO: Carregar CPF e Data de Nascimento quando estiverem disponíveis
    val cpf = "123.456.789-00" // Valor de exemplo
    val dataNascimento = "01/01/2005" // Valor de exemplo

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Minhas Informações",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(label = "Nome", value = name)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
            InfoRow(label = "E-mail", value = email)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
            InfoRow(label = "CPF", value = cpf)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
            InfoRow(label = "Data de Nascimento", value = dataNascimento)
        }
    }
}

@Composable
fun OptionsMenu(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Opções",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                OptionItem("Certificados", painterResource(id = R.drawable.doarsangue)) {
                    navController.navigate("tela_certificado")
                }
            }
        }
    }
}

@Composable
fun OptionItem(title: String, icon: Painter, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = icon, contentDescription = title, tint = Color(0xFF990410))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFF990410).copy(alpha = 0.7f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF990410))
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Sair", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaPerfilPreview() {
    val navController = rememberNavController()
    TelaPerfil(navController = navController)
}
