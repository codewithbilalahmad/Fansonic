package com.muhammad.fansonic.instagram_story

import com.muhammad.fansonic.R
import com.muhammad.fansonic.instagram_story.domain.Story

data class InstagramStoryState(
    val stories: List<Story> = listOf(
        Story(
            username = "mr.bilal.ahmad",
            userImage = R.drawable.user_1,
            duration = "5h",
            emojis = listOf("💻", "🔥", "🚀", "✨", "😎", "🎯"),
            desp = "Midnight coding session 🎉✨",
            media = listOf(R.drawable.story_1, R.drawable.story_2, R.drawable.story_3)
        ),
        Story(
            username = "mr.ali.khan",
            userImage = R.drawable.user_2,
            duration = "2h",
            emojis = listOf("😎", "🍔", "🍕", "😂", "🎉", "🤝"),
            desp = "Chilling with friends 😎",
            media = listOf(
                R.drawable.story_4,
                R.drawable.story_5,
                R.drawable.story_6,
                R.drawable.story_7,
                R.drawable.story_8,
            )
        ),
        Story(
            username = "mr.usman.dev",
            userImage = R.drawable.user_3,
            duration = "8h",
            emojis = listOf("🐛", "💻", "😤", "🔥", "✅", "🧠"),
            desp = "Debugging life one line at a time 💻",
            media = listOf(R.drawable.story_9, R.drawable.story_10)
        ),
        Story(
            username = "mr.hassan.ahmad",
            duration = "1h",
            userImage = R.drawable.user_4,
            emojis = listOf("☕", "💻", "📱", "🎧", "😌", "✨"),
            desp = "Coffee + Code ☕💻",
            media = listOf(
                R.drawable.story_11,
                R.drawable.story_12,
                R.drawable.story_13,
                R.drawable.story_14,
                R.drawable.story_15,
                R.drawable.story_16,
                R.drawable.story_17,
                R.drawable.story_18,
            )
        ),
        Story(
            username = "mr.zain.dev",
            userImage = R.drawable.user_5,
            duration = "3h",
            emojis = listOf("🌙", "✨", "😴", "🎶", "📸", "🌌"),
            desp = "Late night thoughts 🌙",
            media = listOf(R.drawable.story_19)
        )
    ),
)