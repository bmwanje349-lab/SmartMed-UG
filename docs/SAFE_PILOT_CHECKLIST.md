# SmartMed-UG: pilot readiness checklist

SmartMed-UG is a prototype. Passing CI does not authorise use in a health facility.

## Before any real facility use

- Obtain written permission from the facility in-charge and the relevant university supervisor.
- Ask the facility's waste-management focal person to map the app's draft category list and required handover evidence to current Ministry of Health guidance and the facility's SOPs. The app categories are a starting vocabulary, not a claim of official completeness.
- Confirm whether institutional review, research ethics review, district approval or other permissions are required for the proposed activity. Obtain the required approvals before collecting pilot data.
- Agree in writing who may record, correct, review, export and archive records. The current role picker is a label, not authentication or authorisation.
- Define the official register/document that remains authoritative if an app entry conflicts with it.
- Agree which treatment and final-disposal evidence is required for each waste stream and how transporter/manifest references should be captured.
- Confirm which waste streams require treatment and final-disposal records under the facility SOP; an absent outcome should be reviewed in context, not automatically labelled a compliance failure.
- Use fictional records for software testing. During an approved pilot, minimise personal data; never enter patient names, patient identifiers, diagnosis or clinical details.
- Staff must follow existing PPE, segregation, transport and disposal procedures. Do not approach, open, move or handle waste to photograph or record it.
- For suspected dumping, record only observable facts and a safe location description. Do not name or accuse individuals, approach the waste, or attempt to collect evidence. The module stores the entry locally and does not contact an authority.
- Test the full database upgrade path from version 1 through version 4 on a copy of test data before a real deployment. Do not uninstall the app to test upgrades.
- Keep backups/exports in an access-controlled location. Exported CSV files may contain facility operational data and should be handled accordingly.
- Record baseline and follow-up using the same definitions. Example: complete handover rate = eligible handovers with all facility-defined required fields/evidence ÷ all eligible handovers reviewed × 100%.
- Separate application test results from actual pilot results. Do not describe a pilot, environmental improvement, or official endorsement that has not happened.

## Known limits that must be addressed before production

- No sign-in, verified staff identities, facility tenancy boundary or permissions enforcement.
- No database encryption at rest. Real operational data must not be stored until a suitable local-data protection design and security review are completed.
- No remote server, sync endpoint, encryption key management, remote revocation or multi-device conflict resolution. PENDING means locally recorded and not uploaded.
- Audit events are local application records, not a tamper-proof or cryptographically signed audit system.
- Evidence is represented by a human-entered reference; the app does not store, authenticate or independently validate documentary evidence.
- A review entry records that someone selected a role and submitted a note. It does not certify treatment, final disposal or regulatory compliance.
- No automatic NEMA, Ministry, district or facility-system reporting is implemented.
- Category labels, units and evidence requirements must be confirmed against current Ugandan guidance and facility SOPs before a pilot.
- No pilot has been conducted by this software change. Real-world performance and environmental impact remain unproven.
