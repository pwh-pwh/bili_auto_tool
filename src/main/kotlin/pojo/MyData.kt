package pojo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class MyData (

  @SerialName("code"    ) var code    : Int?    = null,
  @SerialName("message" ) var message : String? = null,
  @SerialName("ttl"     ) var ttl     : Int?    = null,
  @SerialName("data"    ) var data    : Data?   = Data()

)