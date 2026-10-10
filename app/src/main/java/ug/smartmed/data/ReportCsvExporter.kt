package ug.smartmed.data

object ReportCsvExporter {
    private val headers = listOf(
        "Batch ID", "Facility", "Category code", "Subcategory", "Quantity",
        "Unit", "Collection point", "Created at (UTC epoch ms)", "Recorded by role",
        "Record status", "Archived", "Local sync state", "Revision",
        "Handover count", "Handovers awaiting evidence", "Handovers awaiting review", "Notes"
    )

    fun export(batches: List<WasteBatch>, handovers: List<WasteHandover>): String {
        val handoversByBatch = handovers.groupBy { it.batchId }
        val rows = batches.map { batch ->
            val related = handoversByBatch[batch.id].orEmpty()
            listOf(
                batch.id, batch.facilityName, batch.category, batch.subcategory,
                batch.quantity.toString(), batch.unit, batch.collectionPoint,
                batch.createdAtEpochMillis.toString(), batch.recordedByRole,
                batch.recordStatus, batch.isArchived.toString(), batch.syncState,
                batch.revision.toString(), related.size.toString(),
                related.count { it.evidenceStatus == "AWAITING_EVIDENCE" }.toString(),
                related.count { it.evidenceStatus == "PRESENT_UNREVIEWED" }.toString(),
                batch.notes
            )
        }
        return (listOf(headers) + rows).joinToString(separator = "\r\n") { row ->
            row.joinToString(",") { escape(it) }
        } + "\r\n"
    }

    private fun escape(value: String): String {
        val cleaned = value.replace("\r\n", "\n").replace("\r", "\n")
        val safe = if (cleaned.firstOrNull() in setOf('=', '+', '-', '@', '\t', '\r')) "'$cleaned" else cleaned
        return if (safe.any { it == ',' || it == '"' || it == '\n' }) {
            "\"" + safe.replace("\"", "\"\"") + "\""
        } else {
            safe
        }
    }
}
