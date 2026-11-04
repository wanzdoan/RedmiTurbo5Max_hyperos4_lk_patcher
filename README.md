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

Nghiên cứu tiếp theo: [5G/NR và biên bản hoàn nguyên](docs/07-nr5g-investigation.md). Các thử nghiệm SA/NSA đã hoàn nguyên theo yêu cầu sau hai lần treo màn hình, nguyên nhân chưa xác định. Chưa có bản patch 5G hoạt động được xác nhận.

Bộ thử tách riêng: [radio region global v1](experiments/radio_region_global_v1/README_VI.md), gồm LK đã boot giữ nguyên byte và module đổi một thuộc tính Android `ro.vendor.radio.build_region=global`. Chưa cài hoặc kiểm thử NR trên máy thật; không phải LK có thêm patch 5G.

Theo yêu cầu tiếp theo chỉ sửa LK: [bản thử bỏ chốt ROM CN/Global](experiments/lk_global_region_v1/README_VI.md) đã qua mô phỏng logic vùng, giữ unlock cũ, chưa flash hoặc boot test. [Docs 08](docs/08-lk-rom-region.md) giải thích ba chốt đã sửa và bằng chứng mới: descriptor modem LK truyền xuống đã chứa bit hỗ trợ NR. Chưa chứng minh bản thử này mở được 5G.

Biến thể theo file H3 của người dùng: [H3 đã patch unlock + CN/Global](experiments/lk_h3_global_region_v1/README_VI.md), tạo trực tiếp từ `lk_hyperos3_patched_unlockBL/dash.img`, không ghép H4. Đã kiểm thử mô phỏng, chưa xác nhận boot bản mới.

Input mới ngày2026-11-04: [LK ENG H3 patch unlock riêng](experiments/lk_h3_eng_unlock_v1/README_VI.md), từ `Downloads/lk.img` SHA6f386241..., giữ toàn bộ thành phần ENG. [Docs09](docs/09-h3-eng-unlock.md) ghi logic và kiểm thử; chưa flash/boot test.

Nghiên cứu hiện tại: [khóa thật và OTA stock China](docs/10-stock-relock-and-ota.md). Đọc ADB/su xác nhận hai LK là token-key clone, kernel unlocked/orange khác props bị che; init_boot_a lệch hash ký, Secinfo fallback hợp lệ. Chưa có quy trình relock an toàn được xác nhận; chưa flash/lock hoặc ghi bảo mật. Raw riêng thiết bị giữ ngoài Git.
