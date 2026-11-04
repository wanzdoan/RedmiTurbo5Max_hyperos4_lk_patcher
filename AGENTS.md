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
- Người dùng sau đó yêu cầu chỉ sửa LK và tìm chốt ROM China/Global. Đọc docs/08-lk-rom-region.md. experiments/lk_global_region_v1 là LK thử riêng đã qua CPU/parser/DER checks, chưa flash/boot test và không phải fix5G đã chứng minh. Bản f57... đã boot vẫn là điểm khôi phục.
- Evidence mới: H4 modem image_type13 -> kernel ccci_util_lib mask0x7b đã có N/NR0x40. Không patch descriptor13->14 để tuyên bố bật NR. Không cài module region phía Android trong phạm vi LK-only.
- Người dùng yêu cầu thêm biến thể từ đúng H3 đã patch: experiments/lk_h3_global_region_v1. Input là lk_hyperos3_patched_unlockBL/dash.img hash e164..., không phải H3 original và không ghép H4. Output dbabb269... giữ unlock/các sửa cũ, chỉ thêm ba chốt vùng và cập nhật prefix; qua mô phỏng nhưng chưa boot test. Giữ nguyên toàn bộ input/artifact cũ.
- Ngày2026-11-04 có input LK ENG mới từ Downloads/lk.img SHA6f386241...; bản gốc được lưu nguyên byte trong experiments/lk_h3_eng_unlock_v1/input. Đọc docs/09-h3-eng-unlock.md. Artifact unlock ENG output0d237263... chỉ patch getter và thêm prefix, giữ chính BL2 ENG mới (parser0x6ea70), không ghép donor và không thêm region/5G. Qua342 CPUmain+9parser+4policy+10DER; chưa flash/boot test. Không dùng offset H3/H4 cũ cho input này.
