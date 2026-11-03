# Reverse và patch LK HyperOS 4 — kết quả mới từ đầu

Ngày: 2026-11-03. Công việc mới nằm trong `fresh_analysis`; không dùng kết luận hoặc patch cũ của dự án.

## Kết quả bàn giao

File thử nghiệm: **`output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img`**

- Kích thước: **4.083.424 byte**.
- SHA256: **`f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`**.
- Main LK/BL33, AEE, DTB và DTBO: HyperOS 4.
- BL2_EXT cùng hai certificate: lấy nguyên vẹn bản HyperOS 3 đã ký.
- Main LK: sửa đúng16 byte hàm unlock, thêm180 byte certificate prefix theo cơ chế quan sát ở bản H3 patched.

**Đây là bản hybrid để thử nghiệm, chưa phải bản thuần HyperOS 4 và chưa được boot trên điện thoại.** Đã kiểm chứng logic ARM64, cách parser chọn hash và chữ ký các DER giữ nguyên. Chưa chứng minh handoff H3-BL2/H4-BL33, rollback/fuse hoặc stock-preloader-H4 tương thích trên máy thật. Không thể cam kết “hoàn hảo/không brick” bằng các kiểm thử này.

Không tạo bản sửa H4 BL2 rồi giả vờ chữ ký vẫn hợp lệ: thay payload BL2 sẽ làm sai digest mà preloader xác thực. Việc dùng nguyên signed H3 BL2 là lựa chọn có bằng chứng về chuỗi chữ ký; khả năng tương thích phần cứng giữa phiên bản vẫn chưa được kiểm tra.

## Phát hiện quyết định: H4 đã chặn cách prefix cũ trong BL2

Chuỗi tải được xác định từ binary:

`preloader -> bl2_ext -> main LK/BL33 -> Android`

Preloader có hàm file0x6a7a4 chọn member `bl2_ext` rồi gọi loader0x7eeec. Trong BL2 H4, entry payload0x9174 tải partition/member `lk` qua loader0x50eb8. Loader hỏi chính sách xác thực0x5b018 ->0x5b0f8. Getter unlock ở tầng BL2 là stub lỗi4; caller trả auth_required=1 khi getter lỗi. Vì vậy patch getter ở main LK không thể tắt bước xác thực diễn ra trước khi main LK chạy.

Khác biệt H3/H4 nằm ở parser cert2:

| Thành phần | HyperOS 3 | HyperOS 4 |
|---|---|---|
| Parser BL2 | payload0x6cf8c | payload0x6d6bc |
| Phạm vi tìm hash | Toàn cert2 | TBS bên trong certificate |
| Certificate có prefix ngoài DER | Lấy hash prefix | Bỏ qua prefix, lấy hash trong TBS |

H4 gọi thêm helper0x6cdc4 xác định TBS trước khi tìm OID. Routine xác minh đầy đủ cũng bổ sung reset con trỏ/độ dài trước signature verification. Thay đổi tương tự có trong main LK H4. Đây là cơ chế mới có bằng chứng thực sự; không phải chỉ khác địa chỉ/build string.

Replay ARM64 của parser trên chính certificate bàn giao:

- H3 BL2 lấy hash payload H4 đã patch: `848759443d2c436306e77df20bdcf87856fec4cb8806386c670db7a239385373`.
- H4 BL2 lấy hash signed payload H4 gốc: `db3e02a56f7e114b33631376128ef997c931f1111af0cf0f97f2aeb75f13a8e4`.
- Hai parser đều có thể trả status0: status này chỉ nghĩa parse thành công, không nghĩa ảnh được xác thực thành công.

Do đó **bản chỉ patch main H4 + prefix nhưng giữ BL2 H4 bị loại**. File đó được chuyển vào `rejected/lk_h4_main_only_REJECTED.img` chỉ để tái kiểm chứng; không phải file bàn giao để nạp.

Bằng chứng: `container_chain/BL2_FINDINGS.md`, `preloader/bl2_parser_emulation.json`, `preloader/hybrid_candidate_parser_validation.json`, `h3_diff/BL2_LOADER.md` và `h3_diff/bl2_policy_emulation.json`.

## Patch unlock chính xác

Input H4 bắt buộc SHA256 `82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b`.

Getter `mi_get_lock_state` H3 ở whole-file0x89a2c; hàm tương ứng H4 ở **whole-file0x89cec** (VAffff000090789aec). Thay16 byte:

```text
Cũ: 3f2303d5ff4305d1fd7b12a9fc5713a9
Mới: 68008052080000b900008052c0035fd6
```

Tương đương:

```c
// Diễn giải mã máy đã quan sát, không phải source gốc.
int mi_get_lock_state(uint32_t *state) {
    *state = 3;
    return 0;
}
```

Chuỗi H4 wrapper0x88c10 ->custom wrapper0x89220 ->getter0x89cec được đối chiếu bằng disassembly và pseudocode. Các caller đã kiểm chứng dùng state3 cho unlocked:

- Fastboot công bố `unlocked=yes`, `secure=no`.
- Callback AVB trả is_device_unlocked=true.
- Bootarg `androidboot.vbmeta.device_state=unlocked`.
- AVB bật allowance cho một số lỗi xác thực; không biến mọi ảnh hỏng/thiếu partition/lỗi I/O thành ảnh boot được.

Có cùng16 callsite trực tiếp tới wrapper trong scan H3/H4 đã xét. H4 có fallback getter-error mới, nhưng getter patch trả0 nên không vào nhánh lỗi đó trên các đường đã kiểm tra. Chưa xác lập critical-unlock độc lập; biến ở gần là `fuse`. Patch không ghi unlock lưu bền vào RPMB; behavior này phụ thuộc LK patch tiếp tục được dùng.

Xem `h3_diff/REPORT.md` và `h3_diff/h4_recovered_semantics.c`.

## Container, chữ ký và lựa chọn hybrid

Mỗi LK image chứa15 record: năm bộ component/cert1/cert2 cho `lk`, `bl2_ext`, `aee`, `lk_main_dtb`, `lk_dtbo`; header512 byte, căn16 byte.

Bản H3 patched dựa trên donor khác build, không chỉ cùng file gốc cộng vài byte: build string khác, AEE và DTBO khác. DTBO có34 thay đổi chủ yếu cấu hình sạc. BL2_EXT H3 original và patched lại giống hoàn toàn. Vì vậy không port toàn bộ diff donor sang H4.

Cert2 H3 patched thêm hai block A0 giống nhau, mỗi block90 byte, trước DER đã ký. Hash OID .2.4 là SHA256(header512); OID .2.1 là SHA256(payload padded16). Prefix chứa hash payload đã patch; DER ký giữ hash donor trước patch. Khôi phục chỉ16 byte prologue trong donor H3 tái tạo đúng hash signed. Không có bằng chứng patcher có private signing key.

Hybrid giữ nguyên bộ BL2 H3 **cả header, payload, padding và hai certificate**, không sửa byte nào trong bộ đó. Giữ nguyên10 record H4 ngoài main/cert2 và bộ BL2; các record AEE/DTB/DTBO đều nguyên H4. Main H4 chỉ đổi16 byte. Main cert2 có prefix180 byte và cập nhật kích thước/căn lề.

Tất cả10 DER trong hybrid giữ chữ ký RSA-PSS SHA256 đúng về toán học dưới public key nhúng. Hash signed của main cố ý không khớp payload sửa; việc chấp nhận dựa vào lựa chọn hash của H3 BL2. Các payload khác khớp digest signed. Kiểm tra khóa nhúng không thay thế xác nhận trust root/rollback của thiết bị.

## File preloader ENG và khác biệt preloader

File người dùng cung cấp `C:/Users/Administrator/Downloads/preloader_dash.bin` giống từng byte với `preloader_hyperos3/preloader_dash.bin`: 1.064.924byte, SHA256 `bcde516c37a8845287cc57441780391e89c16c51eabfd4c5e3c625ecbdf4ad8b`.

Preloader H4 SHA256 `104bb972383b39771ec5bcc4bc79207244306de1d40f370ed2ab3863fb7ceb77`. Có9.781byte khác. Trong vùng code đã scan, thay đổi lệnh không phải địa chỉ tập trung ở predicate marker DFD/expdb. Đây không phải bằng chứng preloader có anti-unlock mới. Có trailer/data chưa phân loại đầy đủ.

Parser xác thực preloader H3/H4 được replay có giới hạn: trên đường signature-success, cả hai lấy digest trong TBS, không lấy prefix ngoài DER. Vì vậy sửa BL2 H4 rồi thêm prefix tương tự không tự giải quyết lớp preloader. File ENG trùng H3 là quan sát file PC; chưa đọc lại partition máy để xác nhận binary đang chạy thực tế.

Xem `preloader/FINDINGS.md`, `comparison.json`, `parser_emulation.json`.

## Kiểm thử và mức chứng minh

| Kiểm tra | Kết quả | Giới hạn |
|---|---|---|
| Getter/wrappers/AVB ARM64 thật |328 ca PASS | Logging/chọn boot partition được stub; không toàn SoC |
| H3/H4 parser BL2 với input 3 phiên bản/mask |18 ca hoàn tất | Chỉ chọn hash, không toàn chain |
| Parser trên cert của đúng hybrid cuối |6 ca PASS | H3 lấy hash sửa, H4 lấy hash gốc; PAC/logging được mô hình hóa |
| Cert2 preloader H3/H4 | TBS selection xác nhận | Một số primitive key/RSA được mock có ghi rõ |
| Policy BL2 H4 | Auth required1 trên các input thử | SBC/helper được mô hình hóa; không máy thật |
| DER của3 input |30 chữ ký PASS | Public key nhúng, không fuse root |
| Hybrid cuối |10 DER PASS, non-main digest đúng | Main phụ thuộc cơ chế parser H3 |
| Tái tạo hybrid | Cùng bytes/SHA256 | Cùng input bắt buộc |

`hybrid_validation.json` gắn danh tính file cuối với đúng code328 ca đã chạy và snapshot certificate đã test. Không sửa main sau kiểm thử. Không có bước flash, nạp preloader hoặc ghi partition nào được thực hiện.

## Source phục hồi và nơi xem

Ghidra 12.1.2 từ cài đặt sẵn `D:/Tools/ghidra` đã xuất:

- Main H4 LK:2.328 hàm nhận diện, `h4_pseudocode`.
- H4 BL2_EXT:1.526 hàm nhận diện, `container_chain/bl2_decompile`, kèm focused exports cho entry loader chưa được auto-recognize.
- H4 AEE:1.827 hàm nhận diện, `h3_diff/aee_analysis/pseudocode`.
- Preloader: focused export220 hàm, `preloader/decompile_h4`.

Tổng ba payload native H4:5.681 file pseudocode theo các hàm tool nhận diện. Có index functions.tsv, database mới và log. Một số target trong vùng dữ liệu bị auto-analysis nhận nhầm/cảnh báo; export thành công không chứng minh mọi hàm/boundary đúng. Đây là pseudocode suy ra, **không phải full source gốc**: kiểu/tên biến, macro, comments và cấu hình build đã mất. ELF carrier chỉ dùng phân tích, không nạp vào máy.

Các thay đổi H4 khác đã rà gồm metadata fastboot, bootargs, dm-verity clear, dconfig và error handling; chưa có bằng chứng cần patch thêm ở các đường main unlock/AVB đã xét. AEE getter là stub lỗi4 khác main; không patch AEE vì chưa có nhu cầu có bằng chứng.

## Tái tạo và kiểm chứng

Chạy từ thư mục gốc dự án, với Python và các module capstone/unicorn/cryptography đã có tại lúc phân tích:

```powershell
python fresh_analysis/build_hybrid.py
python fresh_analysis/validate_candidate.py
python fresh_analysis/validate_hybrid.py
python fresh_analysis/preloader/validate_candidate_parser.py
```

`build_candidate.py` chỉ tái tạo đối chứng thuần H4 bị loại; đầu ra mặc định nằm trong `rejected`. Bản để đánh giá là file hybrid duy nhất trong `output`. Script không giao tiếp thiết bị.

## Phần chưa giải quyết

- Chưa boot hybrid trên điện thoại, chưa xác nhận boot/recovery/fastboot sau nạp.
- Chưa chứng minh H3 BL2 handoff tương thích H4 BL33 và stock H4 preloader trên trạng thái fuse/RPMB hiện tại.
- Chưa có phương án thuần H4 BL2+BL33 vừa sửa unlock vừa vượt các lớp xác thực đã nêu. Cần signing authority hoặc cơ chế khác được chứng minh; thêm prefix cũ là không đủ.
- Chưa kiểm chứng toàn SoC/BootROM/TEE/UFS/A-B/ROM đầy đủ hoặc mọi đường lỗi.

Việc bàn giao hybrid là kết quả thử nghiệm có căn cứ, không đánh dấu các câu hỏi phần cứng hoặc mục tiêu “thuần H4 hoàn hảo” là hoàn tất.
