package com.example.project9.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project9.model.Mahasiswa
import com.example.project9.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class InsertViewModel (
    private val mhs: MahasiswaRepository //untuk menyimpan data ke dalam database
) : ViewModel(){

    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set //Menyimpan status form, seperti apakah form sedang idle, loading, berhasil, atau error
    // Memperbarui state berdasarkan input pengguna
    fun updateState(mahasiswaEvent: MahasiswaEvent) {
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent,
        )
    }
    // Validasi data input pengguna
    fun validateFields(): Boolean {
        val event = uiEvent.insertUiEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
                    nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
                    jenis_kelamin = if (event.jenis_kelamin.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong",
                    alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",
                    kelas = if (event.kelas.isNotEmpty()) null else "Kelas tidak boleh kosong",
                    angkatan = if (event.angkatan.isNotEmpty()) null else "Angkatan tidak boleh kosong"
        )
        uiEvent = uiEvent.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    //Menyimpan data mahasiswa ke dalam database
    fun insertMhs() {
        if (validateFields()) {
            viewModelScope.launch {
                uiState = FormState.Loading
                try {
                    mhs.insertMahasiswa(uiEvent.insertUiEvent.toMhsModel())
                    uiState = FormState.Success("Data berhasil disimpan")
                } catch (e: Exception) {
                    uiState = FormState.Error("Data gagal disimpan")
                }
            }
        } else {
            uiState = FormState.Error("Data tidak valid")
        }
    }

    //Mereset semua input form dan status menjadi kondisi awal
    fun resetForm() {
        uiEvent = InsertUiState()
        uiState = FormState.Idle
    }

    //Menghapus pesan status (misalnya "berhasil" atau "gagal") untuk mengembalikan form ke kondisi awal
    fun resetSnackBarMessage() {
        uiState = FormState.Idle
    }
}

//Mengatur status form
sealed class FormState {
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val message: String) : FormState()
    data class Error(val message: String) : FormState()
}

//Menyimpan data input form dan status validasi
data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
)

//Menyimpan status validasi untuk setiap field form
data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val jenis_kelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null
) {
    fun isValid(): Boolean {
        return nim == null && nama == null && jenis_kelamin == null &&
                alamat == null && kelas == null && angkatan == null
    }
}

//data class Variabel yang menyimpan data input form
data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val jenis_kelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)

//menyimpan input form kedalam entity
fun MahasiswaEvent.toMhsModel(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    jenis_kelamin = jenis_kelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan
)