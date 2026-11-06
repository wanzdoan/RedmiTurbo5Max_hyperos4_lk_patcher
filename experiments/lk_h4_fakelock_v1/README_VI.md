# LK H4 fakelock v1 — HWC tự chọn vùng, MTE_OFF

Một file mới được dựng theo cơ chế **LK H3 ENG fakelock v2 mới nhất**, trên nền **H4 policy v3 MTE_OFF đã được người dùng xác nhận boot và chạy banking**. Ảnh mới đã qua kiểm offline, **chưa được xác nhận boot/root/banking hoặc Play Integrity trên máy thật**. Nhánh nghiên cứu Integrity được tạm dừng theo yêu cầu; đây không phải bản sửa Integrity đã chứng minh.

File: [lk_H4_FAKELOCK_V1_AUTO_HWC_MTE_OFF_TEST.img](lk_H4_FAKELOCK_V1_AUTO_HWC_MTE_OFF_TEST.img), **4.083.424 byte**.

SHA256: `5ca64119c1aa019350d81652d800c0c87d6fefa553e8389580c1ddf15eeb6aa6`.

[ZIP bàn giao](lk_h4_fakelock_v1_bundle.zip) chứa một ảnh cùng tài liệu, manifest, SHA256SUMS và evidence. Không có thêm ảnh normal hoặc control để tránh nhầm với bản này. MTE_OFF được kế thừa từ đúng nền đã chạy.

## Chốt nguồn đúng

| Vai trò | Nguồn giữ nguyên | SHA256 |
|---|---|---|
| Nền H4 đã boot | [Policy v3 MTE_OFF](../lk_h4_policy_bl2_control_v3/lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img) | `6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52` |
| Getter unlocked H4 | [H4 unlocked f57 đã boot](../../fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img) | `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171` |
| Tham chiếu cơ chế H3 mới nhất | [ENG fakelock v2 + auto HWC + MTE_OFF](../lk_h3_eng_mte_off_v1/fakelock_v2_auto_hwc/lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_MTE_OFF_TEST.img) | `9af2950d2df4d5e80905a501c0d1a823f22a97060dd287827d9c3ff7e58103a3` |

Tham chiếu H3 nằm trong nhánh fakelock v2 của ZIP sáu file, không phải stock-policy hoặc hybrid Fenrir đã bị người dùng báo không dùng được. H3 được dùng đối chiếu cơ chế và chạy các ca tham chiếu; **không chép mã H3 hoặc áp offset H3 lên main H4**. Getter 16 byte lấy trực tiếp từ bản H4 unlocked đã chạy.

Ảnh mới giữ nguyên toàn bộ bộ nạp BL2 có chữ ký **H3 thương mại** của v3/f57, AEE, DTB/DTBO H4, bố cục container và mọi DER có chữ ký. Phần main H4 được sửa và cập nhật prefix hash 180 byte tại chỗ. Đây là H4 release main có các chức năng fakelock, **không phải toàn bộ LK H4 ENG/debug**. Không thay bộ nạp về ENG từng dùng ở v1/v2 lỗi recovery.

## Cơ chế như H3 fakelock

| Kênh | Giá trị/hoạt động |
|---|---|
| Getter nội bộ và hai wrapper | Trả state3/success0 như H4 unlocked; không đọc token để quyết định khóa |
| Trạng thái lưu bền | Không ghi/chuyển khóa; mã setter và lưu trữ vẫn giữ từ nền v3 |
| Fastboot trả lời | `unlocked:no`, `secure:yes`, `lks = 1`, public `seccfg_state is 1` |
| Cache biến fastboot | Vẫn giữ giá trị thật; getvar đơn/all chỉ định dạng câu trả lời |
| Đường flash/erase main | Giữ các gate của v3; getter nội bộ unlocked; lỗi đọc quyền vẫn đi theo nhánh lỗi |
| Thông tin xuất sang Android | `vbmeta.device_state=locked`, `verifiedbootstate=green`, `flash.locked=1` |
| Bàn giao root-of-trust | Chọn hash khóa OEM đã tính sẵn; xuất lock1/boot-state0; metadata phiên bản/vá/digest không đổi |
| Trạng thái boot bên trong | Không ghi đè biến toàn cục hoặc local lock word bằng các giá trị xuất giả |
| Cảnh báo | Chỉ bỏ orange/unlocked; giữ warning/handler lỗi yellow và red |
| Vùng ROM/HWC | Kế thừa auto HWC và callback các khóa ENG/CN/GL của v3; chưa có ROM Global thực để kiểm boot ảnh này |
| MTE | Giữ nhánh LK xuất `arm64.nomte` từ v3; không sửa preloader hoặc tắt hồi tố hoạt động đã chạy ở preloader |

Khác stock-policy: dù máy đang lưu trạng thái locked, getter của ảnh mới vẫn cho các đường xử lý bên trong nhìn thấy unlocked. Các giá trị locked/green chỉ là giá trị xuất ra. Không mô tả đây là relock thật, không thay chứng thư/private key attestation và không tạo chứng nhận Google.

## Vị trí sửa H4

Offset tính từ đầu payload main; cộng `0x200` để ra offset trong file. [manifest.json](manifest.json) ghi đầy đủ bytes gốc/mới.

| Offset | Thay đổi |
|---|---|
| `0x89aec` | Getter unlocked 16 byte từ H4 f57 |
| `0xc8e0..0xc96b` | Nén hàm trả `lks`, đặt helper chỉ dùng khi trả getvar và literal flash.locked trong đúng vùng 140 byte của hàm cũ |
| `0xb0ec`, `0xb118` | Getvar đơn/all gọi helper định dạng, không sửa cache/getter cache |
| `0xbc7dc` | Chọn chuỗi locked trong cmdline AVB, giữ cờ nội bộ |
| `0x69574..0x695a7` | Xuất green và flash.locked1; giữ lỗi append |
| `0xc4db0`, `0xc4e10`, `0xc4e18` | Ba lựa chọn giá trị xuất root-of-trust tương ứng H3 v2 |
| `0xc9a8` | Chỉ định dạng public seccfg1; giữ word đọc được và nhánh lỗi |
| `0x69638` | Bỏ riêng warning orange |

Không thêm patch bỏ toàn bộ AVB/khóa lạ, không fake mức bản vá hoặc xóa lỗi mật mã. Giữ nguyên Secure-ID call-site skip vốn có ở v2/v3, HWC và các gate flash từ nền. Getter unlocked làm thay đổi chính sách boot theo đường unlocked gốc; không gọi ảnh ROM sửa hash là “xác minh thành công”.

## Kiểm chứng đúng ảnh cuối

[validation.json](validation.json): **402 ca main native**, gồm **398 ca H4 và 4 ca tham chiếu H3**, thêm 10 DER signatures và 3 ca parser BL2 thương mại. Cả ba input sai hash bị từ chối; build hai lần trùng byte. Builder giới hạn mọi diff vào các span lệnh khai báo và prefix; mọi member khác và DER giữ nguyên.

- Fastboot đơn/all, thứ tự cache đảo, danh sách rỗng, tên không liên quan hoặc gần giống; gọi lại getter cache thật để kiểm giá trị nội bộ không bị fake.
- Bootargs với các trạng thái boot và lỗi append; public AVB locked nhưng cờ unlocked nội bộ không thay. Lỗi append không bị biến thành thành công.
- 120 ca root handoff đối chiếu H4 unlocked gốc và bản fake: getter thật cùng các control ghi rõ, state/error khác nhau, SHA phần mềm native, so đúng thứ tự cả năm packet và kiểm local/global không bị sửa. H4 gốc không kiểm phản hồi secure-monitor trong sender này; giữ nguyên hành vi đó, không suy từ logging của H3.
- Truy vấn seccfg vẫn giữ lỗi đọc; cảnh báo yellow/red vẫn gọi handler lỗi. HWC có CN/Global/EEA/TW và trường hợp thiếu/đọc lỗi/lỗi formatter, MTE_OFF qua các bit/điều kiện bộ nhớ; khóa CN/GL/ENG thật và khóa bị sửa được phân biệt. Kiểm gate flash/erase dừng trước thực thi lệnh.
- Toàn hàm AVB slot và loader native trên ROM China H4 sạch: return0, chạy getter force3 thật, cmdline xuất locked; packet xuất lock1/state0. Các fixture về storage/khóa/hardware/rollback được nêu rõ, không phải boot thiết bị.
- Locator/chứng chỉ/hash của preloader H4 stock qua các boundary native với trusted-key/crypto fixtures. Bộ ba BL2 nguyên nền đã boot; kết quả này chưa chứng minh preloader stock chấp nhận ảnh mới trên máy.

[boot_policy_validation.json](boot_policy_validation.json): **24 ca caller native bổ sung**, tổng **426 ca main** cho cả hai report. Đường unlocked có thể gọi loader khi AVB trả lỗi xác minh; đường yêu cầu xác minh không cho lỗi đi tiếp. Getter force3 thật khiến caller bỏ bước cập nhật rollback dành cho locked. Mã caller này không bị patch.

Một ca sửa hash `init_boot` trong bộ nhớ mô phỏng vẫn trả **verification error3**, giữ nguyên lỗi; native loader vẫn chọn được buffer theo chính sách unlocked gốc. Điều này kiểm đường xử lý cần cho ảnh boot bị sửa, **chưa chứng minh init_boot root thực tế boot được**. Không chạy kernel, mount/FBE, toàn bộ bootconfig, KeyMint/TEE thực hoặc Google Integrity.

## Tái tạo, phiên bản và kết quả máy thật

```powershell
python experiments/lk_h4_fakelock_v1/build_fakelock_h4.py
python experiments/lk_h4_fakelock_v1/validate_fakelock_h4.py
python experiments/lk_h4_fakelock_v1/validate_boot_policy.py
python experiments/lk_h4_fakelock_v1/package_release.py
```

Không dùng Python `-O`. [runtime_confirmation.json](runtime_confirmation.json) tách báo cáo các nền đã chạy khỏi ảnh mới chưa chạy. Tính tương thích với Keystore/dữ liệu đang có, root, banking, Integrity và OTA của ảnh mới cần kết quả máy thật; không tự chuyển báo cáo thành công của v3 sang ảnh fake.

Checkpoint trước sửa đã push repo private: `before-h4-fakelock-v1-20261106` tại `adc8480`. Các file H3, H4 policy v3 và H4 unlocked giữ nguyên tên/bytes để lấy lại. Agent không flash, lock/unlock thiết bị, reboot, xóa dữ liệu hoặc truy cập RPMB/efuse.
