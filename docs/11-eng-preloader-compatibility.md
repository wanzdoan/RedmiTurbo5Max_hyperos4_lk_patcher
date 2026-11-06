# LK ENG và preloader stock H3/H4

Ngày 2026-11-05. Người dùng mở lại nghiên cứu tương thích preloader, yêu cầu kiểm tra bằng file. Phạm vi chỉ đọc/phân tích offline; không flash, reboot, lock hoặc đổi cấu hình máy. Không sửa firmware trong đợt này.

## Kết luận sử dụng

**Chưa đủ bằng chứng để xác nhận sáu LK ENG mới boot được với preloader stock H3 hoặc stock H4.** Đã kiểm được các điều kiện về định dạng, chứng chỉ và hash; chưa xác định được lệnh/điều kiện đã gây brick ở tổ hợp LK H3 patched cũ + stock H4. Không gán một nguyên nhân chưa chứng minh cho ca đó.

| LK | Preloader H3 cũ/ENG người dùng từng giữ | Stock H3 đúng file đang lưu | Stock H4 của ROM OS4.0.3.0 |
|---|---|---|---|
| H3 patched cũ `lk_hyperos3_patched_unlockBL/dash.img` | Người dùng báo chạy được | Chưa xác nhận đúng hash preloader của ca chạy | Người dùng báo brick |
| H4 hybrid `f57d19c9...` | Người dùng báo boot với ENG H3 | Chưa xác nhận riêng | Người dùng báo test thay stock H4 được |
| ENG unlocked + HWC, cả default/off | Chưa xác nhận hai artifact mới | Chưa xác nhận boot | Chưa xác nhận boot |
| ENG fakelock v2 + HWC, cả default/off | Chưa xác nhận hai artifact mới | Chưa xác nhận boot | Chưa xác nhận boot |
| ENG stock-policy + HWC, cả default/off | Chưa xác nhận hai artifact mới | Chưa xác nhận boot | Chưa xác nhận boot |

Các ô runtime là báo cáo của người dùng, không phải kết quả flash do công cụ trong đợt này thực hiện. Chỉ xác nhận đúng tổ hợp đã được báo, không mở rộng sang file khác cùng tên H3/ENG.

## Đúng ROM và đúng file

ROM được yêu cầu là `D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0`.

| Input | SHA256 |
|---|---|
| ROM `images/lk.img` | `82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b` |
| ROM `images/preloader_dash.bin`, cũng là `Preloader/preloader_dash_4.403_china.bin` | `104bb972383b39771ec5bcc4bc79207244306de1d40f370ed2ab3863fb7ceb77` |
| Stock H3 China `Preloader/preloader_dash_3.306_china.bin` | `31748701c84f7cb3890f7880cfb10f90f90dff815b1424bd2de116a22fb96e38` |
| Stock H3 Global `Preloader/preloader_dash_3.304_global.bin` | `4f43d7200acdf2152aeaa25f07a47b54afaad116087b89e28f043d264d35eb68` |
| File trong `preloader_hyperos3/preloader_dash.bin`, trùng file Downloads người dùng gọi ENG | `bcde516c37a8845287cc57441780391e89c16c51eabfd4c5e3c625ecbdf4ad8b` |

Tên thư mục `preloader_hyperos3` không chứng minh đây là stock H3 China/Global đang xét. Các script flash trong ROM dùng **`images/preloader_dash.bin`**, ghi cả `preloader_a` và `preloader_b`, rồi dùng `images/lk.img` cho `lk_ab`. Xem [evidence script](../fresh_analysis/preloader_compatibility_20261105/rom_flash_script_evidence.json); các dòng lệnh chỉ được đọc, không được thực thi.

## Bản H4 đã từng patch thành công nằm ở đâu?

[lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img](../fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img), SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`.

Nó gồm main H4 patch getter 16 byte và prefix hash, bộ BL2 + certificate từ **H3 original**, AEE và cây thiết bị H4. Đây là bản hybrid đã được người dùng báo chạy, không phải LK ENG mới và không phải thuần H4. Xem [logic](01-logic-and-patch.md) và [runtime report](04-runtime-and-preloader.md).

BL2 là bộ nạp trung gian nằm **bên trong file LK**; nó chạy trước main LK. AEE là thành phần phục vụ xử lý lỗi/crash. DTB/DTBO là dữ liệu mô tả phần cứng. Giữ các thành phần H4 trong bản hybrid là một khác biệt thực tế, nhưng chưa có bằng chứng riêng rằng một trong số đó chính là phần đã sửa ca brick.

## Các điểm logic đã phân biệt được

### Không nhầm thay đổi parser BL2 H4 với thay đổi preloader H4

Chuỗi đã thấy trong mã: preloader tìm/nạp `bl2_ext` từ container LK; bộ nạp đó kiểm và nạp main `lk`; sau đó main mới chạy getter unlock, HWC và MTE init.

Parser của **BL2 H4** có thay đổi giới hạn vùng đọc certificate, khiến cách thêm prefix hash của H3 không có cùng tác dụng. Đây là lý do bản H4 hybrid giữ BL2 H3. Tuy nhiên, ca **LK H3 patched cũ + preloader H4** vẫn mang BL2 H3 trong chính file LK H3; không tự biến thành BL2 H4. Không dùng kết quả parser H4 đó làm lời giải trực tiếp cho ca brick này.

Giữa H3 patched cũ và H4 hybrid: payload BL2, header BL2 và cert1 giống nhau. Cert2 có phần dữ liệu được ký giống nhau nhưng chữ ký RSA-PSS khác; cả hai chữ ký đều xác thực được. Bộ ba của hybrid nguyên byte từ H3 original; không mô tả bộ ba hybrid nguyên byte từ H3 patched cũ.

### Chưa thấy hàng rào khóa ký mới đối với BL2 ENG

BL2 ENG mới khác mã BL2 H3 release. Tuy vậy cert1 cùng khóa gốc và khóa ký image, phần dữ liệu chính sách trong cert2 không tạo ra khác biệt được chứng minh về vùng/phiên bản. Chữ ký và hash header/payload xác thực được với các khóa nhúng. Vì vậy không có cơ sở kết luận stock H4 chắc chắn từ chối BL2 chỉ vì nhãn ENG.

Kiểm bằng khóa nhúng không phải đọc efuse, trạng thái rollback hay chứng minh toàn bộ boot trên máy. Không đổi kết quả kiểm mật mã thành kết luận có thể flash an toàn.

### Sáu bản ENG có cùng nền tương thích ở tầng trước main

Cả sáu giữ nguyên BL2/certificate của BL2, AEE, DTB và DTBO của ENG input `6f386241...`. Các khác biệt unlock/fakelock/stock-policy/HWC/MTE-off nằm trong main và prefix tương ứng. Vì vậy patch HWC hoặc MTE-off **không tự sửa** lỗi nạp/xác thực BL2 hay lỗi trước khi main chạy.

Sau khi main chạy, ba nhánh lock/reporting vẫn có hành vi khác nhau; nhất là stock-policy không force-unlock. Không suy ra một nhánh boot được thì mọi nhánh còn lại sẽ boot cùng ROM/trạng thái dữ liệu.

### Khác biệt DFD cần được kiểm tiếp, không coi là nguyên nhân đã chốt

Trong vùng mã preloader đã đối chiếu, ba lệnh thay đổi ngoài nhóm tạo địa chỉ nằm ở `0x6a660`, `0x6a664`, `0x6a66c`. Hàm đọc dữ liệu cuối phân vùng `expdb`; predicate H3 là `word == 0x454e454e`, H4 là `word != 0x44495341`. Một số giá trị như zero khiến kết quả khác nhau. Đây là nhánh xử lý crash/DFD, không phải bằng chứng H4 tăng xác thực LK.

Replay 66 ca native của reader và caller xác nhận H4 có thể bật DFD với marker zero, trong khi H3 không bật ở cùng fixture. Nếu đồng thời cờ debug timeout và trạng thái timeout đã latch đều bật, caller có nhánh đi vào fatal handler. Nếu không gặp các điều kiện đó, các ca đã thử trở về continuation boot chung. Đây là một khác biệt thực có thể kiểm tra, **không phải chứng minh ca brick đã đi vào fatal handler**.

Chưa có giá trị `expdb` và log thời điểm ca brick để kết luận nhánh đó đã chạy trên máy gặp lỗi. Các quan sát địa chỉ/header chung cũng chưa chứng minh toàn bộ giao diện bàn giao bộ nhớ, dữ liệu boot và AEE tương đương.

## Kiểm chứng và bằng chứng tái tạo

- [So sánh preloader](../fresh_analysis/preloader_compatibility_20261105/evidence.json): đúng hai stock H3 và stock H4, bảng policy và 288 lời gọi accessor ARM64 với trạng thái phần cứng được mô hình hóa cho kết quả giống nhau. Có thêm 66 ca DFD/caller; không thực thi fatal handler trên phần cứng.
- [So sánh họ LK](../fresh_analysis/eng_family_compatibility_20261105/README_VI.md): 10 chữ ký BL2, 5 cặp signed header/payload hash đạt; sáu bản ENG giữ nguyên 12 record ngoài main/cert của main.
- Script tái tạo: `python fresh_analysis/preloader_compatibility_20261105/review_preloaders.py` và `python fresh_analysis/eng_family_compatibility_20261105/compare_families.py`. Các script không flash và không sửa firmware. Policy test mock các truy vấn trạng thái phần cứng; DFD test mock storage/guard/log và ánh xạ MMIO vào RAM emulator. Không coi đây là mô phỏng toàn bộ boot.

Ngoài ra, file preloader ở **root ngoài** của thư mục giải nén ROM có SHA256 `75e93936bd14914d25380767f33e55f40aefc77db5cbfaa606af4e6641591c35`, khác `images/preloader_dash.bin` stock. File root đó khớp nguyên byte cửa sổ bắt đầu `0x1000` của boot-LUN đã thu thập trước đây. Đây là nhận diện từ dữ liệu đã có, không có lần đọc máy mới trong đợt này. Không dùng file root đó để thay tên cho stock preloader mà flash script thực sự chỉ tới.

## Điều còn thiếu để chốt nguyên nhân

Thiếu log của lần lỗi cho thấy đã qua bước nào: preloader nạp/xác thực BL2, BL2 nạp main/AEE, hay main khởi tạo phần cứng/Android. Cũng chưa có trạng thái crash/DFD và toàn bộ thành phần bảo mật của chính lần lỗi. Không tái tạo ca brick bằng cách flash trong đợt kiểm tra này.

Kết quả bàn giao là bản đồ bằng chứng và giới hạn trên, không phải xác nhận stock H3/H4 đã tương thích với sáu file ENG. [Bộ sáu file](../experiments/lk_h3_eng_mte_off_v1/README_VI.md) tiếp tục giữ trạng thái chưa boot test; không sửa ảnh hoặc gắn nhãn đã chạy từ các probe offline.

## 2026-11-06: native audit đúng preloader H4 với LK mới

Xem [báo cáo và giới hạn](../fresh_analysis/h4_preloader_lk_acceptance_20261106/README_VI.md). Bảy ảnh gồm H4 unlocked đã boot, ENG gốc, hai H3 stock-policy, hai H4 stock-policy mới và H4 gốc. Locator, ASN.1/key comparison/PSS cert2, helper/comparator header/payload hash đạt offline với khóa cert1 tham chiếu; đối chứng lỗi bị phát hiện. Không mock kết quả chữ ký, nhưng SHA/RSA primitive dùng toán học host và khóa root thiết bị/rollback/handoff chưa đo. Không kết luận flash preloader H4 hoặc boot thật đã thành công. Firmware nguyên byte.
