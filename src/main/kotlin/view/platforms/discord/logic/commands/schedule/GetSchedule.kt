package view.platforms.discord.logic.commands.schedule

import kotlinx.coroutines.runBlocking
import view.botservices.scheduleoperations.Schedule
import view.botservices.scheduleoperations.ScheduleApiService
import view.platforms.discord.logic.services.commandmanagment.Command
import view.platforms.discord.logic.services.commandmanagment.CommandContext
import view.platforms.discord.logic.services.embeds.EmbedSchedule

class GetSchedule : Command {
    override fun handle(ctx: CommandContext) {
        val groupName: String
        val date: String

        if (ctx.args.size < 2) {
            //TODO: обработка аргументов
            return
        } else {
            groupName = ctx.args[0]
            date = ctx.args[1]
        }

        val schedule: Schedule
        runBlocking {
            schedule = ScheduleApiService().getSchedule(groupName, date) //"ПР-31", "16.03.2022"
        }

        ctx.event.channel.sendMessageEmbeds(
            EmbedSchedule().withGroupAndDate(
                schedule.groupName,
                schedule.date,
                schedule.lessons
            ).build()
        ).queue()
    }

    override fun getName(): String =
        "schedule"

}
