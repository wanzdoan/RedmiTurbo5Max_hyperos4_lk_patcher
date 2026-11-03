# Redmi Turbo 5 Max — LK HyperOS 4 hybrid

Người dùng đã xác nhận **boot thành công**, fastboot **unlocked: yes** trên ROM **OS4.0.3.0.XPLCNXM** với preloader ENG H3. Sau đó người dùng báo đã test việc thay preloader stock H4 thành công và yêu cầu dừng kiểm tra, cập nhật docs.

Bản đã chạy được tạo bằng **hai thay đổi phối hợp**: patch getter unlock trong main LK H4, đồng thời ghép bộ BL2_EXT/certificate H3 còn nguyên chữ ký vào container. AEE và DTB/DTBO giữ nguyên H4. Đây là bản hybrid, không phải toàn bộ bootloader thuần H4 được patch.

- [Chỉ mục tài liệu đầy đủ](docs/README.md).
- [Logic reverse và patch](docs/01-logic-and-patch.md).
- [Input, layout và nguồn thành phần](docs/02-inputs-and-layout.md).
- [Build và kiểm chứng](docs/03-build-and-validation.md).
- [Biên bản máy thật và preloader](docs/04-runtime-and-preloader.md).
- [GitHub, phiên bản và quay lại bản cũ](docs/05-versioning-and-rollback.md).
- [Chỉ mục evidence và giới hạn](docs/06-evidence-and-limits.md).
- [Changelog](docs/CHANGELOG.md).

File đã bàn giao: [lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img](fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img), **4.083.424 byte**.

SHA256: `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`.

Giữ nguyên tên và bytes sau xác nhận boot. Tên `EXPERIMENTAL` là tên artifact ban đầu; biên bản runtime riêng ghi kết quả mới. `fresh_analysis/rejected` chỉ chứa đối chứng bị loại. Các thư mục source/reports/ghidra và bản patched cũ ngoài `fresh_analysis` được lưu để bảo tồn lịch sử, không phải quy trình tạo bản mới.
