package com.lamz.trackinv.ui.screen.add

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.lamz.trackinv.R
import com.lamz.trackinv.data.model.KategoriModel
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.view.main.MainActivity
import com.lamz.trackinv.utils.FirebaseUtils

@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToDetail: (String) -> Unit,

    ) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()

    ) {
        AddContent(
            navigateToDetail = navigateToDetail,
            navController = navController
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    navigateToDetail: (String) -> Unit,
    navController: NavHostController,
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }
    var showUpdate by remember { mutableStateOf(false) }
    var addCategory by remember { mutableStateOf("") }
    var categoryId  by remember { mutableStateOf("") }
    var showCategory by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }
    var categoryName by remember { mutableStateOf("") }
    val dbKategori = FirebaseUtils.dbCategory
    var categories by remember { mutableStateOf(emptyList<KategoriModel>()) }

    val listState = rememberLazyListState()

    val  fetchCategories : () -> Unit = {
        dbKategori.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<KategoriModel>()

                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(KategoriModel::class.java)
                    if (category != null) {
                        tempList.add(category)
                    }
                }
                categories = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Gagal membaca data kategori", Toast.LENGTH_SHORT).show()
            }
        })
    }
    val deleteCategories : (String) -> Unit = { idKategori ->
        val akunQuery: Query = dbKategori.orderByChild("idKategori").equalTo(idKategori)
        akunQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue()
                    showDelete = false
                    Toast.makeText(context, "Berhasil menghapus kategori", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Add.route){
                        popUpTo(Screen.Home.route)
                    }
                }
                Toast.makeText(
                    context,
                    "Data Kategori berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    context,
                    "Gagal menghapus data kategori: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    val editCategory : (String, String) -> Unit = { idKategori, newCategoryName ->

        val query: Query = dbKategori.orderByChild("idKategori").equalTo(idKategori)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val key = snap.key
                        if (key != null) {
                            val updatedCategory = KategoriModel(idKategori, newCategoryName)
                            dbKategori.child(key).setValue(updatedCategory)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Data kategori berhasil diperbarui",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        showUpdate = false
                                        Toast.makeText(context, "Berhasil mengubah data kategori", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Screen.Add.route){
                                            popUpTo(Screen.Home.route)
                                        }
                                    }
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(
                    context,
                    "Gagal mengubah data kategori: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }



    LaunchedEffect(true) {
        fetchCategories()
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }

    Box {

        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.kategori),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                )
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight()
                ) {


                    IconButton(
                        onClick = {
                            showCategory = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "Add Category",
                            tint = colorResource(id = R.color.Yellow),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 16.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel",
                            tint = colorResource(id = R.color.Yellow),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 16.dp)
                        )
                    }
                }
            }
        )

        if (showCategory) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismissal if needed
                    showCategory = false
                },
                title = {
                    Text("Tambah Data Kategori")
                },
                text = {
                    val containerColor = colorResource(id = R.color.lavender)
                    OutlinedTextField(
                        value = addCategory,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        ),
                        label = { Text(text = "Nama Kategori") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        onValueChange = { newInput ->
                            addCategory = newInput
                        },
                        shape = RoundedCornerShape(size = 20.dp),
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isFocused = it.isFocused
                            },
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val idKategori = dbKategori.push().key!!
                                  val kategori = KategoriModel(
                                      idKategori,
                                      addCategory
                                  )

                            dbKategori.child(idKategori).setValue(kategori)
                                .addOnCompleteListener {
                                    showCategory = false
                                    Toast.makeText(context, "Berhasil menambah kategori", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.Add.route) {
                                        popUpTo(Screen.Home.route)
                                    }

                                    }
                                .addOnFailureListener {

                                    Toast.makeText(context, "Gagal menambah data kategori: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorResource(id = R.color.Yellow)
                        )
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showCategory = false
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("No")
                    }
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismissal if needed
                    showDialog = false
                },
                title = {
                    Text("Batal Tambah")
                },
                text = {
                    Text("Yakin ingin Batal ?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                            (context as? ComponentActivity)?.finish()

                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorResource(id = R.color.Yellow)
                        )
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("No")
                    }
                }
            )
        }

        LazyColumn(
            state = listState, contentPadding = PaddingValues(bottom = 120.dp),
            modifier = Modifier.padding(top = 48.dp)
        ) {

                    items(categories) { category ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentSize()
                                .clip(RoundedCornerShape(10.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(id = R.color.lavender)
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp, end = 25.dp)
                            ) {
                                Text(
                                    text = category.namaKategori!!,
                                    fontSize = 15.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = modifier
                                        .align(Alignment.CenterVertically)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Image(
                                        imageVector = Icons.Default.Delete, contentDescription = null,
                                        colorFilter = ColorFilter.tint(Color.Red),
                                        modifier = modifier
                                            .padding(start = 8.dp)
                                            .clickable {
                                                categoryId = category.idKategori!!
                                                showDelete = true
                                            }
                                    )
                                    Image(
                                        imageVector = Icons.Default.ModeEdit, contentDescription = null,
                                        colorFilter = ColorFilter.tint(Color.Blue),
                                        modifier = modifier
                                            .padding(start = 8.dp)
                                            .clickable {
                                                categoryId = category.idKategori!!
                                                categoryName = category.namaKategori
                                                showUpdate = true
                                            }
                                    )
                                    Image(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null,
                                        colorFilter = ColorFilter.tint(Color.Black),
                                        modifier = modifier
                                            .padding(start = 8.dp)
                                            .clickable { navigateToDetail(category.namaKategori) }
                                    )
                                }

                            }
                        }
                    }

                    if (categories.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Data kategori masih kosong. Tambahkan dulu.")
                            }
                        }
                    }



            }
        }

        if (showDelete) {
            AlertDialog(
                onDismissRequest = {
                    showDelete = false
                },
                title = {
                    Text("Hapus Data")
                },
                text = {
                    Text("Yakin ingin hapus ?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            deleteCategories(categoryId)
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorResource(id = R.color.Yellow)
                        )
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDelete = false
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("No")
                    }
                }
            )
        }

        if (showUpdate) {
            AlertDialog(
                onDismissRequest = {
                    showCategory = false
                },
                title = {
                    Text("Edit Data Kategori")
                },
                text = {
                    val containerColor = colorResource(id = R.color.lavender)
                    OutlinedTextField(
                        value = categoryName,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        ),
                        label = { Text(text = "Nama Kategori") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        onValueChange = { newInput ->
                            categoryName = newInput
                        },
                        shape = RoundedCornerShape(size = 20.dp),
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isFocused = it.isFocused
                            },
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            editCategory(categoryId, categoryName)
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorResource(id = R.color.Yellow)
                        )
                    ) {
                        Text("Simpan")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showUpdate = false
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("Batal")
                    }
                }
            )
        }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAddScreen() {
    AddContent(navigateToDetail = {}, navController = rememberNavController())
}


