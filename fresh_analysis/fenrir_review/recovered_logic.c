/* Analyst model of observed Fenrir changes, NOT original source or flash code.
 * Input SHA256 dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113.
 * Offsets are payload-relative for main/AEE as explicitly labelled.
 * See native probe JSON for fixtures, mocks, boundaries and remaining unknowns.
 */
#include <stdint.h>

/* main 0x88750. The underlying persistent-state routine is not rewritten;
 * this common wrapper now returns before calling it. */
int fenrir_common_lock_state(uint32_t *out) {
    *out = 4;
    return 0;
}

/* Existing downstream interpretation, unchanged by Fenrir. */
int existing_locked_boolean(uint32_t state) { return state != 3; }
int existing_main_image_auth_required(uint32_t state) { return state != 3; }

/* main 0x68668: force the unlock-style AVB allowance flag even though the
 * common state is locked. The unchanged verifier retains other result filters
 * and IO/format failure paths. This does not model the full slot verifier. */
int fenrir_avb_allow_error_flag(uint32_t ignored_lock_state) {
    (void)ignored_lock_state;
    return 1;
}

/* main 0x692f4 / changed instruction0x692fc. Writes RAM cache, not efuse/RPMB.
 * Any caller reading this cache after the setter observes state0 (green),
 * including the bounded warning function and root-of-trust copy sequence. */
void fenrir_set_verified_boot_state(volatile uint32_t *cache, uint32_t requested) {
    (void)requested;
    *cache = 0;
}

/* Existing H3 handoff observes the fake state and sends:
 * packet1: existing calculated OEM public-key hash (because locked_boolean=1)
 * packet2: lock_boolean=1, boot_state=0, existing OS/patch-level fields
 * packet3/4: existing digest/remaining version fields
 * packet5: existing terminator
 * No changes to these packet-assembly instructions were found in Fenrir.
 * The native secure-call destination was mocked during testing.
 */

/* Additional changed gates, deliberately not represented as full functions:
 * main0xa110/0xa124 and AEE0x13be8/0x13bfc:
 *   remove two command-descriptor permission branches; continue toward callback.
 * main0xce04:
 *   force branch to0xce14, bypassing this locked-flash restriction/exception gate.
 * main0xd1d0/0xd1d8 and AEE0x16498/0x164a0:
 *   load another scratch word, then unconditionally bypass this lock-denial path.
 * Other handler code, earlier helper-error paths and partition writes were not
 * replaced by an unconditional success stub and were not fully emulated here.
 */
