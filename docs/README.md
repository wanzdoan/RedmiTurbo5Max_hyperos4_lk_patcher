# Chỉ mục docs

| Tài liệu | Nội dung |
|---|---|
| [01 — Logic và patch](01-logic-and-patch.md) | Chuỗi boot, cơ chế H4 mới, 16 byte ARM64, ASN.1 và hybrid |
| [02 — Input/layout](02-inputs-and-layout.md) | Hash input, 15 record, nguồn từng member và quy ước địa chỉ |
| [03 — Build/validation](03-build-and-validation.md) | Công cụ, lệnh tái tạo, scripts kiểm thử và đóng gói |
| [04 — Runtime/preloader](04-runtime-and-preloader.md) | ROM, hai xác nhận người dùng, phạm vi tương thích |
| [05 — Git/rollback](05-versioning-and-rollback.md) | Repo private, checkpoint, tags và khôi phục bản cũ |
| [06 — Evidence/giới hạn](06-evidence-and-limits.md) | Các test, mocks, báo cáo reverse và source phục hồi |
| [07 — Nghiên cứu 5G/NR](07-nr5g-investigation.md) | Policy, các phép thử đã hoàn nguyên, sự cố màn hình và bộ thử region riêng |
| [08 — Chốt vùng ROM trong LK](08-lk-rom-region.md) | Chốt CN/Global thật, bản thử LK riêng, kiểm thử CPU và giới hạn về 5G |
| [09 — Unlock LK ENG mới](09-h3-eng-unlock.md) | Input ngày04-11, getter riêng, BL2 ENG giữ nguyên và kiểm thử |
| [10 — Lock thật và OTA China](10-stock-relock-and-ota.md) | Đọc máy qua su, hash init_boot lệch, Secinfo hợp lệ và điều kiện còn thiếu trước lock |
| [Changelog](CHANGELOG.md) | Các mốc phát hiện, boot và versioning |

Ngày cập nhật: 2026-11-03. Tài liệu hiện tại ưu tiên bộ `docs`; các báo cáo reverse trong `fresh_analysis` lưu quá trình điều tra và phạm vi tại lúc chạy.

Quan sát binary, replay ARM64 có mô hình hóa, suy luận và báo cáo người dùng là những loại evidence riêng. Các JSON test ghi “no hardware execution” vẫn đúng đối với probe đó. [runtime_confirmation.json](../fresh_analysis/output/runtime_confirmation.json) bổ sung kết quả máy thật do người dùng xác nhận; không biến các probe cũ thành whole-device tests.

Repo private: [wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher](https://github.com/wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher). Không tiếp tục điều tra hoặc tự flash preloader sau yêu cầu dừng của người dùng.
