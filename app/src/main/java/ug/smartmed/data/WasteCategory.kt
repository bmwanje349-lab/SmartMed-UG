package ug.smartmed.data

/**
 * Initial controlled vocabulary for the prototype.
 * Before a facility pilot, a local environmental-health/waste focal person must
 * map these labels to the current Uganda Ministry of Health operational guideline.
 * "Other" is deliberately marked for review rather than silently normalised.
 */
enum class WasteCategory(
    val code: String,
    val label: String,
    val subcategories: List<String>
) {
    INFECTIOUS(
        "INFECTIOUS",
        "Infectious waste",
        listOf("Contaminated dressings/material", "Laboratory or infectious material", "Other infectious material")
    ),
    SHARPS(
        "SHARPS",
        "Sharps",
        listOf("Needles or syringes with needles", "Scalpel/blade", "Other sharp item")
    ),
    PATHOLOGICAL(
        "PATHOLOGICAL",
        "Pathological/anatomical waste",
        listOf("Tissue or body part", "Placenta or similar material", "Other pathological material")
    ),
    PHARMACEUTICAL(
        "PHARMACEUTICAL",
        "Pharmaceutical waste",
        listOf("Expired or damaged medicines", "Medicine-contaminated material", "Other pharmaceutical waste")
    ),
    CHEMICAL(
        "CHEMICAL",
        "Chemical waste",
        listOf("Laboratory chemical", "Disinfectant or reagent", "Other chemical waste")
    ),
    GENERAL_NON_HAZARDOUS(
        "GENERAL_NON_HAZARDOUS",
        "General non-hazardous waste",
        listOf("Paper or packaging", "Food/general refuse", "Other non-hazardous material")
    ),
    OTHER_REQUIRES_REVIEW(
        "OTHER_REQUIRES_REVIEW",
        "Other — requires review",
        listOf("Needs classification by authorised facility staff")
    );

    companion object {
        fun fromCode(code: String): WasteCategory? = values().firstOrNull { it.code == code }
    }
}

object SmartMedOptions {
    val quantityUnits = listOf("kg", "g", "L", "mL", "items", "bags")
    val authorisedRoles = listOf(
        "Nurse",
        "Waste focal person",
        "Facility manager",
        "Environmental health staff",
        "Other authorised staff"
    )
}
