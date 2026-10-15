package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SlowMotionVideo
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppLanguage
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.VideoPlayerView
import com.example.ui.components.YouTubeTopBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.NotificationsBottomSheet
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.screens.SubscriptionsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.YouTubeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: YouTubeViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            val useDynamicColor by viewModel.useDynamicColor.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkTheme, dynamicColor = useDynamicColor) {
                YouTubeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun YouTubeApp(viewModel: YouTubeViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val filteredVideos by viewModel.filteredVideos.collectAsStateWithLifecycle()
    val allVideos by viewModel.videos.collectAsStateWithLifecycle()
    val shorts by viewModel.shorts.collectAsStateWithLifecycle()
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val watchHistory by viewModel.watchHistory.collectAsStateWithLifecycle()
    val downloadedVideos by viewModel.downloadedVideos.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    val selectedVideo by viewModel.selectedVideo.collectAsStateWithLifecycle()
    val isMiniPlayer by viewModel.isMiniPlayer.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentPositionSeconds by viewModel.currentPositionSeconds.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val isCaptionsEnabled by viewModel.isCaptionsEnabled.collectAsStateWithLifecycle()
    val isDownloading by viewModel.isDownloading.collectAsStateWithLifecycle()
    val downloadProgress by viewModel.downloadProgress.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val useDynamicColor by viewModel.useDynamicColor.collectAsStateWithLifecycle()

    val isSearchOpen by viewModel.isSearchOpen.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isNotificationsOpen by viewModel.isNotificationsOpen.collectAsStateWithLifecycle()

    // Handle Hardware / Predictive Back
    BackHandler(enabled = selectedVideo != null && !isMiniPlayer) {
        viewModel.minimizePlayer()
    }

    BackHandler(enabled = isSearchOpen) {
        viewModel.closeSearch()
    }

    BackHandler(enabled = currentTab != MainTab.HOME && (selectedVideo == null || isMiniPlayer) && !isSearchOpen) {
        viewModel.selectTab(MainTab.HOME)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            if (!isSearchOpen && (selectedVideo == null || isMiniPlayer)) {
                YouTubeTopBar(
                    unreadNotificationsCount = notifications.size,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { viewModel.toggleTheme() },
                    onSearchClick = { viewModel.openSearch() },
                    onNotificationsClick = { viewModel.openNotifications() },
                    onProfileClick = { viewModel.selectTab(MainTab.YOU) }
                )
            }
        },
        bottomBar = {
            if (!isSearchOpen && (selectedVideo == null || isMiniPlayer)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Docked Mini-Player above bottom nav
                    if (selectedVideo != null && isMiniPlayer) {
                        MiniPlayerBar(
                            video = selectedVideo!!,
                            isPlaying = isPlaying,
                            currentPositionSeconds = currentPositionSeconds,
                            onMaximize = { viewModel.maximizePlayer() },
                            onTogglePlayPause = { viewModel.togglePlayPause() },
                            onClose = { viewModel.closePlayer() }
                        )
                    }

                    // YouTube 4-Tab Bottom Navigation
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        // Home
                        NavigationBarItem(
                            selected = currentTab == MainTab.HOME,
                            onClick = { viewModel.selectTab(MainTab.HOME) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            },
                            label = {
                                Text(
                                    text = if (appLanguage == AppLanguage.BENGALI) "হোম" else "Home",
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = YouTubeRed,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                indicatorColor = Color.Transparent
                            )
                        )

                        // Shorts
                        NavigationBarItem(
                            selected = currentTab == MainTab.SHORTS,
                            onClick = { viewModel.selectTab(MainTab.SHORTS) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == MainTab.SHORTS) Icons.Filled.SlowMotionVideo else Icons.Outlined.SlowMotionVideo,
                                    contentDescription = "Shorts"
                                )
                            },
                            label = {
                                Text(
                                    text = if (appLanguage == AppLanguage.BENGALI) "শর্টস" else "Shorts",
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = YouTubeRed,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                indicatorColor = Color.Transparent
                            )
                        )

                        // Subscriptions
                        NavigationBarItem(
                            selected = currentTab == MainTab.SUBSCRIPTIONS,
                            onClick = { viewModel.selectTab(MainTab.SUBSCRIPTIONS) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == MainTab.SUBSCRIPTIONS) Icons.Filled.Subscriptions else Icons.Outlined.Subscriptions,
                                    contentDescription = "Subscriptions"
                                )
                            },
                            label = {
                                Text(
                                    text = if (appLanguage == AppLanguage.BENGALI) "সাবস্ক্রিপশন" else "Subscriptions",
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = YouTubeRed,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                indicatorColor = Color.Transparent
                            )
                        )

                        // You (Library)
                        NavigationBarItem(
                            selected = currentTab == MainTab.YOU,
                            onClick = { viewModel.selectTab(MainTab.YOU) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == MainTab.YOU) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                                    contentDescription = "You"
                                )
                            },
                            label = {
                                Text(
                                    text = if (appLanguage == AppLanguage.BENGALI) "আপনি" else "You",
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = YouTubeRed,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Tab Content or Search
            if (isSearchOpen) {
                SearchScreen(
                    query = searchQuery,
                    results = searchResults,
                    appLanguage = appLanguage,
                    onQueryChange = { viewModel.updateSearchQuery(it) },
                    onBack = { viewModel.closeSearch() },
                    onVideoClick = { video ->
                        viewModel.closeSearch()
                        viewModel.playVideo(video)
                    },
                    onSaveToWatchLater = { viewModel.toggleSave(it.id) },
                    onDownload = { viewModel.downloadVideo(it.id) }
                )
            } else {
                when (currentTab) {
                    MainTab.HOME -> {
                        HomeScreen(
                            videos = filteredVideos,
                            selectedCategory = selectedCategory,
                            appLanguage = appLanguage,
                            onCategorySelected = { viewModel.selectCategory(it) },
                            onVideoClick = { viewModel.playVideo(it) },
                            onSaveToWatchLater = { viewModel.toggleSave(it.id) },
                            onDownload = { viewModel.downloadVideo(it.id) }
                        )
                    }
                    MainTab.SHORTS -> {
                        ShortsScreen(
                            shorts = shorts,
                            appLanguage = appLanguage,
                            onToggleLike = { viewModel.toggleShortLike(it) },
                            onToggleSubscribe = { viewModel.toggleShortSubscribe(it) }
                        )
                    }
                    MainTab.SUBSCRIPTIONS -> {
                        SubscriptionsScreen(
                            channels = channels,
                            videos = allVideos,
                            appLanguage = appLanguage,
                            onVideoClick = { viewModel.playVideo(it) },
                            onSaveToWatchLater = { viewModel.toggleSave(it.id) },
                            onDownload = { viewModel.downloadVideo(it.id) }
                        )
                    }
                    MainTab.YOU -> {
                        LibraryScreen(
                            watchHistory = watchHistory,
                            downloadedVideos = downloadedVideos,
                            playlists = playlists,
                            isDarkTheme = isDarkTheme,
                            useDynamicColor = useDynamicColor,
                            appLanguage = appLanguage,
                            onVideoClick = { viewModel.playVideo(it) },
                            onRemoveDownload = { viewModel.removeDownload(it) },
                            onClearHistory = { viewModel.clearHistory() },
                            onToggleTheme = { viewModel.toggleTheme() },
                            onToggleDynamicColor = { viewModel.toggleDynamicColor() },
                            onToggleLanguage = { viewModel.toggleLanguage() }
                        )
                    }
                }
            }

            // Full Video Player Overlay
            AnimatedVisibility(
                visible = selectedVideo != null && !isMiniPlayer,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                selectedVideo?.let { video ->
                    VideoPlayerView(
                        video = video,
                        allVideos = allVideos,
                        isPlaying = isPlaying,
                        currentPositionSeconds = currentPositionSeconds,
                        playbackSpeed = playbackSpeed,
                        isCaptionsEnabled = isCaptionsEnabled,
                        isDownloading = isDownloading,
                        downloadProgress = downloadProgress,
                        appLanguage = appLanguage,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onSeekTo = { viewModel.seekTo(it) },
                        onMinimize = { viewModel.minimizePlayer() },
                        onClose = { viewModel.closePlayer() },
                        onSpeedSelected = { viewModel.setPlaybackSpeed(it) },
                        onToggleCaptions = { viewModel.toggleCaptions() },
                        onToggleLike = { viewModel.toggleLike(it) },
                        onToggleDislike = { viewModel.toggleDislike(it) },
                        onToggleSubscribe = { viewModel.toggleSubscribe(it) },
                        onToggleSave = { viewModel.toggleSave(it) },
                        onDownload = { viewModel.downloadVideo(it) },
                        onAddComment = { vidId, text -> viewModel.addComment(vidId, text) },
                        onSelectNextVideo = { viewModel.playVideo(it) }
                    )
                }
            }

            // Notifications Bottom Sheet
            if (isNotificationsOpen) {
                NotificationsBottomSheet(
                    notifications = notifications,
                    appLanguage = appLanguage,
                    onDismiss = { viewModel.closeNotifications() }
                )
            }
        }
    }
}
