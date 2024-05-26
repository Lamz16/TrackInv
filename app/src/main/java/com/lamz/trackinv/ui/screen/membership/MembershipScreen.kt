package com.lamz.trackinv.ui.screen.membership

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lamz.trackinv.R
import com.lamz.trackinv.helper.UiState

@Composable
fun MembershipScreen(
    context: Context = LocalContext.current,
) {
//    val uploadMembership by viewModel.uploadMembership.observeAsState()
//    when (val state = uploadMembership) {
//
//        is UiState.Success -> {
//
//            val redirectUrl = state.data.data.redirectUrl
//            LaunchedEffect(true) {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
//                context.startActivity(intent)
//                (context as? ComponentActivity)?.finish()
//            }
//        }
//        else -> {}
//    }


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =  CardDefaults.cardColors(Color.LightGray),
            elevation =CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Membership Bulanan", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Dapatkan lebih banyak akses menambah data", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
//                    viewModel.membership()
                                 }, colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Yellow))) {
                    Text("Rp. 50.000", color = Color.Black)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =  CardDefaults.cardColors(Color.LightGray),
            elevation =CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Membership Tahunan", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Dapatkan lebih banyak akses menambah data", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
//                    viewModel.membershipTahun()
                                 }, colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Yellow))) {
                    Text("Rp. 500.000", color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemberShip(){
    MembershipScreen()
}