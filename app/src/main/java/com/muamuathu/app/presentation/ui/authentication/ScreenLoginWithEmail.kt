@file:OptIn(ExperimentalMaterial3Api::class)

package com.muamuathu.app.presentation.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.muamuathu.app.R
import com.muamuathu.app.presentation.common.text_input_validator.EmailTextInputValidator
import com.muamuathu.app.presentation.common.text_input_validator.PasswordTextInputValidator
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.graph.NavTarget

const val TAG_SIGNUP = "SIGNUP"
const val MAX_LENGTH = 500

@Composable
fun ScreenLoginWithEmail() {
    val eventHandler = initEventHandler()
    Content(
        {
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        },
        {
            // TODO: Login
        },
        {
            eventHandler.postNavEvent(NavEvent.Action(NavTarget.SignUpWithEmail))
        },
        {
            // TODO: Forgot password
        },
        {
            eventHandler.postNavEvent(NavEvent.ActionWeb(it))
        },
        {
            eventHandler.postNavEvent(NavEvent.ActionWeb(it))
        })
}

@Composable
private fun Content(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onSignup: () -> Unit,
    onForgotPassword: () -> Unit,
    onServiceTerm: (url: String) -> Unit,
    onPrivacyPolicy: (url: String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }

    val isEmailValid by remember {
        derivedStateOf {
            EmailTextInputValidator().validate(email)
        }
    }

    val isPasswordValid by remember {
        derivedStateOf {
            PasswordTextInputValidator().validate(password)
        }
    }

    val isInputValid by remember {
        derivedStateOf {
            isEmailValid && isPasswordValid
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .background(colorResource(R.color.alice_blue)).padding(20.dp)) {
        IconButton(onClick = { onBack() }) {
            Image(painter = painterResource(R.drawable.ic_back),
                contentDescription = "back")
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState())) {
            val (contentView, bottomView) = createRefs()
            Column(
                modifier = Modifier.constrainAs(contentView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomView.top)
                },
                verticalArrangement = Arrangement.Center) {
                Text(text = stringResource(R.string.txt_log_in),
                    color = colorResource(R.color.royal_blue),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = stringResource(R.string.txt_welcome_back),
                    color = colorResource(R.color.storm_grey),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                    value = email,
                    onValueChange = { if (email.length < MAX_LENGTH) email = it },
                    label = { Text(text = stringResource(R.string.txt_email)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = colorResource(R.color.royal_blue),
                        focusedBorderColor = if (isEmailValid) colorResource(R.color.royal_blue) else Color.Red
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    value = password,
                    onValueChange = { if (password.length < MAX_LENGTH) password = it },
                    label = { Text(text = stringResource(R.string.txt_password)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Password Toggle"
                            )
                        }
                    }, colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = colorResource(R.color.royal_blue),
                        focusedBorderColor = if (isPasswordValid) colorResource(R.color.royal_blue) else Color.Red
                    )
                )

                TextButton(onClick = { onForgotPassword() },
                    modifier = Modifier.padding(vertical = 16.dp).align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(R.string.txt_forgot_password),
                        color = colorResource(R.color.storm_grey),
                        fontSize = 14.sp,
                    )
                }

                Button(onClick = {
                    onLogin()
                }, modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = isInputValid,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(
                        R.color.royal_blue)), shape = RoundedCornerShape(4.dp)) {
                    Text(text = stringResource(R.string.txt_login),
                        color = colorResource(R.color.white),
                        fontSize = 14.sp
                    )
                }

                val annotatedString = buildAnnotatedString {
                    append(stringResource(R.string.txt_not_have_account))
                    append(" ")
                    pushStringAnnotation(TAG_SIGNUP, stringResource(R.string.txt_sign_up))
                    withStyle(style = SpanStyle(color = colorResource(R.color.royal_blue),
                        fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.txt_sign_up))
                    }
                }

                ClickableText(text = annotatedString,
                    style = TextStyle(fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(
                            R.color.gulf_blue)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp),
                    onClick = {
                        annotatedString.getStringAnnotations(TAG_SIGNUP, start = it, end = it)
                            .firstOrNull()
                            ?.let {
                                onSignup()
                            }
                    })
            }
            TextClickablePrivacy(modifier = Modifier.constrainAs(bottomView) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }, {
                onServiceTerm(it)
            }, {
                onPrivacyPolicy(it)
            })
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content({ }, {}, {}, {}, {}, {})
}


