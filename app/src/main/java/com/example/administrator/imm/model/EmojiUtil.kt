package com.example.administrator.imm.model

import android.content.Context

class EmojiUtil {

    companion object {
        // 按分类生成 Emoji 列表
        fun generateEmojisByCategory(context: Context): Map<String, List<String>> {
            val categoryMap = mutableMapOf<String, List<String>>()
            getAllEmojiGroups().forEach { category ->
                val emojis = mutableListOf<String>()
                category.codeRanges.forEach { range ->
                    range.forEach { codePoint ->
                        if (!category.excludeCodePoints.contains(codePoint)) {
                            // 过滤无效字符
                            if (isValidEmoji(codePoint)) {
                                emojis.add(String(Character.toChars(codePoint)))
                            }
                        }
                    }
                }
                categoryMap[category.groupName] = emojis.distinct()
            }
            return categoryMap
        }

        // 验证是否为有效 Emoji（过滤控制字符）
        private fun isValidEmoji(codePoint: Int): Boolean {
            return when (Character.getType(codePoint)) {
                Character.SURROGATE.toInt(), Character.OTHER_SYMBOL.toInt() -> true
                else -> false
            }
        }

        /*data class EmojiCategory(
            val name: String, val emojis: List<String>
        )*/

        fun loadCategorizedEmojis(): List<EmojiCategory> {
            return listOf(

                /*EmojiCategory(
                    name = "笑脸表情  Smileys & Emotion",
                    emojis = (0x1F600..0x1F64F).map {
                        String(Character.toChars(it))
                    }),
                EmojiCategory(name = "People & Body",
                    emojis = listOf(0x1F46B..0x1F480, 0x1F481..0x1F64F).flatMap { range ->
                        range.map { String(Character.toChars(it)) }
                    }),

                EmojiCategory(name = "动物Animals & Nature",
                    emojis = listOf(0x1F400..0x1F43F, 0x1F980..0x1F9AE).flatMap { range ->
                        range.map { String(Character.toChars(it)) }
                    }),*/
                /*EmojiCategory(name = "Food & Drink", emojis = (0x1F32D..0x1F37F).map {
                    String(Character.toChars(it))
                }),*/
                /*EmojiCategory(name = "Travel & Places",
                    emojis = listOf(0x1F680..0x1F6FF, 0x2600..0x26FF).flatMap { range ->
                        range.map { String(Character.toChars(it)) }
                    }),*/

                /*EmojiCategory(name = "Activities",
                    emojis = listOf(0x1F383..0x1F3C6, 0x1F3CA..0x1F3E0).flatMap { range ->
                        range.map { String(Character.toChars(it)) }
                    }),*/
                /*EmojiCategory(name = "Objects",
                    emojis = listOf(0x1F4A1..0x1F4F7, 0x1F52B..0x1F6D6).flatMap { range ->
                        range.map { String(Character.toChars(it)) }
                    }),
                EmojiCategory(name = "Symbols",
                    emojis = listOf(0x1F300..0x1F5FF, 0x1F6A9..0x1F6B9).flatMap { range ->
                        range.map { String(Character.toChars(it)) }
                    }),*/
                /*EmojiCategory(name = "Flags",
                    emojis = (0x1F1E6..0x1F1FFF).map {
                        String(Character.toChars(it))
                    }),*/
                /*EmojiCategory(name = "Skin Tones & Modifiers",
                    emojis = (0x1F3FB..0x1F3FF).map {
                        String(Character.toChars(it))
                    }),*/
            )
        }

        fun test(ctx: Context) {
            /*val categories = loadCategorizedEmojis()
            categories.forEach {
//                println("${it.name}: ${it.emojis.take(50)}")
                println("${it.name}: ${it.emojis.take(it.emojis.size)}")
            }*/
            // 使用示例
            val emojiMap = generateEmojisByCategory(ctx)
            emojiMap["Flags"]?.take(5)?.forEach { println(it) } // 输出前5个国旗
        }

        // 定义 Emoji 分类数据类
        data class EmojiCategory(
            val groupName: String,
            val codeRanges: List<IntRange>,
            val excludeCodePoints: List<Int> = emptyList() // 排除特定码点
        )

        // 初始化所有分类
        fun getAllEmojiGroups(): List<EmojiCategory> {
            return listOf(
                EmojiCategory(
                    "Smileys & Emotion",
                    codeRanges = listOf(0x1F600..0x1F64F),
                    excludeCodePoints = listOf(0x1F6D0) // 排除宗教符号等
                ),
                EmojiCategory(
                    "Animals & Nature",
                    codeRanges = listOf(0x1F400..0x1F43F, 0x1F980..0x1F9AE)
                ),
                EmojiCategory(
                    "Flags",
                    codeRanges = listOf(0x1F1E6..0x1F1FF),
                    excludeCodePoints = listOf(0x1F1F4, 0x1F1E7) // 排除无效组合
                )
                // 添加其他分类...
            )
        }


    }


}