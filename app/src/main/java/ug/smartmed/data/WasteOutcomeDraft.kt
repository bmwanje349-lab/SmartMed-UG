package ug.smartmed.data

data class WasteOutcomeDraft(
    val batchId: String,
    val stageCode: String,
    val methodOrFacility: String,
    val performedAtEpochMillis: Long,
    val recordedByRole: String,
    val evidenceReference: String = ""
)

object WasteOutcomeValidator {
    fun validate(draft: WasteOutcomeDraft): List<String> {
        val errors = mutableListOf<String>()
        if (draft.batchId.isBlank()) errors += "Choose an existing batch."
        if (WasteOutcomeStage.fromCode(draft.stageCode) == null) errors += "Choose a valid outcome stage."
        if (draft.methodOrFacility.isBlank()) errors += "Enter the treatment method or final-disposal facility."
        if (draft.methodOrFacility.length > 200) errors += "Method/facility must be 200 characters or fewer."
        if (draft.performedAtEpochMillis <= 0L || draft.performedAtEpochMillis > System.currentTimeMillis()) {
            errors += "Enter a valid date that is not in the future."
        }
        if (draft.recordedByRole !in SmartMedOptions.authorisedRoles) errors += "Choose a recorder role."
        if (draft.evidenceReference.length > 240) errors += "Evidence reference must be 240 characters or fewer."
        return errors
    }
}
