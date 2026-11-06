# H4 ENG policy v2 — bỏ riêng gate Secure ID trước boot như ENG H3

**User đã báo v2 vẫn vào recovery; đây không phải bản fix runtime thành công.** Cùng ROM China H4 sạch, không root, đổi ENG H3 thì vào Android; fastboot của v2 báo `unlocked:no`, vẫn cho flash. Chưa có log/readback và user yêu cầu tiếp tục offline. [Đối chiếu AVB mở rộng sau thất bại](../../fresh_analysis/h4_recovery_v3_20261106/README_VI.md) cho thấy hai main đều xác minh/nạp ảnh ROM sạch trong các fixtures đã kiểm. Mô phỏng Secure-ID trước đây chỉ tái hiện một đường có thể gây recovery, không chứng minh đó là nguyên nhân thật.

## File mới, giữ nguyên v1

| Biến thể | File | SHA256 |
|---|---|---|
| MTE_OFF — tương ứng nhánh user vừa thử | [H4 v2 MTE_OFF](lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_MTE_OFF_TEST.img) | `48912f04727fede359a77dbe57dcb636cbb70f52121f7052ab00af46fbc22c53` |
| Chính sách MTE gốc | [H4 v2 normal](lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_TEST.img) | `f93f513ef36accfd0056dd130d6bfc592ac1944820900785c059a46eed010884` |

Mỗi ảnh4100080byte. [ZIP hai file](lk_h4_eng_policy_recovery_fix_v2_two_files.zip). Đây vẫn là main H4 release đã port chức năng + signed BL2 ENG H3; chưa phải full ENG H4 chính thức.

## Phát hiện có thể kiểm lại

H4 caller `boot_linux_from_storage` tại payload`0x14714`:

1. Nạp/xác minh boot bằng AVB.
2. Nếu không ở recovery mode2, gọi `Is_Secure_ID` tại`0x86b8c`.
3. Checker lỗi thì gọi `reboot_recovery` tại`0x87630`.

Checker nguyên bản bỏ qua khi getter báo unlocked3 hoặc SBC không bật. Khi locked4 + SBC bật, nó đọc bản Secure ID chính, có đường fallback, rồi kiểm các binding phần cứng/định danh. Lỗi đọc/format/binding có thể đưa ra recovery. Không cần có lỗi vùng ROM hoặc chữ đỏ để nhánh code này tồn tại; chưa có log để khẳng định người dùng đã đi chính nhánh này.

Trong ENG H3, caller cùng chức năng tại`0x13fb4` đi từ profile-log`0x1403c` thẳng sang bootargs`0x14040`, không gọi checker tại vị trí tương ứng. Điều đó giải thích một **khác biệt được chứng minh trong mã**, phù hợp việc ENG locked có thể boot khi H4 release locked vướng gate này. Không có nghĩa mọi ca recovery đều do Secure ID.

Đợt trước chỉ bỏ chốt so vùng bên trong đường Secure ID. **Tôi đã để sót gate tổng tại boot caller**, gồm cả lỗi đọc và binding không thuộc vùng. V2 sửa đúng phần này để gần hành vi ENG H3 hơn; không giải quyết bằng ép getter unlocked hoặc bỏ mọi lỗi AVB.

## Một instruction thay đổi

Payload offset`0x147ac`, file offset`0x149ac`:

```text
40010054   b.eq 0x147d4
0a000014   b    0x147d4
```

Nhánh cũ chỉ bỏ qua gate khi recovery mode2. Nhánh mới đi thẳng đến continuation như ENG, không chạy lời gọi Secure ID ở boot caller. Phần main cert2 tính lại hash prefix180byte. **Mọi byte khác giữ nguyên nguồn v1 tương ứng**, kể cả DER, BL2/certificates, AEE, DTB/DTBO, MTE-off, HWC, ba khóa ROM và các gate fastboot đã port.

Đây là **bỏ một bước ràng buộc phần cứng/provisioning của hãng tại call site**, không sửa chữa Secure ID lưu trong máy. Hàm kiểm Secure ID vẫn giữ nguyên so với v1 cho caller khác. Getter/setter, trạng thái khóa lưu bền, AVB flags/hash/signature và root-of-trust handoff không đổi. Không force locked/unlocked/green; không thêm lệnh ghi RPMB/efuse hoặc dữ liệu máy.

## Kiểm chứng

- Mỗi ảnh: **96cặp tình huống caller trước/sau** (192lần chạy), tổ hợp mode/state/SBC/lỗi query/primary/fallback/binding. V1 vào recovery ở đúng trường hợp gate lỗi; v2 đi continuation và không gọi checker. Recovery routine dừng ngay ở entry, trước mọi ghi misc/reboot.
- **8cặp gọi thẳng checker mỗi ảnh** xác nhận checker cũ vẫn cho cùng lỗi/kết quả; không bị sửa thành luôn thành công. Thêm8control trên caller ENG H3 không có gate.
- Toàn bộ kiểm hồi quy v1 chạy trên **bytes v2 cuối**, report chỉ ghi trong thư mục v2:390trường hợp main có fixture/đối chứng,3parser BL2,10DER mỗi ảnh. HWC/khóa/MTE/policy/root-state kiểm tương ứng vẫn đạt.
- Hai ảnh mới kiểm lại locator/coordinator cert2/PSS và hash BL2 bằng preloader H4 stock SHA104bb... theo [phạm vi audit](../../fresh_analysis/h4_preloader_lk_acceptance_20261106/README_VI.md). BL2 triple nguyên byte bằng v1. Đây vẫn không phải full loader hoặc hardware acceptance.

[recovery_gate_validation.json](recovery_gate_validation.json), [default_validation.json](default_validation.json), [default_behavior.json](default_behavior.json), hai report `mte_off_*` và [preloader_validation.json](preloader_validation.json) ghi hashes/phạm vi/mocks. Token, trạng thái bảo mật, đọc lưu trữ/binding/UFS và formatter là fixtures; không dùng dữ liệu Secure ID thật của người dùng. Mã caller/checker/predicate chạy native. Rebuild trùng byte, sai input bị từ chối, byte ngoài instruction/prefix được kiểm nguyên vẹn.

## Trạng thái máy và giới hạn

User chỉ rõ đã thử v1 MTE_OFF trên cả China và Global, với preloader ENG từ `Downloads/preloader_dash (1).bin`. File host1091004byte, SHA256`75e93936bd14914d25380767f33e55f40aefc77db5cbfaa606af4e6641591c35`, khớp file ENG đã đối chiếu trong hồ sơ trước. Chưa đọc lại phân vùng máy để xác minh image đang thực thi; không đồng nhất file này với stock H4`104bb...` hoặc ENG cũ`bcde...`.

V1 chưa có log lỗi. V2 chưa flash/boot/lock/banking/Integrity/stock-preloader test trên máy. Nếu lỗi do AVB hash ảnh root, rollback, Android recovery hoặc bước khác, patch này không tự giải quyết. **Không ép AVB bỏ lỗi hoặc giả green để gọi là fix.** Không có quy trình relock/OTA thực an toàn mới được chứng minh. AEE release gates H4 vẫn như v1.

## Tái tạo và quay lại

```powershell
python experiments/lk_h4_eng_policy_recovery_fix_v2/build_fix.py
python experiments/lk_h4_eng_policy_recovery_fix_v2/validate_fix.py
python experiments/lk_h4_eng_policy_recovery_fix_v2/validate_regression.py
python experiments/lk_h4_eng_policy_recovery_fix_v2/package_release.py
```

Không dùng Python`-O`. Nguồn normal v1 SHA`da05dc16...`, off v1`4206c420...`; builder buộc đúng hash. Các file v1 không bị ghi đè. Checkpoint trước sửa đã push:`before-h4-locked-recovery-fix-20261106` tại`23d6872` trong repo private.
