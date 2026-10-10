# SmartMed-UG roadmap

## Stage 1 — Offline prototype (current)

- Native Kotlin/Jetpack Compose Android interface.
- Room local database with an explicit versioned migration.
- Controlled, draft healthcare-waste categories and supported units.
- Batch registration, searchable active list, reasoned corrections and soft archiving.
- Transactional local audit events.
- Handover log with transporter organisation and transport-manifest reference, plus explicit missing/unreviewed/review-entry statuses.
- Treatment and final-disposal outcomes with method/facility, performed date, evidence reference and a separate review entry.
- A local-only suspected-dumping incident log with date, location description, factual observation, and optional manually entered external reference. Reports remain unverified and are not sent to authorities.
- Local summary by category and by day, documentation-gap indicators, and dedicated CSV exports for batch/handover, treatment/disposal and suspected-dumping records.
- Unit tests and CI APK build.
- Pending sync state is recorded, but no upload or server is implemented.

## Stage 2 — Supervised workflow validation

- Confirm categories, subcategories, units and evidence requirements against current Ministry of Health guidance and the facility's SOPs.
- Review user roles and work with a facility supervisor to define the audit/correction workflow.
- Test database migrations from version 1 to version 4, interrupted writes, duplicate IDs, CSV output and record retention.
- Use fictional records until written facility and required institutional approvals are in place.
- Run a permitted pilot only after permissions, training, baseline definition and a safe operating protocol are agreed.

## Stage 3 — Pilot evidence

Candidate metrics (set the final target with a supervisor and facility before data collection):

- Required-field completeness rate.
- Proportion of eligible handovers with the facility-required evidence reference.
- Number and age of unresolved documentation gaps.
- Time to find a batch or handover record.
- User-reported usability and time spent completing a record.
- Data loss, duplicate record and failed-save rate during offline tests.

Improved documentation is not by itself proof that illegal dumping or environmental contamination has fallen. Environmental impact requires independent, appropriate evidence.

## Stage 4 — Production prerequisites

- Authenticated users and server-enforced role/facility permissions.
- A secure backend, transport security, authentication, data minimisation, retention policy and tested backups.
- A durable sync outbox, retry with backoff, idempotency keys, conflict resolution, server acknowledgement and visible sync timestamps.
- Tamper-evident audit logging and a clear correction/retention policy.
- Verified evidence-handling, secure export and applicable privacy/security review.
- Operational support, incident response and facility-approved training.
- Any government integrations must be authorised, technically documented and tested; none is implied by this prototype.
