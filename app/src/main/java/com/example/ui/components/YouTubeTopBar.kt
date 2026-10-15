package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.YouTubeRed

@Composable
fun YouTubeTopBar(
    unreadNotificationsCount: Int,
    isDarkTheme: Boolean = true,
    onToggleTheme: () -> Unit = {},
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // YouTube Logo Brand Mark
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("youtube_logo_header")
        ) {
            Box(
                modifier = Modifier
                    .size(width = 30.dp, height = 21.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(YouTubeRed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "YouTube",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "BD",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Cast Icon
        IconButton(
            onClick = { /* Cast dialog */ },
            modifier = Modifier.testTag("cast_button")
        ) {
            Icon(
                imageVector = Icons.Default.Cast,
                contentDescription = "Cast",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Notifications Bell
        IconButton(
            onClick = onNotificationsClick,
            modifier = Modifier.testTag("notifications_button")
        ) {
            BadgedBox(
                badge = {
                    if (unreadNotificationsCount > 0) {
                        Badge(
                            containerColor = YouTubeRed,
                            contentColor = Color.White
                        ) {
                            Text(text = "$unreadNotificationsCount", fontSize = 10.sp)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Search Button
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.testTag("search_button")
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Theme Toggle Button (Dark / Light)
        IconButton(
            onClick = onToggleTheme,
            modifier = Modifier.testTag("theme_toggle_button")
        ) {
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = if (isDarkTheme) "Switch to Light Mode" else "Switch to Dark Mode",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Profile Avatar
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(32.dp)
                .clip(CircleShape)
                .clickable { onProfileClick() }
                .testTag("profile_avatar_topbar")
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
                contentDescription = "User Avatar",
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
