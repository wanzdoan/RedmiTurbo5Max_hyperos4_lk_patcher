# Fenrir: container, BL2, AEE và certificate

Ngày 2026-11-05. Chỉ đọc và chạy các đoạn mã trong emulator; không flash, không sửa input. Input immutable ở `../input/dash-fenrir-versioned.bin`, SHA256 `dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113`, 4.083.872 byte. So với `lk_hyperos3_original/lk.img`, không dùng ENG input `6f386241...` làm bản gốc giả định.

Script: `python fresh_analysis/fenrir_review/chain_analysis/analyze_chain.py`. [chain_evidence.json](chain_evidence.json) chứa diff thành phần, toàn bộ kết quả kiểm chữ ký/parser và 80 ca AEE.

## Thành phần giữ nguyên và thay đổi

Container có đủ 15 record MediaTek, rồi **768 byte zero** sau record cuối. Đây là lý do parser cũ đòi EOF ngay sau 15 member báo lỗi; không coi lỗi parser đó là file hỏng. Chưa có metadata dạng version trong trailer này.

| Thành phần | So với H3 original |
|---|---|
| Main | Cùng size/layout; 63 byte khác (phân tích ở nhánh main) |
| Main cert1 | Giống nguyên byte |
| Main cert2 | Thêm một prefix 90 byte; DER ký gốc giống nguyên byte |
| BL2 + cert1 + cert2 | Tất cả nguyên byte H3 original |
| AEE | 11 byte khác ở bốn instruction |
| AEE cert1 | Giống nguyên byte |
| AEE cert2 | Thêm một prefix 90 byte; DER ký gốc giống nguyên byte |
| DTB/DTBO và certificates | Nguyên byte H3 original |

BL2 payload SHA256 `e9aca46a63376b22daf9150319e27c8fc2da1e176781af8bc1392da4f178e76b`. Nó không phải BL2 ENG `ab5a5fbc...` của fakelock v2 đang có. Do đó không bê trực tiếp offset Fenrir sang v2 hoặc suy runtime tương đương giữa hai nền.

Hai prefix 90 byte tận dụng padding cũ, nên vùng 15 record tăng tổng cộng 160 byte; thêm 768 byte zero tạo chênh lệch whole-file 928 byte so với H3 original. Không có member BL2 mới, thành phần TEE mới hoặc chuỗi certificate mới được thêm.

## Certificate và hash

**10 chữ ký DER đều xác thực offline bằng khóa nhúng**, và toàn bộ DER giống chính record H3 original tương ứng. Với main và AEE đã patch, hash payload nằm trong DER ký gốc không còn khớp payload thực tế; prefix 90 byte chứa hash mới của header và payload aligned16. Ba thành phần không sửa (BL2/DTB/DTBO) vẫn khớp hash đã ký.

Một prefix 90 byte của Fenrir đúng bằng một nửa block 180 byte duplicate mà builder v2 dùng. Đây là khác biệt cách đóng gói hash override; không phải bổ sung chữ ký chính hãng hoặc nâng bảo đảm xác thực.

**12 ca chạy parser BL2 native**: main/AEE × BL2 H3/H4 × mask `0x60`, `0x1f4e2`, `0x1f400`. H3 chọn hash mới trong prefix khi yêu cầu hash; H4 chọn hash cũ trong signed DER. Những ca này không mock ASN.1/OID/hash selection/crypto result; chỉ bỏ qua PAC và logging. Kết quả không chứng minh toàn bộ required-auth chain hoặc boot thật; đặc biệt không suy H4 BL2 chấp nhận hash payload Fenrir đã patch.

## AEE mở rộng những gì?

Địa chỉ payload tính từ đầu member AEE; whole-file chỉ áp dụng đúng Fenrir input này.

| AEE payload | Whole-file | Đổi | Hành vi |
|---|---|---|---|
| `0x13be8` | `0x2572a8` | `cbz w8, 0x13c20` → `nop` | Không bỏ qua callback chỉ vì command descriptor `+0x14` bằng zero dưới điều kiện query đang xét |
| `0x13bfc` | `0x2572bc` | `b.ne 0x13c20` → `nop` | Không bỏ qua callback do command descriptor `+0x18` yêu cầu state3 nhưng state fixture khác3 |
| `0x16498` | `0x259b58` | `ldr w8,[sp,#4]` → `ldr w8,[sp,#0xc]` | Đổi word được đọc trước compare; compare trở nên không quyết định nhánh tiếp vì patch bên dưới |
| `0x164a0` | `0x259b60` | `b.ne 0x164b0` → `b 0x164b0` | Bỏ đường báo `Flashing is not allowed in Lock state` khi query thành công |

Các dòng này tương ứng nhóm patch ở main `0xa110/0xa124` và `0xd1d0/0xd1d8`. Đây là mở rộng vào **fastboot AEE**, trong khi fakelock v2 hiện tại giữ nguyên AEE của ENG input. Nó không phải patch riêng tạo chứng thực phần cứng hay thay Root of Trust của AEE.

**80 ca AEE native** gồm hai input (stock/Fenrir), 64 ca command-dispatch gate với state/query/descriptor fixtures và 16 ca cổng flash theo state/query-result. Fenrir đi tới callback qua hai gate đã sửa; cổng flash bỏ kiểm state1 nhưng **vẫn giữ deny nếu helper query trả lỗi**. Chưa chạy toàn bộ handler ghi partition, nên không tuyên bố mọi lệnh flash đều thành công hoặc mọi chốt an toàn đều được mở.

## Cách đánh giá so với v2

- Phạm vi AEE là thay đổi đáng chú ý hơn: Fenrir sửa cả chế độ xử lý lỗi, còn v2 chỉ sửa main. Có thể hữu ích nếu cần fastboot ở AEE, nhưng cũng mở thêm đường command dưới trạng thái locked.
- BL2 không có cải tiến code mới so với H3 original. Dùng prefix đơn thay duplicate làm gọn, không thay đổi bản chất bypass chọn hash.
- Tính nhất quán Root of Trust phụ thuộc patch main/global-state, do nhánh main phân tích. Không gán kết quả AEE command-gate hoặc chữ ký certificate thành thành công Play Integrity/VNeID/TEE.
- Không có bằng chứng kernel/secure-world/attestation private key được thêm, sửa hay giả mạo trong container này. Đây là kết luận về các thành phần đã đối chiếu và các diff đã tìm; không phải chứng minh mọi dịch vụ bên ngoài sẽ chấp nhận trạng thái báo cáo.

Giới hạn: emulator không chạy SoC, secure monitor, kernel hoặc boot toàn bộ; trạng thái AEE thực của thiết bị chưa đo. MMIO/storage không được truy cập. Giữ nguyên firmware và không nhập patch Fenrir vào v2 trong đợt phân tích này.
