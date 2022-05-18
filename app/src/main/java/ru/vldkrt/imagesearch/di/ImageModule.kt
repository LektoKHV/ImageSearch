package ru.vldkrt.imagesearch.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.vldkrt.imagesearch.data.api.ImageClient
import ru.vldkrt.imagesearch.data.repositories.ImageRepositoryImpl
import ru.vldkrt.imagesearch.domain.repositories.ImageRepository
import ru.vldkrt.imagesearch.domain.usecases.GetImagesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ImageModule {

    @Singleton
    @Provides
    fun provideGetImageUseCase(repository: ImageRepository): GetImagesUseCase =
        GetImagesUseCase(repository)

    @Singleton
    @Provides
    fun provideRepository(imageClient: ImageClient): ImageRepository =
        ImageRepositoryImpl(imageClient)

    @Singleton
    @Provides
    fun provideImageClient(retrofit: Retrofit): ImageClient {
        return retrofit.create(ImageClient::class.java)
    }
}