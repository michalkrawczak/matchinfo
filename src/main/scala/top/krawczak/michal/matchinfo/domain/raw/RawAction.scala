package top.krawczak.michal.matchinfo.domain.raw

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import scala.util.Try

/**
  * Holds a single row read from the CSV, i.e. a single action.
  *
  * TODO Many of these fields are actually enum values (for example `action`, `period`, `function`).
  *      It would be better to represent them as enum-like types instead of string.
  */
case class RawAction(actionId: String,
                     competition: String,
                     matchId: String,
                     date: Date,
                     action: String,
                     period: Option[String],
                     startTime: Option[Long],
                     endTime: Option[Long],
                     homeOrAway: Option[String],
                     teamId: Option[String],
                     team: Option[String],
                     personId: Option[String],
                     person: Option[String],
                     function: Option[String],
                     shirtNr: Option[String],
                     actionReason: Option[String],
                     actionInfo: Option[String],
                     subpersonId: Option[String],
                     subperson: Option[String])

object RawAction {
  /**
    * Given a map where each property of an action is represented by a single entry with a known
    * key, convert it to a [[RawAction]].
    */
  def fromMap(map: Map[String, String]) = {
    def optionalString(key: String) = toStringOption(map.get(key))
    def optionalLong(key: String) = toNumberOption(map.get(key))
    RawAction(
      actionId = map("n_actionid"),
      competition = map("c_competition"),
      matchId = map("n_Matchid"),
      date = parseDateTime(map("d_date")),
      action = map("c_Action"),
      period = optionalString("c_Period"),
      startTime = optionalLong("n_StartTime"),
      endTime = optionalLong("n_Endtime"),
      homeOrAway = optionalString("c_HomeOrAway"),
      teamId = optionalString("n_TeamID"),
      team = optionalString("c_Team"),
      personId = optionalString("n_personid"),
      person = optionalString("c_person"),
      function = optionalString("c_Function"),
      shirtNr = optionalString("n_ShirtNr"),
      actionReason = optionalString("c_ActionReason"),
      actionInfo = optionalString("c_actionInfo"),
      subpersonId = optionalString("n_Subpersonid"),
      subperson = optionalString("c_Subperson")
    )
  }

  private[this] def parseDateTime(input: String): Date = {
    val formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm", Locale.US)
    formatter.parse(input)
  }

  private[this] def toStringOption(input: Option[String]): Option[String] =
    if (input.isEmpty || input.contains("NULL"))
      None
    else
      input

  private[this] def toNumberOption(input: Option[String]): Option[Long] =
    if (input.isEmpty || input.contains("NULL"))
      None
    else
      Try(input.get.trim.toLong).toOption
}