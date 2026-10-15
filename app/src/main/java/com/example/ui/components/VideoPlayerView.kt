package com.example.ui.components

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.ClosedCaptionDisabled
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.data.model.Comment
import com.example.data.model.Video
import com.example.ui.theme.YouTubeDarkRed
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YtBlue
import com.example.ui.theme.YtDarkSurfaceVariant
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerView(
    video: Video,
    allVideos: List<Video>,
    isPlaying: Boolean,
    currentPositionSeconds: Int,
    playbackSpeed: Float,
    isCaptionsEnabled: Boolean,
    isDownloading: Boolean,
    downloadProgress: Float,
    appLanguage: AppLanguage,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onMinimize: () -> Unit,
    onClose: () -> Unit,
    onSpeedSelected: (Float) -> Unit,
    onToggleCaptions: () -> Unit,
    onToggleLike: (String) -> Unit,
    onToggleDislike: (String) -> Unit,
    onToggleSubscribe: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    onDownload: (String) -> Unit,
    onAddComment: (String, String) -> Unit,
    onSelectNextVideo: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var areControlsVisible by remember { mutableStateOf(true) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }
    var isSubscribed by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf("") }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    // Simulated progress timer if video is playing
    var localSeconds by remember(video.id) { mutableIntStateOf(currentPositionSeconds) }
    val totalSeconds = remember(video.durationSeconds) { if (video.durationSeconds > 0) video.durationSeconds else 600 }

    LaunchedEffect(isPlaying, video.id) {
        while (isPlaying) {
            delay(1000)
            localSeconds = (localSeconds + 1) % totalSeconds
            onSeekTo(localSeconds)
        }
    }

    // Auto-hide controls after 4 seconds
    LaunchedEffect(areControlsVisible, isPlaying) {
        if (areControlsVisible && isPlaying) {
            delay(4000)
            areControlsVisible = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Player Surface (16:9)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black)
                .clickable { areControlsVisible = !areControlsVisible }
                .testTag("video_player_surface")
        ) {
            // Video Thumbnail / Visual
            if (video.localDrawableRes != null) {
                Image(
                    painter = painterResource(id = video.localDrawableRes),
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Simulated dynamic Closed Captions overlay
            if (isCaptionsEnabled) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xB3000000))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    val caption = if (appLanguage == AppLanguage.BENGALI) {
                        "আজকের এই ভিডিওতে আমরা বিস্তারিত আলোচনা করবো..."
                    } else {
                        "In this section, we are diving deep into the performance..."
                    }
                    Text(
                        text = caption,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Controls Overlay
            androidx.compose.animation.AnimatedVisibility(
                visible = areControlsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x77000000))
                ) {
                    // Top Player Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onMinimize,
                            modifier = Modifier.testTag("player_minimize_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Minimize Player",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // CC toggle
                        IconButton(onClick = onToggleCaptions) {
                            Icon(
                                imageVector = if (isCaptionsEnabled) Icons.Default.ClosedCaption else Icons.Default.ClosedCaptionDisabled,
                                contentDescription = "Captions",
                                tint = if (isCaptionsEnabled) YouTubeRed else Color.White
                            )
                        }

                        // Speed & Settings menu
                        Box {
                            IconButton(onClick = { showSpeedMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = showSpeedMenu,
                                onDismissRequest = { showSpeedMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Quality: 1080p60 HD") },
                                    onClick = { showSpeedMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Speed: ${playbackSpeed}x") },
                                    leadingIcon = { Icon(Icons.Default.Speed, contentDescription = null) },
                                    onClick = {
                                        val nextSpeed = when (playbackSpeed) {
                                            1.0f -> 1.5f
                                            1.5f -> 2.0f
                                            else -> 1.0f
                                        }
                                        onSpeedSelected(nextSpeed)
                                        showSpeedMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Center Playback Buttons: 10s Rewind | Play/Pause | 10s Forward
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        IconButton(
                            onClick = {
                                localSeconds = (localSeconds - 10).coerceAtLeast(0)
                                onSeekTo(localSeconds)
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay10,
                                contentDescription = "Rewind 10s",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0x99000000))
                                .clickable { onTogglePlayPause() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                localSeconds = (localSeconds + 10).coerceAtMost(totalSeconds)
                                onSeekTo(localSeconds)
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forward10,
                                contentDescription = "Forward 10s",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    // Bottom Bar with Progress Slider & Time
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, bottom = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val currentFormatted = formatTime(localSeconds)
                            val totalFormatted = formatTime(totalSeconds)
                            Text(
                                text = "$currentFormatted / $totalFormatted",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Fullscreen",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Slider(
                            value = localSeconds.toFloat(),
                            onValueChange = {
                                localSeconds = it.toInt()
                                onSeekTo(localSeconds)
                            },
                            valueRange = 0f..totalSeconds.toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = YouTubeRed,
                                activeTrackColor = YouTubeRed,
                                inactiveTrackColor = Color(0x66FFFFFF)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                        )
                    }
                }
            }
        }

        // Scrollable Video Details & Recommendations
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                // Title & Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${video.views} • ${video.publishedTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Expandable Description
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = video.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (video.tags.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = video.tags.joinToString(" "),
                                    color = YtBlue,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Channel Bar Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = video.channelAvatarUrl,
                        contentDescription = video.channelName,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.channelName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = video.channelSubscribers,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Subscribe Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSubscribed) MaterialTheme.colorScheme.surfaceVariant else YouTubeRed)
                            .clickable {
                                isSubscribed = !isSubscribed
                                onToggleSubscribe(video.channelName)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("subscribe_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSubscribed) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (appLanguage == AppLanguage.BENGALI) "সাবস্ক্রাইবড" else "Subscribed",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            } else {
                                Text(
                                    text = if (appLanguage == AppLanguage.BENGALI) "সাবস্ক্রাইব" else "Subscribe",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // Horizontal Action Buttons: Like, Dislike, Share, Download, Save
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Like / Dislike Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onToggleLike(video.id) }
                                .padding(end = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (video.isLiked) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                                contentDescription = "Like",
                                tint = if (video.isLiked) YouTubeRed else MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatCount(video.likesCount),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(16.dp)
                                .background(MaterialTheme.colorScheme.outline)
                        )

                        IconButton(
                            onClick = { onToggleDislike(video.id) },
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (video.isDisliked) Icons.Default.ThumbDown else Icons.Default.ThumbDownOffAlt,
                                contentDescription = "Dislike",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Share Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Watch: ${video.title} on YouTube")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Video"))
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (appLanguage == AppLanguage.BENGALI) "শেয়ার" else "Share",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Download Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                if (!video.isDownloaded && !isDownloading) {
                                    onDownload(video.id)
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isDownloading) {
                            CircularProgressIndicator(
                                progress = { downloadProgress },
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = YouTubeRed
                            )
                        } else {
                            Icon(
                                imageVector = if (video.isDownloaded) Icons.Default.Check else Icons.Default.Download,
                                contentDescription = "Download",
                                tint = if (video.isDownloaded) YouTubeRed else MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (video.isDownloaded) {
                                if (appLanguage == AppLanguage.BENGALI) "সংরক্ষিত" else "Downloaded"
                            } else if (isDownloading) {
                                "${(downloadProgress * 100).toInt()}%"
                            } else {
                                if (appLanguage == AppLanguage.BENGALI) "ডাউনলোড" else "Download"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Save / Bookmark
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onToggleSave(video.id) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (video.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (video.isSaved) YouTubeRed else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Comments Preview Box
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showCommentsSheet = !showCommentsSheet }
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (appLanguage == AppLanguage.BENGALI) "মন্তব্য ${video.comments.size}" else "Comments ${video.comments.size}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (showCommentsSheet) "Hide" else "View all",
                                color = YtBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (video.comments.isNotEmpty()) {
                            val topComment = video.comments.first()
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.Top) {
                                AsyncImage(
                                    model = topComment.userAvatarUrl,
                                    contentDescription = topComment.userName,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = topComment.userName,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = topComment.text,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Expanded Comments List with Comment Input Field
            if (showCommentsSheet) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        // Add comment row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newCommentText,
                                onValueChange = { newCommentText = it },
                                placeholder = {
                                    Text(
                                        if (appLanguage == AppLanguage.BENGALI) "একটি মন্তব্য যোগ করুন..." else "Add a comment...",
                                        fontSize = 13.sp
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("comment_input_field"),
                                shape = RoundedCornerShape(20.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = YouTubeRed,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (newCommentText.isNotBlank()) {
                                        onAddComment(video.id, newCommentText)
                                        newCommentText = ""
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(YouTubeRed)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Post Comment",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        video.comments.forEach { comment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                AsyncImage(
                                    model = comment.userAvatarUrl,
                                    contentDescription = comment.userName,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = comment.userName,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = comment.timeAgo,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = comment.text,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Up Next / Related Videos Header
            item {
                Text(
                    text = if (appLanguage == AppLanguage.BENGALI) "পরবর্তী ভিডিও (Up Next)" else "Up Next",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 14.dp, top = 14.dp, bottom = 8.dp)
                )
            }

            // Related Videos List
            val related = allVideos.filter { it.id != video.id }
            items(related) { nextVid ->
                VideoCard(
                    video = nextVid,
                    onVideoClick = onSelectNextVideo,
                    onSaveToWatchLater = { onToggleSave(nextVid.id) },
                    onDownload = { onDownload(nextVid.id) },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}

private fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
