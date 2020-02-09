package com.android.coroutines_playground.di

import android.content.Context
import androidx.room.Room
import com.android.coroutines_playground.data.db.Common
import com.android.coroutines_playground.data.db.MainDB
import com.android.coroutines_playground.data.db.entity.mapper.CategoryEntityMapper
import com.android.coroutines_playground.data.db.entity.mapper.QuestionEntityMapper
import com.android.coroutines_playground.data.repository.DetectiveRiddlesRepositoryImpl
import com.android.coroutines_playground.domain.interactor.DetectiveRiddlesInteractor
import com.android.coroutines_playground.domain.repository.DetectiveRiddlesRepository
import com.android.coroutines_playground.navigation.BaseRouter
import com.android.coroutines_playground.presentation.categories.CategoriesPresenter
import com.android.coroutines_playground.presentation.riddle.RiddlePresenter
import com.android.coroutines_playground.presentation.riddlelist.RiddleListPresenter
import com.android.coroutines_playground.utils.RecyclerStateStorage
import com.android.coroutines_playground.utils.RecyclerStateStorageImpl
import com.fstyle.library.helper.AssetSQLiteOpenHelperFactory
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import ru.terrakok.cicerone.Cicerone

private val applicationModule = applicationContext {
    bean { mainDB(get()) }
    bean { Cicerone.create(BaseRouter()) as Cicerone<BaseRouter> }
    bean { get<Cicerone<BaseRouter>>().router as BaseRouter }
    bean { get<Cicerone<BaseRouter>>().navigatorHolder }
    bean { RecyclerStateStorageImpl() as RecyclerStateStorage }
}

private val interactorModule = applicationContext {
    bean { DetectiveRiddlesInteractor(get()) }
}

private val mapperModule = applicationContext {
    bean { CategoryEntityMapper() }
    bean { QuestionEntityMapper() }
}

private val repositoryModule = applicationContext {
    bean { DetectiveRiddlesRepositoryImpl(get(), get(), get()) as DetectiveRiddlesRepository }
}

private val presentersModule = applicationContext {
    viewModel { CategoriesPresenter(get()) }
    viewModel { RiddleListPresenter(get()) }
    viewModel { RiddlePresenter(get()) }
}

private fun mainDB(context: Context) = Room.databaseBuilder(
    context.applicationContext,
    MainDB::class.java,
    Common.DATABASE_NAME
)
    .openHelperFactory(AssetSQLiteOpenHelperFactory())
    .build()

val modules = listOf(
    applicationModule,
    interactorModule,
    mapperModule,
    repositoryModule,
    presentersModule
)