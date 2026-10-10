package ug.smartmed.data

import org.junit.Assert.assertTrue
import org.junit.Test

class WasteOutcomeCsvExporterTest {
    @Test
    fun formulaLikeMethodIsNeutralisedAndEvidenceStatusExported() {
        val outcome = WasteOutcome(
            id = "OUT-TEST-001",
            batchId = "SM-TEST-001",
            stage = WasteOutcomeStage.TREATMENT.code,
            methodOrFacility = "=1+1",
            performedAtEpochMillis = 1000L,
            recordedAtEpochMillis = 2000L,
            recordedByRole = "Nurse",
            evidenceStatus = "AWAITING_EVIDENCE"
        )

        val csv = WasteOutcomeCsvExporter.export(listOf(outcome))
        assertTrue(csv.contains(",'=1+1,1000,"))
        assertTrue(csv.contains("AWAITING_EVIDENCE"))
        assertTrue(csv.contains("OUT-TEST-001"))
    }
}
