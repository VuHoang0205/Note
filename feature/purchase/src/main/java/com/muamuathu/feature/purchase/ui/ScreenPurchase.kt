package com.muamuathu.feature.purchase.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.muamuathu.feature.purchase.BuildConfig
import com.muamuathu.feature.purchase.ext.WrappedProduct
import com.muamuathu.feature.purchase.model.ProFeature
import com.muamuathu.feature.purchase.viewmodel.PurchaseViewModel
import com.muamuathu.theme.OnPurchaseColor
import com.muamuathu.theme.PurchaseColor

@Composable
fun ScreenPurchase(modifier: Modifier, onCloseClicked: () -> Unit) {
    val purchaseViewModel = hiltViewModel<PurchaseViewModel>()
    val isPremiumUser by purchaseViewModel.isPremiumUser().collectAsState(initial = false)

    if (isPremiumUser) {
        SuccessPurchase(onCloseClicked)
    } else {
        ConstraintLayout(modifier = modifier) {
            val (description, features, offers) = createRefs()

            Text(
                modifier = Modifier.constrainAs(description) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
                text = "Upgrade to unlock extra features",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )

            ExtraFeatures(
                modifier = Modifier.constrainAs(features) {
                    centerHorizontallyTo(parent)
                    top.linkTo(description.bottom, 16.dp)
                    height = Dimension.value(160.dp)
                }
            )

            ProductList(
                modifier = Modifier.constrainAs(offers) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ExtraFeatures(modifier: Modifier) {
    val features = remember {
        ProFeature.all()
    }

    HorizontalPager(
        modifier = modifier,
        count = features.size,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        val feature = features[it]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(end = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = MaterialTheme.shapes.small
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = "",
                    tint = PurchaseColor
                )

                Text(
                    text = feature.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = PurchaseColor
                )
            }

            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(0.8f)
            )
        }
    }
}

@Composable
private fun ProductList(modifier: Modifier) {
    val activity = LocalContext.current as ComponentActivity
    val purchaseViewModel = hiltViewModel<PurchaseViewModel>()
    val products by purchaseViewModel.getAllProducts().collectAsState(initial = emptyList())

    var currentProduct: ProductDetails? by remember(products) {
        mutableStateOf(products.find { it.productId == BuildConfig.BestValue })
    }

    val offerToken: String? by remember(currentProduct) {
        val token = currentProduct?.let {
            WrappedProduct(it).offerToken()
        }
        mutableStateOf(token)
    }

    val purchaseEnabled by remember(currentProduct, offerToken) {
        mutableStateOf(
            (currentProduct?.productType == BillingClient.ProductType.SUBS && offerToken != null)
                    || currentProduct?.productType == BillingClient.ProductType.INAPP
        )
    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        products.forEach {
            ProductItem(
                modifier = Modifier.fillMaxWidth(),
                product = WrappedProduct(it),
                isSelected = currentProduct == it,
                isBestValue = it.productId == BuildConfig.BestValue,
                onProductSelected = {
                    currentProduct = it
                }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                currentProduct?.let {
                    purchaseViewModel.buy(
                        activity = activity,
                        productId = it.productId,
                        offerToken = offerToken
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PurchaseColor,
                contentColor = OnPurchaseColor
            ),
            enabled = purchaseEnabled
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Continue",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        Text(
            modifier = Modifier
                .clickable { purchaseViewModel.restorePurchase() }
                .fillMaxWidth(),
            text = "Restore purchases",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProductItem(
    modifier: Modifier,
    product: WrappedProduct,
    isSelected: Boolean,
    isBestValue: Boolean,
    onProductSelected: (ProductDetails) -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .clickable {
                onProductSelected(product.product)
            }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.inverseOnSurface,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) PurchaseColor else MaterialTheme.colorScheme.inverseOnSurface,
                shape = MaterialTheme.shapes.small
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        val (title, price, badge) = createRefs()

        Text(
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                end.linkTo(if (isBestValue) badge.start else parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            },
            text = product.title(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )

        if (isBestValue) {
            Text(
                modifier = Modifier
                    .constrainAs(badge) {
                        end.linkTo(parent.end)
                        top.linkTo(title.top)
                        bottom.linkTo(title.bottom)
                    }
                    .background(
                        color = PurchaseColor,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = "Best".uppercase(),
                style = MaterialTheme.typography.bodySmall,
                color = OnPurchaseColor,
                textAlign = TextAlign.Center
            )
        }

        Text(
            modifier = Modifier
                .constrainAs(price) {
                    start.linkTo(title.start)
                    end.linkTo(title.end)
                    top.linkTo(title.bottom)
                    width = Dimension.fillToConstraints
                },
            text = if (product.isSubscription()) product.price() else product.oneTimePrice(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )

    }
}

@Composable
private fun SuccessPurchase(onCloseClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp, vertical = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Rounded.LockOpen,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Thank you!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You have become a premium user and have access to all the app's features now and forever. Once again, thank you for your trust and support.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.wrapContentSize(),
                onClick = onCloseClicked
            ) {
                Text(
                    text = "Continue".uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun PurchasePreview() {
    ScreenPurchase(modifier = Modifier.fillMaxSize()) {

    }
}