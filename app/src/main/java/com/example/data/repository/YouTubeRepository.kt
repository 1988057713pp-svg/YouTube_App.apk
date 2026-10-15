package com.example.data.repository

import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.Channel
import com.example.data.model.Comment
import com.example.data.model.NotificationItem
import com.example.data.model.Playlist
import com.example.data.model.ShortVideo
import com.example.data.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class YouTubeRepository {

    private val initialComments = listOf(
        Comment(
            id = "c1",
            userName = "Tanvir Ahmed",
            userAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
            text = "অসাধারণ একটি ভিডিও! অনেক কিছু জানতে পারলাম, ধন্যবাদ ভাই। ❤️🔥",
            timeAgo = "২ ঘন্টা আগে",
            likes = 342,
            isLiked = false
        ),
        Comment(
            id = "c2",
            userName = "TechExplorer",
            userAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=100&q=80",
            text = "The camera comparison and gaming performance test was top notch! Subscribed!",
            timeAgo = "5 hours ago",
            likes = 89,
            isLiked = false
        ),
        Comment(
            id = "c3",
            userName = "Sadia Islam",
            userAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&q=80",
            text = "এই গানটার লিরিক্স মন ছুয়ে গেল। বারবার শুনছি!",
            timeAgo = "১ দিন আগে",
            likes = 124,
            isLiked = true
        ),
        Comment(
            id = "c4",
            userName = "Alex Rivera",
            userAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&q=80",
            text = "Can't wait for episode 2! Truly mind-blowing visuals.",
            timeAgo = "2 days ago",
            likes = 45,
            isLiked = false
        )
    )

    private val initialVideos = listOf(
        Video(
            id = "v1",
            title = "Next-Gen Smartphone 2026: Future Tech, AI Processor & 200MP Camera Test!",
            description = "In this video we review the most advanced futuristic smartphone featuring next-gen neural silicon, cybernetic holographic display, ultra-fast charging and studio-grade 200MP optics.",
            channelName = "TechPulse Global",
            channelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80",
            channelSubscribers = "3.45M subscribers",
            views = "842K views",
            publishedTime = "1 day ago",
            duration = "14:28",
            durationSeconds = 868,
            thumbnailUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800&q=80",
            localDrawableRes = R.drawable.video_thumb_tech_1792003591930,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            category = "Tech",
            likesCount = 48200,
            tags = listOf("#TechReview", "#NextGen", "#Smartphones", "#AI2026"),
            comments = initialComments
        ),
        Video(
            id = "v2",
            title = "Harmony Fest 2026 - লাইভ কনসার্ট ও সেরা রোমান্টিক বাংলা গান | Live Acoustic Session",
            description = "হারমনি ফেস্ট ২০২৬ এর জমকালো উন্মাদনায় মঞ্চ মাতানো সেরা গানের এক অপূর্ব সমাহার। গিটারের সুর আর হাজারো দর্শকের উচ্ছ্বাসে উপভোগ করুন পুরো কনসার্ট!",
            channelName = "SoundStage Bangla",
            channelAvatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=100&q=80",
            channelSubscribers = "1.89M subscribers",
            views = "1.2M views",
            publishedTime = "৩ দিন আগে",
            duration = "22:15",
            durationSeconds = 1335,
            thumbnailUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&q=80",
            localDrawableRes = R.drawable.video_thumb_music_1792003617305,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            category = "Music",
            likesCount = 92400,
            tags = listOf("#BanglaMusic", "#LiveConcert", "#HarmonyFest", "#Acoustic"),
            comments = initialComments
        ),
        Video(
            id = "v3",
            title = "Bangladesh Cricket Thriller: শেষ ওভারের রুদ্ধশ্বাস জয়! Match Winning Highlights",
            description = "শেষ ওভারে দরকার ছিল ১২ রান! মাঠে ছিল চরম উত্তেজনা। দেখুন টাইগারদের ইতিহাস সৃষ্টিকারী সেই অসাধারণ ফিনিশ ও ট্রফি জয়ের মুহূর্ত।",
            channelName = "BCB Sports Bangla",
            channelAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&q=80",
            channelSubscribers = "5.12M subscribers",
            views = "3.8M views",
            publishedTime = "৫ ঘন্টা আগে",
            duration = "09:40",
            durationSeconds = 580,
            thumbnailUrl = "https://images.unsplash.com/photo-1531415074868-036b10f01a40?w=800&q=80",
            localDrawableRes = null,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            category = "Cricket",
            likesCount = 210500,
            tags = listOf("#Cricket", "#Bangladesh", "#Highlights", "#Tigers"),
            comments = initialComments
        ),
        Video(
            id = "v4",
            title = "মজার কমেডি নাটক ২০২৬ | হাসতে হাসতে পেট ব্যথা হবেই! Superhit Bangla Eid Natok",
            description = "গ্রামের মোড়ের চায়ের দোকান থেকে শুরু হয়ে অদ্ভুত সব কাণ্ডকারখানা! পরিবারের সবাইকে নিয়ে দেখার মতো সুস্থ বিনোদনের কমেডি নাটক।",
            channelName = "Bangla Natok Entertainment",
            channelAvatarUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&q=80",
            channelSubscribers = "2.41M subscribers",
            views = "2.5M views",
            publishedTime = "৪ দিন আগে",
            duration = "38:45",
            durationSeconds = 2325,
            thumbnailUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=800&q=80",
            localDrawableRes = null,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            category = "Bangla Natok",
            likesCount = 145000,
            tags = listOf("#BanglaNatok", "#Comedy", "#Natok2026", "#BanglaDrama"),
            comments = initialComments
        ),
        Video(
            id = "v5",
            title = "$1,000,000 Island Survival: 100 People vs The Wildest Storm Ever!",
            description = "We put 100 subscribers on an island with nothing. The last one standing wins a million dollars cash! Watch till the end to see the craziest final showdown.",
            channelName = "MrBeast",
            channelAvatarUrl = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=100&q=80",
            channelSubscribers = "310M subscribers",
            views = "48M views",
            publishedTime = "2 days ago",
            duration = "24:10",
            durationSeconds = 1450,
            thumbnailUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
            localDrawableRes = null,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            category = "Gaming",
            likesCount = 3800000,
            tags = listOf("#MrBeast", "#Challenge", "#Island", "#MillionDollars"),
            comments = initialComments
        ),
        Video(
            id = "v6",
            title = "Learn Jetpack Compose & Android in 30 Minutes: Zero to Hero Masterclass",
            description = "Build a modern production-ready Android app using Kotlin, Material 3, Navigation, Coroutines, and MVVM architecture from scratch with live hands-on examples.",
            channelName = "Android Devs Hub",
            channelAvatarUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=100&q=80",
            channelSubscribers = "750K subscribers",
            views = "320K views",
            publishedTime = "1 week ago",
            duration = "31:18",
            durationSeconds = 1878,
            thumbnailUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&q=80",
            localDrawableRes = null,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            category = "Tech",
            likesCount = 28900,
            tags = listOf("#Android", "#JetpackCompose", "#Kotlin", "#Coding"),
            comments = initialComments
        ),
        Video(
            id = "v7",
            title = "Lo-Fi Beats 24/7 - Relaxing Chill Music to Study, Code & Sleep ☕",
            description = "Peaceful beats and soothing lo-fi hip hop melodies designed to keep you focused and relaxed throughout your day and night.",
            channelName = "ChillVibes Radio",
            channelAvatarUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=100&q=80",
            channelSubscribers = "4.2M subscribers",
            views = "15K watching now",
            publishedTime = "Started streaming 2 hours ago",
            duration = "LIVE",
            durationSeconds = 0,
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80",
            localDrawableRes = null,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            category = "Live",
            likesCount = 89000,
            isLive = true,
            tags = listOf("#Lofi", "#Chill", "#StudyBeats", "#Live"),
            comments = initialComments
        )
    )

    private val initialShorts = listOf(
        ShortVideo(
            id = "s1",
            title = "Wait for the unbelievable finish! 😱🔥 #shorts #cricket #epic",
            channelName = "SportsZone Bangla",
            channelAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&q=80",
            likesCount = "1.4M",
            commentsCount = "12.8K",
            soundTitle = "Original Audio - Tigers Victory Beat",
            bgGradientColors = listOf(0xFFFF416C, 0xFFFF4B2B)
        ),
        ShortVideo(
            id = "s2",
            title = "Testing the holographic laser keyboard in 2026! 🚀📱",
            channelName = "TechPulse Global",
            channelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80",
            likesCount = "890K",
            commentsCount = "5.4K",
            soundTitle = "Cyber Future Synth - NeonWave",
            bgGradientColors = listOf(0xFF8E2DE2, 0xFF4A00E0)
        ),
        ShortVideo(
            id = "s3",
            title = "এই ম্যাজিক ট্রিকটা দেখার পর চোখ কপালে উঠবে! 🤯✨ #magic",
            channelName = "Bangla Illusionist",
            channelAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&q=80",
            likesCount = "2.1M",
            commentsCount = "18.2K",
            soundTitle = "Mystic Melody - Bengali Beats",
            bgGradientColors = listOf(0xFF11998E, 0xFF38EF7D)
        ),
        ShortVideo(
            id = "s4",
            title = "Giving $50,000 to random students who can answer this question! 💸",
            channelName = "MrBeast",
            channelAvatarUrl = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=100&q=80",
            likesCount = "6.5M",
            commentsCount = "45.1K",
            soundTitle = "MrBeast Epic Hype Theme",
            bgGradientColors = listOf(0xFFF12711, 0xFFF5AF19)
        )
    )

    private val initialChannels = listOf(
        Channel(
            id = "ch1",
            name = "TechPulse Global",
            handle = "@techpulse",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80",
            subscribers = "3.45M",
            isSubscribed = true,
            hasNewUpload = true
        ),
        Channel(
            id = "ch2",
            name = "SoundStage Bangla",
            handle = "@soundstagebangla",
            avatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=100&q=80",
            subscribers = "1.89M",
            isSubscribed = true,
            hasNewUpload = true
        ),
        Channel(
            id = "ch3",
            name = "MrBeast",
            handle = "@MrBeast",
            avatarUrl = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=100&q=80",
            subscribers = "310M",
            isSubscribed = true,
            hasNewUpload = false
        ),
        Channel(
            id = "ch4",
            name = "BCB Sports Bangla",
            handle = "@bcbsports",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&q=80",
            subscribers = "5.12M",
            isSubscribed = true,
            hasNewUpload = true
        ),
        Channel(
            id = "ch5",
            name = "Bangla Natok Entertainment",
            handle = "@banglanatok",
            avatarUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&q=80",
            subscribers = "2.41M",
            isSubscribed = false,
            hasNewUpload = false
        )
    )

    private val initialNotifications = listOf(
        NotificationItem(
            id = "n1",
            title = "TechPulse Global uploaded a new video",
            subtitle = "Next-Gen Smartphone 2026: Future Tech & 200MP Camera Test!",
            timeAgo = "1 hour ago",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300&q=80"
        ),
        NotificationItem(
            id = "n2",
            title = "BCB Sports Bangla is LIVE now!",
            subtitle = "Live: Press Conference & Squad Announcement",
            timeAgo = "3 hours ago",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1531415074868-036b10f01a40?w=300&q=80"
        ),
        NotificationItem(
            id = "n3",
            title = "SoundStage Bangla responded to your comment",
            subtitle = "❤️ Loved your review of Harmony Fest!",
            timeAgo = "Yesterday",
            avatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=100&q=80"
        )
    )

    private val _videos = MutableStateFlow(initialVideos)
    val videos: StateFlow<List<Video>> = _videos.asStateFlow()

    private val _shorts = MutableStateFlow(initialShorts)
    val shorts: StateFlow<List<ShortVideo>> = _shorts.asStateFlow()

    private val _channels = MutableStateFlow(initialChannels)
    val channels: StateFlow<List<Channel>> = _channels.asStateFlow()

    private val _notifications = MutableStateFlow(initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _watchHistory = MutableStateFlow<List<Video>>(listOf(initialVideos[0], initialVideos[1]))
    val watchHistory: StateFlow<List<Video>> = _watchHistory.asStateFlow()

    private val _downloadedVideos = MutableStateFlow<List<Video>>(emptyList())
    val downloadedVideos: StateFlow<List<Video>> = _downloadedVideos.asStateFlow()

    private val _playlists = MutableStateFlow<List<Playlist>>(
        listOf(
            Playlist("p1", "Liked Videos", 142, isPrivate = true),
            Playlist("p2", "Watch Later", 18, isPrivate = true),
            Playlist("p3", "Bangla Hits & Natok", 34, isPrivate = false),
            Playlist("p4", "Tech & Coding Tutorials", 12, isPrivate = false)
        )
    )
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    private val _appLanguage = MutableStateFlow(AppLanguage.BENGALI)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    fun toggleLanguage() {
        _appLanguage.update { if (it == AppLanguage.BENGALI) AppLanguage.ENGLISH else AppLanguage.BENGALI }
    }

    fun toggleLike(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val wasLiked = video.isLiked
                    val newLikes = if (wasLiked) video.likesCount - 1 else video.likesCount + 1
                    video.copy(
                        isLiked = !wasLiked,
                        likesCount = newLikes,
                        isDisliked = if (!wasLiked) false else video.isDisliked
                    )
                } else video
            }
        }
    }

    fun toggleDislike(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val wasDisliked = video.isDisliked
                    val wasLiked = video.isLiked
                    val newLikes = if (wasLiked) video.likesCount - 1 else video.likesCount
                    video.copy(
                        isDisliked = !wasDisliked,
                        isLiked = false,
                        likesCount = newLikes
                    )
                } else video
            }
        }
    }

    fun toggleSubscribe(channelName: String) {
        _channels.update { list ->
            list.map { channel ->
                if (channel.name == channelName) {
                    channel.copy(isSubscribed = !channel.isSubscribed)
                } else channel
            }
        }
    }

    fun addComment(videoId: String, commentText: String) {
        if (commentText.isBlank()) return
        val newComment = Comment(
            id = "c_${System.currentTimeMillis()}",
            userName = "Mamun Gaming",
            userAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
            text = commentText.trim(),
            timeAgo = "Just now",
            likes = 1,
            isLiked = true
        )
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(comments = listOf(newComment) + video.comments)
                } else video
            }
        }
    }

    fun toggleSave(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(isSaved = !video.isSaved)
                } else video
            }
        }
    }

    fun downloadVideo(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val updated = video.copy(isDownloaded = true)
                    if (_downloadedVideos.value.none { it.id == videoId }) {
                        _downloadedVideos.update { it + updated }
                    }
                    updated
                } else video
            }
        }
    }

    fun removeDownload(videoId: String) {
        _videos.update { list ->
            list.map { if (it.id == videoId) it.copy(isDownloaded = false) else it }
        }
        _downloadedVideos.update { list -> list.filter { it.id != videoId } }
    }

    fun recordWatch(video: Video) {
        _watchHistory.update { list ->
            val filtered = list.filter { it.id != video.id }
            listOf(video) + filtered
        }
    }

    fun toggleShortLike(shortId: String) {
        _shorts.update { list ->
            list.map { short ->
                if (short.id == shortId) {
                    short.copy(isLiked = !short.isLiked)
                } else short
            }
        }
    }

    fun toggleShortSubscribe(shortId: String) {
        _shorts.update { list ->
            list.map { short ->
                if (short.id == shortId) {
                    short.copy(isSubscribed = !short.isSubscribed)
                } else short
            }
        }
    }

    fun clearHistory() {
        _watchHistory.update { emptyList() }
    }

    fun uploadVideo(
        title: String,
        description: String,
        category: String,
        thumbnailUrl: String? = null,
        videoUriString: String? = null
    ): Video {
        val newVideo = Video(
            id = "user_v_${System.currentTimeMillis()}",
            title = title,
            description = description.ifBlank { "Uploaded by Mamun Gaming" },
            channelName = "Mamun Gaming",
            channelAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
            channelSubscribers = "1 subscriber",
            views = "1 view",
            publishedTime = "Just now",
            duration = "03:45",
            durationSeconds = 225,
            thumbnailUrl = thumbnailUrl ?: "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800&q=80",
            localDrawableRes = null,
            videoUrl = videoUriString ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            category = category,
            likesCount = 0,
            tags = listOf("#MamunGaming", "#$category", "#NewVideo")
        )
        _videos.update { listOf(newVideo) + it }
        return newVideo
    }

    fun createShort(
        title: String,
        soundTitle: String
    ): ShortVideo {
        val newShort = ShortVideo(
            id = "user_s_${System.currentTimeMillis()}",
            title = title,
            channelName = "Mamun Gaming",
            channelAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
            likesCount = "1",
            commentsCount = "0",
            soundTitle = soundTitle.ifBlank { "Original Sound - Mamun Gaming" },
            bgGradientColors = listOf(0xFFFF416C, 0xFF8A2387)
        )
        _shorts.update { listOf(newShort) + it }
        return newShort
    }
}

