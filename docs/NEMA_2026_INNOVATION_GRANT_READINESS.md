# SmartMed-UG — NEMA Innovation Grant Readiness Pack

**Status as of 10 October 2026:** the Android prototype builds on `main` and has automated unit tests. A facility pilot has **not** been conducted, no health facility has yet been confirmed as a pilot site, and no university/facility approval is recorded in this repository. This document is a preparation aid, not proof of eligibility or piloting.

## 1. The key eligibility gate — do not skip it

NEMA's official [Research and Innovations Student Support Grant 2026–2027](https://www.nema.go.ug/en/research-and-innovation-grant/) states that the innovation grant is for registered students at accredited Ugandan universities, supports environmental innovations **at the piloting phase**, has a maximum of **UGX 7,000,000**, and must be completed within six months of grant approval and signing. The published deadline is **Friday, 23 October 2026**.

The official [Innovation Grant Application Form](https://www.nema.go.ug/en/innovation-application-form/) asks for (among other things) the research origin and findings, the environmental challenge, novelty, proof that ideation/prototyping is complete, pilot site and users, measurable indicators, a six-month work plan, budget, and supporting documents.

**Current gap:** a green build and an APK show that software exists; they do not, by themselves, demonstrate that the innovation is already at the piloting phase. Do not describe a real facility pilot, approval, research finding, environmental benefit, or NEMA integration unless evidence exists. Contact NEMA to ask whether your present evidence meets its interpretation of "piloting phase" before investing time in a final application.

The NCHE public register currently lists Avance International University as holding a **provisional licence to operate as a university** and lists Bachelor of Nursing Science with active programme accreditation shown through 22 August 2027. Because NEMA's call specifically says "accredited university", ask NEMA and your university to confirm in writing whether your exact institution, programme, and current student-registration status satisfy this grant's eligibility wording. See [NCHE university register](https://unche.or.ug/universities/) and [NCHE programme register](https://unche.or.ug/all-academic-programs/?institution_id=2024).

## 2. Positioning and novelty

NEMA announced the Solid Waste Information Management System (SWIMS) on 24 September 2026 as a centralised national repository for solid-waste information from credible waste handlers, district/urban authorities, and civil-society organisations. Therefore, SmartMed must **not** claim that Uganda has no digital waste information system. See [NEMA's SWIMS announcement](https://www.nema.go.ug/en/nema-and-cse-launch-centralised-waste-management-information-database-for-uganda/).

A defensible proposed distinction, subject to a real user and systems review, is:

> SmartMed-UG is a proposed facility-level, healthcare-waste workflow companion designed for intermittent connectivity. It connects a locally recorded batch to handover and transporter references, then to separate treatment/final-disposal entries and evidence-review status. Its purpose is to make missing documentation visible to facility staff, rather than to declare compliance automatically. The prototype is local-only and has no SWIMS or other government-system integration. Any future compatibility would require NEMA/facility guidance and authorised technical arrangements.

The public SWIMS announcement does not establish whether SWIMS already covers every healthcare-waste use case or offers an API. Ask NEMA about scope, data definitions and complementarity; do not assume duplication or integration compatibility.

## 3. Draft innovation title and priority area

**Proposed title:** SmartMed-UG: Offline-First Healthcare-Waste Batch, Handover and Disposal Traceability for Health Facilities.

**Priority area:** Pollution and Waste Management. The project also uses environmental digital technology, but select the priority category exactly as instructed in the current application form.

## 4. Draft answers for the application form

These are editable drafts within the form's indicated word limits. Replace bracketed fields and confirm every statement. They are not ready to submit until the factual gaps below are resolved.

### Section 3 — Research origin and findings (maximum 300 words)

**Do not submit a fictional origin story.** This section asks for the research study from which the innovation emerged, including key findings. The repository currently contains prototype software, not a documented facility assessment or completed study.

Fill this section only from evidence you actually collect or already hold:

> SmartMed-UG emerged from [describe the actual nursing/environmental-health problem observation, course project, structured needs assessment, or study that genuinely led to the idea]. On [date/period], [person/team] reviewed or observed [specific records/workflow/setting], with permission from [responsible person, where applicable]. The evidence comprised [number and type of records, interviews, observations, or other sources]. The documented findings were [insert actual counts or themes with a denominator and dates; do not invent values]. These findings identified [specific, supported documentation or workflow problem]. The initial prototype was then designed to [explain how those findings informed the offline batch, handover and outcome workflow]. At present, the application has passed automated software build/unit-test checks; a health-facility pilot and measured environmental impact have not yet been demonstrated.

If you have not completed a study or structured needs assessment, ask NEMA whether the form accepts an innovation developed from documented problem discovery without a separate formal research study. Do not turn assumptions into "findings".

### Section 4 — Environmental management challenge (maximum 200 words)

> Healthcare waste must be segregated, moved, treated and finally disposed of according to the applicable Ministry of Health guidance and the facility's procedures. For a facility relying on paper registers or disconnected records, staff may find it difficult to see whether a batch record, handover reference and subsequent treatment/disposal evidence are complete in one place. The frequency and extent of this problem at the proposed pilot site have not yet been measured and will be established with the facility before the pilot.
>
> SmartMed-UG will test whether an offline-capable phone-based record can improve the completeness and retrieval of healthcare-waste documentation without requiring continuous internet access. The pilot will compare defined documentation indicators before and during use. It will not replace the facility's official registers, waste-handling practices, authorised disposal arrangements, or regulatory reporting procedures. Better records alone will not be presented as proof that pollution or illegal dumping has decreased.

### Section 5 — Description and novelty (maximum 200 words)

> SmartMed-UG is a native Android prototype using local Room database storage. It records a unique batch reference, waste category/subcategory, quantity and unit, facility/collection point and recorder role. It links batches to handover and transporter/manifest references, and to separate treatment and final-disposal outcome entries. Missing evidence references, unreviewed references and locally recorded review entries are displayed as different states. Records can be searched and exported as CSV, and the prototype can be used without an internet connection.
>
> Its proposed novelty is the connected, facility-level healthcare-waste workflow and documentation-gap view for settings with intermittent connectivity—not the general concept of digitising waste information. NEMA has announced SWIMS, so SmartMed is positioned as a possible specialised workflow companion, pending confirmation of SWIMS scope and relevant user needs. The current build has no user authentication, encrypted database, backend synchronisation, or official NEMA/Ministry integration. A recorded entry or review does not prove safe treatment, final disposal or regulatory compliance.

### Section 6 — Evidence of completed ideation and prototyping (maximum 200 words)

> Evidence of the current software prototype includes the public source repository, automated Android build and unit-test run, and a downloadable debug APK. The prototype contains batch registration/search, reasoned corrections and soft archiving, a local audit-event log, handover and transporter references, treatment/final-disposal outcome records, a local suspected-dumping incident log, category/daily summaries, and CSV exports. Automated tests cover key input-validation and CSV handling rules.
>
> Repository: https://github.com/bmwanje349-lab/SmartMed-UG
>
> Passing build: https://github.com/bmwanje349-lab/SmartMed-UG/actions/runs/38054348366
>
> These are software-development proofs only. They are not evidence of a facility pilot, secure production readiness, endorsement, improved compliance, or reduced environmental pollution. Attach screenshots or a short demonstration only after checking that they match the current build. Add actual pilot-site permissions and demonstration/test results only when obtained.

### Section 7 — Pilot objective, site and intended users (maximum 250 words)

> **Objective:** assess the feasibility and usefulness of SmartMed-UG for improving completeness and retrieval of healthcare-waste batch, handover and outcome documentation at one participating health facility.
>
> **Proposed site:** [facility name, district and facility level — insert only after the facility has agreed in writing]. The pilot will not begin until the facility in-charge, relevant university supervisor, and any required institutional/research or district reviewers have approved the activity. The facility's waste-management focal person will map the prototype's draft vocabulary and evidence requirements to current Ministry of Health guidance and the facility SOPs.
>
> **Intended users:** designated facility waste-management focal person and authorised staff who create or check waste records, with oversight by the facility manager and relevant environmental-health personnel. The prototype's role picker is currently a label, not identity verification; access controls and data-protection safeguards must be addressed before any live facility data is stored.
>
> The evaluation will use a defined baseline and follow-up period, the same indicator definitions at both points, and only the minimum non-patient operational data needed. Existing safe handling and disposal procedures remain in force. App entries will not replace official records. The pilot will assess documentation and usability; it will not claim reduced environmental contamination unless separate appropriate evidence supports that conclusion.

### Section 8 — Proposed results, indicators, targets and verification

Agree the final definitions with the facility before data collection. The targets below are **provisional planning targets**, not existing results.

| Expected result | Indicator and calculation | Provisional target | Means of verification |
|---|---|---:|---|
| Required batch records are more complete | Complete eligible batch records ÷ all eligible records reviewed × 100 | At least 90% during the pilot | De-identified checklist review against a facility-approved field list |
| Eligible handovers have supporting references | Eligible handovers with the required reference(s) ÷ all eligible handovers reviewed × 100 | At least 90% during the pilot, if consistent with the facility SOP | Compare app log to authorised register/document references |
| Staff can retrieve a record promptly | Median time taken by a trained user to find a specified batch during a supervised usability task | 2 minutes or less | Timed task sheet, task success and error count |
| Core offline operation is reliable | Fictional test records saved and still available after app restart and airplane-mode test ÷ fictional records created × 100 | 100% in the agreed test set | Dated test log and CI report; report every failure, not only successes |

Do not compare kilograms, litres, items and bags as if they were one common unit. Do not define missing treatment/disposal as a compliance failure unless the facility's SOP says that outcome is required for that batch. Use the same eligibility rules for baseline and follow-up.

### Section 9 — Adoption, replication and scale-up (maximum 200 words)

> If the pilot demonstrates acceptable data completeness, offline reliability, record retrieval and usability, the next step would be a supervised review with the pilot facility and university. The facility would decide whether the workflow merits continued use and identify changes needed for its procedures. Further replication could be considered with other facilities only after their own waste focal persons map categories and evidence requirements to current guidance and SOPs.
>
> Before wider adoption, SmartMed would require authenticated staff identities, facility-level permissions, appropriate protection of local data, a tested backup and recovery process, robust change history, and a secure, authorised synchronisation design. Any exchange with NEMA SWIMS, Ministry of Health platforms, district systems or waste-handler systems would need confirmation of scope, data definitions, approval and technical arrangements. No such connection currently exists. Scaling decisions should be based on pilot results, lifecycle cost, maintenance arrangements, training needs, and evidence that the workflow fits existing records—not on the prototype's build success alone.

## 5. Six-month work plan after grant approval

| Period | Key activity | Output/evidence |
|---|---|---|
| Month 1 | Obtain/confirm facility and university permissions; consult the facility waste focal person; map current Ministry of Health guidance and local SOPs; confirm any required ethics/research review; agree data minimisation and security plan | Written site agreement/permission; approved workflow map; evaluation protocol and indicator definitions |
| Month 2 | Implement only pilot-critical refinements; review authentication/data-protection risks; test Room migrations and backups on copies of fictional data; validate categories and forms with authorised personnel | Versioned test build, defect log, migration test record, signed category/evidence mapping |
| Month 3 | Collect an approved baseline using existing authorised records; train designated staff; run fictional-data exercises and usability tests | Baseline table, training attendance/feedback, test log, corrected defect list |
| Month 4 | Conduct supervised, permissioned pilot; compare app records with authorised source records; monitor offline operation and documentation gaps | Pilot log, documented exceptions, weekly monitoring summaries |
| Month 5 | Continue monitoring; repeat usability/retrieval tasks; analyse baseline-versus-pilot documentation indicators | Clean analysis table, limitations, user-feedback summary |
| Month 6 | Review findings with facility/university stakeholders; prepare final technical and environmental report; document adoption conditions and budget expenditure | Final report, validated results, recommendations and dissemination record |

If the proposed pilot cannot lawfully or practically begin until approvals or security work are complete, revise the timeline with NEMA rather than pretending the pilot has started.

## 6. Provisional pilot budget (not quotations)

NEMA caps an innovation grant at UGX 7,000,000 and says costs must be directly related to the project; the call excludes tuition and salaries. This draft focuses on supervised piloting/validation rather than general product development. **Obtain realistic unit-cost quotations and confirm eligibility for each item before submitting.** Reduce or replace any unquotable or ineligible line.

| Budget item/activity | Unit | Quantity | Unit cost (UGX) | Total (UGX) |
|---|---|---:|---:|---:|
| Pilot-critical data protection, migration and reliability hardening (defined deliverables; no salary claim) | Package | 1 | 1,200,000 | 1,200,000 |
| Local return travel for baseline, training, monitoring and follow-up visits | Trip | 24 | 25,000 | 600,000 |
| Facility workflow validation and staff orientation sessions, including session materials | Session | 4 | 150,000 | 600,000 |
| Approved field forms, batch ID labels where permitted by facility SOPs, stationery and printing | Lot | 1 | 350,000 | 350,000 |
| Project coordination connectivity/airtime | Month | 6 | 40,000 | 240,000 |
| Data collation, analysis and final report preparation | Package | 1 | 850,000 | 850,000 |
| Results review/dissemination session and report copies | Session/lot | 1 | 300,000 | 300,000 |
| **Provisional total** |  |  |  | **4,140,000** |

The total is intentionally below the cap; it is not a recommendation to spend unneeded funds. Check that travel and session costs do not duplicate one another, that the hardening item is allowed, and that every rate is backed by a quote. Do not list labels as QR/barcode technology: the current app does not generate or scan QR codes.

## 7. Supporting documents required by the current NEMA form

The official form requests one PDF containing:
1. Curriculum Vitae of the innovator (maximum two pages).
2. Signed academic testimonial/provisional results as proof of student status.
3. Letter of recommendation from the Head of Department addressed to the Executive Director, NEMA.
4. Letter of Expression of Interest addressed to the Executive Director, NEMA.
5. Copy of National ID/passport.
6. NEMA declaration form (Appendix 1).

Follow the current application page's submission method and file instructions. Keep identity documents private and do not commit them to this public GitHub repository. The official form page lists email submission and a submission control; confirm the exact process with NEMA if anything on the live form is unclear.

## 8. Must-complete checklist before submission

- [ ] Ask NEMA to confirm whether the current state of the project satisfies "at piloting phase".
- [ ] Ask NEMA and the university to confirm student/programme/institution eligibility under the call.
- [ ] Identify a real pilot facility and get written permission; do not write a site name before that.
- [ ] Obtain the required HoD recommendation and expression-of-interest letters.
- [ ] Obtain an actual documented research/problem-discovery origin and findings for Section 3, or get NEMA clarification on the requirement.
- [ ] Agree baseline period, sample/eligibility rules, indicator definitions and data-protection arrangements with the facility.
- [ ] Review and address local data protection/authentication before handling identifiable or sensitive facility operational records.
- [ ] Replace every provisional budget amount with a defensible quote or written estimate; ensure no salary/tuition/ineligible spending.
- [ ] Assemble the six required supporting documents as one PDF, protect it, and submit only through NEMA's current channel.
- [ ] Keep proof of submission and a copy of the exact version submitted.

## 9. Official references

- NEMA, Research and Innovations Student Support Grant 2026–2027: https://www.nema.go.ug/en/research-and-innovation-grant/
- NEMA, Innovation Grant Application Form: https://www.nema.go.ug/en/innovation-application-form/
- NEMA, SWIMS announcement (24 September 2026): https://www.nema.go.ug/en/nema-and-cse-launch-centralised-waste-management-information-database-for-uganda/
- Ministry of Health, National Healthcare Waste Operational Guideline (2025): https://library.health.go.ug/environmental-health/inspection-sanitation-and-hygiene/national-healthcare-waste-operational
- Ministry of Health, National Healthcare Waste Management Strategy (2025/26–2029/30): https://library.health.go.ug/index.php/environmental-health/inspection-sanitation-and-hygiene/national-healthcare-waste-management
- NCHE university register: https://unche.or.ug/universities/
- NCHE programme register for Avance International University: https://unche.or.ug/all-academic-programs/?institution_id=2024
