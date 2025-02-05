package com.example.project9.repository

import com.example.project9.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

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
        try {
            firestore.collection("Mahasiswa").add(mahasiswa).await()
        } catch (e: Exception) {
            throw Exception ("Gagal menambahkan data mahasiswa: ${e.message}")
        }
    }

    override suspend fun updateMahasiswa(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .set(mahasiswa)
                .await()
        } catch (e: Exception) {
            throw Exception("Gagal mengupdate data mahasiswa:${e.message}")
        }
    }


    override suspend fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .delete()
                .await()
        } catch (e: Exception) {
            throw Exception("Gagal menghapus data mahasiswa:${e.message}")
        }
    }

    override suspend fun getMahasiswaById(nim: String): Flow<Mahasiswa> = callbackFlow {
        val mhsDocument = firestore.collection("Mahasiswa")
            .document(nim)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val mhs = value.toObject(Mahasiswa::class.java)!!
                    trySend(mhs)
                }
            }
        awaitClose {
            mhsDocument.remove()
        }
    }
}