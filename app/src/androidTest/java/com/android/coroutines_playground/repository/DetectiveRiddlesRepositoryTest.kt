package com.android.coroutines_playground.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.android.coroutines_playground.data.db.MainDB
import com.android.coroutines_playground.data.db.entity.detectiveriddles.CategoryEntity
import com.android.coroutines_playground.data.db.entity.detectiveriddles.QuestionEntity
import com.android.coroutines_playground.data.db.entity.mapper.CategoryEntityMapper
import com.android.coroutines_playground.data.db.entity.mapper.QuestionEntityMapper
import com.android.coroutines_playground.data.repository.DetectiveRiddlesRepositoryImpl
import com.android.coroutines_playground.domain.model.result.CategoriesResult
import com.android.coroutines_playground.domain.model.result.QuestionResult
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetectiveRiddlesRepositoryTest {

    private lateinit var db: MainDB

    private lateinit var detectiveRiddlesRepositoryImpl: DetectiveRiddlesRepositoryImpl

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MainDB::class.java
        ).allowMainThreadQueries().build()

        val categories = mutableListOf<CategoryEntity>()
        for (i in 1..4) {
            categories.add(
                CategoryEntity(
                    i,
                    "title_$i",
                    "description_$i",
                    null,
                    i,
                    4,
                    0
                )
            )
        }

        val questions = mutableListOf<QuestionEntity>()
        for (i in 1..12) {
            val catID: Int = when (i) {
                in 1..3 -> 1
                in 4..6 -> 2
                in 7..9 -> 3
                else -> 4
            }
            questions.add(
                QuestionEntity(
                    i, catID, "question_$i", "desc_$i",
                    null, "question", "answer", 0
                )
            )
        }
        db.runInTransaction {
            db.detectiveRiddlesDao().insertCategories(categories)
            db.detectiveRiddlesDao().insertQuestions(questions)
        }

        detectiveRiddlesRepositoryImpl = DetectiveRiddlesRepositoryImpl(
            db,
            CategoryEntityMapper(),
            QuestionEntityMapper()
        )
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun get_categories_is_not_null() = runBlocking {
        detectiveRiddlesRepositoryImpl.getCategories().let {
            Assert.assertNotNull(it)
        }
    }

    @Test
    fun get_categories_is_instance_of_CategoriesResult() = runBlocking {
        detectiveRiddlesRepositoryImpl.getCategories().let {
            Assert.assertThat(it, instanceOf(CategoriesResult.Get::class.java))
        }
    }

    @Test
    fun get_categories() = runBlocking {
        detectiveRiddlesRepositoryImpl.getCategories().let {
            when (it) {
                is CategoriesResult.Get.List -> {
                    Assert.assertNotNull(it.result)
                    Assert.assertNotNull(it.result.rows)
                    Assert.assertEquals(4, it.result.rows.size)
                }
                else -> {
                    // this will cause test to fail
                    Assert.assertThat(it, instanceOf(CategoriesResult.Get.List::class.java))
                }
            }
        }
    }

    @Test
    fun get_questions_is_not_null() = runBlocking {
        detectiveRiddlesRepositoryImpl.getQuestions(1).let {
            Assert.assertNotNull(it)
        }
    }

    @Test
    fun get_questions_is_instance_of_QuestionResult() = runBlocking {
        detectiveRiddlesRepositoryImpl.getQuestions(1).let {
            Assert.assertThat(it, instanceOf(QuestionResult::class.java))
        }
    }

    @Test
    fun get_questions_with_valid_category_id() = runBlocking {
        detectiveRiddlesRepositoryImpl.getQuestions(1).let {
            when (it) {
                is QuestionResult.Get.List -> {
                    Assert.assertNotNull(it.question)
                    Assert.assertNotNull(it.question.rows)
                    Assert.assertEquals(3, it.question.rows.size)
                }
            }
        }
    }

    @Test
    fun get_questions_with_invalid_category_id() = runBlocking {
        detectiveRiddlesRepositoryImpl.getQuestions(50).let {
            Assert.assertThat(it, instanceOf(QuestionResult.Get.None::class.java))
        }
    }

    @Test
    fun get_question_with_valid_id_is_not_null() = runBlocking {
        detectiveRiddlesRepositoryImpl.getQuestion(1).let {
            Assert.assertNotNull(it)
        }
    }

    @Test
    fun get_question_with_valid_id_is_instance_of_QuestionResult_GetOne() = runBlocking {
        detectiveRiddlesRepositoryImpl.getQuestion(1).let {
            Assert.assertThat(it, instanceOf(QuestionResult.GetOne::class.java))
        }
    }

    @Test
    fun get_question_with_invalid_id() = runBlocking {
        detectiveRiddlesRepositoryImpl.getQuestion(50).let {
            Assert.assertThat(it, instanceOf(QuestionResult.GetOne.Failure::class.java))
        }
    }

    @Test
    fun switch_question_answer() = runBlocking {
        detectiveRiddlesRepositoryImpl.switchQuestionAnswer(1, 0)
        detectiveRiddlesRepositoryImpl.getQuestion(1).let {
            Assert.assertEquals(true, (it as QuestionResult.GetOne.Data).some.isAnswered)
        }
        detectiveRiddlesRepositoryImpl.switchQuestionAnswer(1, 1)
        detectiveRiddlesRepositoryImpl.getQuestion(1).let {
            Assert.assertEquals(false, (it as QuestionResult.GetOne.Data).some.isAnswered)
        }
    }

    @Test
    fun load_next_question_is_not_null() = runBlocking {
        detectiveRiddlesRepositoryImpl.loadNextQuestion(1, 1).let {
            Assert.assertNotNull(it)
        }
    }

    @Test
    fun load_next_question_without_changing_answer_flag() = runBlocking {
        val q = detectiveRiddlesRepositoryImpl.getQuestion(1)
        val cQuestion = (q as QuestionResult.GetOne.Data).some
        detectiveRiddlesRepositoryImpl.loadNextQuestion(cQuestion.id, cQuestion.categoryId)
            .let {
                val nQuestion = (it as QuestionResult.GetOne.Data).some
                Assert.assertEquals(cQuestion.categoryId, nQuestion.categoryId)
                Assert.assertEquals(cQuestion.id, nQuestion.id)
            }
    }

    @Test
    fun load_next_question_with_changing_answer_flag() = runBlocking {
        val q = detectiveRiddlesRepositoryImpl.getQuestion(1)
        val cQuestion = (q as QuestionResult.GetOne.Data).some
        detectiveRiddlesRepositoryImpl.switchQuestionAnswer(
            cQuestion.id,
            if (cQuestion.isAnswered) 1 else 0
        )
        detectiveRiddlesRepositoryImpl.loadNextQuestion(
            cQuestion.id,
            cQuestion.categoryId
        ).let {
            val nQuestion = (it as QuestionResult.GetOne.Data).some
            Assert.assertEquals(cQuestion.categoryId, nQuestion.categoryId)
            Assert.assertNotEquals(cQuestion.id, nQuestion.id)
        }
    }
}