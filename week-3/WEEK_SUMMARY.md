# Week 3: Seller Dashboard, DTOs & Service Tests (Aug 10 – Aug 16)

## Goal
Establish seller inventory management, refine DTO layer architecture, standardize API response envelopes, and implement unit tests with Mockito.

## Accomplishments
- **Seller Listing Management**: Developed seller portal APIs allowing sellers to inspect stock levels, add new products, edit existing items, and delete listings.
- **DTO Separation Pattern (Section 13 Compliance)**: Created `UserResponseDTO` to strictly prevent password hashes from being leaked over API responses.
- **JSON Response Envelope**: Standardized all API responses under `/api/v1/...` using `{ "success": boolean, "data": T, "error": String, "timestamp": String }`.
- **Service Layer Mockito Tests**: Created Mockito unit tests mocking DAO behaviors to verify business logic isolation.
