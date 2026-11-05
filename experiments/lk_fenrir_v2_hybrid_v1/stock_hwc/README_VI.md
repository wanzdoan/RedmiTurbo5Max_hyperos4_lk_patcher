# HWC tự chọn vùng cho hai nền hybrid

Phần này cung cấp patch code riêng cho đúng hai layout, không tạo hoặc flash firmware. Parent builder chịu trách nhiệm input/output hash, prefix certificate và bảo toàn các member khác.

API `hwc_patch.py`:

- `patches(image, kind)` → danh sách `(main_payload_offset, bytes, purpose)`.
- `assemble(kind)` → code136 byte, không relocation chưa giải quyết.
- `check_external_branches(image, kind)` → kiểm không có nhánh trực tiếp ngoài block đi vào interior bị tái sử dụng.
- `CONFIG` dùng hai tên `stock`, `debug`.

API `validate_hwc.py`: `flow_case(kind, final_image, country, ...)` hoặc `cases_for(kind, final_image)` chạy147 ca native trên đúng bytes final. Không sửa code trong RAM khi kiểm một final image.

## Giữ bản sao phần cứng sớm

Không bê nguyên cách dời getter của bản debug auto-HWC cũ sang stock H3. Stock H3 có các phép đọc HW level ở `0x13b38` và `0x13ba0` **trước** lần đọc countrycode. Patch mới vẫn chạy getter native ở thời điểm đầu như stock, giữ snapshot trên stack; chỉ dời bước chọn/định dạng HWC tới sau countrycode.

Debug cũng dùng nguyên tắc giữ early copy, dù vùng intervening của layout debug đã xét không có cùng các phép đọc level như stock. Không sửa các artifact debug auto-HWC cũ; đây là code riêng cho hybrid mới.

| Thành phần | Stock H3 | Debug |
|---|---|---|
| Function bootargs gốc | `0x13930` | layout debug cũ |
| Block thay thế136 byte | `0x13a78..0x13aff` | `0x13364..0x133eb` |
| Getter native/cache | `0x34f8` / `0x13d018` | `0x3530` / `0x135018` |
| Stack HW record/output | `sp+0xb0` / `sp+0x110` | `sp+0x120` / `sp+0x180` |
| Điểm tiếp tục early | `0x13b00` | `0x133ec` |
| Điểm vào formatter late | `0x13a88` | `0x13378` |
| Zero country buffer gốc | `0x13e18` | `0x13728` |
| Block lỗi đọc sửa12 byte | `0x14050` | `0x13960` |
| Common continuation sửa4 byte | `0x140b0` | `0x139c0` |
| Sau formatter late | `0x140b4` | `0x139c4` |

Stock block136 byte bao gồm đoạn định dạng HWC cũ và một diagnostic HW log. Bỏ diagnostic đó để có chỗ, nhưng giữ `str wzr,[x29,#0x1c]` trước lời gọi trạng thái khóa ngay sau block. Debug block kéo dài thêm8 byte vào hai lệnh chuẩn bị memset; early path tái tạo đúng `x0` và `w1`, rồi quay về `mov w2,#0xc8` gốc. Cả hai không thêm frame hoặc lần đọc partition.

Literal `GLOBAL\0\0` nằm bên trong block thay thế sau nhánh rời block. Không ghi lên callback tin khóa ký hoặc dùng offset của nền còn lại. Nhánh lỗi formatter vẫn log suffix `mi_hwid: snprintf error.`, không append chuỗi lỗi và khôi phục `x19` trỏ HWversion cùng `x0` output cho caller.

## Mapping

Đúng chuỗi `cn` không phân biệt hoa thường → `CN`; mọi countrycode khác không rỗng → `GLOBAL`; thiếu/rỗng/lỗi/đọc thiếu → HWC phần cứng trong snapshot đầu. Giữ slot, close path, HWlevel/HWversion và một lần xuất HWC. EEA/TW dùng `GLOBAL`, không invent HWC EEA/TW. Không coi đây là mở khóa ký mọi ROM hoặc chứng minh5G/boot.

## Kiểm thử

```powershell
python experiments/lk_fenrir_v2_hybrid_v1/stock_hwc/hwc_patch.py
python experiments/lk_fenrir_v2_hybrid_v1/stock_hwc/validate_hwc.py
```

Code hash:

- Stock136 byte: `457316ad59069bd11009994ab97f6dec1def480b2f6b858804834cfb456921f5`.
- Debug136 byte: `b94fea87d9428b7f6d1678bfde2ca310abc33ff3fff1f518777843ac4f19ec12`.

[validation.json](validation.json): mỗi nền147 ca original +147 ca patched, tổng588 ca ARM64 giới hạn. Bao gồm hai slot, case/chuỗi CN, Global/EEA/TW, hardware fallback, missing/empty/short/error, lỗi name/country/HWC formatter. Getter và memcpy thật chạy **một lần ở early**, strlen/memset thật chạy; partition/slot/formatter/log/append và PAC được mô hình hóa.

Ba fixture bổ sung đổi cache giữa early và late trong emulator RAM, chứng minh HWC late vẫn lấy bản snapshot phần cứng gốc, không đọc lại hoặc làm mất dữ liệu trước các consumer ở giữa. Kiểm vùng canh stack, cache nguyên vẹn trong ca không chủ động đổi cache, con trỏ version/output và các thanh ghi callee-saved liên quan.

Đây là hai đoạn giới hạn của function; không chạy toàn bộ bootargs, Android hoặc thiết bị. Candidate dùng trong test chỉ là bytes ghép trong bộ nhớ host, chưa có prefix hash mới và không được ghi ra `.img`. Parent cần chạy lại `cases_for()` trên final image đã build và kiểm prefix/DER/BL2 riêng.
