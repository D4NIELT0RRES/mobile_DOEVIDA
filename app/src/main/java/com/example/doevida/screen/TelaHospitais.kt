package com.example.doevida.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.abrirNoMaps
import com.example.doevida.util.ligarPara
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHospitais(navController: NavController) {
    var listaHospitais by remember { mutableStateOf<List<HospitaisCards>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isMapView by remember { mutableStateOf(false) } // Toggle entre Lista e Mapa
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Configuração do OSM - Importante: carregar configuração
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // Estado de Permissão de Localização
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasLocationPermission = isGranted }
    )

    fun fetchHospitais() {
        isLoading = true
        errorMessage = null
        scope.launch {
            try {
                val response = RetrofitFactory(context).getHospitalService().getHospitais()
                if (response.isSuccessful) {
                    listaHospitais = response.body()?.hospitais ?: emptyList()
                } else {
                    errorMessage = "Erro ao carregar hospitais: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão. Verifique sua internet."
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchHospitais()
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        topBar = { TopBarHospitais(navController) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isMapView = !isMapView },
                containerColor = Color(0xFF990410),
                contentColor = Color.White,
                icon = {
                    Icon(
                        imageVector = if (isMapView) Icons.Default.List else Icons.Default.Map,
                        contentDescription = "Alternar Visualização"
                    )
                },
                text = { Text(if (isMapView) "Ver Lista" else "Ver Mapa") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F7F7))
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF990410))
                }
            } else if (errorMessage != null) {
                ErrorState(errorMessage!!) { fetchHospitais() }
            } else if (listaHospitais.isEmpty()) {
                EmptyStateHospitais()
            } else {
                if (isMapView) {
                    OsmMapView(listaHospitais, context)
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 80.dp, top = 16.dp, start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listaHospitais) { hospital ->
                            HospitalCard(
                                hospital = hospital,
                                onInfoClick = { navController.navigate("tela_detalhe_hospital/${hospital.id}") },
                                onOpenMaps = { abrirNoMaps(context, hospital) },
                                onCall = { ligarPara(context, hospital.telefone) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OsmMapView(hospitais: List<HospitaisCards>, context: Context) {
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(-23.5505, -46.6333)) // Centro SP
        }
    }
    
    // Gerencia o ciclo de vida do mapa para garantir que ele carregue
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            // O MapView deve ser desligado corretamente se possível, mas aqui o contexto cuida
        }
    }

    // Atualiza marcadores
    DisposableEffect(hospitais) {
        mapView.overlays.clear()
        hospitais.forEach { hospital ->
            val (lat, lng) = hospital.getCoordenadas()
            val marker = Marker(mapView)
            marker.position = GeoPoint(lat, lng)
            marker.title = hospital.nomeHospital
            marker.snippet = hospital.endereco
            
            marker.setOnMarkerClickListener { m, _ ->
                m.showInfoWindow()
                true
            }
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
        onDispose { }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHospitais(navController: NavController) {
    TopAppBar(
        title = { Text("Hospitais e Hemocentros", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF990410)
        )
    )
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410))) {
            Text("Tentar Novamente")
        }
    }
}

@Composable
fun EmptyStateHospitais() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(60.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nenhum hospital encontrado", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Não foi possível localizar hospitais no momento.", color = Color.Gray, textAlign = TextAlign.Center)
    }
}

@Composable
fun HospitalCard(
    hospital: HospitaisCards,
    onInfoClick: () -> Unit,
    onOpenMaps: () -> Unit,
    onCall: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onInfoClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    painter = painterResource(id = R.drawable.hospitais), // Garanta que esse recurso existe ou use um vector icon
                    contentDescription = null,
                    tint = Color(0xFF990410),
                    modifier = Modifier.size(32.dp).padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = hospital.nomeHospital.takeIf { !it.isNullOrBlank() } ?: "Hospital não informado",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = hospital.endereco,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onOpenMaps,
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Ver Rota", modifier = Modifier.size(18.dp), tint = Color(0xFF990410))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Rota", color = Color.DarkGray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onCall,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Ligar", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ligar", fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaHospitaisPreview() {
    TelaHospitais(navController = rememberNavController())
}
