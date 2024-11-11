package com.example.miok_quick_response_app.di

// di = Dependency injection
import android.app.Application
import androidx.room.Room
import com.example.miok_quick_response_app.data.QuestionDao
import com.example.miok_quick_response_app.data.QuestionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)  // Use SingletonComponent instead of ApplicationComponent
object AppModule {

    @Provides
    @Singleton  // Ensure this is a singleton across the entire application
    fun provideDatabase(
        app: Application,
        callback: QuestionDatabase.Callback
    ): QuestionDatabase = Room.databaseBuilder(app, QuestionDatabase::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideTaskDao(db: QuestionDatabase): QuestionDao = db.questionDao()

    @Provides
    @Singleton  // Ensure this is a singleton across the entire application
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}

// Custom scope annotation
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
