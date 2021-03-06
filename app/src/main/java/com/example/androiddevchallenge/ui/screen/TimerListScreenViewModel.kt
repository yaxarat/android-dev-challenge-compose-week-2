package com.example.androiddevchallenge.ui.screen

import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerListScreenViewModel
@Inject
constructor(private val repository: Repository) : ViewModel() {

    companion object {
        val viewModelKey = TimerListScreenViewModel.javaClass.simpleName
    }
}