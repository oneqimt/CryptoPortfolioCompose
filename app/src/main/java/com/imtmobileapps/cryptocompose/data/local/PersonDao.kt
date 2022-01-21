package com.imtmobileapps.cryptocompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imtmobileapps.cryptocompose.model.Person

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: Person):Long

    @Query(value = "SELECT * FROM person WHERE person_id = :personId")
    suspend fun getPerson(personId: Int):Person

    @Query(value = "DELETE FROM person")
    suspend fun deletePerson()


}