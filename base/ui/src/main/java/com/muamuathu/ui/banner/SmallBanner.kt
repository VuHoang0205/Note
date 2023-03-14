package com.muamuathu.ui.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muamuathu.theme.OnPurchaseColor
import com.muamuathu.theme.PurchaseColor
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon

@Composable
fun SmallBanner(
    modifier: Modifier,
    iconSource: IconSource,
    text: String,
    ctaText: String,
    onCtaClicked: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .background(
                color = PurchaseColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(16.dp),
    ) {
        val (icon, message, cta) = createRefs()
        JcIcon(
            modifier = Modifier.constrainAs(icon) {
                top.linkTo(message.top)
                start.linkTo(parent.start)
                width = Dimension.value(24.dp)
                height = Dimension.value(24.dp)
            },
            iconSource = iconSource,
            tint = OnPurchaseColor
        )

        Text(
            modifier = Modifier.constrainAs(message) {
                top.linkTo(parent.top)
                start.linkTo(icon.end, 12.dp)
                bottom.linkTo(cta.top, 8.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = OnPurchaseColor
        )

        Button(
            modifier = Modifier.constrainAs(cta) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            onClick = onCtaClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = OnPurchaseColor,
                contentColor = PurchaseColor
            )
        ) {
            Text(
                text = ctaText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}