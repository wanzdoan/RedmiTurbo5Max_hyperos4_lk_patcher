# 10 — Khóa thật và OTA stock China: kết quả tiền kiểm

Dừng theo yêu cầu người dùng ngày2026-11-05; giữ lại bằng chứng chỉ đọc, không tiếp tục thử lock/flash.

2026-11-04. Mục tiêu người dùng chọn là **China chính hãng trước**, sau đó mới đánh giá vùng khác. Người dùng chỉ cho thu thập thông tin qua ADB/su, không flash. **Chưa có bằng chứng đủ để khẳng định một quy trình lock thật an toàn/OTA thành công.**

Báo cáo và script đầy đủ: [relock_ota_research](../fresh_analysis/relock_ota_research/README_VI.md). Các kết quả quan trọng:

1. Máy đang chạy H4 China `OS4.0.3.0.XPLCNXM`, slot A. Hai LK đọc lại đều là token-key clone`69089ebb...`, không phải ENG/fakelock. Bootconfig và log LK ghi unlocked/orange, state3; properties Android báo locked/green do bị che ở môi trường root. Không lấy properties đó làm bằng chứng khóa thật.
2. `init_boot_a` **không khớp hash được metadata China ký**;5 hash descriptor boot khác đã đọc thì khớp.4 metadata signature hợp lệ, root key là China chuẩn và flags0. File init_boot H4 gốc trên host và toàn phân vùng init_boot_b đều trùng bản sạch đúng descriptor. Chỉ đối chiếu, không ghi/phục hồi.
3. `secinfo` fallback có header thật đúng stock, chữ ký P-256/SHA256 kiểm bằng khóa stock H3/H4 đạt;12 ca native matcher kiểm3 binding và control sai trên H3/H4 đều đúng. Các binding khớp bootconfig. Không kết luận fallback của máy này thiếu/hỏng.
4. Secure ID primary trong RPMB chưa được đọc/xác minh. Gate chỉ dùng fallback nếu bước lấy primary lỗi; primary có chữ ký hợp lệ nhưng binding khác vẫn có thể chặn mà không thử fallback. Không dùng ioctl/SMC/RPMB chưa được xác minh.
5.152 ca native getter/setter chứng minh invalid/empty token thường dẫn locked4/success0, không panic trực tiếp. Getter gốc có nhánh ghi magic trong RPMB; force-getter bỏ qua nó. Lệnh lock chuẩn viết sentinel chữ ký và có đường xóa userdata; chưa có backup của token256 byte đang dùng. Bản backup LK không giữ chữ ký trong RPMB.
6. Ca brick của bạn người dùng đã xảy ra cả khi thay stock LK rồi reboot **không chạy lock**, trên China H3. Không có đúng build/hash/log để quy nguyên nhân. Locked policy, AVB, Secure ID, trước-main auth và rollback là các lớp riêng. Không gán lỗi init_boot của máy hiện tại thành nguyên nhân ca đó.

Các bản LK ENG/HWC trước có thể vẫn phụ thuộc code chỉnh sửa. Khi OTA thay lại LK gốc, những thay đổi đó không còn. Mục tiêu bền phải là dữ liệu máy và toàn chuỗi ảnh được bootloader OEM nguyên bản chấp nhận. [AOSP device state](https://source.android.com/docs/security/features/verifiedboot/device-state) mô tả locked chỉ boot OS hợp root of trust; [A/B OTA](https://source.android.com/docs/core/ota/ab/ab_implement) có danh sách phân vùng cập nhật do build chọn. Chưa có payload OTA thực được kiểm trong ca này.

Điều kiện còn thiếu trước khi xét thử chuyển trạng thái: bộ ảnh sạch của đúng build, hashtree/chuỗi slot đầy đủ, dữ liệu primary/khả năng bảo toàn token, rollback và preloader thực tế. Hai boot LUN4MiB đã sao lưu chỉ đọc và trùng nhau nhưng chưa khớp các preloader tham chiếu. Không tự xem ENG preloader là bảo đảm cứu máy.

Không có file firmware mới hoặc lệnh lock/flash/reboot/erase được thực hiện. Dữ liệu riêng thiết bị được giữ ngoài Git; repo chỉ có kết quả đã lược thông tin. Bản cũ H3/H4/ENG không thay đổi. Checkpoint đầu đợt: `before-stock-relock-ota-research-20261104` tại`e3e27af`.
