package com.muamuathu.app.presentation.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.muamuathu.app.R
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler

@Composable
fun ScreenVerifyEmail(email: String) {
    val eventHandler = initEventHandler()
    Content(email,
        {
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        },
        {
            //todo verify email})
        })
}


@Composable
private fun Content(email: String, onBack: () -> Unit, onVerifyEmail: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()
        .background(colorResource(R.color.alice_blue)).padding(20.dp)) {
        val (imgBack, textTitle, contentView) = createRefs()
        IconButton(onClick = { onBack() },
            modifier = Modifier.constrainAs(imgBack) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }) {
            Image(painter = painterResource(R.drawable.ic_back),
                contentDescription = "back")
        }

        Text(
            text = stringResource(R.string.txt_verification),
            color = colorResource(R.color.gulf_blue),
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp).constrainAs(textTitle) {
                top.linkTo(imgBack.top)
                bottom.linkTo(imgBack.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        ConstraintLayout(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState()).constrainAs(contentView) {
                top.linkTo(imgBack.bottom)
                bottom.linkTo(parent.bottom)
            }) {

            val (textDiscussed, btnResend, textNote, textEmail) = createRefs()

            Text(
                text = stringResource(R.string.txt_to_be_discussed).toUpperCase(Locale.current),
                color = colorResource(R.color.venetian_red),
                fontSize = 24.sp,
                modifier = Modifier.constrainAs(textDiscussed) {
                    top.linkTo(parent.top, 70.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.txt_verification_note),
                color = colorResource(R.color.gulf_blue),
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(textNote) {
                    top.linkTo(textDiscussed.bottom, 70.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, textAlign = TextAlign.Center
            )

            Text(
                text = email,
                color = colorResource(R.color.royal_blue),
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(textEmail) {
                    top.linkTo(textNote.bottom, 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, textAlign = TextAlign.Center
            )

            Button(onClick = {
                onVerifyEmail()
            }, modifier = Modifier.padding(horizontal = 12.dp).height(50.dp)
                .constrainAs(btnResend) {
                    top.linkTo(textEmail.bottom, 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(
                    R.color.royal_blue)),
                shape = RoundedCornerShape(4.dp)) {
                Text(text = stringResource(R.string.txt_resend_email),
                    color = colorResource(R.color.white),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content("Test@gmail.com", {}, {})
}


