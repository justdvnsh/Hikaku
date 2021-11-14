package divyansh.tech.hikaku.home.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import divyansh.tech.hikaku.home.source.HomeDataSource
import divyansh.tech.hikaku.home.source.HomeDefaultRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeDefaultRepositoryProvider {
    @Binds
    @ViewModelScoped
    abstract fun bindHomeDefaultRepository(repo: HomeDefaultRepository): HomeDataSource
}