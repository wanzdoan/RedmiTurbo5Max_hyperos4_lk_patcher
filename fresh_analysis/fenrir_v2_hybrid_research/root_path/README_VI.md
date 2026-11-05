# Luồng init_boot đã sửa: H3 stock và debug

Ngày 2026-11-05. Phân tích theo nhiệm vụ ghép Fenrir + v2, không ADB, không flash, không sửa firmware. Người dùng làm rõ bản không boot với init_boot root là **stock-policy**: flash thành công, boot chuyển recovery; nạp init_boot gốc thì boot lại. Đó không phải bằng chứng fakelock v2 hỏng root.

Kết quả mã phù hợp với hiện tượng trên: khi nguồn trạng thái khóa là4, stock-policy dựng cờ cho phép lỗi xác minh bằng0. Fenrir ép cùng nguồn locked4 nhưng **ép riêng bit allow-verification-error bằng1**, rồi giả trạng thái báo cáo locked/green. Đây là khác biệt có quan hệ trực tiếp tới lỗi hash của init_boot đã sửa. Chưa có log của lần recovery để khẳng định nguyên nhân thực trên điện thoại.

## Mapping đã kiểm

Tất cả địa chỉ là offset payload main (whole-file cộng512 byte).

| Thành phần | H3 stock/Fenrir | Debug6f386 |
|---|---|---|
| Common lock wrapper | `0x88750` | `0x88c18` (thunk thường gặp `0x885fc`) |
| Cờ AVB allow verification error | `0x68668` | `0x69438` |
| Lệnh setter cache verified boot | `0x692fc` | `0x69df4` |
| Global cache | `0x121990` | `0x119968` |
| Caller slot verification | `0x6858c` | `0x6935c` |
| Slot verifier | `0xbe0cc` | `0xbe700` |
| Hash descriptor verifier | `0xbfdb0` | `0xc03e4` |
| Filter kết quả verifier | `0xbe270..0xbe298` | `0xbe8a4..0xbe8cc` |
| Caller sau slot verification | `0x6887c` | `0x6964c` |

Patch cờ cả hai nền: `f6179f1a` (`cset w22,eq`) → `36008052` (`mov w22,#1`). Setter H3 `009109b9` → `1f9109b9`; debug `006909b9` → `1f6909b9`. Common wrapper trả4/success0, không ghi trạng thái persistent. Bản stock-policy cũ không có cờ allow-error ép1 này.

## Kiểm hash thật với fixture init_boot

[probe_init_boot_hash.py](probe_init_boot_hash.py) dựng một image có header hình dạng Android boot v4, ramdisk64 byte và một `AvbHashDescriptor` chuẩn cho `init_boot`. Descriptor được tạo bằng hash gốc; các fixture sau thay một byte ramdisk hoặc tăng ramdisk, cập nhật kích thước trong header.

Mã native của chính hai LK:

- Parse và validate descriptor, xác nhận partition được yêu cầu, ghép suffix `_a`.
- Đọc dữ liệu qua boundary helper mô hình hóa `init_boot_a`.
- Tính hash salt+data và so sánh digest với descriptor.
- Với bit allow-error1, query kích thước partition thực và nạp đủ dữ liệu fixture đã tăng, thay vì chỉ kích thước gốc trong descriptor.
- Ảnh gốc trả0; ảnh ramdisk sửa/tăng trả3 và log hash mismatch. Descriptor không hợp lệ hoặc thuật toán không hỗ trợ trả6; lỗi helper đọc trả2.

Streaming SHA của libavb trong build này gọi crypto engine bảo mật. Probe thay **biên engine đó** bằng hàm SHA256 phần mềm native có sẵn trong cùng image (`0xa4550` H3 / `0xa4b20` debug), chạy trong Unicorn riêng; kết quả được đối chiếu độc lập với `hashlib`. Không thay bằng một kết quả hash cố định, không mock parser, digest comparison hoặc code return của verifier. Biên streaming engine, storage, logging, strdup allocation và PAC vẫn được ghi rõ trong report.

Filter native chỉ nhận các mã `0,3,4,5` khi flags bit0=1; flags0 chỉ nhận0. Nó vẫn loại mã1/2/6..8. Caller native với bit0=1 đi tiếp đến bước load sau slot khi có mismatch3; flags0 đưa cùng kết quả đó vào nhánh reject. Việc caller đi tiếp không tự chứng minh loader/kernel/root daemon boot thành công.

## Kết quả và API trên file cuối

Chạy nền đối chứng:

```powershell
python fresh_analysis/fenrir_v2_hybrid_research/root_path/probe_init_boot_hash.py
```

[init_boot_hash_validation.json](init_boot_hash_validation.json): 24 ca descriptor/hash,36 filter,4 cờ nguồn khóa và12 caller, tổng76 ca giới hạn.

API `validate(image_bytes,kind)` nhận `kind='stock_h3'` hoặc `'debug'`, chạy trực tiếp bytes cuối, không sửa code trong RAM. Mỗi image37 ca:12 hash,18 filter,6 caller,1 cờ native thực.

Hai file cuối do parent tạo đã qua API:

| Nền | SHA256 | Report |
|---|---|---|
| Stock H3 hybrid | `50e1fe9d598da67021ba22fe4e67c52ccd539a076ada116ccbdd2f66a7daec02` | [stock_h3_final_validation.json](stock_h3_final_validation.json) |
| Debug H3 hybrid | `a8b894d476d8af889b0f82f746a523d19591d4e30c2a122dd0c7b17302692a62` | [debug_final_validation.json](debug_final_validation.json) |

Cả hai dựng bit1 bằng mã final thật và cho mismatch3 đi qua filter/caller trong các fixture. Không có lệnh khóa, ghi RPMB/efuse hoặc thao tác điện thoại.

Giới hạn: đây là kiểm tra hash descriptor và các đoạn điều khiển liên quan, không thực thi toàn bộ chuỗi ký vbmeta, loader boot image, secure monitor, kernel hoặc ứng dụng root. Không xác nhận root/OTA/Integrity hoặc tương thích preloader trên máy thật. Root-capable ở thiết kế này là cho phép luồng kiểm hash đi tiếp như Fenrir, không phải biến ảnh đã sửa thành ảnh được OEM ký hợp lệ.


## Nhánh sau predicate auth và bảo toàn cơ chế bỏ maintenance của v2

Tên predicate `image_auth_required` không có nghĩa lệnh kế tiếp luôn xác thực lại init_boot. Trong caller đang xét:

- H3 gọi predicate `0x689cc`, rồi nếu true gọi `0x68fd0` tại `0x688b8`.
- Debug gọi predicate `0x6979c`, rồi nếu true gọi `0x69a9c` tại `0x69688`.
- Hai hàm sau cùng đọc `AvbSlotVerifyData` phần rollback indexes (`+0x30..+0x128`), tính index nhỏ nhất và gọi helper maintenance nhóm recovery/AVB nếu giá trị nằm trong giới hạn. Debug thêm log rõ `Rollback index` và các lỗi update nhóm; không phải lần hash hay ký lại init_boot.
- Kết quả trả từ maintenance không được caller kiểm: H3 nhảy `0x6884c`, debug nhảy `0x6961c`, đều ghi `w23=0` trước phần cleanup. Đổi riêng lệnh call thành NOP không bỏ một giá trị trả cần cho nhánh sau.

Để giữ hành vi v2 nội bộ unlocked3 vốn bỏ nhánh maintenance này, parent đã thêm đúng **một NOP ở call site** cho mỗi hybrid: H3 `0x688b8: c6010094 -> 1f2003d5`; debug `0x69688: 05010094 -> 1f2003d5`. Không sửa helper OTP/rollback dùng chung, không vô hiệu mọi kiểm rollback trong firmware. Không khẳng định Fenrir gốc luôn ghi fuse: bản thân maintenance còn các điều kiện và helper phía sau.

`probe_rollback_skip.py` có API `validate_rollback_skip(image,kind)`. 24 ca nền xác nhận native predicate dẫn state4 tới maintenance entry, state3 bỏ entry. Maintenance được dừng ngay ở entry khi làm control; không chạy OTP helper. Mỗi file cuối thêm6 ca native (boot-mode và flags fixtures) xác nhận code chứa NOP, không vào maintenance/OTP helper và vẫn đến continuation với `w23=0`. Code RAM không bị sửa.

Hai report final trên đã được cập nhật SHA sau NOP này; mỗi report ghi37 ca hash/filter/caller trước đó +6 ca guard, tổng43. Các report nền76 ca giữ nguyên phạm vi đối chứng.

Ngoài ra loader debug `0x69824` còn kiểm/copy header của các image đã load (helper `0xf8c0` nhận dạng magic Android/vendor và sao chép header), tìm boot/vendor/init_boot trong các record đã xác minh rồi bàn giao buffer qua `0x10588`. Các kiểm header/partition/data hợp lệ vẫn tồn tại; probe không chạy toàn bộ loader. Hai nền không được coi là có loader byte-identical. Chưa thấy tại nhánh predicate true nêu trên một cơ chế hash lại init_boot có thể đảo ngược allow-error3; điều đó vẫn không thay thế boot test.
