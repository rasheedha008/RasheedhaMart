# Week 6: Reviews, Ratings & Edge Case Hardening (Aug 31 – Sep 6)

## Goal
Implement Product Reviews & Star Ratings (F8) and harden application boundaries against edge cases.

## Accomplishments
- **F8: Product Reviews & Ratings**: Implemented `ReviewDAO.java`, `ReviewService.java`, and `ReviewServlet.java` enabling buyers to leave 1–5 star reviews on items they have purchased.
- **Verified Purchase Enforcement**: Restricted review submissions to buyers who have an established purchase history for the target product.
- **Edge Case Hardening**:
  - Prevented checkout attempts on empty carts.
  - Handled race conditions during simultaneous stock depletion via atomic SQL updates (`stock_quantity >= quantity`).
  - Added HTML escaping (`XSSUtil.java`) to scrub review comments and user inputs against XSS attacks.
