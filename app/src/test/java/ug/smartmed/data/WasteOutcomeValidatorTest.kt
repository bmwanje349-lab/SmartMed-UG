package ug.smartmed.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WasteOutcomeValidatorTest {
    private fun validDraft() = WasteOutcomeDraft(
        batchId = "SM-TEST-001",
        stageCode = WasteOutcomeStage.TREATMENT.code,
        methodOrFacility = "Autoclave treatment",
        performedAtEpochMillis = 1_000L,
        recordedByRole = SmartMedOptions.authorisedRoles.first(),
        evidenceReference = "register page 2"
    )

    @Test
    fun validOutcomeHasNoErrors() {
        assertEquals(emptyList<String>(), WasteOutcomeValidator.validate(validDraft()))
    }

    @Test
    fun invalidStageAndBlankMethodAreRejected() {
        val errors = WasteOutcomeValidator.validate(
            validDraft().copy(stageCode = "UNSUPPORTED", methodOrFacility = " ")
        )
        assertTrue(errors.any { it.contains("stage") })
        assertTrue(errors.any { it.contains("method") || it.contains("facility") })
    }

    @Test
    fun futureDatesAndUnapprovedRoleLabelsAreRejected() {
        val future = System.currentTimeMillis() + 86_400_000L
        val errors = WasteOutcomeValidator.validate(
            validDraft().copy(performedAtEpochMillis = future, recordedByRole = "Unknown")
        )
        assertTrue(errors.any { it.contains("future") })
        assertTrue(errors.any { it.contains("role") })
    }

    @Test
    fun longEvidenceReferenceIsRejected() {
        val errors = WasteOutcomeValidator.validate(validDraft().copy(evidenceReference = "x".repeat(241)))
        assertTrue(errors.any { it.contains("Evidence reference") })
    }
}
