package view.platforms.discord.logic.commands.schedule

import kotlinx.coroutines.runBlocking
import view.botservices.DatabaseOperations
import view.botservices.DateFormatter
import view.botservices.GroupFinder
import view.botservices.scheduleoperations.Schedule
import view.botservices.scheduleoperations.ScheduleApiService
import view.platforms.discord.logic.services.commandmanagment.ICommand
import view.platforms.discord.logic.services.commandmanagment.CommandContext
import view.platforms.discord.logic.services.embeds.EmbedError
import view.platforms.discord.logic.services.embeds.EmbedSchedule
import view.platforms.discord.logic.services.embeds.SUBGROUP_DOES_NOT_EXISTS

class GetSchedule : ICommand {
    override fun name(): String =
        "schedule"

    override fun handle(ctx: CommandContext) {
        val group: String
        val date: String

        val args = ctx.args

        if (args.isEmpty()) {
            group = subgroup(ctx.event.guild.idLong)
            date = DateFormatter.today()
        }

        else if (args.size == 1) {
            if (args[0][0].isDigit()) {
                date = args[0]
                group = subgroup(ctx.event.guild.idLong)
            }

            else {
                group = GroupFinder.execute(args[0])
                date = DateFormatter.today()
            }
        }

        else {
            group = GroupFinder.execute(args[0])
            date = args[1]
        }

        if (group == "none") {
            ctx.event.channel.sendMessageEmbeds(
                EmbedError().incorrectSetSetting(SUBGROUP_DOES_NOT_EXISTS).build()
            ).queue()
            return
        }

        val schedule: Schedule
        runBlocking {
            schedule = ScheduleApiService().getSchedule(group, date)
        }

        ctx.event.channel.sendMessageEmbeds(
            EmbedSchedule().withGroupAndDate(
                group,
                date,
                schedule.lessons
            ).build()
        ).queue()
    }

    private fun subgroup(id: Long): String =
        DatabaseOperations().getDiscordPropertyValue("subgroup", id)
}
