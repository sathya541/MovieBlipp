package sathya.com.movieblipp.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sathya.com.movieblipp.data.local.BookmarkDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbDiModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) = BookmarkDB.getInstance(application)

    @Provides
    @Singleton
    fun provideBookmarkDao(DB: BookmarkDB) = DB.getBookmarkDao()

}