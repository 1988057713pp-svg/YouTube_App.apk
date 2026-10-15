package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.Channel
import com.example.data.model.NotificationItem
import com.example.data.model.Playlist
import com.example.data.model.ShortVideo
import com.example.data.model.Video
import com.example.data.repository.YouTubeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    SHORTS,
    SUBSCRIPTIONS,
    YOU
}

class YouTubeViewModel(
    private val repository: YouTubeRepository = YouTubeRepository()
) : ViewModel() {

    val videos: StateFlow<List<Video>> = repository.videos
    val shorts: StateFlow<List<ShortVideo>> = repository.shorts
    val channels: StateFlow<List<Channel>> = repository.channels
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val watchHistory: StateFlow<List<Video>> = repository.watchHistory
    val downloadedVideos: StateFlow<List<Video>> = repository.downloadedVideos
    val playlists: StateFlow<List<Playlist>> = repository.playlists
    val appLanguage: StateFlow<AppLanguage> = repository.appLanguage

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedVideo = MutableStateFlow<Video?>(null)
    val selectedVideo: StateFlow<Video?> = _selectedVideo.asStateFlow()

    private val _isMiniPlayer = MutableStateFlow(false)
    val isMiniPlayer: StateFlow<Boolean> = _isMiniPlayer.asStateFlow()

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionSeconds = MutableStateFlow(0)
    val currentPositionSeconds: StateFlow<Int> = _currentPositionSeconds.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _isCaptionsEnabled = MutableStateFlow(false)
    val isCaptionsEnabled: StateFlow<Boolean> = _isCaptionsEnabled.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _useDynamicColor = MutableStateFlow(true)
    val useDynamicColor: StateFlow<Boolean> = _useDynamicColor.asStateFlow()

    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isNotificationsOpen = MutableStateFlow(false)
    val isNotificationsOpen: StateFlow<Boolean> = _isNotificationsOpen.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress: StateFlow<Float> = _downloadProgress.asStateFlow()

    private val _isCreateSheetOpen = MutableStateFlow(false)
    val isCreateSheetOpen: StateFlow<Boolean> = _isCreateSheetOpen.asStateFlow()

    private val _isUploadVideoDialogOpen = MutableStateFlow(false)
    val isUploadVideoDialogOpen: StateFlow<Boolean> = _isUploadVideoDialogOpen.asStateFlow()

    private val _isCreateShortDialogOpen = MutableStateFlow(false)
    val isCreateShortDialogOpen: StateFlow<Boolean> = _isCreateShortDialogOpen.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress.asStateFlow()

    private val _uploadSuccessMessage = MutableStateFlow<String?>(null)
    val uploadSuccessMessage: StateFlow<String?> = _uploadSuccessMessage.asStateFlow()

    val filteredVideos: StateFlow<List<Video>> = combine(videos, selectedCategory) { videoList, category ->
        if (category == "All") {
            videoList
        } else {
            videoList.filter { it.category.equals(category, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchResults: StateFlow<List<Video>> = combine(videos, searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            val q = query.lowercase().trim()
            list.filter {
                it.title.lowercase().contains(q) ||
                it.channelName.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.tags.any { tag -> tag.lowercase().contains(q) }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun playVideo(video: Video) {
        _selectedVideo.value = video
        _isMiniPlayer.value = false
        _isPlaying.value = true
        _currentPositionSeconds.value = 0
        repository.recordWatch(video)
    }

    fun togglePlayPause() {
        _isPlaying.update { !it }
    }

    fun seekTo(seconds: Int) {
        _currentPositionSeconds.value = seconds.coerceAtLeast(0)
    }

    fun minimizePlayer() {
        _isMiniPlayer.value = true
    }

    fun maximizePlayer() {
        _isMiniPlayer.value = false
    }

    fun closePlayer() {
        _selectedVideo.value = null
        _isMiniPlayer.value = false
        _isPlaying.value = false
        _currentPositionSeconds.value = 0
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun toggleCaptions() {
        _isCaptionsEnabled.update { !it }
    }

    fun toggleTheme() {
        _isDarkTheme.update { !it }
    }

    fun toggleDynamicColor() {
        _useDynamicColor.update { !it }
    }

    fun toggleLanguage() {
        repository.toggleLanguage()
    }

    fun openSearch() {
        _isSearchOpen.value = true
    }

    fun closeSearch() {
        _isSearchOpen.value = false
        _searchQuery.value = ""
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openNotifications() {
        _isNotificationsOpen.value = true
    }

    fun closeNotifications() {
        _isNotificationsOpen.value = false
    }

    fun toggleLike(videoId: String) {
        repository.toggleLike(videoId)
        _selectedVideo.value?.let { current ->
            if (current.id == videoId) {
                val wasLiked = current.isLiked
                val count = if (wasLiked) current.likesCount - 1 else current.likesCount + 1
                _selectedVideo.value = current.copy(
                    isLiked = !wasLiked,
                    likesCount = count,
                    isDisliked = if (!wasLiked) false else current.isDisliked
                )
            }
        }
    }

    fun toggleDislike(videoId: String) {
        repository.toggleDislike(videoId)
        _selectedVideo.value?.let { current ->
            if (current.id == videoId) {
                val wasLiked = current.isLiked
                val count = if (wasLiked) current.likesCount - 1 else current.likesCount
                _selectedVideo.value = current.copy(
                    isDisliked = !current.isDisliked,
                    isLiked = false,
                    likesCount = count
                )
            }
        }
    }

    fun toggleSubscribe(channelName: String) {
        repository.toggleSubscribe(channelName)
    }

    fun addComment(videoId: String, commentText: String) {
        repository.addComment(videoId, commentText)
        _selectedVideo.value?.let { current ->
            if (current.id == videoId) {
                val updated = repository.videos.value.find { it.id == videoId }
                if (updated != null) {
                    _selectedVideo.value = updated
                }
            }
        }
    }

    fun toggleSave(videoId: String) {
        repository.toggleSave(videoId)
        _selectedVideo.value?.let { current ->
            if (current.id == videoId) {
                _selectedVideo.value = current.copy(isSaved = !current.isSaved)
            }
        }
    }

    fun downloadVideo(videoId: String) {
        viewModelScope.launch {
            _isDownloading.value = true
            _downloadProgress.value = 0f
            for (step in 1..10) {
                delay(120)
                _downloadProgress.value = step / 10f
            }
            repository.downloadVideo(videoId)
            _isDownloading.value = false
            _selectedVideo.value?.let { current ->
                if (current.id == videoId) {
                    _selectedVideo.value = current.copy(isDownloaded = true)
                }
            }
        }
    }

    fun removeDownload(videoId: String) {
        repository.removeDownload(videoId)
        _selectedVideo.value?.let { current ->
            if (current.id == videoId) {
                _selectedVideo.value = current.copy(isDownloaded = false)
            }
        }
    }

    fun clearHistory() {
        repository.clearHistory()
    }

    fun toggleShortLike(shortId: String) {
        repository.toggleShortLike(shortId)
    }

    fun toggleShortSubscribe(shortId: String) {
        repository.toggleShortSubscribe(shortId)
    }
}
