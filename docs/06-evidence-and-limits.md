# 06 — Evidence, kiểm thử và giới hạn

## Ma trận kiểm thử

| Nhóm | Kết quả | Phạm vi |
|---|---|---|
| Main getter/wrapper/AVB | 328 ca PASS | Actual ARM64, ABI/stack/vùng ghi và các controls |
| Parser BL2 ba certificate input | 18 ca hoàn tất | Phạm vi hash OID H3/H4 theo mask |
| Parser cert đúng hybrid cuối | 6 ca PASS | H3 chọn hash sửa, H4 chọn hash signed gốc |
| Policy BL2 H4 | Các ca xét trả auth-required 1 | Error getter dẫn tới conservative policy |
| Certificates ba input | 30 DER signatures PASS | RSA-PSS dưới public key nhúng |
| Hybrid cuối | 10 DER signatures PASS | Non-main payload digests đúng; main dùng hash prefix |
| Deterministic rebuild | PASS | Đúng input tạo cùng bytes/hash |
| Input sai danh tính | Bị từ chối trong test | SHA256 guard của builder |
| Runtime ENG H3 | Người dùng xác nhận boot và unlocked yes | ROM OS4.0.3.0.XPLCNXM |
| Runtime thay stock H4 | Người dùng báo đã test được, dừng điều tra | Theo ngữ cảnh yêu cầu thay preloader; không log agent |

## Mocks/model

`validate_candidate.py`: stub logging/chọn boot partition; control cases mới mock getter state để kiểm nhánh. Actual patched path chạy đúng code main/wrappers/AVB. Mapping CPU thấp được dùng cho probe, không mô phỏng toàn MMU/SoC.

`emulate_bl2_parser.py` và `validate_candidate_parser.py`: actual parser và OID scans, không mock crypto/auth-result để chọn hash. PAC/logging được mô hình hóa/bỏ qua, BSS zero và routine được gọi trực tiếp. Đây không phải toàn signature chain hoặc whole boot.

`emulate_cert_parser.py` của preloader: key extraction/equality, RSA primitives và helper được mock như JSON ghi. SHA256 tính bằng Python, kết quả RSA mô hình hóa bị ràng buộc vào signed TBS biết trước. Chỉ diễn giải TBS/hash selection dưới các điều kiện đó.

`bl2_policy_emulation.py`: mô hình hóa sboot helper/logging/strcmp; actual getter/policy vẫn chạy. Các control đổi getter success/state3 không phải bản firmware đã bàn giao.

## Bản đồ bằng chứng

Các đường dẫn dưới đây tính từ `fresh_analysis`:

| Câu hỏi | Evidence |
|---|---|
| Input đúng gì? | `manifest.json`, `container_chain/container_inventory.json` |
| Patch 16 byte và callers? | `h3_diff/REPORT.md`, `h4_call_map.json`, `h4_recovered_semantics.c` |
| Ai nạp/xác thực main? | `h3_diff/BL2_LOADER.md`, `bl2_policy_emulation.json` |
| Cơ chế H4 mới? | `container_chain/BL2_FINDINGS.md`, focused H3/H4 parser exports |
| Hash và chữ ký? | `container_chain/cryptographic_evidence.json` |
| Parser đúng artifact? | `preloader/hybrid_candidate_parser_validation.json` |
| Byte identity hybrid? | `hybrid_validation.json` |
| File ENG trùng H3? | `preloader/eng_identity.json` |
| Preloader khác gì trong vùng đã scan? | `preloader/comparison.json`, `address_reference_checks.json` |
| ROM đang dùng đúng input? | `stock_h4_preloader_review/rom_inventory.json` |
| Người dùng test gì? | `output/runtime_confirmation.json` |

## Source phục hồi

2.328 main + 1.526 BL2 + 1.827 AEE = **5.681 file pseudocode native H4** theo các hàm Ghidra nhận diện/export. Có thêm focused preloader exports và các entry phải đánh dấu thủ công. Một số data/indirect targets gây warnings. Đây không phải source C gốc, không chứng minh toàn bộ function boundaries/typing/flow đúng hoặc phủ mọi đường chạy.

Các raw reports giữ lịch sử điều tra. Một số mô tả “no hardware execution”, “not built by this sub-investigation” là scope của bước/nhánh đó, không phủ định kết quả tích hợp và người dùng báo sau. Xem docs04 và runtime sidecar để biết trạng thái hiện tại.

## Không suy rộng

PSS hợp lệ dưới embedded SPKI không tự chứng minh fuse trust root mọi máy. Parser return0 không đồng nghĩa image auth thành công. Fastboot unlocked yes không đồng nghĩa RPMB đã được thay unlock vĩnh viễn. Boot thành công trên cấu hình được báo không tự xác nhận OTA, relock, recovery, mọi build H4 hoặc mọi preloader/fuse state.

Người dùng yêu cầu dừng kiểm tra preloader sau khi tự test được. Giữ các kết quả tĩnh đã có, không tiếp tục hoặc bịa kết quả hoàn chỉnh cho các phần chưa xong.
