# Đối chiếu preloader stock H3/H4 với nhánh ENG LK

2026-11-05, mở lại theo yêu cầu mới của người dùng. Chỉ phân tích offline, không ADB, không flash hoặc thay firmware. Kết quả tái tạo bằng `python fresh_analysis/preloader_compatibility_20261105/review_preloaders.py`, chi tiết ở [evidence.json](evidence.json).

**Chưa chứng minh sáu LK ENG mới tương thích preloader stock H4.** Đã xác định đúng input và một khác biệt thật ở cơ chế bật debug DFD. Không thấy thay đổi siết xác thực LK trong vùng mã/policy đã kiểm; chưa có log của ca brick để quy kết nguyên nhân.

## Định danh đúng các file

| File | Byte | SHA256 |
|---|---:|---|
| `Preloader/preloader_dash_3.304_global.bin` | 1064924 | `4f43d7200acdf2152aeaa25f07a47b54afaad116087b89e28f043d264d35eb68` |
| `Preloader/preloader_dash_3.306_china.bin` | 1064924 | `31748701c84f7cb3890f7880cfb10f90f90dff815b1424bd2de116a22fb96e38` |
| `Preloader/preloader_dash_4.403_china.bin` | 1064924 | `104bb972383b39771ec5bcc4bc79207244306de1d40f370ed2ab3863fb7ceb77` |
| H4 ROM **bên trong `images/preloader_dash.bin`** | 1064924 | `104bb972383b39771ec5bcc4bc79207244306de1d40f370ed2ab3863fb7ceb77` |
| File `preloader_dash.bin` **ở gốc thư mục chứa ROM** | 1091004 | `75e93936bd14914d25380767f33e55f40aefc77db5cbfaa606af4e6641591c35` |

H4 stock nằm tại `D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0\images\preloader_dash.bin`, nguyên byte giống cả `preloader_hyperos4/preloader_dash.bin` và bản `4.403_china`.

File khác ở **gốc** `D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\preloader_dash.bin` lại khớp nguyên byte với cửa sổ `[0x1000,0x1000+1091004)` của boot-LUN đã đọc trước đó, SHA toàn LUN `e987fa22c5ee5d03cf992581b268466e0f557f9e47e6d9580b7283a5e4bdd800`. Như vậy đã định danh được file tương ứng preloader hiện có từ snapshot; không cần truy cập máy mới. Không nhầm file gốc thư mục với file stock trong `images`.

`preloader_hyperos3/preloader_dash.bin` SHA `bcde516c...` từng được dùng làm tham chiếu lịch sử thực ra giống file ENG người dùng đưa từ Downloads; lần này không dùng nó để đại diện stock H3.

## So mã stock H3 → stock H4

GFH của ba file stock có cùng load address `0x02000f00`, cùng file length `0x103fdc`, cùng đầu `0x400` byte. Dùng ánh xạ **linked VA = raw offset + 0x02000f00**, entry raw `0x100` thành VA `0x02001000`. Không dùng ánh xạ lệch `0x1000` của một số probe parser lịch sử để kết luận địa chỉ thật.

Trong vùng aligned ARM64 `[0x100,0xb0000)`:

| So với stock H4 | Byte khác toàn file | Instruction tạo địa chỉ khác | Trỏ chuỗi giống nhau | Trỏ ngày build khác |
|---|---:|---:|---:|---:|
| H3 Global 3.304 | 26546 | 1220 | 1214 | 6 |
| H3 China 3.306 | 35185 | 1531 | 1525 | 6 |

Ngoài các instruction ADD/ADR/ADRP nói trên, chỉ có ba instruction khác tại `0x6a660`, `0x6a664`, `0x6a66c`. Chúng đổi predicate đọc marker DFD từ `marker == 0x454e454e` sang `marker != 0x44495341`. Các word khác ở vùng data `[0xd1800,0x103e00)` đều thay `-16` (99 word Global, 136 word China), phù hợp việc chuyển địa chỉ chuỗi. Đây là kiểm tra có giới hạn, không phải chứng minh toàn chương trình tương đương.

## Chính sách xác thực không đổi trong phạm vi đã chạy

Hàm `0x8cad8` và accessor `0x8cc18` chạy bằng Unicorn với query Secure Boot Control và device-state được cung cấp rõ ràng dưới dạng fixtures. Cả 18 hàng policy đều giống nhau giữa ba stock input. Hàng `lk` ở raw `0xed180` vẫn có bốn byte `01 00 03 02`.

**288 lời gọi native accessor** bao gồm mọi SBC fixture `0/1`, state `0/1/2/3/4/0xffffffff`, thành công/lỗi của hai query. Kết quả ba file giống nhau. Với query thành công và state enum `3/4`, SBC=0 cho auth-required=0; SBC=1 cho auth-required=1. Không tự gán enum này thành kết quả `fastboot unlocked`, cũng không suy ra SBC/efuse thật từ fixtures.

Preloader có đường tìm member `bl2_ext` rồi gọi generic loader (`0x6a7a4` → tìm member `0x7f424` → load `0x7eeec`), đã có evidence trong [nghiên cứu handoff trước](../stock_h4_preloader_review/handoff/preloader_handoff_disassembly.txt). Điều đó không tự chứng minh ENG BL2 mới qua được trust-root, rollback và policy thật. Không tìm được một điều kiện native đã kiểm chứng kiểu “stock H4 preloader bắt buộc main LK/AEE/DTB phải là H4”; cũng chưa chứng minh ngược lại rằng chúng luôn tương thích.

## Khác biệt DFD có tác dụng phần cứng thật

Hàm raw `0x6a578` đọc 52 byte ở đầu 4KiB cuối của `expdb`, kiểm word buffer+`0x24`. Marker đọc thành công:

| Marker số | H3 stock bật nhánh debug | H4 stock bật nhánh debug |
|---|---|---|
| `0` | Không | Có |
| `0x454e454e` | Có | Có |
| `0x44495341` | Không | Không |
| `0xffffffff` | Không | Có |

Partition thiếu, thiết bị lưu trữ thiếu hoặc đọc lỗi đều trả false ở cả hai phiên bản. Không diễn giải các magic theo thứ tự ký tự host nếu chưa nêu endian.

Caller `0x6a9d0` thực sự dùng kết quả đó để bỏ qua hoặc gọi `0x7f7c8`, `0x7f7fc`, `0x7f814`. Hai thông điệp tương ứng là `mcu&soc_dfd_debug_en` và `mfg_dfd_debug_en`. Các wrapper gọi RMW helper `0xa3f54`, không chỉ truyền một flag boottag sang LK:

- Đặt bit `0x40000` của thanh ghi `0x1800e0d8`.
- Đặt các bit `0x01000000`, `0x00400000`, `0x00100000`, `0x10000000`, `0x20000000` của thanh ghi `0x1800e0f0` theo thứ tự các wrapper.
- Sau đó về cùng continuation raw `0x6aa50`. Trong đoạn này không có so phiên bản LK hay chọn riêng AEE H3/H4.
- Có các nhánh fatal chung tại `0x6b4ac/0x6b4d8` nếu query debug-timeout bật **và** thanh ghi `0x1c01000c` có bit 29. Không phải cứ DFD bật là tự reset/fatal; cần thêm trạng thái timeout đó.

**66 case native** chạy chính reader, caller, wrappers và RMW helper của ba file. MMIO là RAM emulator, storage/query timing là fixtures. Kiểm marker, lỗi đọc/thiếu partition/thiếu device, debug-timeout và latch. [Disassembly](dfd_call_path.asm) ghi cả đường gọi.

DFD là khác biệt có khả năng ảnh hưởng hành vi debug/crash và đáng đối chiếu nếu có log cũ. **Chưa có bằng chứng nó là nguyên nhân ca H3 patched + stock H4 brick.** Không sửa marker expdb, không patch preloader hoặc LK để “fix” một giả thuyết chưa chứng minh.

## Kết luận dùng cho ma trận tương thích

Người dùng đã báo hybrid H4 `f57d19c9...` boot với stock H4; xác nhận đó thuộc đúng hybrid. Main payload/header/cert của ENG mới và phần AEE/DTB khác họ release, nên không chuyển kết quả boot này sang các ENG HWC/MTE image. BL2 H3 donor cũng không được gọi là cùng toàn bộ triple với mọi LK H3 patched chỉ vì payload/header giống; các chữ ký certificate có thể khác.

Đã loại bỏ được cách giải thích thiếu evidence rằng “H4 preloader đổi một flag bảo mật nên chặn mọi LK H3/ENG”. Chưa xác định đầy đủ ABI/handoff, root trust phần cứng, rollback hoặc nguyên nhân brick. Các kiểm thử này không đưa ra bảo đảm flash an toàn và không thay thế boot log ở cặp thất bại.
