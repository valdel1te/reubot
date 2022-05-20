package view.botservices.database

data class DiscordValues(
    val PREFIX: String = "prefix",
    val SUBGROUP: String = "subgroup",
    val SUBCHANNEL_ID: String = "subchannel_id"
)

data class TelegramValues(
    val SUB_ENABLED: String = "sub_enabled"
)