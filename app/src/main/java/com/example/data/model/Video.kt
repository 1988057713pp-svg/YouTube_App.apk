package com.example.data.model

data class Video(
    val id: String,
    val title: String,
    val description: String,
    val channelName: String,
    val channelAvatarUrl: String,
    val channelSubscribers: String,
    val views: String,
    val publishedTime: String,
    val duration: String,
    val durationSeconds: Int,
    val thumbnailUrl: String,
    val localDrawableRes: Int? = null,
    val videoUrl: String = "",
    val category: String,
    val likesCount: Long,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isSaved: Boolean = false,
    val isDownloaded: Boolean = false,
    val isLive: Boolean = false,
    val tags: List<String> = emptyList(),
    val comments: List<Comment> = emptyList()
)

data class ShortVideo(
    val id: String,
    val title: String,
    val channelName: String,
    val channelAvatarUrl: String,
    val likesCount: String,
    val commentsCount: String,
    val soundTitle: String,
    val isLiked: Boolean = false,
    val isSubscribed: Boolean = false,
    val videoUrl: String = "",
    val bgGradientColors: List<Long> = listOf(0xFF8E2DE2, 0xFF4A00E0)
)

data class Comment(
    val id: String,
    val userName: String,
    val userAvatarUrl: String,
    val text: String,
    val timeAgo: String,
    val likes: Int,
    val isLiked: Boolean = false
)

data class Channel(
    val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val subscribers: String,
    val isSubscribed: Boolean = false,
    val hasNewUpload: Boolean = false
)

data class Playlist(
    val id: String,
    val title: String,
    val description: String = "",
    val videoIds: List<String> = emptyList(),
    val thumbnailRes: Int? = null,
    val thumbnailUrl: String? = null,
    val isPrivate: Boolean = false,
    val isSystemPlaylist: Boolean = false
) {
    val videoCount: Int get() = videoIds.size
}

data class NotificationItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val timeAgo: String,
    val avatarUrl: String,
    val thumbnailUrl: String? = null,
    val isRead: Boolean = false
)

enum class AppLanguage {
    ENGLISH,
    BENGALI
}
