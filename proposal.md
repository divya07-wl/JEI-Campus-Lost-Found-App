# Android Midterm Project — Planning & Design Proposal

| Item | Details |
|------|---------|
| **App Name** | JEI Campus Lost & Found App |
| **Student ID** | 2025621033 |
| **Student Name** | RAI DIVYA |
| **GitHub Repository URL** | https://github.com/divya07-wl/JEI-Campus-Lost-Found-App |
| **Date** | 2026-10-27 |

## 1. Project Overview

JEI Campus Lost & Found App helps JEI University students report lost or found items, browse campus posts, and submit a claim until the item is returned.

Students currently ask in group chats or visit offices, so reports are scattered and there is no clear returned status. This app stores reports in a local Room database and lets students search by title, category, and location.

## 2. Core Features

| # | Feature | User Action & Expected Result | Android Technology |
|---|---------|-------------------------------|--------------------|
| 1 | Browse items | The user sees cards with title, type, location, and status. | RecyclerView, ItemListFragment, Room Flow |
| 2 | Search and filter | Search or filter by Lost, Found, or Returned. | ChipGroup, ViewModel, Room query |
| 3 | Report an item | The user enters title, category, location, date, description, and type. | ReportItemFragment, DatePickerDialog, Room @Insert |
| 4 | Claim an item | The user opens details and submits a claim message. | ItemDetailFragment, Navigation, Room @Insert |
| 5 | My Reports | The user deletes a report or marks it returned. | MyReportsFragment, RecyclerView, Room @Update / @Delete |

The most important feature is reporting an item, because the list depends on saved reports.

RecyclerView is used on the item list and on My Reports. Cards show the title, type badge, location, date, and status. Adapters use ListAdapter and DiffUtil.

## 3. Screens

1. **ItemListFragment** — browse, search, and filter reports.
2. **ItemDetailFragment** — full item information and claim action.
3. **ReportItemFragment** — form to report a lost or found item.
4. **MyReportsFragment** — the user's own reports, with delete and mark returned.

## 4. Navigation

`ItemListFragment` opens `ItemDetailFragment` with `itemId`, or `ReportItemFragment` from the add button. Bottom navigation switches between the item list and My Reports. My Reports can also open item details.

## 5. Room Database

Database name: `jei_lost_found_database`

| Table | Key columns |
|-------|-------------|
| `users` | `user_id` PK, `student_number`, `full_name`, `email` |
| `items` | `item_id` PK, `reporter_id` FK, `title`, `category`, `location`, `item_date`, `description`, `type` (LOST/FOUND), `status` (OPEN/CLAIMED/RETURNED) |
| `claims` | `claim_id` PK, `item_id` FK, `claimant_id` FK, `message`, `status`, `created_at` |

Example item: Blue Umbrella, FOUND, Student Center, 2026-10-26, status OPEN, reported by RAI DIVYA.

## 6. Data flow

Fragment validates input, ViewModel calls the repository, the repository calls the DAO, and Room updates the Flow so the RecyclerView refreshes.

## 7. Development Schedule

| Weeks | Work |
|-------|------|
| 9–10 | XML layouts for the four screens |
| 11–12 | Navigation and RecyclerView |
| 13–14 | Room entities, DAOs, and ViewModels |
| 15–16 | Testing, GitHub upload, and demo |

## 8. AI disclosure

Cursor AI was used to assist in structuring the project proposal and drafting the initial Room DB schema. All content was thoroughly reviewed, edited, and validated by the student.
