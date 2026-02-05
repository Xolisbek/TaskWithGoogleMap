package uz.coderqwerty.taskwithgooglemap.assembly.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.coderqwerty.taskwithgooglemap.presentation.ui.screens.main.MainScreenContract
import uz.coderqwerty.taskwithgooglemap.presentation.ui.screens.main.MainScreenViewModelImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    fun bindMainScreenViewModel(impl: MainScreenViewModelImpl): MainScreenContract.Model
}