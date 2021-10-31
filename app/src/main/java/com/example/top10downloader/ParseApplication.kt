package com.example.top10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplication {
    private val TAG = "ParseApplication"
    val application = ArrayList<FeedEntry>()

    fun parse(xmlData: String) : Boolean {
        Log.d(TAG, "parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (eventType) {

                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for " + tagName)
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for " + tagName)
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    application.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry() //create a new object
                            }
                                "name" -> currentRecord.name
                                "artist" -> currentRecord.artist
                                "releasedate" -> currentRecord.releaseDate
                                "summary" -> currentRecord.summary
                                "image" -> currentRecord.imageURL
                            }
                        }
                    }
                }
                // Nothing else to do
                eventType = xpp.next()
            }
            for (app in application) {
                Log.d(TAG, "**********************")
                Log.d(TAG, app.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }
}