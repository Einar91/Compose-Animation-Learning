package com.example.animationdemo_v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

@Composable
fun ScreenContents(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,

    content: @Composable() (ColumnScope.() -> Unit) = {}
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                ),
            ) {
                Icon(Icons.Outlined.Menu, contentDescription = "menu")
            }
            content()
        }
    }
}