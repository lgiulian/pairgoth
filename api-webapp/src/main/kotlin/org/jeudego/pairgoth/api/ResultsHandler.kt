package org.jeudego.pairgoth.api

import com.republicate.kson.Json
import com.republicate.kson.toJsonArray
import org.jeudego.pairgoth.api.ApiHandler.Companion.badRequest
import org.jeudego.pairgoth.model.Game
import org.jeudego.pairgoth.model.toJson
import org.jeudego.pairgoth.server.Event
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object ResultsHandler: PairgothApiHandler {

    override fun get(request: HttpServletRequest, response: HttpServletResponse): Json? {
        val tournament = getTournament(request)
        val round = getSubSelector(request)?.toIntOrNull() ?: badRequest("invalid round number")
        val games = tournament.games(round).values
        return games.map { it.toJson() }.toJsonArray()
    }

    override fun put(request: HttpServletRequest, response: HttpServletResponse): Json? {
        val tournament = getTournament(request)
        val round = getSubSelector(request)?.toIntOrNull() ?: badRequest("invalid round number")
        val payload = getObjectPayload(request)
        val game = tournament.games(round)[payload.getInt("id")] ?: badRequest("invalid game id")
        game.result = Game.Result.fromSymbol(payload.getChar("result") ?: badRequest("missing result"))
        tournament.dispatchEvent(Event.ResultUpdated, request, Json.Object("round" to round, "data" to game))
        return Json.Object("success" to true)
    }

    override fun delete(request: HttpServletRequest, response: HttpServletResponse): Json {
        val tournament = getTournament(request)
        val round = getSubSelector(request)?.toIntOrNull() ?: badRequest("invalid round number")
        for (game in tournament.games(round).values) {
            game.result = Game.Result.UNKNOWN
        }
        tournament.dispatchEvent(Event.ResultsCleared, request, Json.Object("round" to round))
        return Json.Object("success" to true)
    }
}
