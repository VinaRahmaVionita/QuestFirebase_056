package com.example.project9.ui.theme.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.project9.MahasiswaApplications

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                mahasiswaApplications().container.mahasiswaRepository
            )
        }
        initializer {
            InsertViewModel(
                mahasiswaApplications().container.mahasiswaRepository
            )
        }
        initializer {
            DetailViewModel(
                createSavedStateHandle(),
                mahasiswaApplications().container.mahasiswaRepository
            )
        }
    }
}

fun CreationExtras.mahasiswaApplications(): MahasiswaApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MahasiswaApplications)