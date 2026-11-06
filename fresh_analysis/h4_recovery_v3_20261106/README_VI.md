# Sau khi user báo v2 vẫn recovery — đối chiếu AVB native trên ROM China thật

User xác nhận ROM `OS4.0.3.0.XPLCNXM` tại đường dẫn host đã cung cấp, không root; ENG H3 vào Android với cùng bộ ROM, H4 v2 vào recovery. Fastboot báo `unlocked:no`, quyền flash vẫn hoạt động. Không có readback/hash hoặc log máy. User yêu cầu chỉ tiếp tục offline vì máy không ở đây; không gọi thêm ADB/fastboot hoặc thao tác thiết bị.

**V2 không giải quyết được lỗi runtime đã báo. Không coi việc bỏ Secure-ID gate là fix thành công hoặc nguyên nhân đã được chứng minh.**

## Kiểm thử được mở rộng

[slot_replay.py](slot_replay.py) chạy toàn hàm `avb_slot_verify` native của main H4 v2 và ENG H3 với chính các ảnh boot/vbmeta của ROM China:

- H4 entry`0xbe740`, ENG`0xbe700`, giữ function pointer AVB operations gốc, callback tin khóa và RSA/descriptor parsing/comparison native.
- Host chỉ cung cấp đọc partition, SHA engine, heap, memcpy/memset. Danh sách được lấy từ pointer table firmware: boot/vendor_boot/init_boot/countrycode; bốn vbmeta thực sự được đưa qua mã kiểm signature/hash. DTBO không nằm trong danh sách caller này, không khẳng định payload DTBO được kiểm trong lượt này. Không mock chữ ký hoặc trả kết quả verify cố định.
- Kích thước partition lấy từ scatter ROM. Đuôi chưa có bytes trong file được zero-fill như một fixture, không khẳng định đuôi phân vùng máy thật là0.
- Trạng thái getter4, regionCN, rollback0, GUID tham chiếu, persistence không có giá trị là fixtures. PAC được thay bằng NOP trong RAM emulator; file firmware không đổi.
- Tiếp tục chạy loader native H4`0x68d1c`/ENG`0x69824` đến điểm chọn ba buffer boot/vendor_boot/init_boot. Dừng trước copy/handoff global, không chạy kernel.
- Chạy packet assembly root-of-trust với metadata phiên bản/hash do loader native lấy từ ROM thật. Khóa OEM lấy từ certificate như fixture, boot-state clean0 được cấp rõ; secure-monitor là sink mô phỏng, không phải trusted world thật.

## Kết quả có thể kết luận

[clean_rom_comparison.json](clean_rom_comparison.json) và input hashes ghi:

| Thành phần | H4 v2 | ENG H3 |
|---|---|---|
| Slot verification, locked4/flags0 | return0 | return0 |
| Khóa/chữ ký/hash ROM China sạch | Qua trong mô hình | Qua trong mô hình |
| Chọn boot/vendor_boot/init_boot | Cùng buffer/size/magic | Cùng buffer/size/magic |
| AVB cmdline sau loader | Giống nhau | Giống nhau |
| Năm packet root handoff với cùng fixtures | Giống nhau | Giống nhau |

Metadata chung: OS version170000, system patch202610, vendor/boot patch20261001. Không thấy khác biệt ở các trường này đủ giải thích recovery trong các đầu vào đã kiểm.

Năm đối chứng âm tính H4: sửa byte init_boot/countrycode→mismatch3; sửa signature vbmeta→verification3; lỗi đọc init_boot→IO2; rollback fixture64→rollback4. Các kết quả lỗi được giữ nguyên, không gọi chúng là verify thành công. Chỉ tổng kiểm harness PASS khi mọi ca cho đúng kết quả mong đợi.

`h4_slot_probe.json`/`eng_slot_probe.json` giữ lượt probe chẩn đoán riêng; báo cáo tổng có assertions là `clean_rom_comparison.json`. Các lần thử harness ban đầu thiếu padding hoặc backend phần cứng không được coi là lỗi LK trên máy.

## Một biến số còn khác bản H4 unlocked từng boot

Ảnh H4 unlocked `f57d19c9...` dùng signed BL2 **H3 thương mại**. H4 v1/v2 dùng signed BL2 **ENG H3**. Vì vậy so sánh cũ đang đổi cả nguồn trạng thái khóa và bộ nạp, không thể suy “chắc chỉ do locked”. Native AVB trong main không mô phỏng bước BL2/preloader bàn giao nên không giải quyết được biến số này.

Hướng thử có kiểm soát: giữ nguyên main/cert/AEE/DTB của v2, chỉ thay signed BL2 triple bằng đúng triple từ f57 đã boot; giữ normal/off riêng. Một control force-unlocked dùng BL2 ENG có thể tách thêm biến số khi có máy. Không bỏ thêm signature/hash hoặc giả green vì chưa có bằng chứng những bước đó chặn ROM sạch trong mô hình.

Chưa xác định nguyên nhân thực, chưa có firmware sửa được boot xác nhận. Các bước chưa được bao phủ: mode/BCB thực, provisioning và rollback thực, mọi thiết bị/driver, bootconfig/DTB/ramdisk handoff, kernel/FBE/dm-verity, trusted world, phản hồi recovery. Kết quả fixture không thay thế kiểm thiết bị. Chỉ kiểm metadata của system/vendor, chưa đọc/kiểm mọi block của super trên máy.

Tái tạo: `python fresh_analysis/h4_recovery_v3_20261106/compare_clean_rom.py`. Không dùng Python`-O`. Không patch ảnh nguồn, không flash hay ghi dữ liệu bảo mật. Checkpoint trước nghiên cứu:`before-h4-recovery-v3-investigation-20261106` tại`5d04b69`.
