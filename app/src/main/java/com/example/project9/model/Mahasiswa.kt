package com.example.project9.model

data class Mahasiswa(
    val nim: String,
    val nama: String,
    val alamat: String,
    val jenis_kelamin: String,
    val kelas: String,
    val angkatan: String,
    val judul_skripsi: String,
    val dospem1: String,
    val dospem2: String
){
    constructor():this("","","","","","","","","")
}
