/* Recovered semantic model, NOT original source and NOT a firmware build.
 * Evidence: supplied H4 LK and fresh_analysis/h4_pseudocode exports.
 * Types/names are analyst interpretations. Raw addresses identify evidence.
 */
#include <stdint.h>
#include <stdbool.h>

enum { MI_UNLOCKED = 3, MI_LOCKED = 4 };

/* H4 VA ffff000090789aec / file 0x89cec: exact observed H3 patch semantics.
 * The original routine checks RPMB magic/signature and writes state 3 or 4.
 * This replacement bypasses that routine's accesses; it does not persist state.
 */
int patched_mi_get_lock_state(uint32_t *out_state) {
    *out_state = MI_UNLOCKED;
    return 0;
}

/* H4 VA ffff0000907b65c4 / file 0xb67c4. */
int recovered_get_locked(uint32_t *out_locked) {
    uint32_t state = 0;
    (void)patched_mi_get_lock_state(&state);
    *out_locked = state != MI_UNLOCKED;
    return 0;
}

/* AVB ops table at VA ffff0000908385e0 / file 0x1387e0.
 * Callback +0x48 is ffff0000907c5ba8 / file 0xc5da8.
 */
int recovered_avb_read_is_device_unlocked(void *ops, bool *out_unlocked) {
    uint32_t locked = 1;
    (void)ops;
    if (!out_unlocked) return 2;
    *out_unlocked = false;
    if (recovered_get_locked(&locked) != 0) return 2;
    *out_unlocked = locked == 0;
    return 0;
}

/* H4 VA ffff000090768c94 / file 0x68e94. */
bool recovered_img_auth_required(void) {
    uint32_t state = MI_LOCKED;
    (void)patched_mi_get_lock_state(&state);
    return state != MI_UNLOCKED;
}

/* Relevant flag construction in ffff000090768854 / file 0x68a54.
 * Other existing flags: bit2 for recovery, bit1 from separate config.
 */
uint32_t recovered_avb_flags(bool recovery, bool separate_config_bit) {
    uint32_t state = MI_LOCKED;
    (void)patched_mi_get_lock_state(&state);
    uint32_t flags = state == MI_UNLOCKED ? 1u : 0u;
    if (recovery) flags |= 4u;
    if (!separate_config_bit) flags |= 2u;
    return flags;
}

/* Inner slot verifier ffff0000907be740: bounded numeric result filter.
 * Bit0 enables the path; fatal result codes still prevent normal output.
 * Numeric values retained rather than importing unverified enum labels.
 */
bool recovered_avb_result_eligible(uint32_t result, uint32_t flags) {
    if (!(flags & 1u) && result != 0) return false;
    return result <= 5 && ((1u << result) & 0x39u) != 0;
}

/* Fastboot ffff00009070c3ac uses recovered_get_locked:
 * secure = locked ? "yes" : "no";
 * unlocked = locked ? "no" : "yes";
 * Separate variable "fuse" derives from hardware-function(0x1b)&0xe.
 * No separately named critical-unlock query was established in this search.
 * AVB cmdline ffff0000907bc5d4 uses +0x48 callback to emit
 * androidboot.vbmeta.device_state=unlocked when out_unlocked is true.
 */
