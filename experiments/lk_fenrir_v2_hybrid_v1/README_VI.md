# Hai bản Fenrir + fakelock v2: H3 stock và H3 debug

Ngày 2026-11-05. Người dùng yêu cầu hai ảnh riêng, kết hợp cách fake trạng thái của Fenrir với phần báo cáo của v2, hướng tới sử dụng được `init_boot` đã root. **Đã kiểm thử mã ARM64 trên hai file cuối; chưa flash/boot/root test trên máy.**

| Nền | File | SHA256 |
|---|---|---|
| H3 stock, từ đúng Fenrir đã phân tích | [LK STOCK](stock_h3/lk_H3_STOCK_FENRIR_V2_HYBRID_AUTO_HWC_TEST.img), 4.083.872 byte | `50e1fe9d598da67021ba22fe4e67c52ccd539a076ada116ccbdd2f66a7daec02` |
| H3 debug/userdebug, từ input `6f386241...` | [LK DEBUG](debug_h3/lk_H3_DEBUG_FENRIR_V2_HYBRID_AUTO_HWC_TEST.img), 4.043.792 byte | `a8b894d476d8af889b0f82f746a523d19591d4e30c2a122dd0c7b17302692a62` |

[ZIP đủ hai file](lk_fenrir_v2_hybrid_two_bases.zip) · [Manifest release](release_manifest.json). Mỗi thư mục ảnh có manifest patch và báo cáo kiểm thử riêng.

## Đã kết hợp những gì?

| Phần | Cách thực hiện |
|---|---|
| Nguồn trạng thái khóa | Fenrir: common wrapper trả `4/locked`, success0; không ghi trạng thái khóa bền vững |
| Verified boot trong RAM | Fenrir: setter ghi0/green; giữ rõ đây là trạng thái phần mềm giả |
| Cho phép đường boot với init_boot sửa | Fenrir: đặt bit allow-verification-error của caller AVB; giữ các filter lỗi còn lại |
| Fastboot secure/unlocked | Hàm publish gốc tự xuất yes/no từ common locked4 |
| LKS, seccfg, flash.locked | V2: public LKS1, public seccfg reply1, bootarg `androidboot.flash.locked=1` rõ ràng |
| Root of Trust xuất xuống secure monitor | Code của chính từng nền xuất hash khóa OEM, lock1/state0 nhờ nguồn locked/green; không thêm khóa riêng/certificate attestation |
| Quyền lệnh main/AEE | Stock kế thừa gate Fenrir; debug vốn đã không có các gate tương ứng, giữ AEE nguyên byte |
| HWC all-region | countrycode đúng `cn` không phân biệt hoa thường →CN; chuỗi khác không rỗng →GLOBAL; thiếu/rỗng/lỗi đọc →HWC phần cứng gốc |
| MTE | Giữ chính sách gốc từng nền; không thêm patch MTE-off trong bộ hai file này |
| Bảo trì phiên bản sau load | Bỏ riêng lời gọi conditional rollback-index/OTP maintenance mà trạng thái unlocked3 của v2 vốn bỏ qua |

Common wrapper giả locked không làm thiết bị khóa thật. Các lớp xuất trạng thái không sửa RPMB/seccfg/efuse. Những routine ghi bảo mật khác vẫn nằm nguyên trong firmware; không dùng báo locked để suy rằng có thể chạy lock/OTA như stock an toàn.

## Vì sao stock-policy root init_boot vào recovery?

Người dùng xác nhận file gặp lỗi là **stock-policy**, flash thành công nhưng sau đó máy vào recovery; nạp lại init_boot gốc thì boot lại. Người dùng cũng báo Fenrir dùng được root. Hai báo cáo được giữ riêng trong [runtime_confirmation.json](runtime_confirmation.json), chưa có hash readback hoặc log lỗi của chính lần boot đó.

Stock-policy giữ chính sách verify theo khóa thật. Việc sửa ramdisk để root thường làm hash khác descriptor đã ký, nên hiện tượng phù hợp với lỗi xác minh. Đây là suy luận có hỗ trợ từ mã, chưa phải chẩn đoán từ log máy.

Test mới tạo image có header Android v4 và vùng ramdisk: bản gốc, bản đổi ramdisk, bản tăng ramdisk. Hàm xử lý hash descriptor và so sánh digest của chính LK chạy native: bản đổi/tăng trả3/hash mismatch. Với bit allow-error1 của hai hybrid, result3 được filter/caller cho đi tiếp. Descriptor/thuật toán sai hoặc lỗi đọc vẫn bị filter loại. Không thay mọi kết quả lỗi bằng thành công.

Đây là bằng chứng cho đường chấp nhận hash mismatch cần cho root; **không chứng minh kernel, Magisk/KernelSU hoặc toàn bộ chuỗi boot của điện thoại đã chạy thành công**. Test dùng fixture tổng hợp, không sửa hoặc flash init_boot của người dùng.

## Một khác biệt bảo mật đã được giữ như v2

Theo mã của cả hai nền, sau khi load, predicate theo trạng thái locked còn có thể dẫn đến hàm bảo trì rollback index/phiên bản. Nó không phải một lần kiểm hash init_boot bổ sung. Trạng thái unlocked3 của v2 thường bỏ qua đường đó, nhưng locked4 giả của Fenrir có thể kích hoạt nó.

Hai bản cuối bỏ **đúng một lời gọi** tại main stock `0x688b8` hoặc debug `0x69688` bằng NOP. Hàm OTP/cập nhật phiên bản dùng chung không đổi; không có lệnh ghi vào thiết bị trong quá trình build/test. Native control chứng minh state3 bỏ qua, state4 đi đến entry bảo trì; native test file cuối chứng minh lời gọi này không chạy và continuation vẫn có cùng trạng thái thành công. Không tuyên bố đã vô hiệu hóa mọi đường chống hạ cấp trong toàn firmware.

## Khác nhau giữa hai nền

**STOCK:** input là Fenrir `dee32d18...`, vốn được patch trên H3 release. Giữ cơ chế fake trạng thái và gate lệnh Fenrir trong main/AEE, BL2 H3 release, DTB/DTBO và trailer768 byte zero. Thêm public exports v2, HWC và chặn lời gọi bảo trì nêu trên. Main cert2 vẫn dùng prefix90 byte, DER gốc giữ nguyên; AEE/certificates giữ nguyên byte Fenrir.

**DEBUG:** input nguyên gốc `6f386241...`, không lấy getter force-unlocked của bản v2 rồi chồng locked lên. Giữ getter backing gốc, patch common wrapper thành4, AVB allow-error và setter0; thêm public exports, HWC và chặn cùng lời gọi bảo trì. BL2/AEE/DTB và certificates của chính debug giữ nguyên. Code debug main và AEE đã không có các chốt khóa cụ thể mà Fenrir phải sửa trên stock; 128 ca native xác nhận gate/callback tới điểm query partition, không cần sửa AEE hoặc thêm prefix AEE. Main cert2 thêm prefix180 byte.

Không ghép H4 hoặc lấy BL2 từ nền còn lại. Tên DEBUG mô tả input debug/userdebug đã phân tích, không khẳng định một build `eng` chính thức của hãng.

## HWC giữ dữ liệu phần cứng sớm

Hai bản dùng [port HWC riêng](stock_hwc/README_VI.md), giữ getter thông tin phần cứng chạy sớm đúng một lần. Điều này bảo toàn HWlevel/HWversion cho các consumer trước lúc đọc countrycode. Sau countrycode mới định dạng HWC, khôi phục các con trỏ cần cho caller. GLOBAL nằm trong block mới, không đè callback khóa ký. Một số diagnostic log trong block được rút gọn/bỏ để đủ chỗ; các phép kiểm stack, error/close và dữ liệu HW được giữ.

HWC all-region chỉ là mapping mã vùng. Không thêm khóa ký mọi ROM, không chứng minh NR/5G hoặc khắc phục tương thích preloader H4. Các tổ hợp preloader stock tiếp tục có giới hạn đã ghi trong [docs11](../../docs/11-eng-preloader-compatibility.md).

## Tái tạo và kiểm thử

Chạy từ root repo, không dùng Python `-O`:

```powershell
python experiments/lk_fenrir_v2_hybrid_v1/build_hybrid.py
python experiments/lk_fenrir_v2_hybrid_v1/validate_hybrid.py
python experiments/lk_fenrir_v2_hybrid_v1/validate_layout.py
python experiments/lk_fenrir_v2_hybrid_v1/package_two.py
```

Builder buộc đúng hash input, khai báo từng span main, kiểm không overlap, chỉ thay main/cert2 của main và giữ nguyên các record khác theo từng nguồn. Prefix được cập nhật theo bytes cuối. Source Fenrir được parse15 member và trailerzero rõ ràng; không sửa parser/builder cũ để chấp nhận file mới.

`layout_validation.json` kiểm không có nhánh trực tiếp từ ngoài đi vào interior các block public/common đã thay trong vùng mã đã scan. HWC có kiểm riêng tương tự. Không coi scan nhánh trực tiếp là chứng minh mọi computed jump hoặc toàn bộ control flow.

| Ca native trên bytes cuối | STOCK | DEBUG |
|---|---:|---:|
| Common state, boolean/auth caller | 105 | 105 |
| Publish secure/unlocked | 2 | 2 |
| LKS/seccfg/green/flash.locked, cả lỗi append/query | 21 | 21 |
| Root handoff, SHA256 native và năm packet | 20 | 20 |
| HWC hai slot, dữ liệu vùng/lỗi/early snapshot | 147 | 147 |
| Gate main/AEE | 62 | 128 |
| Init_boot hash, filter, caller và bit allow-error | 37 | 37 |
| Bỏ riêng lời gọi bảo trì phiên bản | 6 | 6 |
| Parser BL2 với main/AEE cert cuối | 6 | 6 |
| Tổng native | **406** | **472** |
| Chữ ký DER gốc kiểm offline | 10 | 10 |

Các count không cộng thêm control nghiên cứu nền. Bằng chứng bổ sung: [root/init_boot và call bảo trì](../../fresh_analysis/fenrir_v2_hybrid_research/root_path/README_VI.md), [gate debug](../../fresh_analysis/fenrir_v2_hybrid_research/debug_gates/README_VI.md). Các probe ghi rõ mocks: storage/slot/formatter/log, secure-monitor sink, trạng thái seccfg và key extraction. Riêng hash-descriptor dùng native SHA256 của cùng image tại biên crypto engine, so với hashlib độc lập; không thực thi secure-world hoặc OTP.

Forced green có thể che cảnh báo lỗi ở các luồng đọc cache; đó là đánh đổi giữ từ Fenrir, không phải bằng chứng ROM đã verified thật. Hai ảnh mới chưa chứng minh boot/root, OTA, Integrity hay ứng dụng ngân hàng. Không chạy lock, flash, reboot hoặc thay preloader trong đợt này.

## Phiên bản

- Checkpoint đầu: `before-fenrir-v2-hybrid-two-bases-20261105` tại `552072a`.
- Prototype chưa boot trước guard bảo trì: commit `46c11af`, tag `before-hybrid-rollback-maintenance-guard-20261105`. Hash prototype41125.../c2c2... là lịch sử, **không phải hash hai file cuối ở đầu trang**.
- Các artifact đã boot, sáu file cũ và input giữ nguyên byte. Dùng Git để lấy đúng phiên bản cũ ra file riêng; không cần hard reset hoặc force push.
