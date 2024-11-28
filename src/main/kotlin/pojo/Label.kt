package pojo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Label (

  @SerialName("path"         ) var path        : String? = null,
  @SerialName("text"         ) var text        : String? = null,
  @SerialName("label_theme"  ) var labelTheme  : String? = null,
  @SerialName("text_color"   ) var textColor   : String? = null,
  @SerialName("bg_style"     ) var bgStyle     : Int?    = null,
  @SerialName("bg_color"     ) var bgColor     : String? = null,
  @SerialName("border_color" ) var borderColor : String? = null

)