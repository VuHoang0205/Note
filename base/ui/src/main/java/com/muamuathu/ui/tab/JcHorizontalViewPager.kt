package com.muamuathu.ui.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.rememberPagerState
import com.muamuathu.ui.model.JcMenuItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun JcHorizontalViewPager(
    modifier: Modifier,
    menuItems: List<JcMenuItem>,
    selectedColor: Color,
    contentColor: Color,
    containerColor: Color,
    selectedContainerColor: Color,
    content: @Composable PagerScope.(Int) -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(modifier = modifier) {
        val (tab, pager) = createRefs()

        JcTabRow(
            modifier = Modifier.constrainAs(tab) {
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            },
            selectedTabIndex = pagerState.currentPage,
            menuItems = menuItems,
            selectedColor = selectedColor,
            contentColor = contentColor,
            containerColor = containerColor,
            selectedContainerColor = selectedContainerColor,
            onTabSelected = { position, item ->
                coroutineScope.launch {
                    pagerState.scrollToPage(position)
                }
            }
        )

        HorizontalPager(
            modifier = Modifier.constrainAs(pager) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(tab.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            },
            count = menuItems.size,
            state = pagerState,
            verticalAlignment = Alignment.Top,
            content = content
        )
    }
}