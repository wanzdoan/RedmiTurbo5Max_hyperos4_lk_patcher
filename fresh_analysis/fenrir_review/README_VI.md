# Mổ xẻ dash-fenrir-versioned.bin và so sánh fakelock v2

Ngày 2026-11-05. Theo yêu cầu người dùng, chỉ phân tích và chạy các đoạn mã ARM64 trong emulator. Không flash, không gọi ADB, không sửa input hoặc đưa patch vào bản của người dùng.

**Kết luận: Fenrir có cách giả trạng thái khóa rộng hơn và mở thêm cổng fastboot trong AEE; chưa có bằng chứng nó cải thiện chứng thực phần cứng so với v2.** Nó cũng đánh đổi việc giữ trạng thái lỗi boot thật: setter ghi luôn green vào RAM, kể cả khi caller muốn ghi trạng thái lỗi.

## Nhận diện đúng nền

- Input gốc: `C:/Users/Administrator/Downloads/dash-fenrir-versioned.bin`.
- Bản giữ nguyên byte trong repo: [input/dash-fenrir-versioned.bin](input/dash-fenrir-versioned.bin).
- SHA256: `dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113`.
- Dài 4.083.872 byte; 15 record MediaTek, sau đó 768 byte zero.
- Main trùng layout/size của `lk_hyperos3_original/lk.img`, chỉ **63 byte khác**. AEE chỉ **11 byte khác**. BL2, DTB/DTBO và các certificate tương ứng giữ nguyên H3 stock. Main/AEE cert2 thêm prefix 90 byte mỗi record, DER đã ký giữ nguyên.

Vì vậy đây là **patch trên nền H3 release đang có**, không phải cùng nền debug/userdebug `6f386241...` của bộ ba LK ENG hiện tại. Tên file hoặc chuỗi version đã đổi không chứng minh một build ENG, một cơ chế OTA hoặc chứng thực mới. Trailer zero không chứa metadata version bổ sung.

## Fenrir khác v2 thế nào?

| Hạng mục | Fenrir | Fakelock v2 hiện tại |
|---|---|---|
| Trạng thái khóa mà main thường đọc | Common wrapper trả `4/locked`, success0 | Getter trả `3/unlocked`, success0 |
| Trạng thái verified boot trong RAM | Setter luôn ghi `0/green` | Giữ trạng thái verify nội bộ; fake giá trị xuất ở những điểm đã patch |
| Fastboot `secure`/`unlocked` | Code stock tự publish `yes/no` từ nguồn locked giả | Lớp xuất getvar fake `yes/no`; nguồn nội bộ vẫn unlocked |
| Dữ liệu khóa/trạng thái boot gửi xuống secure monitor | Tự ra lock1/green0/hash khóa OEM qua code gốc | V2 đã patch để xuất lock1/green0/hash khóa OEM |
| Quyền dispatcher/flash/erase trong main | Bỏ các chốt riêng để vẫn thao tác dù main báo locked | Giữ cơ chế quyền gốc, dựa vào trạng thái nội bộ unlocked |
| Fastboot trong AEE | Sửa thêm bốn lệnh để bỏ hai gate dispatcher và nhánh deny do locked | AEE giữ nguyên từ input debug/userdebug |
| Cảnh báo lỗi red/yellow dùng cache | Setter ép green có thể che lỗi khỏi hàm warning đã thử | Giữ red/yellow và đường xử lý lỗi liên quan; chỉ bỏ orange warning |
| Một predicate yêu cầu xác thực image khác | Vẫn trả required1 từ locked4 | Trả 0 theo unlocked3 trong các điều kiện đã kiểm |
| Public `seccfg_state` so với `lks` | Hàm seccfg giữ nguyên stock và in trạng thái getter riêng trả về; có thể khác `lks=1` | V2 đã fake riêng public seccfg thành1 |
| `androidboot.flash.locked=1` | Không thêm literal/patch này như v2 | V2 xuất rõ giá trị này; không suy từ Fenrir không có literal rằng Android chắc chắn không có property tương ứng |
| HWC tự chọn vùng và lựa chọn MTE-off của bộ sáu file | Không có các patch đó trong diff Fenrir | Đã có ở các biến thể riêng mới của người dùng |

### Điểm đáng học

**Giả ở nguồn trạng thái chung:** locked4 khiến nhiều caller gốc tự đồng thuận, thay vì phải fake từng giá trị báo cáo. Green cũng được ghi ngay ở cache mà các caller gốc đọc. Đây là khác biệt thiết kế thực, không chỉ đổi chữ fastboot.

**Bao phủ thêm AEE:** có thể hữu ích nếu cần một số lệnh ở fastboot của nhánh xử lý lỗi/crash. Tuy nhiên các probe chỉ đến gate/callback boundary, chưa chạy ghi partition thật. Bỏ kiểm quyền rộng hơn cũng có nghĩa là các hạn chế gốc không còn được giữ ở các điểm đó.

### Những điểm không vượt v2 hoặc phải đánh đổi

**Root of Trust chưa thấy nội dung cải tiến:** 160 ca stock H3/Fenrir và 20 ca v2 kiểm được mã setter, chuỗi copy state, common wrapper, SHA256 phần mềm thật và lắp năm packet. Trong 80 đối chiếu Fenrir-v2, hash khóa, lock1 và state0 đầu ra đều khớp nhau dưới cùng fixture. Fenrir không thêm khóa riêng hay secure-world mới vào container.

Khác biệt là Fenrir đã xóa state lỗi trong RAM trước khi gửi; v2 giữ state nội bộ rồi fake đầu ra. Khi đưa state1/3 vào setter, Fenrir biến chúng thành0 và hàm warning không còn gọi đường cảnh báo/fatal như stock control trong probe. Đây không phải sửa chữ ký sai hay làm ảnh ROM thành verified thật. Không suy rằng mọi lỗi đều được bỏ qua: verifier còn giữ các filter/format/I/O, và predicate image-auth-required khác vẫn bằng1.

**Một kênh v2 bao phủ tốt hơn:** thêm10 ca native cho thấy Fenrir giữ nguyên hàm public seccfg, in word thực từ một getter khác. Với fixture state0 hoặc3, nó in0 hoặc3 dù LKS đã báo1. V2 có patch riêng ép giá trị public này thành1. Điều đó không phải thay trạng thái seccfg thật; cũng cho thấy ép common wrapper không bảo đảm mọi kênh trạng thái đều đồng thuận.

**AEE không đồng nghĩa TEE:** AEE ở đây là thành phần xử lý lỗi/crash; các patch tìm thấy là cổng lệnh fastboot của nó. Đây không phải bằng chứng sửa môi trường bảo mật hoặc tạo chứng thực hợp lệ.

**Prefix 90 byte chỉ gọn cách đóng gói:** Fenrir dùng một block hash mới, thay vì prefix duplicate180 byte của builder v2. H3 BL2 chọn được hash mới trong probe; H4 BL2 vẫn chọn hash cũ trong DER đã ký. Không coi prefix đơn là chữ ký OEM mới hay bằng chứng tương thích mọi preloader.

## Offset và kiểm chứng

Các offset main tính từ sau header512 byte; offset AEE tính riêng từ đầu payload AEE. Không dùng các offset này cho nền debug/userdebug của v2.

| Main payload | Thay đổi |
|---|---|
| `0x88750..0x88764` | Ép common lock wrapper trả state4/success0, bỏ qua getter bên dưới |
| `0x68668` | Luôn đặt bit allow-verification-error tại caller AVB đã xét |
| `0x692fc` | Setter verified-boot-state luôn ghi zero |
| `0xa110`, `0xa124` | NOP hai nhánh gate quyền command-dispatch |
| `0xce04` | Nhảy qua chốt flash-while-locked tại điểm này |
| `0xd1d0`, `0xd1d8` | Đổi load scratch và ép nhánh qua deny erase-while-locked |
| `0xd9cec..0xd9d03` | Đổi dữ liệu build identity; không phải mã thực thi |

AEE thay `0x13be8`, `0x13bfc`, `0x16498`, `0x164a0`. Xem bảng old/new, fixture và giới hạn trong [phân tích main](main_analysis/README_VI.md) và [phân tích chain/AEE](chain_analysis/README_VI.md). [Mô hình logic C](recovered_logic.c) là diễn giải của người phân tích, không phải source gốc hoặc firmware có thể build để flash.

Chạy tại root repo:

```powershell
python fresh_analysis/fenrir_review/main_analysis/probe_main.py
python fresh_analysis/fenrir_review/main_analysis/probe_seccfg.py
python fresh_analysis/fenrir_review/chain_analysis/analyze_chain.py
python fresh_analysis/fenrir_review/probe_root_handoff.py
```

| Bằng chứng | Kết quả |
|---|---:|
| Main: wrapper, lock/auth, publish, dispatcher, flash/erase gate, AVB flag, setter/warning | 189 ca native |
| Main bổ sung: seccfg và LKS | 10 ca native |
| AEE: dispatcher và gate flash | 80 ca native |
| Root handoff stock H3/Fenrir | 160 ca native |
| Root handoff của v2 hiện có để đối chiếu | 20 ca native |
| Parser BL2 H3/H4 với main/AEE cert | 12 ca native |
| DER gốc xác thực bằng khóa nhúng | 10 chữ ký |

[root_handoff_validation.json](root_handoff_validation.json) ghi thêm80 đối chiếu packet đã nêu; đó là đối chiếu các case đã chạy, không cộng thêm80 lần thực thi CPU. Mỗi probe ghi rõ boundary mocks; đặc biệt secure monitor là sink giả, trạng thái backing/RPMB, key extraction và IO là fixtures. Không chạy kernel, TEE, SoC hoặc toàn bộ boot.

REA MCP trên Windows hiện không có provider ARM64 đã cấu hình cho target này; phân tích dùng Capstone, cryptography và Unicorn cài sẵn. Không đổi cấu hình MCP hoặc cài thêm công cụ. Không coi kết quả parser/chữ ký hoặc emulator là xác nhận boot, OTA hay Play Integrity/VNeID.

## Kết luận cho công việc tiếp theo

Không tự đưa các patch này vào v2. Hướng có thể nghiên cứu riêng là tăng tính nhất quán giữa các nguồn báo trạng thái và đánh giá nhu cầu AEE; phải giữ được quan sát lỗi thật và kiểm lại mọi caller chịu ảnh hưởng nếu đổi nguồn trạng thái khóa chung. Không mặc định cách ép green toàn bộ là một cải tiến nên sao chép.

Fenrir cũng không có patch phần khởi tạo/handoff để chứng minh chữa ca stock H4 preloader + LK H3 đã báo brick trước đó. Nó tiếp tục dùng H3 release BL2 và các thành phần H3; tính tương thích stock preloader còn phải phân biệt với các chức năng fakelock trong main.

Checkpoint trước nghiên cứu: `before-fenrir-fakelock-analysis-20261105` tại `53e9a62`. Input, firmware người dùng đã boot, sáu LK hiện hành và ZIP bàn giao giữ nguyên byte.
