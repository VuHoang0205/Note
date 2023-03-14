package com.muamuathu.feature.purchase.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.muamuathu.theme.AppTheme
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.toolbar.JcMediumTopBar
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class PurchaseActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PurchaseActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(useDarkTheme = true) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    topBar = {
                        JcMediumTopBar(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Teleprompter Pro",
                            navIcon = IconSource.Vector(Icons.Rounded.Close),
                            onNavigationClicked = {
                                finish()
                            }
                        )
                    }
                ) {
                    ScreenPurchase(
                        modifier = Modifier.fillMaxSize().padding(it),
                        onCloseClicked = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}