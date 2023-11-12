package com.lamz.trackinv.ui.screen.inventory

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamz.trackinv.R
import com.lamz.trackinv.ui.component.CardLongItem
import com.lamz.trackinv.ui.screen.inventory.model.InventoryData
import com.lamz.trackinv.ui.view.add.AddActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    ) {

    var showDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(id = R.string.daftar_barang),
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,) },
        actions = {
            IconButton(onClick = {
                showDialog = false
                val intent = Intent(context,AddActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                (context as? ComponentActivity)?.finish()

            }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Logout",
                    tint = colorResource(id = R.color.Yellow),
                    modifier = Modifier
                        .size(36.dp)
                )
            }
        }
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        InventoryContent()
    }
}

@Composable
fun InventoryContent(
    modifier: Modifier = Modifier
){
    val listState = rememberLazyListState()

    Box( modifier = Modifier){
        
        LazyColumn (state = listState,  contentPadding = PaddingValues(bottom = 80.dp), modifier = Modifier.padding(top = 48.dp)){

            items(InventoryData.inventory, key = {it.id}){inventory ->
                CardLongItem(namaItem = inventory.name, pieces = inventory.stok, category = inventory.category, id = inventory.id)
            }
        }
    }
}