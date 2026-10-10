package ug.smartmed.data

data class WasteBatchDraft(
    val facilityName: String,
    val categoryCode: String,
    val subcategory: String,
    val quantityText: String,
    val unit: String,
    val collectionPoint: String,
    val recorderRole: String,
    val notes: String = ""
)

object WasteBatchValidator {
    fun validate(draft: WasteBatchDraft): List<String> {
        val errors = mutableListOf<String>()
        if (draft.facilityName.isBlank()) errors += "Enter the facility name."
        val category = WasteCategory.fromCode(draft.categoryCode)
        if (category == null) {
            errors += "Choose a waste category."
        }
        if (draft.subcategory.isBlank()) {
            errors += "Choose a waste subcategory."
        } else if (category != null && draft.subcategory.trim() !in category.subcategories) {
            errors += "Choose a subcategory that belongs to the selected category."
        }
        val quantity = draft.quantityText.trim().toDoubleOrNull()
        if (quantity == null || !quantity.isFinite() || quantity <= 0.0) {
            errors += "Quantity must be a number greater than zero."
        }
        if (draft.unit !in SmartMedOptions.quantityUnits) errors += "Choose a supported unit."
        if (draft.collectionPoint.isBlank()) errors += "Enter the department or collection point."
        if (draft.recorderRole !in SmartMedOptions.authorisedRoles) errors += "Choose the recorder's role."
        if (draft.facilityName.length > 160) errors += "Facility name is too long."
        if (draft.collectionPoint.length > 160) errors += "Collection point is too long."
        if (draft.subcategory.length > 120) errors += "Subcategory is too long."
        if (draft.notes.length > 1000) errors += "Notes must be 1,000 characters or fewer."
        return errors
    }
}
