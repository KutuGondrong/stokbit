package com.kutugondrong.cryptoonlinekg.screen.home

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kutugondrong.cryptoonlinekg.data.local.entity.Crypto
import com.kutugondrong.cryptoonlinekg.data.repository.CryptosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CryptosRepository
) : ViewModel() {

    private var currentResult: Flow<PagingData<Crypto>>? = null

    @ExperimentalPagingApi
    fun getCryptos(): Flow<PagingData<Crypto>> {
        val newResult: Flow<PagingData<Crypto>> =
            repository.getCryptos().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

}