package com.example.gramclient.domain.profile
fun main(){
    val a = 10
    when{
        a > 10 -> println("$a > 10")
    }
}
data class GetProfileInfoResult(
    val avatar_url: String,
    val birth_date: String,
    val email: String,
    val first_name: String,
    val gender: Int,
    val id: Int,
    val last_name: String,
    val login: String,
    val phone: String,
    val user_id: Int
)