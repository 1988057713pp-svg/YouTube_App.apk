package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Video
import com.example.ui.components.VideoCard
import com.example.ui.theme.YouTubeRed

@Composable
fun HomeScreen(
    videos: List<Video>,
    selectedCategory: String,
    appLanguage: AppLanguage,
    onCategorySelected: (String) -> Unit,
    onVideoClick: (Video) -> Unit,
    onSaveToWatchLater: (Video) -> Unit,
    onDownload: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "All",
        "Gaming",
        "Music",
        "Bangla Natok",
        "Tech",
        "Cricket",
        "Live"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Horizontal Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Explore Compass Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { /* Explore */ }
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = "Explore",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )

            categories.forEach { category ->
                val isSelected = category.equals(selectedCategory, ignoreCase = true)
                val localizedCategory = when (category) {
                    "All" -> if (appLanguage == AppLanguage.BENGALI) "সকল" else "All"
                    "Gaming" -> if (appLanguage == AppLanguage.BENGALI) "গেমিং" else "Gaming"
                    "Music" -> if (appLanguage == AppLanguage.BENGALI) "মিউজিক ও গান" else "Music"
                    "Bangla Natok" -> if (appLanguage == AppLanguage.BENGALI) "বাংলা নাটক" else "Bangla Natok"
                    "Tech" -> if (appLanguage == AppLanguage.BENGALI) "প্রযুক্তি (Tech)" else "Tech"
                    "Cricket" -> if (appLanguage == AppLanguage.BENGALI) "ক্রিকেট খেলা" else "Cricket"
                    "Live" -> if (appLanguage == AppLanguage.BENGALI) "লাইভ সম্প্রচার" else "Live"
                    else -> category
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("category_chip_$category")
                ) {
                    Text(
                        text = localizedCategory,
                        color = if (isSelected) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Feed of Videos
        if (videos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (appLanguage == AppLanguage.BENGALI) "কোন ভিডিও পাওয়া যায়নি" else "No videos found in this category",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("home_video_list"),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(videos, key = { it.id }) { video ->
                    VideoCard(
                        video = video,
                        onVideoClick = onVideoClick,
                        onSaveToWatchLater = onSaveToWatchLater,
                        onDownload = onDownload,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}
