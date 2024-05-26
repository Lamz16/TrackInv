package com.lamz.trackinv.ui.screen.register

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lamz.trackinv.R
import com.lamz.trackinv.data.model.AuthModel
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.ui.component.TextItem
import com.lamz.trackinv.ui.navigation.Screen
import com.lamz.trackinv.ui.view.main.MainActivity
import com.lamz.trackinv.utils.FirebaseUtils
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val scrollStateHorizontal = rememberScrollState()
    val scrollStateVertical = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(scrollStateHorizontal)
            .verticalScroll(scrollStateVertical),
        contentAlignment = Alignment.Center,
    ) {
        RegisterContent(navController = navController)
    }
}

@Composable
fun RegisterContent(
    context: Context = LocalContext.current,
    navController: NavHostController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(value = false) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }
    val containerColor = colorResource(id = R.color.lavender)
    var namaToko by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(false) }
    val regisState by viewModel.uiState.collectAsState()
    val isNotMatching = remember {
        derivedStateOf {
            password != confirmPassword
        }
    }

    val database = FirebaseUtils.dbUser


    LaunchedEffect(true) {
        if (wasFocused) {
            focusRequester.requestFocus()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextItem(
            desc = stringResource(id = R.string.register),
            fontWeight = FontWeight.SemiBold,
            fontSize = 45.sp,
        )


        OutlinedTextField(
            value = namaToko,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
            ),
            label = { Text(text = "Nama Pemilik toko") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            onValueChange = { newInput ->
                namaToko = newInput
            },
            shape = RoundedCornerShape(size = 20.dp),
            modifier = Modifier
                .padding(bottom = 24.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                },

            )

        OutlinedTextField(
            value = email,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
            ),
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            onValueChange = { newInput ->
                email = newInput
            },
            shape = RoundedCornerShape(size = 20.dp),
            modifier = Modifier
                .padding(bottom = 24.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                },

            )

        OutlinedTextField(
            value = username,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
            ),
            label = { Text(text = "Username") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            onValueChange = { newInput ->
                username = newInput
            },
            shape = RoundedCornerShape(size = 20.dp),
            modifier = Modifier
                .padding(bottom = 24.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                },

            )


        OutlinedTextField(
            value = password,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
            ),
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            onValueChange = { newInput ->
                password = newInput
            },
            shape = RoundedCornerShape(size = 20.dp),
            modifier = Modifier
                .padding(bottom = 24.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { //restore keyboard while rotation
                    isFocused = it.isFocused
                },
            visualTransformation = if (showPassword) {

                VisualTransformation.None

            } else {

                PasswordVisualTransformation()

            },
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )

        OutlinedTextField(
            value = confirmPassword,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
            ),
            label = { Text(text = "Confirm Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            onValueChange = { newInput ->
                confirmPassword = newInput
            },
            shape = RoundedCornerShape(size = 20.dp),
            modifier = Modifier
                .padding(bottom = 24.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { //restore keyboard while rotation
                    isFocused = it.isFocused
                },
            visualTransformation = if (showPassword) {

                VisualTransformation.None

            } else {

                PasswordVisualTransformation()

            },
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )

        OutlinedTextField(
            value = alamat,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
            ),
            label = { Text(text = "Alamat") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            onValueChange = { newInput ->
                alamat = newInput
            },
            shape = RoundedCornerShape(size = 20.dp),
            modifier = Modifier
                .padding(bottom = 24.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                },

            )

        if (isNotMatching.value) {
            Text(text = "Password tidak sama!!", color = Color.Red)
        }

        ElevatedButton(
            onClick = {
                if (password.length < 8) {
                    Toast.makeText(context, "Password kurang dari 8", Toast.LENGTH_SHORT).show()
                    return@ElevatedButton
                }
                // set action firebase
                val userId = database.push().key!!
                val register = AuthModel(
                    userId,
                    namaToko,
                    email,
                    username,
                    password
                )
                showLoading = true

                viewModel.register(register)

            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorResource(id = R.color.Yellow)
            ),
        ) {
            when (val state = regisState) {
                UiState.Loading -> {
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Gray
                        )
                    } else {
                        Text("REGISTER")
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    LaunchedEffect(state) {
                        showDialog = true
                    }
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismissal if needed
                    showDialog = false
                },
                title = {
                    Text("Register Succesfull")
                },
                text = {
                    Text("Are you sure you want to proceed?")
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


        Row {
            Text(text = stringResource(id = R.string.or))

            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.login)),
                onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 16.sp,
                ),

                )
        }

    }
}