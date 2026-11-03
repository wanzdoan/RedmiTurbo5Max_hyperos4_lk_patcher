# HyperOS 3 đã patch unlock + bản thử CN/Global riêng

File: [lk_hyperos3_patched_unlockBL_CN_GLOBAL_REGION_TEST.img](lk_hyperos3_patched_unlockBL_CN_GLOBAL_REGION_TEST.img), **4.082.928 byte**.

SHA256: `dbabb26989e9270a1b2ebd747820dca7f3630779bb5b5f54b21c30209a4b00b8`.

Đầu vào đúng theo yêu cầu: `lk_hyperos3_patched_unlockBL/dash.img`, SHA256 `e164fb22511d93584afab2183c275374d7ab892dd22e3a2132c4f3b1e245ac18`. Không lấy H3 original để build lại và không ghép thành phần H4. Giữ nguyên file đầu vào, patch unlock cũ và mọi thay đổi cũ ngoài các vùng ghi rõ dưới đây.

## Thay đổi thêm

| Offset trong file | Thay đổi |
|---|---|
| `0x68f84`, vùng lệnh4 byte | Bỏ nhánh dừng boot của máy CN khi mã vùng ROM khác CN/NATIVE |
| `0x88810`, vùng lệnh28 byte | Nếu khóa ảnh không khớp khóa CN, so thêm đúng khóa GL256 byte có sẵn trong H3; giữ xác thực chuỗi chứng chỉ và lỗi trích khóa |
| `0xc55dc`, vùng lệnh4 byte | Callback xác thực boot chấp nhận modulus512 byte của một trong hai khóa CN/GL có sẵn; khóa lạ vẫn không được tin |
| Prefix180 byte của main cert2 | Tính lại hash cho main LK mới, giữ nguyên DER có chữ ký và kích thước container |

Các địa chỉ được đối chiếu trực tiếp trên binary H3 patched; không dùng offset H4 để ghi vào H3. Getter unlock ở file`0x89a2c` vẫn nguyên16 byte. BL2, AEE, DTB/DTBO, các header, certificate và padding ngoài prefix giữ nguyên byte của `dash.img` đầu vào.

Chốt Secure ID đã được unlock cũ vượt qua nên không patch thêm. Không thêm lệnh ghi efuse; logic EP/MP, chống hạ phiên bản và các bước xác thực khác giữ nguyên.

## Kiểm chứng và giới hạn

- 532 ca mô phỏng lệnh ARM64: chạy code cũ/mới, SKU, khóa CN/GL/khóa lạ, lỗi chứng chỉ, lỗi trích khóa, lỗi đọc efuse, getter/wrapper unlock và Secure ID.
- 10 chữ ký DER giữ lại hợp lệ; 6 ca bộ đọc BL2 chạy trên cert của artifact mới. BL2 trong input H3 patched đã được đối chiếu byte với mã H3 dùng để mô phỏng.
- Build xác định, từ chối input khác hash và kiểm không có byte nào đổi ngoài ba vùng lệnh cùng prefix.

Mô phỏng giữ lệnh so sánh byte/chuỗi, memcpy và ABI thật; phần cứng, kết quả chain/key extraction và SKU được mô hình hóa; PAC/logging được bỏ qua. Xem [validation.json](validation.json), [manifest.json](manifest.json) và [address map](replay_address_map.json).

**Chưa flash hoặc xác nhận boot bản mới trên điện thoại. Chưa có ROM Global đích để kiểm tra tương thích. Đây không phải bản fix 5G đã chứng minh.** Bỏ chốt SKU cũng cho qua mã vùng trống/không nhận diện; không thay thế kiểm tra model, partition và firmware. Các bypass dconfig có sẵn giữ nguyên. Với phần cứng nonCN, nhánh mismatch khóa ảnh trở nên nghiêm hơn; bản này nhắm máy CN `dash` trong dự án.

Không sửa modem, preloader, Android, thuộc tính vùng hoặc setting mạng. Giữ bản H3 cũ để quay lại. Checkpoint trước sửa: `before-h3-patched-global-region-20261103` trong repo private.

## Tái tạo

```powershell
python experiments/lk_h3_global_region_v1/build_h3_region_lk.py
python experiments/lk_h3_global_region_v1/validate_h3_region_lk.py
```

Pseudocode đối chiếu: [SKU](../../ghidra_export/h3_patched/functions/ffff000090768a54_FUN_ffff000090768a54.c), [khóa ảnh](../../ghidra_export/h3_patched/functions/ffff000090788474_FUN_ffff000090788474.c), [khóa boot](../../ghidra_export/h3_patched/functions/ffff0000907c5304_FUN_ffff0000907c5304.c). Các kết luận được kiểm lại bằng bytes và thực thi mô phỏng của input đúng hash; không mặc định báo cáo cũ đúng.
