# Ba bản ENG all-region HWC, mỗi bản hai file MTE

Theo xác nhận của người dùng ngày 2026-11-05: **ENG unlocked, ENG fakelock v2, ENG stock-policy**; mỗi bản có file giữ xử lý MTE gốc và file ép nhánh tắt MTE phía kernel. Tổng cộng **6 file riêng**, tất cả có HWC tự chọn vùng. Chưa flash/boot test các biến thể này.

Tải cùng lúc: [ZIP đủ 6 file](lk_eng_all_region_6_files.zip). [release_manifest.json](release_manifest.json) ghi đủ tên, đường dẫn, hash, loại và nguồn của từng file; `SHA256SUMS.txt` trong ZIP dùng để đối chiếu.

| Bản | Không thêm patch tắt MTE | Ép nhánh tắt MTE phía kernel |
|---|---|---|
| ENG unlocked + HWC | [LK gốc của cặp](../lk_h3_eng_auto_hwc_v1/unlocked/lk_hyperos3_ENG_UNLOCKBL_AUTO_HWC_TEST.img) | [LK MTE_OFF](unlocked_auto_hwc/lk_hyperos3_ENG_UNLOCKBL_AUTO_HWC_MTE_OFF_TEST.img) |
| ENG fakelock v2 + HWC | [LK gốc của cặp](../lk_h3_eng_auto_hwc_v1/fakelock_v2/lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_TEST.img) | [LK MTE_OFF](fakelock_v2_auto_hwc/lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_MTE_OFF_TEST.img) |
| ENG stock-policy + HWC | [LK gốc của cặp](../lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/lk_hyperos3_ENG_STOCK_AUTO_HWC_CN_GLOBAL_EEA_TW_TEST.img) | [LK MTE_OFF](stock_policy_auto_hwc/lk_hyperos3_ENG_STOCK_AUTO_HWC_MTE_OFF_TEST.img) |

"Không tắt MTE" nghĩa là **không thêm patch ép tắt**: vẫn chạy chính sách MTE gốc theo cờ preloader/cấu hình. Không phải file cưỡng bức bật MTE. Mỗi file dài 4.043.792 byte.

## All-region ở đây là gì?

Cả sáu dùng `countrycode` theo slot hiện hành để chọn HWC: đúng `cn` không phân biệt hoa thường → `CN`; mọi chuỗi khác không rỗng, gồm Global/EEA/TW → `GLOBAL`; thiếu/rỗng/lỗi đọc → HWC phần cứng gốc. Nếu giữ countrycode của ROM cũ, vùng chọn có thể vẫn theo dữ liệu cũ.

Giữ đúng chức năng từng nhánh. Unlocked giữ force-unlock nội bộ; fakelock v2 giữ force-unlock và các lớp báo locked/green của v2; stock-policy giữ xử lý trạng thái khóa gốc và callback tin các khóa ENG/CN/GL đã thêm từ trước. Không chuyển stock-policy thành force-unlock hoặc fakelock. **HWC all-region không tự chứng minh mọi ROM, model, rollback hoặc khóa ký mới đều tương thích.** Chưa boot EEA/TW thực tế, chưa chứng minh mở 5G, khóa thật hoặc OTA.

## MTE được xử lý thế nào?

MTE là *Memory Tagging Extension*, tính năng gắn tag cho bộ nhớ để phát hiện lỗi truy cập. [Nghiên cứu đầy đủ](../../fresh_analysis/mte_research/README_VI.md) đã tìm đường thật từ preloader đến LK:

1. Bản preloader đã thu thập từ máy có nhánh debug OR ba bit thấp vào `mte_info`; misc/dconfig vẫn có thể ghi đè. File preloader ENG cũ cung cấp ban đầu khác byte và không có nhánh mặc định này.
2. LK lấy `mte_info` qua boot tag và lưu ở global, rồi chạy `kernel_mte_init`.
3. LK vốn có đường suppress: cấu hình booker theo nhánh off của chính nó và xuất `arm64.nomte` xuống kernel.

Ba file `MTE_OFF` chỉ đổi một lệnh tại payload `0x34d20` (whole-file `0x34f20`): `20020054` (`b.eq 0x34d64`) → `11000014` (`b 0x34d64`). Nhờ vậy LK luôn chọn nhánh suppress sẵn có. Không sửa global cờ đầu vào, không thêm lệnh ghi thanh ghi hệ thống CPU và không đụng preloader/misc/dconfig. Cập nhật prefix hash 180 byte, giữ nguyên DER gốc và mọi member khác.

Tham số `arm64.nomte` là giao diện tắt hỗ trợ MTE của kernel: xem [Linux v6.12](https://github.com/torvalds/linux/blob/v6.12/arch/arm64/kernel/pi/idreg-override.c#L207) và [hướng dẫn bootloader Android](https://source.android.com/docs/security/test/memory-safety/bootloader-support).

**Phạm vi là yêu cầu tắt MTE phía kernel/Android, không quay ngược tắt MTE trong preloader đã chạy trước LK.** Chưa đo kernel vendor trên điện thoại nhận tham số hoặc HWCAP sau patch. Không chứng minh thay đổi này chữa các lần sọc/đen màn trước đây. Bản off giảm cơ chế phát hiện lỗi bộ nhớ trên kernel có hỗ trợ tham số đó; chọn riêng file off khi muốn thử.

## Hash của ba file MTE_OFF

| Bản | SHA256 |
|---|---|
| Unlocked | `3cc3dd40d121c1e316295a7552b98c4a6f02a8510fd0d2d7a3f2ffe891152112` |
| Fakelock v2 | `9af2950d2df4d5e80905a501c0d1a823f22a97060dd287827d9c3ff7e58103a3` |
| Stock-policy | `b2535248b59fd65c8bb855d6bfa49b86ffbef31bcc7b647df0e8ca34a73df606` |

Hash ba file không ép tắt nằm trong manifest release và manifest của đúng bản gốc. Bản không off giữ nguyên byte; không xây lại bằng builder có patch off.

## Kiểm thử trên bytes cuối

```powershell
python experiments/lk_h3_eng_mte_off_v1/build_mte_off.py
python experiments/lk_h3_eng_mte_off_v1/validate_mte_off.py
python experiments/lk_h3_eng_mte_off_v1/package_six.py
```

Không dùng Python `-O`. Builder buộc đúng hash và lệnh input, chỉ cho đổi lệnh nhảy 4 byte và prefix, kiểm DER/member khác nguyên byte. Kiểm thử dùng đúng file đầu ra, **không sửa mã MTE trong RAM emulator**.

| Ca ARM64 | Unlocked off | Fakelock v2 off | Stock-policy off |
|---|---:|---:|---:|
| MTE: gốc và off, nhiều cờ và reserved memory có/không | 48 | 48 | 48 |
| HWC/countrycode, hai slot, lỗi đọc/formatter | 144 | 144 | 144 |
| Chính sách unlock/lock nội bộ | 342 | 335 | 12 |
| Handoff/seccfg/cảnh báo v2 | 0 | 82 | 0 |
| Báo trạng thái public fakelock | 0 | 38 | 0 |
| Callback khóa ENG/CN/GL/unknown | 0 | 0 | 4 |
| Tổng ca main | **534** | **647** | **208** |

Mỗi file off thêm 10 chữ ký DER giữ lại được xác thực offline và 3 ca parser BL2 chọn prefix cuối. Nghiên cứu nền có thêm 48 ca MTE LK và 168 ca chính sách preloader. Các mẫu EEA/TW là fixture. MMIO và các biên storage/secure-monitor/formatter/log là mô hình; không thực thi kernel, TEE hoặc toàn bộ boot.

File non-off unlocked/fakelock đã qua [kiểm thử riêng](../lk_h3_eng_auto_hwc_v1/README_VI.md); stock-policy non-off giữ nguyên [bản auto tham chiếu](../lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/README_VI.md). Xác nhận boot fixed-GLOBAL của nhánh stock trước đây không tự xác nhận bản auto hoặc off mới.

Checkpoint trước thêm MTE-off: `before-eng-three-variants-mte-off-20261105` tại `71caf4c`. Giữ nguyên tất cả input, bản từng boot và các file non-off. Không có thao tác flash, reboot, lock, ghi RPMB/efuse hoặc thay cấu hình máy trong đợt này.
