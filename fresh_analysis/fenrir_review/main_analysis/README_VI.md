# Fenrir main LK: khóa giả ở nguồn trạng thái, mở riêng các chốt quyền

Input: `../input/dash-fenrir-versioned.bin`, SHA256 `dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113`. Chỉ so sánh và chạy các đoạn ARM64 trong emulator; không chạy binary trên host, không ADB/flash hoặc sửa firmware.

## Nền chính xác và danh sách thay đổi đầy đủ

Main Fenrir có declared size `0x139b28`, bằng main của `lk_hyperos3_original/lk.img` SHA `e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb`. Toàn bộ payload main chỉ khác **63 byte**, trong **19 word 4 byte**. Vì thế không cần đoán offset từ ENG mới: đối chiếu được trực tiếp với đúng stock H3 release. Dữ liệu cuối main, chữ ký/các member và trailer do phân tích container riêng xử lý.

Các offset sau tính từ main payload; whole-file = payload + `0x200`:

| Payload offset | Thay đổi | Hiệu ứng đã xác định |
|---|---|---|
| `0xa110`, `0xa124` | Hai conditional branch thành NOP | Bỏ hai chốt permission trong dispatcher trước khi gọi callback lệnh fastboot |
| `0xce04` | `cbz w0,0xce14` thành `b 0xce14` | Sau query trạng thái thành công, bỏ chốt flash-while-locked chỉ cho ngoại lệ `boot_para` |
| `0xd1d0` | `ldr w8,[sp,#4]` thành `[sp,#0xc]` | Chuyển nguồn word so sánh sang scratch đã zero, nhưng phép so sánh không còn quyết định nhánh vì patch kế tiếp |
| `0xd1d8` | `b.ne 0xd1e8` thành `b 0xd1e8` | Luôn đi qua chốt erase-while-locked tại đoạn đã thử |
| `0x68668` | `cset w22,eq` thành `mov w22,#1` | Luôn đặt bit allow-verification-error cho caller AVB này, dù nguồn trạng thái trả locked |
| `0x692fc` | `str w0,[x8,#0x990]` thành `str wzr,...` | Mọi lời gọi setter verified-boot-state đều lưu **0/green** vào RAM global `main+0x121990` |
| `0x88750..0x88764` | 4 lệnh ghi state4/return0, rồi 2 NOP | Common lock-state wrapper trả **locked4 + success0**; bỏ qua getter đọc/kiểm trạng thái phía dưới |
| `0xd9cec..0xd9d03` | Dữ liệu chuỗi build identity | Đổi tên/version hiển thị; không phải lệnh thực thi hoặc tính năng lock độc lập |

Các vùng khác trong main nguyên byte stock H3. [validation.json](validation.json) chứa old/new từng word; incidental disassembly của chuỗi build không được coi là code.

## Khác với fakelock v2 hiện tại

| Hành vi | Fenrir | ENG fakelock v2 |
|---|---|---|
| Nền binary | Stock H3 release main | Input debug/userdebug ENG mới `6f386241...` |
| Nguồn lock-state phần mềm mà caller thường dùng | Common wrapper ép `4/locked`, success | Getter ép `3/unlocked`, success |
| Fastboot `secure`/`unlocked` | Được tính tự nhiên từ locked boolean của wrapper; cache được publish `yes/no` | Lớp định dạng getvar công khai fake `yes/no`; giá trị cache nội bộ giữ theo nền unlocked |
| Public `seccfg_state` | Hàm gốc không sửa, in word do getter riêng trả về, không nhất thiết là1 | V2 ép phần định dạng public thành1, giữ word thực và báo lỗi đọc |
| Mở quyền dispatcher | NOP cả hai chốt thuộc tính quyền lệnh | Giữ cơ chế quyền gốc của nền ENG; không cùng kiểu bỏ cả hai chốt |
| Chốt flash/erase while locked | Bỏ riêng các nhánh đã nêu | Nền nội bộ unlocked nên các điều kiện unlock có thể qua theo logic gốc |
| Verified boot state nội bộ | Setter ghi luôn `0`, cả khi caller muốn ghi red/yellow/orange | Giữ state nội bộ thực của quá trình verify; fake giá trị xuất ở các điểm đã patch |
| Input yêu cầu xác thực image ngoài caller slot AVB | Native function `0x689cc` trả **1/required** từ locked4 | Getter unlocked3 làm predicate tương ứng trả 0 khi các phần khác không lỗi |
| Chính sách AVB caller | Ép bit allow-error riêng dù locked | Theo logic unlocked của nền ENG |
| Giữ cảnh báo lỗi red/yellow | Setter có thể che chúng khỏi hàm warning dùng cache | V2 chỉ bỏ orange warning; giữ red/yellow và đường fatal liên quan |

Điểm khác đáng học là **đưa locked/green vào nguồn dữ liệu chung**, giúp nhiều caller gốc và các báo cáo tự nhất quán, thay vì cần patch từng đầu ra. Tuy nhiên, nó là **trạng thái phần mềm giả**, không phải khóa thật trong bộ nhớ bảo mật, không tạo chữ ký OEM hoặc chứng thực phần cứng hợp lệ. Getter RPMB phía dưới vẫn là mã stock nhưng không được wrapper này gọi. Các routine ghi trạng thái khóa vẫn tồn tại; không được thử các lệnh lock/unlock với suy nghĩ file này không thể ghi trạng thái thật.

Không gọi Fenrir "bỏ AVB toàn bộ": một bit allow-error ở caller không bỏ format/I/O, mọi filter kết quả hoặc toàn bộ verifier. Predicate yêu cầu xác thực image khác còn trả required1. Các chốt lỗi trước query thành công trong hàm flash/erase vẫn nguyên. Dispatcher bỏ permission cũng không chứng minh mọi callback lệnh sẽ thực hiện thành công.

## Replay ARM64 trên chính bytes input

Chạy tại root repo: `python fresh_analysis/fenrir_review/main_analysis/probe_main.py`.

**189 ca PASS**:

- 100 giá trị đầu vào getter: code cuối thật đều ghi state4, trả0 và giữ ABI.
- 12 ca locked boolean/image-auth predicate, có stock controls ghi rõ state3/4.
- 6 ca native publish fastboot: locked4 → `secure=yes`, `unlocked=no`; stock unlocked control cho ngược lại; cover cache trống/có.
- 24 ca dispatcher: secure fixture, thuộc tính được chạy khi secure và thuộc tính cần unlock. Fenrir gọi callback ngay cả các tổ hợp stock locked phải bỏ qua.
- 16 ca chốt flash: target `boot_para`, `boot`, `init_boot`, `lk`; trạng thái locked/unlocked. Patch qua chốt với tất cả target; stock locked chỉ cho ngoại lệ `boot_para` tại chốt này.
- 12 ca chốt erase: word lock và scratch khác nhau, chứng minh nguồn scratch không còn quyết định quyền vì unconditional branch.
- 3 ca chọn cờ AVB: Fenrir wrapper locked4 nhưng flag1; state candidate3 sau đó được setter lưu0. Stock locked control cho flag0/state3; stock unlocked control cho flag1/state2.
- 16 ca setter + warning: các input0/1/2/3 và boot mode0/1. Fenrir lưu0 và không gọi warning/fatal sink qua hàm này; stock controls bảo toàn input, phát warning tương ứng và gọi fatal sink với state1/3.

Mã thật chạy từ binary, không sửa mã trong RAM. PAC được bỏ qua; logging, variable lookup/publish, strcmp, callback lệnh, chọn boot, UI/timing/fatal handler được mô hình hóa ở biên. Chỉ stock controls mới mock common getter để đặt state3/4; các ca Fenrir chạy wrapper thực.

Đây không phải phép thử flash/erase thực, AVB slot verify đầy đủ, TEE, OTA, Play Integrity hoặc boot thiết bị. Handoff secure-world là phạm vi phân tích riêng của đợt này. Tác động thay cache state lên các caller khác mới được suy từ xref/static code, không tự coi tất cả consumer đã được replay.

## Bổ sung: LKS và seccfg là hai báo cáo khác nhau

`python fresh_analysis/fenrir_review/main_analysis/probe_seccfg.py` cho **10 ca native bổ sung PASS**, lưu [seccfg_validation.json](seccfg_validation.json).

- Hàm public seccfg `0xc8f4..0xc993` nguyên byte stock. Getter riêng được gọi ở `0xc91c` → `0xb35b8`; tại `0xc930` nó nạp word vừa nhận, rồi định dạng `seccfg_state is %u`. Đây là cùng loại đầu ra v2 đã có patch ép1.
- Fixtures state0/1/3 và status0/7 chứng minh Fenrir in đúng word0/1/3; status lỗi vẫn gửi `Get seccfg_state failed.`. Nếu getter lỗi mà không ghi output, giá trị khởi tạo4 được in. Word getter không bị chức năng report sửa.
- Hàm LKS `0xc868` đọc common wrapper đã patch, luôn báo `lks = 1` trong ba trường hợp đối chiếu; nó **không gọi getter seccfg**. Do đó không dùng LKS=1 để suy mọi kênh báo trạng thái khóa đã thống nhất.

Đây là điểm Fenrir **chưa bao phủ bằng v2**, dù cách fake common lock-source của nó rộng hơn ở các caller khác. Các fixture không phải kết quả đọc phân vùng thật trên máy.

Main Fenrir không chứa literal `androidboot.flash.locked` bổ sung của v2. Kết quả tìm chuỗi này chỉ chứng minh không thấy đường literal đó trong main; không chứng minh property Android cuối cùng chắc chắn absent, vì Android hoặc nguồn bootargs khác có thể thiết lập nó.
