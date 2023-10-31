package com.hjz.storyapp

import com.hjz.storyapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoriesResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                i.toString(),
                "createdAt + $i",
                "name $i",
                "description $i",
                "lon $i",
                "id $i",
                "lat $i"
            )
            items.add(stories)
        }
        return items
    }
}
