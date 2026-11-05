# Bản H4 hybrid đã boot, tạo riêng biến thể MTE-off

Theo yêu cầu quay lại bản H4 đã được người dùng xác nhận chạy. **Không lấy Fenrir hoặc các hybrid H3/debug mới làm nền.**

Input: [lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img](../../fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img), SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`. Đây là main H4 patch unlock, BL2/certificates H3 original, AEE/DTB H4; không phải thuần H4 hoặc build ENG hoàn chỉnh.

File mới: [lk_hyperos4_BL33_H3BL2_MTE_OFF_TEST.img](lk_hyperos4_BL33_H3BL2_MTE_OFF_TEST.img), **4.083.424 byte**.

SHA256: `7873361e63ade5e6840d985c23c44bbb01fe449e7f89fd3ebbdba0d3d244e2a4`.

**Bản nền đã có báo cáo boot thành công, bản MTE-off mới chưa boot test.** Xem [báo cáo nguồn](../../fresh_analysis/output/runtime_confirmation.json) và [trạng thái bản mới](runtime_confirmation.json). Không flash/reboot/lock thiết bị trong đợt này.

## Thay đổi duy nhất

Định vị lại bằng mã H4 và registry init, không dùng offset của H3/debug:

- Hook `mte_info`: main payload `0x346c8`, lưu cờ vào global `0x16dac0`.
- `kernel_mte_init`: main payload `0x346f4`, lookup reserved memory `security_booker_tag`.
- Patch tại payload **`0x34740`**, whole-file **`0x34940`**: `20020054` (`b.eq 0x34784`) → `11000014` (`b 0x34784`).
- Nhánh off gốc cấu hình booker và append **`arm64.nomte`** qua hàm H4 `0x19078`.
- Cập nhật prefix main cert2 **180 byte**, giữ DER đã ký và padding nguyên byte.

Builder kiểm mọi byte ngoài instruction4 byte và prefix đều giống input. Patch unlock16 byte giữ nguyên tại whole-file `0x89cec`; header/layout, BL2 H3, AEE/DTB/certificates H4 không đổi. Không thêm HWC/chốt vùng/fakelock hoặc thay chính sách boot nào khác.

Phạm vi MTE-off là yêu cầu kernel tắt hỗ trợ MTE theo đường có sẵn của LK. Không tắt ngược hoạt động preloader đã chạy và không chứng minh trạng thái secure-world đã thay đổi. Chưa đọc kernel command line/HWCAP trên máy dùng ảnh mới.

## Tái tạo và kiểm thử

```powershell
python experiments/lk_h4_booted_hybrid_mte_off_v1/build_mte_off.py
python experiments/lk_h4_booted_hybrid_mte_off_v1/validate_mte_off.py
```

Không dùng Python `-O`. [manifest.json](manifest.json) ghi hashes/input/offset; [validation.json](validation.json) ghi:

- 48 ca MTE native: bản nền/bản off, cờ0..7 cùng405/407/f07/ffffffff, reserved memory có/không. Kiểm output bootargs, global cờ, RMW booker và ABI; không sửa code trong RAM emulator.
- 328 ca getter/wrapper/AVB predicate và negative controls, giữ hành vi unlock của H4 đã kiểm trước đây.
- 3 ca parser của chính BL2 H3 giữ lại, chọn được hash main cuối từ prefix.
- 10 chữ ký DER gốc xác thực offline; signed header hashes vẫn khớp, payload các member không sửa vẫn khớp.

Tổng379 ca native có giới hạn. MMIO/lookup/log/append/formatter/boot selector/PAC được mô hình hóa; không chạy SoC/kernel/TEE hoặc toàn bộ boot. Trạng thái getter chỉ được mock trong negative controls có nhãn; các ca còn lại chạy patch unlock thật trong ảnh.

Checkpoint trước sửa: `before-h4-booted-hybrid-mte-off-20261105` tại `c84149f`. Input đã boot giữ nguyên bytes, tên và hash; mọi artifact trước đó cũng giữ lại.
