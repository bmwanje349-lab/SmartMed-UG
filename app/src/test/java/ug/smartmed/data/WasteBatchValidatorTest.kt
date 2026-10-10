package ug.smartmed.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WasteBatchValidatorTest {
    private fun validDraft(
        quantity: String = "2.5",
        facility: String = "Demo Health Centre",
        category: String = WasteCategory.INFECTIOUS.code,
        role: String = SmartMedOptions.authorisedRoles.first()
    ) = WasteBatchDraft(
        facilityName = facility,
        categoryCode = category,
        subcategory = WasteCategory.fromCode(category)?.subcategories?.firstOrNull().orEmpty(),
        quantityText = quantity,
        unit = "kg",
        collectionPoint = "Demo collection point",
        recorderRole = role,
        notes = "Fictional test record"
    )

    @Test
    fun validDraftHasNoErrors() {
        assertEquals(emptyList<String>(), WasteBatchValidator.validate(validDraft()))
    }

    @Test
    fun zeroNegativeNanAndInvalidQuantityAreRejected() {
        listOf("0", "-1", "NaN", "Infinity", "not-a-number", "").forEach { value ->
            assertTrue(
                "Expected quantity '$value' to be rejected",
                WasteBatchValidator.validate(validDraft(quantity = value)).any { it.contains("Quantity") }
            )
        }
    }

    @Test
    fun requiredFacilityCategoryAndRecorderRoleAreValidated() {
        val draft = validDraft(facility = " ", category = "UNLISTED", role = "Unauthorised role")
        val errors = WasteBatchValidator.validate(draft)
        assertTrue(errors.any { it.contains("facility") })
        assertTrue(errors.any { it.contains("category") })
        assertTrue(errors.any { it.contains("role") })
    }

    @Test
    fun subcategoryMustBelongToSelectedCategory() {
        val draft = validDraft().copy(
            categoryCode = WasteCategory.SHARPS.code,
            subcategory = WasteCategory.INFECTIOUS.subcategories.first()
        )
        assertTrue(
            WasteBatchValidator.validate(draft).any { it.contains("subcategory that belongs") }
        )
    }

    @Test
    fun unsupportedUnitIsRejected() {
        val draft = validDraft().copy(unit = "unknown-unit")
        assertTrue(WasteBatchValidator.validate(draft).any { it.contains("unit") })
    }
}
