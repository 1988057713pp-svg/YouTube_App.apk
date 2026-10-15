package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.data.model.Channel
import com.example.data.model.Video
import com.example.ui.components.VideoCard
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YtBlue

@Composable
fun SubscriptionsScreen(
    channels: List<Channel>,
    videos: List<Video>,
    appLanguage: AppLanguage,
    onVideoClick: (Video) -> Unit,
    onSaveToWatchLater: (Video) -> Unit,
    onDownload: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedChannelFilter by remember { mutableStateOf<String?>("All") }
    val subscribedChannels = channels.filter { it.isSubscribed }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Horizontal Subscribed Channels Story Row
        if (subscribedChannels.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(subscribedChannels, key = { it.id }) { channel ->
                    val isSelected = selectedChannelFilter == channel.name
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                selectedChannelFilter = if (isSelected) "All" else channel.name
                            }
                            .testTag("channel_item_${channel.id}")
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            AsyncImage(
                                model = channel.avatarUrl,
                                contentDescription = channel.name,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) YouTubeRed else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            // Unread upload blue/red dot indicator
                            if (channel.hasNewUpload) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(YtBlue)
                                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = channel.name,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.width(62.dp)
                        )
                    }
                }
            }
        }

        // Subscriptions video feed
        val displayVideos = remember(videos, selectedChannelFilter) {
            if (selectedChannelFilter == null || selectedChannelFilter == "All") {
                videos.filter { v -> subscribedChannels.any { it.name == v.channelName } }
            } else {
                videos.filter { it.channelName == selectedChannelFilter }
            }
        }

        if (displayVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (appLanguage == AppLanguage.BENGALI) "সাবস্ক্রাইব করা চ্যানেলের কোন নতুন ভিডিও নেই" else "No new videos from your subscriptions",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("subscriptions_feed_list"),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(displayVideos, key = { it.id }) { video ->
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
