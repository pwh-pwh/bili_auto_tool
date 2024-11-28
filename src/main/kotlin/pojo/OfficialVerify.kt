package pojo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class OfficialVerify (

  @SerialName("type" ) var type : Int?    = null,
  @SerialName("desc" ) var desc : String? = null

)