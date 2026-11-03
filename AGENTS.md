# Hướng dẫn làm việc trong repository

## Yêu cầu phiên bản của người dùng

- Repo GitHub phải giữ PRIVATE: `wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher`.
- Trước khi sửa file hiện có, kiểm tra Git; trạng thái trước sửa phải có checkpoint commit có thể quay lại và được push. Nếu workspace có thay đổi chưa lưu, lưu checkpoint trước công việc mới. Không bỏ mất thay đổi của người dùng.
- Với sửa logic patch/firmware, thêm tag checkpoint trước sửa; commit thay đổi riêng và push. Không amend/rewrite/force-push các mốc đã bàn giao hoặc đã boot.
- Không cần xin lại phép cho commit/push thông thường trong repo private này khi nằm trong tác vụ người dùng đã giao; không thay đổi visibility.

## Nguồn đúng và artifact

- Đọc root README và docs/README.md trước. Đợt reverse hiện hành ở fresh_analysis; các thư mục cũ được giữ làm lịch sử, không mặc định đúng.
- Bản đã được người dùng xác nhận chạy: fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img.
- SHA256: f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171.
- Nó là H4 main/BL33 patch 16 byte + H3 signed BL2 triple + H4 AEE/DTBs. Không mô tả là thuần H4.
- Sửa docs không thay bytes/tên artifact đã chạy. Phân biệt machine-test scope với runtime report của người dùng trong runtime_confirmation.json.
- Chỉ thay firmware hoặc flash thiết bị khi tác vụ người dùng thực sự yêu cầu; không tự flash để kiểm docs. Người dùng đã yêu cầu dừng điều tra stock preloader sau khi họ tự test được.

## Kiểm chứng

- Không chạy builder bằng Python -O vì assert đang bảo vệ input/layout.
- Khi đổi firmware/patch, chạy tests phù hợp trong docs/03-build-and-validation.md và ghi input/output hashes.
- Khi chỉ sửa docs, kiểm link/metadata, firmware hash và bundle; không cần lặp toàn bộ mô phỏng CPU.
- Duy trì docs và commit/tag để lần sau có thể lấy lại bản cũ mà không hard reset hay force push.

## Trạng thái nghiên cứu 5G hiện tại

- Đọc docs/07-nr5g-investigation.md và fresh_analysis/nr5g_research/crash_review/rollback_and_current_state.json. vina_outdoor_setup.json chỉ là lịch sử, không còn là trạng thái cuối.
- Sau hai lần sọc màu/đen màn, người dùng yêu cầu hoàn nguyên. Đã xác nhận ro.vendor.radio.5g=2, Settings.System 5g_sa_mode_disabled absent/null, preferred_network_mode=33,33. Nguyên nhân treo chưa xác định; không tự bật lại thử nghiệm SA/NSA.
- Yêu cầu mới là bộ thử ro.vendor.radio.build_region=global tách riêng. experiments/radio_region_global_v1 chứa LK nguyên byte đã boot + module property-only; chưa cài lên máy, chưa chứng minh sửa được NR. Không mô tả LK sao chép là LK patch 5G.
- Không coi ERROR là thành công. Không flash/NVRAM để tiếp tục nếu chưa có mục tiêu/điều kiện được chứng minh.
