package com.example.ecolog

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val activities by dataStoreManager.activitiesFlow.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("EcoLog - Dashboard") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, LogActivity::class.java))
            }) {
                Icon(Icons.Default.Add, "Registrar")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activities) { activity ->
                ActivityItem(
                    activity = activity,
                    onDelete = {
                        scope.launch {
                                dataStoreManager.deleteActivity(activity)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ActivityItem(
    activity: ActivityLog,
    onDelete: () -> Unit
) {
    val icon = when (activity.category) {
        "Transporte" -> Icons.AutoMirrored.Filled.DirectionsBike
        "Hogar" -> Icons.Default.Home
        "Comida" -> Icons.Default.Restaurant
        else -> Icons.Default.Check
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ListItem(
                headlineContent = { Text(activity.name) },
                supportingContent = { Text("${activity.category} • ${activity.impact} kg CO2") },
                leadingContent = { Icon(icon, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}