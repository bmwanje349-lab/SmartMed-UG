package ug.smartmed.data

import org.junit.Assert.assertTrue
import org.junit.Test

class ReportCsvExporterTest {
    @Test
    fun exportsBatchesAndQuotesCsvSpecialCharacters() {
        val batch = WasteBatch(
            id = "SM-TEST-001",
            category = WasteCategory.INFECTIOUS.code,
            quantity = 2.5,
            unit = "kg",
            collectionPoint = "Room 1, clinic",
            createdAtEpochMillis = 1000L,
            facilityName = "Demo Health Centre",
            subcategory = "Contaminated dressings/material",
            recordedByRole = "Nurse",
            notes = "Needs \"review\""
        )

        val csv = ReportCsvExporter.export(listOf(batch), emptyList())

        assertTrue(csv.startsWith("Batch ID,Facility,"))
        assertTrue(csv.contains("\"Room 1, clinic\""))
        assertTrue(csv.contains("\"Needs \"\"review\"\"\""))
        assertTrue(csv.contains("SM-TEST-001"))
    }

    @Test
    fun neutralizesSpreadsheetFormulaCells() {
        val batch = WasteBatch(
            id = "SM-TEST-003",
            category = WasteCategory.CHEMICAL.code,
            quantity = 1.0,
            unit = "kg",
            collectionPoint = "Demo",
            createdAtEpochMillis = 3000L,
            notes = "=1+1"
        )
        val csv = ReportCsvExporter.export(listOf(batch), emptyList())
        assertTrue(csv.contains(",'=1+1\r\n"))
    }

    @Test
    fun exportsSeparateMissingAndUnreviewedEvidenceCounts() {
        val batch = WasteBatch(
            id = "SM-TEST-002",
            category = WasteCategory.SHARPS.code,
            quantity = 1.0,
            unit = "bags",
            collectionPoint = "Demo point",
            createdAtEpochMillis = 2000L
        )
        val handovers = listOf(
            WasteHandover(
                id = "HO-1", batchId = batch.id, senderRole = "Nurse",
                receiverRole = "Waste focal person", timestampEpochMillis = 3000L
            ),
            WasteHandover(
                id = "HO-2", batchId = batch.id, senderRole = "Nurse",
                receiverRole = "Waste focal person", timestampEpochMillis = 4000L,
                evidenceReference = "logbook page 3",
                evidenceStatus = "PRESENT_UNREVIEWED"
            )
        )

        val csv = ReportCsvExporter.export(listOf(batch), handovers)
        assertTrue(csv.contains(",2,,,1,1,"))
    }
}
