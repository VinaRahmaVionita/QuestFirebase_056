package com.example.project9.repository

import com.example.project9.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkMahasiswaRepository(
    private val firestore: FirebaseFirestore
) : MahasiswaRepository {
    override suspend fun getMahasiswa(): Flow<List<Mahasiswa>> = callbackFlow{//mendukung data secara realtime
       val mhsCollection = firestore.collection("Mahasiswa")
           .orderBy("nama", Query.Direction.ASCENDING)
           .addSnapshotListener { value, error -> //untuk data listener yang realtime

               //kalau datanya tidak sama dengan null maka data dalam dokumen di masukkan ke model
               if (value != null) {
                   val mhsList = value.documents.mapNotNull {
                       it.toObject(Mahasiswa::class.java)!!
                   }
                   trySend(mhsList) //memberikan fungsi untuk mengirim data ke flow
               }
           }
        awaitClose{
            mhsCollection.remove() //menutup collection
        }
    }

    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMahasiswa(nim: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getMahasiswaById(nim: String): Flow<Mahasiswa> {
        TODO("Not yet implemented")
    }
}