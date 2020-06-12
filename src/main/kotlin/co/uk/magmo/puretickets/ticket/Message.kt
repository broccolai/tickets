package co.uk.magmo.puretickets.ticket

import java.time.LocalDateTime
import java.util.UUID

class Message(val reason: MessageReason, val data: String?, val sender: UUID?, val date: LocalDateTime = LocalDateTime.now())