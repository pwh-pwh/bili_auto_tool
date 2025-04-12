package pojo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class ListItem (

  @SerialName("mid"             ) var mid            : Long?            = null,
  @SerialName("attribute"       ) var attribute      : Int?            = null,
  @SerialName("mtime"           ) var mtime          : Int?            = null,
  @SerialName("special"         ) var special        : Int?            = null,
  @SerialName("uname"           ) var uname          : String?         = null,
  @SerialName("face"            ) var face           : String?         = null,
  @SerialName("sign"            ) var sign           : String?         = null,
  @SerialName("face_nft"        ) var faceNft        : Int?            = null,
  @SerialName("official_verify" ) var officialVerify : OfficialVerify? = OfficialVerify(),
  @SerialName("nft_icon"        ) var nftIcon        : String?         = null,
  @SerialName("rec_reason"      ) var recReason      : String?         = null,
  @SerialName("track_id"        ) var trackId        : String?         = null,
  @SerialName("follow_time"     ) var followTime     : String?         = null

)