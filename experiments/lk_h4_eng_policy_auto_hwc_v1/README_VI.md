# H4 stock-policy + HWC tự chọn vùng — bản thử dùng BL2 ENG H3

Ngày2026-11-06. Chuyển các chức năng đã xác định từ nhánh **`stock_policy_auto_hwc`** trong ZIP sáu file H3 sang main H4. Đây là **bản port chức năng trên main H4 thương mại, ghép bộ nạp ENG H3**, không phải bản ENG H4 chính thức hoặc chuyển toàn bộ firmware thành ENG. **Chưa flash/boot/lock hoặc kiểm với preloader H4 trên máy.**

## Hai file riêng

| MTE | File | SHA256 |
|---|---|---|
| Chính sách H4 gốc | [H4 stock-policy auto HWC](lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_TEST.img) | `da05dc16e6a2e1d48a7f824a9382cf9a4dc4ce3ad1fe58387ca34519a65cbf3c` |
| Ép nhánh kernel MTE-off | [H4 stock-policy auto HWC MTE_OFF](lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_MTE_OFF_TEST.img) | `4206c420124ec5d2e95a7e6fe22fe81b05c405abd7940a74c7e6501d8b7dc893` |

Mỗi file **4100080byte**. [ZIP hai file và evidence](lk_h4_stock_policy_auto_hwc_two_files.zip). Không dùng prototype China-only trong thư mục `lk_h4_eng_key_stock_policy_v1` cho mục tiêu all-region này.

## Đúng nguồn người dùng chỉ định

Người dùng làm rõ bản đang tốt nhất là **thư mục `stock_policy_auto_hwc` trong `lk_eng_all_region_6_files.zip`**, thay vì chắc chắn bản v1 ở gốc. ZIP hiện có SHA256`66e55fd39ae9840c4abeea512fee0f6e68eb925914ce7c4829c5f8f3f2c5c9a6`, CRC đạt. Hai ảnh trong nhánh:

- MTE gốc: `fe60552c60d3495ad479e34e2610ab37acca064c367f8fce750989f13f4e12a0`.
- MTE_OFF: `b2535248b59fd65c8bb855d6bfa49b86ffbef31bcc7b647df0e8ca34a73df606`.

User báo nhánh này cài các ROM đã dùng, khóa được và dùng banking/tính năng tốt. Chưa đọc lại hash ảnh đang chạy hoặc xác định riêng normal/off; không biến báo cáo này thành bằng chứng mọi ROM hoặc Play Integrity của hai ảnh H4 mới. [runtime_confirmation.json](runtime_confirmation.json) phân biệt rõ hai phạm vi. Lỗi recovery Global được ghi trước đây thuộc lịch sử, không xóa hoặc gán nó cho lần báo thành công mới.

Nguồn build H4 là `lk_hyperos4_original/lk.img`, SHA256`82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b`; nguồn bộ nạp ENG là `experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img`, SHA256`6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d`.

## Thành phần và chính sách

| Phần | Nguồn/hành vi |
|---|---|
| Main LK | H4, chỉ các span trong manifest được sửa |
| BL2 và hai certificate | Nguyên byte bộ có chữ ký của ENG H3 |
| AEE, DTB, DTBO và certificate | H4 nguyên byte |
| Trạng thái khóa, setter và AVB flags | H4 gốc, đọc trạng thái lưu bền; không force unlocked/fakelock/green |
| Root-of-trust handoff | H4 nguyên byte; không sửa khóa chứng thực hoặc giả verdict |
| HWC | CN khi countrycode chính xác `cn` không phân biệt hoa thường; chuỗi không rỗng khác → GLOBAL; thiếu/rỗng/lỗi đọc → HWC phần cứng |

**Stock-policy không tự khóa máy.** Bản H4 không ghép getter force-unlock của ảnh H4 từng boot. Các ca getter/setter native có journal ảo cho thấy trạng thái token hợp lệ→3, token sai/sentinel→4 vẫn theo H4 và trùng các fixture ENG đã đối chiếu. Không kiểm token hay ghi RPMB máy thật. Getter/setter gốc vẫn có thể ghi/khởi tạo dữ liệu trên những đường gốc; không coi chạy lệnh khóa/getter trên máy là phép thử chỉ đọc.

## Những phần đã port

- Callback AVB tại payload`0xc5978..0xc5b17` nhận đúng ba modulus ENG2048, China4096 và Global4096 đã biết. Tính SHA256 bằng routine H4 native`0xa4810`, so đủ32byte; kiểm pointer/size, từ chối khóa lạ/khóa sửa bit/lỗi hash. Bên trong cùng span có block HWC riêng tại`0xc5aa0`.
- Bỏ chốt SKU CN/NATIVE tại`0x6904c`; fallback khóa ảnh tại`0x888d0` gọi helper mới`0x13b70..0x13bc3`, thử đúng CN/GL256byte. Giữ kiểm chain, lỗi trích khóa, EP/MP và logic efuse gốc. Không thêm ghi efuse.
- Bỏ riêng nhánh recovery do CN-hardware/non-CN-software tại`0x87368`; giữ các kiểm chữ ký/binding Secure ID khác. Bỏ riêng chốt vùng `oem flash_check` tại`0x68308`, giữ gate RPMB chưa provision trước đó.
- Main fastboot: mở hai gate dispatcher tại`0xa09c/0xa0b0`, gate flash`0xce68`, erase`0xd250`. Lỗi query lock ở entry flash/erase vẫn bị từ chối; mã H4 thương mại khác vẫn còn. **AEE giữ H4 và các gate release của AEE chưa port**, vì vậy chưa gọi đây là tương đương toàn bộ ENG H3.
- HWC: giữ copy hardware92byte sớm tại`0x13b5c`; chỉ dời xuất bootarg đến sau lần đọc countrycode có sẵn, không thêm đọc partition. Giữ cache phần cứng và con trỏ version cho consumer ở giữa luồng. Lỗi/short read xóa dữ liệu đọc dở; lỗi formatter vẫn có diagnostic.
- MTE_OFF chỉ thêm lệnh`0x34740:20020054→11000014`, đi nhánh H4 có sẵn xuất`arm64.nomte`. Không tắt hồi tố hoạt động preloader hoặc secure-world. Bản normal giữ chính sách MTE H4 gốc.

Phần main cert2 có prefix hash180byte, DER có chữ ký giữ nguyên. Không có private key và không ký OEM lại main đã sửa. Bộ đọc BL2 ENG chọn đúng prefix trong các ca native; kiểm parse không thay thế xác thực/nạp/boot đầy đủ SoC.

## Kiểm thử và tái tạo

```powershell
python experiments/lk_h4_eng_policy_auto_hwc_v1/build_port.py
python experiments/lk_h4_eng_policy_auto_hwc_v1/validate_port.py
python experiments/lk_h4_eng_policy_auto_hwc_v1/validate_behavior.py
python experiments/lk_h4_eng_policy_auto_hwc_v1/package_release.py
```

Không dùng Python`-O`. Mỗi ảnh có **390 trường hợp main có đối chứng/fixture**, **3 ca parser BL2 ENG**, **10 chữ ký DER**:

| Nhóm | Số trường hợp |
|---|---:|
| Khóa, corruption, size, null, lỗi SHA | 57 |
| Predicate H4 trước/sau | 24 |
| HWC, slot A/B, CN/Global, đọc/format lỗi | 62 |
| SKU và khóa ảnh/lỗi chain/trích khóa | 38 |
| Dispatcher/flash/erase/region main | 168 |
| Getter/setter và so với ENG | 17 |
| MTE | 24 |

[default_validation.json](default_validation.json), [default_behavior.json](default_behavior.json) và hai báo cáo `mte_off_*` gắn đúng hash ảnh cuối. Rebuild trùng byte; input sai bị từ chối; byte ngoài span/các member giữ nguyên; kiểm branch không trỏ từ ngoài vào giữa vùng callback/helper gốc được tái sử dụng. PAC, token-verification result, phần cứng/RPMB/storage, formatter và callback sink được mô hình hóa. SHA256/memcmp/memcpy/control flow/native parser chạy thật; không chạy ghi partition.

## Preloader H4: chưa kết luận flash dùng được

H4 main/AEE/DTB tránh bê nguyên phần khởi tạo H3 sang, nhưng **BL2 ENG H3 vẫn là biến số chưa boot test cùng tổ hợp này**. Cùng root SPKI và chuỗi chữ ký hợp lệ trong [đối chiếu họ LK](../../fresh_analysis/eng_family_compatibility_20261105/README_VI.md) là bằng chứng hỗ trợ xác thực offline, không chứng minh ABI/handoff, rollback và phần cứng thật.

Bản H4`f57d19c9...` người dùng từng xác nhận dùng preloader H4 lại dùng **BL2 H3 release**, không phải BL2 ENG của hai ảnh này. Không chuyển xác nhận đó sang đây. Chưa có bản mới được xác nhận cho phép giữ preloader H4 khi flash ROM. Không flash/lock/reboot/erase hay ghi dữ liệu bảo mật trong đợt này.

Không đảm bảo mọi ROM, banking, root, OTA hoặc Play Integrity. Đặc biệt stock-policy vẫn có thể từ chối `init_boot` đã root khi locked. Việc OTA thay LK này bằng LK gốc cũng không được giải quyết bằng một patch trong file sắp bị ghi đè.

Checkpoint trước đợt: `before-h4-eng-compatible-stock-policy-20261106` tại`a82fd1a`; checkpoint prototype trước mở CN/Global: `before-h4-eng-cn-global-policy-port-20261106` tại`0f1b4ed`. Bản cũ và ZIP sáu file nguyên byte.
