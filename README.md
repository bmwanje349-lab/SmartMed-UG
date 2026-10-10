# SmartMed-UG

**Offline-first healthcare-waste traceability prototype for environmental protection in Uganda.**

SmartMed-UG is a Kotlin/Android prototype designed to test whether simple local batch and handover records can make healthcare-waste documentation gaps easier to identify at facility level, including when internet access is unavailable.

## Current features

- Native Android UI built with Kotlin, Jetpack Compose and Room.
- Local batch records with a generated batch ID, facility, controlled draft category/subcategory, quantity/unit, collection point, recorder role, timestamp and revision.
- Search across active batches.
- Corrections require a reason. The previous and corrected snapshots are appended to a local audit log.
- Soft archiving rather than destructive delete; batch history and related handovers are retained.
- Batch handover records with sender/receiver role labels and a supporting-document reference.
- Separate status for missing evidence, recorded-but-unreviewed reference, and a review entry. A review entry is not proof of safe treatment or disposal.
- Basic count-by-category report and local CSV export.
- A sync state is stored for future work. **There is no server connection or synchronisation implementation yet.**
- Local-only suspected-dumping incident log with a date, safe location description, factual observation and optional manually entered external reference. All reports remain unverified; the app sends nothing to authorities.
- GitHub Actions runs local unit tests and builds/uploads the debug APK.

## Build and test

The GitHub Actions workflow runs:

```bash
gradle testDebugUnitTest assembleDebug --no-daemon
```

The APK is provided as the `smartmed-debug-apk` artifact for successful runs. It is a debug build and is intended for controlled testing.

## Safety, privacy and regulatory limits

- Use fictional records during software tests. Do not enter patient names, identifiers or clinical information.
- Follow existing facility segregation, PPE, transport, treatment and disposal procedures. Do not approach, open, move or handle waste to record it.
- The initial category vocabulary is a draft for prototype use. It must be checked and mapped by the facility's waste/environmental-health focal person to the current Uganda Ministry of Health operational guidelines and facility SOPs before a pilot.
- A recorder/reviewer role is a label, not a verified identity. The prototype has no login or server-enforced access control.
- The local Room database is not encrypted at rest by this prototype; do not put sensitive or identifiable operational data into it until a reviewed security design is implemented.
- The audit trail is local and not tamper-proof. CSV exports are not automatically encrypted or transmitted.
- Recording a handover or a review does not independently establish that waste was received, treated or finally disposed of safely.
- No NEMA, Ministry, district or facility integration is implemented or implied.
- The software does not itself prove environmental impact or constitute a completed real-world pilot.
- Do not use the suspected-dumping screen to approach or handle waste, accuse named people, or claim an authority was notified. The feature is a local record, not an official reporting channel.

## Current readiness

This is a **technical prototype**, not an approved production system. Read [the safe pilot checklist](docs/SAFE_PILOT_CHECKLIST.md) and [the roadmap](docs/ROADMAP.md) before planning any real facility use.

Relevant official references for a supervisor-led mapping exercise:
- [Uganda Ministry of Health — National Healthcare Waste Operational Guideline (2025)](https://library.health.go.ug/environmental-health/inspection-sanitation-and-hygiene/national-healthcare-waste-operational)
- [Uganda Ministry of Health — National Healthcare Waste Management Strategy (2025/26–2029/30)](https://library.health.go.ug/index.php/environmental-health/inspection-sanitation-and-hygiene/national-healthcare-waste-management)
- [NEMA — Research and Innovations Student Support Grant 2026–2027](https://www.nema.go.ug/en/research-and-innovation-grant/)

NEMA's current innovation call states that support is for innovations at piloting phase, not idea generation, basic research, concept development or initial prototype development. Software build success alone is not evidence that a project meets that threshold; follow NEMA's current application instructions and secure all required university/facility approvals honestly.

## Next steps

1. Confirm category names and evidence requirements with an appropriate facility supervisor.
2. Test version-1-to-version-2 database migration and all batch/handover workflows using fictional data.
3. Confirm university, facility, ethics/research and NEMA requirements before collecting any real data.
4. Conduct and document an approved pilot with baseline and follow-up measures before claiming operational or environmental benefit.
5. Before production, add authenticated identities, access enforcement, secure backend/sync, tested backups, tamper-evident audit logging, privacy/security review and operational support.
