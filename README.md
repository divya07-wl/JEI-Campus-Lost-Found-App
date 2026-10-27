# JEI Campus Lost & Found App

Android project for **JEI University** (Computer Software).

| | |
|---|---|
| **Student** | RAI DIVYA (2025621033) |
| **GitHub** | https://github.com/divya07-wl/JEI-Campus-Lost-Found-App |
| **Language / UI** | Kotlin, XML layouts with ViewBinding, Material 3 |
| **Architecture** | Fragment → ViewModel → Repository → Room DAO → SQLite |
| **Min / Target SDK** | 28 / 36 |

Students report items they lost or found on campus, browse the list, claim an item, and mark a report as returned.

## Screens

1. **ItemListFragment** – RecyclerView of reports, search, Lost / Found / Returned filters, and a button to add a report.
2. **ItemDetailFragment** – item information and a claim button.
3. **ReportItemFragment** – form for a lost or found item.
4. **MyReportsFragment** – the current user's reports, with delete and mark-returned actions.

## Database (`jei_lost_found_database`)

| Table | Purpose |
|---|---|
| `users` | students |
| `items` | lost and found reports (`reporter_id` → `users.user_id`) |
| `claims` | claim requests (`item_id` → `items.item_id`, `claimant_id` → `users.user_id`) |

Sample data is inserted the first time the database is created. The app runs as the demo student **RAI DIVYA** (`user_id = 1`).

## Build

```bash
gradlew.bat assembleDebug
gradlew.bat installDebug
```

Open the folder in Android Studio and press Run. `local.properties` is created locally and is not committed.

## AI disclosure

Cursor AI was used to assist in structuring the project proposal and scaffolding the app code. All content was reviewed and validated by the student.
