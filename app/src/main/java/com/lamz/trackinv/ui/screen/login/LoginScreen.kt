package com.lamz.trackinv.ui.screen.login

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.graphicsLayer
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
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.ui.component.TextItem
import com.lamz.trackinv.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val scrollStateHorizontal = rememberScrollState()
    val scrollStateVertical = rememberScrollState()
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(scrollStateHorizontal)
            .verticalScroll(scrollStateVertical),

        ) {
        LoginContent(navController = navController)
    }
}

@Composable
fun LoginContent(
    context: Context = LocalContext.current,
    navController: NavHostController,
    viewModel: LoginViewModel = koinViewModel()

) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(value = false) }

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val wasFocused = remember { isFocused }

    var showLoading by remember { mutableStateOf(false) }
    val loginState by viewModel.uiState.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedFloat = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )



    LaunchedEffect(Unit) {
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
            desc = stringResource(id = R.string.login),
            fontWeight = FontWeight.SemiBold,
            fontSize = 45.sp,
            modifier = Modifier.graphicsLayer {
                this.translationY = animatedFloat.value
                this.translationX = animatedFloat.value
            }
        )

        val containerColor = colorResource(id = R.color.lavender)
        OutlinedTextField(
            value = email,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
            ),
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

        val containerColor1 = colorResource(id = R.color.lavender)
        OutlinedTextField(
            value = password,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor1,
                unfocusedContainerColor = containerColor1,
                disabledContainerColor = containerColor1,
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

        ElevatedButton(
            onClick = {
                if (password.length < 8) {
                    Toast.makeText(context, "Password kurang dari 8", Toast.LENGTH_SHORT).show()
                    return@ElevatedButton
                }
                showLoading = true
                viewModel.login(email,password)
            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorResource(id = R.color.Yellow)
            ),
        ) {

            when(val state = loginState){
                UiState.Loading ->{
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Gray
                        )
                    }else{
                        Text("LOGIN")
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    LaunchedEffect(state) {
                        viewModel.saveSession(UserModel(state.data.email ?:"",state.data.idUser ?: "", state.data.nama ?: "", true))
                    }
                }
            }

        }




        Row {
            Text(text = stringResource(id = R.string.to_register))

            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.register)),
                onClick = {
                    navController.navigate(Screen.Register.route) {
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
