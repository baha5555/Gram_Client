package com.gram.client.domain.myAddresses

import retrofit2.http.Field
import retrofit2.http.Path

data class UpdateMyAddressRequest(
    @Path("id") var id: Int,
    @Field("name") var name : String,
    @Field("search_address_id") var search_address_id : Int,
    @Field("meet_info") var meet_info : String?,
    @Field("comment_to_driver") var comment_to_driver : String?,
    @Field("type") var type : String,
)
