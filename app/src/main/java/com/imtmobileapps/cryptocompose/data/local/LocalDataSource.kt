package com.imtmobileapps.cryptocompose.data.local

import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val cryptoValuesDao:CryptoValuesDao,
    private val personDao: PersonDao,
    private val totalValuesDao: TotalValuesDao
) {




}