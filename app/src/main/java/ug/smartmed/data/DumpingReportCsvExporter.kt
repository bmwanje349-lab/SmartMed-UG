package ug.smartmed.data

object DumpingReportCsvExporter {
    private val headers = listOf(
        "Report ID", "Description", "Location description", "Observed at (epoch ms)",
        "Recorded at (epoch ms)", "Reporter role label", "Status", "External referral reference",
        "Follow-up notes", "Local sync state"
    )

    fun export(reports: List<DumpingReport>): String =
        (listOf(headers) + reports.map { report ->
            listOf(
                report.id,
                report.description,
                report.locationDescription,
                report.observedAtEpochMillis.toString(),
                report.reportedAtEpochMillis.toString(),
                report.reporterRole,
                report.status,
                report.referralReference,
                report.followUpNotes,
                report.syncState
            )
        }).joinToString("\r\n") { row ->
            row.joinToString(",") { escape(it) }
        } + "\r\n"

    private fun escape(value: String): String {
        val cleaned = value.replace("\r\n", "\n").replace("\r", "\n")
        return if (cleaned.any { it == ',' || it == '"' || it == '\n' }) {
            "\"" + cleaned.replace("\"", "\"\"") + "\""
        } else {
            cleaned
        }
    }
}
