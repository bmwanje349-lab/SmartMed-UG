package ug.smartmed.data

object WasteOutcomeCsvExporter {
    private val headers = listOf(
        "Outcome ID", "Batch ID", "Stage", "Method or facility",
        "Performed at (epoch ms)", "Recorded at (epoch ms)", "Recorder role label",
        "Evidence reference", "Evidence status", "Reviewer role label",
        "Reviewed at (epoch ms)", "Review note", "Local sync state"
    )

    fun export(outcomes: List<WasteOutcome>): String =
        (listOf(headers) + outcomes.map { outcome ->
            listOf(
                outcome.id, outcome.batchId, outcome.stage, outcome.methodOrFacility,
                outcome.performedAtEpochMillis.toString(), outcome.recordedAtEpochMillis.toString(),
                outcome.recordedByRole, outcome.evidenceReference, outcome.evidenceStatus,
                outcome.reviewedByRole.orEmpty(), outcome.reviewedAtEpochMillis?.toString().orEmpty(),
                outcome.reviewNote, outcome.syncState
            )
        }).joinToString("\r\n") { row ->
            row.joinToString(",") { escape(it) }
        } + "\r\n"

    private fun escape(value: String): String {
        val cleaned = value.replace("\r\n", "\n").replace("\r", "\n")
        val firstNonWhitespace = cleaned.firstOrNull { !it.isWhitespace() }
        val safe = if (firstNonWhitespace != null && firstNonWhitespace in setOf('=', '+', '-', '@', '\t', '\r')) "'$cleaned" else cleaned
        return if (safe.any { it == ',' || it == '"' || it == '\n' }) {
            "\"" + safe.replace("\"", "\"\"") + "\""
        } else {
            safe
        }
    }
}
