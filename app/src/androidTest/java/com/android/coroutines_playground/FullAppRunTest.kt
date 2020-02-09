package com.android.coroutines_playground

import android.os.SystemClock
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.android.coroutines_playground.data.db.Common
import com.android.coroutines_playground.data.db.MainDB
import com.android.coroutines_playground.presentation.activitycontainer.MainActivity
import com.android.coroutines_playground.presentation.categories.CategoriesFragment
import com.fstyle.library.helper.AssetSQLiteOpenHelperFactory
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.interaction.BaristaScrollInteractions.scrollTo
import com.schibsted.spain.barista.interaction.BaristaViewPagerInteractions.swipeViewPagerForward
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FullAppRunTest {

    companion object {
        private const val SHORT_PAUSE: Long = 300
        private const val MEDIUM_PAUSE: Long = 5000
        private const val LONG_PAUSE: Long = 1000
    }

    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private lateinit var db: MainDB
    private var categoriesCount: Int = 0
    private var sizeMap = mutableMapOf<Int, Int>()

    @Before
    fun init() {
        db = Room.databaseBuilder(
            activityRule.activity.applicationContext, MainDB::class.java,
            Common.DATABASE_NAME
        )
            .openHelperFactory(AssetSQLiteOpenHelperFactory())
            .build()

        db.runInTransaction {
            db.detectiveRiddlesDao().setAllQuestionsToNotAnswered()
        }

        val categories = db.detectiveRiddlesDao().getCategories().sortedBy { it.priority }
        categoriesCount = categories.size

        var index = 0
        for (cat in categories) {
            sizeMap[index++] = db.detectiveRiddlesDao().getQuestions(cat.id).size + 1
        }

        activityRule
            .activity
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, CategoriesFragment())
            .commit()
    }

    @Test
    fun fullAppTest() {
        SystemClock.sleep(SHORT_PAUSE)

        for (i in 0 until categoriesCount) {
            scrollListToPosition(R.id.rvCategories, i)
            clickListItem(R.id.rvCategories, i)

            SystemClock.sleep(SHORT_PAUSE)

            val startQuestionIndex = 1

            scrollListToPosition(R.id.rvQuestions, startQuestionIndex)
            clickListItem(R.id.rvQuestions, startQuestionIndex)
            SystemClock.sleep(SHORT_PAUSE)

            for (j in startQuestionIndex until sizeMap[i]!!) {

                try {
                    assertDisplayed(R.id.rvImages)
                    clickOn(R.id.rvImages)

                    clickOn(R.id.imageViewerPager)
                    clickOn(R.id.imageViewerPager)

                    for (swipe in 0 until 6) {
                        swipeViewPagerForward()
                    }
                    clickBack()
                } catch (e: Throwable) {
                }
                SystemClock.sleep(SHORT_PAUSE)
                scrollTo(R.id.btnAnswer)
                clickOn(R.id.btnAnswer)
                SystemClock.sleep(SHORT_PAUSE)
                clickOn(R.id.btnNextQuestion)
            }
            clickBack()
            clickBack()
            SystemClock.sleep(SHORT_PAUSE)
        }
        SystemClock.sleep(SHORT_PAUSE)
    }
}