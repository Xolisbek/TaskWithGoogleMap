package uz.coderqwerty.taskwithgooglemap.assembly.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.coderqwerty.taskwithgooglemap.assembly.navigation.AppNavigationDispatcher
import uz.coderqwerty.taskwithgooglemap.assembly.navigation.AppNavigator
import uz.coderqwerty.taskwithgooglemap.assembly.navigation.NavigationHandler

@Module
@InstallIn(SingletonComponent::class)
internal interface NavigationModule {

    /* ============ AppNavigator ============ */
    @Binds
    fun bindAppNavigator(dispatcher: AppNavigationDispatcher): AppNavigator

    @Binds
    fun bindNavigationHandler(dispatcher: AppNavigationDispatcher): NavigationHandler

}