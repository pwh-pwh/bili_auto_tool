package pojo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Data (

  @SerialName("list"       ) var list      : ArrayList<ListItem> = arrayListOf(),
  @SerialName("re_version" ) var reVersion : Int?            = null,
  @SerialName("total"      ) var total     : Int?            = null

)