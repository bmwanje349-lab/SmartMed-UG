package ug.smartmed.data

import org.junit.Assert.assertTrue
import org.junit.Test

class DumpingReportCsvExporterTest {
    @Test
    fun formulaLikeDescriptionIsEscapedForSpreadsheets() {
        val report = DumpingReport(
            id = "DR-TEST-FORMULA",
            description = "=1+1",
            locationDescription = "Safe location text",
            observedAtEpochMillis = 1000L,
            reportedAtEpochMillis = 2000L,
            reporterRole = "Nurse"
        )
        val csv = DumpingReportCsvExporter.export(listOf(report))
        assertTrue(csv.contains(",\"'=1+1\","))
    }

    @Test
    fun exportEscapesCommasAndKeepsUnverifiedStatus() {
        val report = DumpingReport(
            id = "DR-TEST-001",
            description = "A sealed bag was seen near the road, no handling performed",
            locationDescription = "Near clinic gate, west side",
            observedAtEpochMillis = 1000L,
            reportedAtEpochMillis = 2000L,
            reporterRole = "Environmental health staff",
            status = "UNVERIFIED_LOCAL"
        )

        val csv = DumpingReportCsvExporter.export(listOf(report))

        assertTrue(csv.contains("\"Near clinic gate, west side\""))
        assertTrue(csv.contains("UNVERIFIED_LOCAL"))
        assertTrue(csv.contains("DR-TEST-001"))
    }
}
