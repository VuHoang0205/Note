package com.muamuathu.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource

@Composable
fun JcSettingPremiumLock(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null
) {
    val context = LocalContext.current
    JcSettingItem(
        modifier = modifier.clickable {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("solid://purchase/upgrade")))
        },
        title = title,
        summary = summary,
        icon = icon,
        widget = {
            Text(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = "Pro".uppercase(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.Center
            )
        }
    )
}