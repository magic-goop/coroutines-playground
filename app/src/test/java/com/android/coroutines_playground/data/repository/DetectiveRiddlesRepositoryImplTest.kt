package com.android.coroutines_playground.data.repository

import com.android.coroutines_playground.data.db.MainDB
import com.android.coroutines_playground.data.db.entity.detectiveriddles.CategoryEntity
import com.android.coroutines_playground.data.db.entity.detectiveriddles.QuestionEntity
import com.android.coroutines_playground.data.db.entity.mapper.CategoryEntityMapper
import com.android.coroutines_playground.data.db.entity.mapper.QuestionEntityMapper
import com.android.coroutines_playground.domain.model.result.CategoriesResult
import com.android.coroutines_playground.domain.model.result.QuestionResult
import com.android.coroutines_playground.domain.repository.DetectiveRiddlesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DetectiveRiddlesRepositoryImplTest {

    companion object {
        private val dummyQuestionEntity = QuestionEntity(
                id = 0,
                categoryId = 0,
                title = "Title",
                description = "Description",
                image = null,
                question = "Question",
                answer = "Answer",
                isAnswered = 0
        )

        private val dummyCategoryEntity = CategoryEntity(
                id = 0,
                title = "Title",
                description = "Description",
                image = null,
                priority = 0,
                total = 0,
                answered = 0
        )
    }

    private val mockMainDB: MainDB = mockk()
    private val categoriesMapper: CategoryEntityMapper = spyk(CategoryEntityMapper())
    private val questionsMapper: QuestionEntityMapper = spyk(QuestionEntityMapper())

    private lateinit var repository: DetectiveRiddlesRepository

    @Before
    fun setup() {
        repository = DetectiveRiddlesRepositoryImpl(
            mockMainDB, categoriesMapper, questionsMapper
        )
    }

    @Test
    fun `when executing getQuestions should return List data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getQuestions(any())
        } returns listOf(dummyQuestionEntity, dummyQuestionEntity.copy(id = 1))

        repository.getQuestions(0).let {
            assertEquals(it is QuestionResult.Get.List, true)
            assertEquals((it as QuestionResult.Get.List).question.rows.size, 2)
        }
    }

    @Test
    fun `when executing getQuestions should return None data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getQuestions(any())
        } returns emptyList()

        repository.getQuestions(0).let {
            assertEquals(it is QuestionResult.Get.None, true)
        }
    }

    @Test
    fun `when executing getQuestions should return Failure data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getQuestions(any())
        }.throws(KotlinNullPointerException())

        repository.getQuestions(0).let {
            assertEquals(it is QuestionResult.Get.Failure, true)
        }
    }

    @Test
    fun `when executing getQuestion should return Data data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getQuestion(any())
        } returns dummyQuestionEntity

        repository.getQuestion(0).let {
            assertEquals(it is QuestionResult.GetOne.Data, true)
        }
    }

    @Test
    fun `when executing getQuestion should return Failure data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getQuestion(any())
        }.throws(KotlinNullPointerException())

        repository.getQuestion(0).let {
            assertEquals(it is QuestionResult.GetOne.Failure, true)
        }
    }

    @Test
    fun `when executing switchQuestionAnswer should return Data data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getQuestion(any())
        } returns dummyQuestionEntity
        every {
            mockMainDB.detectiveRiddlesDao().switchQuestionAnswer(any(), any())
        } returns Unit

        repository.switchQuestionAnswer(0, 1).let {
            assertEquals(it is QuestionResult.GetOne.Data, true)
        }
    }

    @Test
    fun `when executing switchQuestionAnswer should return Failure data type result`() =
            runBlocking {
                every {
                    mockMainDB.detectiveRiddlesDao().getQuestion(any())
                }.throws(KotlinNullPointerException())
                every {
                    mockMainDB.detectiveRiddlesDao().switchQuestionAnswer(any(), any())
                } returns Unit

                repository.switchQuestionAnswer(0, 1).let {
                    assertEquals(it is QuestionResult.GetOne.Failure, true)
                }
            }

    @Test
    fun `when executing getCategories should return List data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getCategories()
        } returns listOf(dummyCategoryEntity, dummyCategoryEntity.copy(id = 1))

        repository.getCategories().let {
            assertEquals(it is CategoriesResult.Get.List, true)
            assertEquals((it as CategoriesResult.Get.List).result.rows.size, 2)
        }
    }

    @Test
    fun `when executing getCategories should return None data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getCategories()
        } returns emptyList()

        repository.getCategories().let {
            assertEquals(it is CategoriesResult.Get.None, true)
        }
    }

    @Test
    fun `when executing getCategories should return Failure data type result`() = runBlocking {
        every {
            mockMainDB.detectiveRiddlesDao().getCategories()
        }.throws(KotlinNullPointerException())

        repository.getCategories().let {
            assertEquals(it is CategoriesResult.Get.Failure, true)
        }
    }

    @Test
    fun `when executing loadNextQuestion should return Data data type result with id equals to 0`() =
            runBlocking {
                every {
                    mockMainDB.detectiveRiddlesDao().getQuestions(any())
                } returns listOf(
                        dummyQuestionEntity.copy(isAnswered = 1),
                        dummyQuestionEntity.copy(id = 1, isAnswered = 1)
                )

                repository.loadNextQuestion(1, 1).let {
                    assertEquals(it is QuestionResult.GetOne.Data, true)
                    assertEquals((it as QuestionResult.GetOne.Data).some.id, 0)
                }
            }
}