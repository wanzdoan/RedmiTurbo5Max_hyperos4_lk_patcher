# Integrity khác nhau giữa ROM H3 và H4 khi giữ cùng LK

Ngày 2026-11-06, người dùng xác nhận **cùng file Policy H4 v3 MTE_OFF**, SHA256 `6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52`, cài ROM H3 thì đủ ba mức Integrity; cài ROM H4 thì không có mức nào. Trên H4 banking vẫn hoạt động đầy đủ theo báo cáo. H4 là China OS4.0.3.0.XPLCNXM đúng thư mục ROM đã dùng nghiên cứu, không root; kiểm trong Play Store. Chưa có bản chụp/nguyên văn verdict, phiên bản Google services hoặc thông tin thiết bị đọc qua ADB. Chưa chốt màn hình được dùng là Play Integrity trong tùy chọn nhà phát triển hay chứng nhận Play Protect.

**Đối chứng chính là cùng LK trên hai ROM**, không phải H3 LK so với H4 LK. Điều này giảm khả năng lỗi do patch LK cố định, nhưng không loại trừ tương tác của LK với metadata/secure firmware từng ROM. Chưa có kết quả chứng minh sửa Integrity; giữ nguyên ảnh đã boot và chưa tạo firmware mới.

## Kiểm offline mới

[audit_handoff.py](audit_handoff.py) kiểm đúng hash ảnh đã boot, so toàn bộ sender root-of-trust payload `0xc4ce4..0xc4edb` với LK H4 stock và chạy native packet assembly/software SHA. Sender nguyên byte stock. Bốn ca kết hợp metadata phiên bản `160000`/`170000` với fixture trạng thái lưu bền `3`/`4` đều chuyển tiếp đúng phiên bản, tháng vá, digest, trạng thái khóa và trạng thái boot. Không thấy chặn/cắt phiên bản trong phạm vi sender này. [handoff_audit.json](handoff_audit.json) giữ kết quả và hashes.

Hai phiên bản metadata là **fixture tổng hợp**, không phải hai ROM H3/H4 đã chạy trong emulator. OEM key cache, getter lưu bền, boot-state và phản hồi secure-monitor đều được mô hình hóa. Chưa kiểm đường cấp chứng thư hoặc phần xử lý thực trong môi trường bảo mật. Kiểm trước đó về AVB/loader cũng có fixture khóa/boot-state: PASS của các probe đó không chứng minh Google chấp nhận attestation.

Header v4 của boot/init_boot ROM H4 có trường phiên bản bằng 0. Không coi đây là nguyên nhân: [AOSP boot header](https://source.android.com/docs/core/architecture/bootloader/boot-image-header) nêu phiên bản header 4 dùng thông tin phiên bản hệ điều hành từ AVB thay cho trường này. Probe AVB/loader trước đã lấy được metadata 170000/202610, vendor và boot 20261001 từ đúng ROM.

## Những gì cần phân biệt để sửa đúng

Theo [Google Integrity verdicts](https://developer.android.com/google/play/integrity/verdicts), kết quả thiết bị rỗng có thể phản ánh dấu hiệu can thiệp hoặc vấn đề hệ thống. Basic trên Android 13+ phụ thuộc root chứng thực do Google cấp; Strong còn yêu cầu Device và bản vá đủ mới. Nhãn tùy chọn cũng phụ thuộc điều kiện cấp phép của ứng dụng yêu cầu. Vì thế không suy một dấu tick mất thành một nhánh LK sai.

[AOSP attestation](https://source.android.com/docs/security/features/keystore/attestation) phân biệt verifiedBootKey, deviceLocked, verifiedBootState và verifiedBootHash. Những giá trị trong chứng thư phần cứng thật có giá trị chẩn đoán khác các thuộc tính Android hoặc `fastboot unlocked:no`. Banking hoạt động không tự chứng minh ba nhãn Play Integrity.

| Bằng chứng cần đọc | Câu hỏi được giải quyết | Hướng xử lý khi xác định lỗi |
|---|---|---|
| Nguyên văn verdict/lỗi trong Play Store; màn hình sử dụng | Kết quả Integrity rỗng, lỗi API hay chứng nhận Play Protect? | Theo đúng lỗi và lớp kiểm tra, không đổi LK khi chưa biết |
| Build fingerprint, SDK, bản vá, phiên bản Play Store/GMS | Khác biệt môi trường H4 có nhất quán không? | Sửa thành phần/cấu hình bị lỗi nếu được chứng minh |
| Chứng thư attestation thật hoặc lỗi KeyMint/cấp chứng thư từ xa | Có chuỗi Google hợp lệ và dịch vụ tạo chứng thực hoạt động không? | Phân biệt provisioning, dịch vụ ROM và dữ liệu boot |
| Giá trị chứng thư đối chiếu bootconfig và hash ROM | LK có truyền sai giá trị vào phần bảo mật không? | Chỉ patch LK khi tái hiện được phép truyền sai |

Hiện ADB không thấy thiết bị. [collect_readonly.py](collect_readonly.py) chỉ có lệnh đọc allowlist: thuộc tính build/boot, metadata phiên bản các gói Google và tên dịch vụ bảo mật. Không dùng su, cài app, sửa thuộc tính, xóa dữ liệu, reboot hoặc flash. Báo cáo buộc lưu ngoài repository; script chưa thu được dữ liệu máy. Script không tạo key, không lấy token Integrity và chưa đọc chứng thư thật; các dữ liệu này vẫn còn thiếu.

```powershell
python fresh_analysis/h4_integrity_20261106/audit_handoff.py
python fresh_analysis/h4_integrity_20261106/collect_readonly.py --output "$env:TEMP\dash-h4-integrity-readonly-20261106.json"
```

Chạy collector khi có đúng một thiết bị ADB đã cho phép. Không yêu cầu flash lại ROM H3 để làm đối chứng. Checkpoint trước nghiên cứu: `before-h4-integrity-investigation-20261106`, commit `4f1bcd5`, đã push repo private. Ảnh LK, bộ nguồn và ZIP H3 giữ nguyên.
