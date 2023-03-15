package com.muamuathu.app.presentation.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.muamuathu.app.R
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.graph.NavTarget

const val TAG_TERMS_OF_SERVICE = "TERMS_OF_SERVICE"
const val TAG_PRIVACY_POLICY = "PRIVACY_POLICY"

@Composable
fun ScreenLogin() {
    val eventHandler = initEventHandler()
    Content({
        // TODO: Login Google
    }, {
        // TODO: Login Facebook
    }, {
        eventHandler.postNavEvent(NavEvent.Action(NavTarget.LoginWithEmail))
    }, {
        eventHandler.postNavEvent(NavEvent.ActionWeb(it))
    }, {
        eventHandler.postNavEvent(NavEvent.ActionWeb(it))
    })
}

@Composable
private fun Content(
    onLoginGoogle: () -> Unit,
    onLoginFacebook: () -> Unit,
    onLoginEmail: () -> Unit,
    onServiceTerm: (url: String) -> Unit,
    onPrivacyPolicy: (url: String) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .background(Color.White).padding(20.dp),
    ) {
        val (imgSync, textKeepSync, textContent, btnGoogle, btnFacebook, btnEmail, textPrivacy, textSync) = createRefs()

        Image(
            contentDescription = "sync",
            painter = painterResource(R.drawable.ic_sync),
            modifier = Modifier.constrainAs(imgSync) {
                top.linkTo(parent.top, 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = stringResource(R.string.txt_keep_me_synced),
            color = colorResource(R.color.royal_blue),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(textKeepSync) {
                top.linkTo(imgSync.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = stringResource(R.string.txt_content_keep_me_synced),
            color = colorResource(R.color.storm_grey),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(textContent) {
                top.linkTo(textKeepSync.bottom, 4.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        ButtonAuthentication(
            {
                onLoginGoogle()
            },
            Modifier.padding(horizontal = 35.dp).height(40.dp).fillMaxWidth()
                .constrainAs(btnGoogle) {
                    top.linkTo(textContent.bottom, 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            R.color.white,
            R.drawable.ic_google,
            R.string.txt_login_with_google,
            R.color.gulf_blue
        )

        ButtonAuthentication(
            {
                onLoginFacebook()
            },
            Modifier.padding(horizontal = 35.dp).height(40.dp).fillMaxWidth()
                .constrainAs(btnFacebook) {
                    top.linkTo(btnGoogle.bottom, 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            R.color.royal_blue,
            R.drawable.ic_facebook,
            R.string.txt_login_with_facebook,
            R.color.white
        )

        ButtonAuthentication({
            onLoginEmail()
        }, Modifier.padding(horizontal = 35.dp).fillMaxWidth().height(40.dp)
            .constrainAs(btnEmail) {
                top.linkTo(btnFacebook.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            R.color.venetian_red,
            R.drawable.ic_email,
            R.string.txt_login_with_email,
            R.color.white
        )

        TextClickablePrivacy(modifier = Modifier.constrainAs(textPrivacy) {
            top.linkTo(btnEmail.bottom, 24.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, {
            onServiceTerm(it)
        }, {
            onPrivacyPolicy(it)
        })

        TextButton(onClick = {},
            modifier = Modifier.constrainAs(textSync) {
                top.linkTo(textPrivacy.bottom, 30.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            Text(
                text = stringResource(R.string.txt_not_want_sync),
                color = colorResource(R.color.royal_blue),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    Content({}, {}, {}, {}, {})
}

@Composable
fun ButtonAuthentication(
    onClick: () -> Unit,
    modifier: Modifier,
    backgroundColor: Int,
    icon: Int,
    text: Int,
    textColor: Int,
) {
    Button(
        onClick = {
            onClick()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(backgroundColor)),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Image(painterResource(icon), contentDescription = "")
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(text),
            color = colorResource(textColor), fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TextClickablePrivacy(
    modifier: Modifier,
    onClickTerms: (url: String) -> Unit,
    onClickPrivacy: (url: String) -> Unit,
) {
    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.txt_note_privacy))
        append(" ")
        pushStringAnnotation(tag = "Terms", "https://www.google.lk/")
        withStyle(
            style = SpanStyle(
                color = colorResource(R.color.royal_blue),
                fontSize = 11.sp,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(R.string.txt_term_of_service))
        }
        append(" ")
        append("and")
        append(" ")

        pushStringAnnotation(tag = "Privacy", "https://www.google.lk/")
        withStyle(
            style = SpanStyle(
                color = colorResource(R.color.royal_blue),
                fontSize = 11.sp,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(R.string.txt_privacy_policy))
        }
        pop()
    }

    ClickableText(
        modifier = modifier,
        text = annotatedText,
        style = TextStyle(fontSize = 11.sp, textAlign = TextAlign.Center),
        onClick = {
            annotatedText.getStringAnnotations(tag = TAG_TERMS_OF_SERVICE, start = it, end = it)
                .firstOrNull()
                ?.let { it ->
                    onClickTerms(it.item)
                }

            annotatedText.getStringAnnotations(tag = TAG_PRIVACY_POLICY, start = it, end = it)
                .firstOrNull()
                ?.let { it ->
                    onClickPrivacy(it.item)
                }
        })
}