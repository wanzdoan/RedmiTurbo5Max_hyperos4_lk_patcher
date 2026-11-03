# 08 — Chốt vùng ROM trong LK và giới hạn của nghiên cứu 5G

Ngày 2026-11-03. Người dùng yêu cầu chỉ sửa LK, không dùng module/đổi thành phần khác. Đã tạo [bản thử LK riêng](../experiments/lk_global_region_v1/README_VI.md) để bỏ các chốt ROM CN/Global đã định vị. **Chưa có xác nhận boot bản này, chưa có ROM Global đích, chưa có fix 5G được chứng minh.**

## Kết quả mới về 5G

Đã theo thông tin modem từ binary đến mã kernel đọc nó, thay vì suy từ tên trường. H4 modem CHECK_HEADER tại file0x2950000 có image_type13. LK `ffff00009072e484` truyền số này qua tag `md_capability`. `ccci_util_lib.ko` trích trực tiếp từ vendor_boot đúng hash chứa bảng `md_img_capability_map`: type13 → `0x7b`. Hàm `get_capability_bit` định nghĩa `N`/`n` → `0x40`, đã nằm trong0x7b. Chưa chứng minh hạn chế 5G nằm ở LK; sửa descriptor13→14 chỉ thêm C, không thêm NR đang thiếu.

Xem [kernel_capability.json](../fresh_analysis/nr5g_research/lk_causal/kernel_capability.json), [nguồn/cách trích](../fresh_analysis/nr5g_research/lk_causal/extract_handoff_inputs.py), [pseudocode kernel](../fresh_analysis/nr5g_research/lk_causal/ccci_util/functions.tsv). Đây là năng lực được mô tả, không phải trạng thái cho phép băng tần/nhà mạng hoặc đăng ký NR.

LK có truyền `androidboot.ptcountrycode`; một consumer thật là import property theo vùng trong **recovery**. Chưa có nội dung ODM property và normal-boot property loader để khép kín đường LK→vùng Android→chính sách radio. Không đổi text countrycode dựa trên giả định nó sẽ mở 5G.

## Ba chốt vùng được kiểm tra

1. Hàm [ffff000090768d1c](../fresh_analysis/h4_pseudocode/ffff000090768d1c.c) đọc `com.android.build.init_boot.sku` qua [ffff0000907c4f6c](../fresh_analysis/h4_pseudocode/ffff0000907c4f6c.c). Khi secure boot bật, CN hardware chỉ qua với prefix CN/NATIVE. Dconfig hợp lệ có thể bypass; getter unlocked không bypass block này. Patch nhánh payload0x6904c từ `cbnz w0,panic` thành `b continuation0x6905c`, không return sớm cả hàm và không thay logic EP/MP phía trước.
2. Hàm [ffff000090788734](../fresh_analysis/h4_pseudocode/ffff000090788734.c) giữ xác thực certificate chain, lấy public key ảnh256 byte rồi chọn khóa theo efuse vùng. Patch block mismatch payload0x888d0 thành so thêm đúng khóa GL tại payload0x1122d7, trả0 nếu khớp và-1 nếu không khớp. Trên CN hardware, khóa CN đi nhánh cũ; GL qua fallback. Lỗi chain, trích khóa và đọc efuse giữ nguyên. Trên nonCN hardware, mismatch trước đây được bỏ qua, nay fallback làm nhánh đó nghiêm hơn; artifact chỉ nhắm máy CN trong dự án.
3. Callback AVB [ffff0000907c5978](../fresh_analysis/h4_pseudocode/ffff0000907c5978.c) đã so với cả hai khóa CN/GL512 byte nhưng secure boot chọn một theo vùng. Patch branch payload0xc5a50 đến nhánh union sẵn có0xc5a9c. Unknown key vẫn giữ `trusted_out=0`; callback return0 chỉ nói thao tác thành công, không nói key được tin.

Chốt [ffff000090787318](../fresh_analysis/h4_pseudocode/ffff000090787318.c) có thông báo CN không được flash GL nhưng nằm trong đường boot của [Is_Secure_ID](../fresh_analysis/h4_pseudocode/ffff000090786b8c.c). Caller đã return0 khi getter cũ báo unlocked3. Không patch trùng.

## Artifact và kiểm chứng

- File: [lk_h4_unlock_CN_GLOBAL_REGION_TEST.img](../experiments/lk_global_region_v1/lk_h4_unlock_CN_GLOBAL_REGION_TEST.img), 4.083.424 byte.
- SHA256: `782df4cb90d2bc773a1a599d6d1c77dfc765c62efe3357185013ebbd2e731095`.
- Input: bản hybrid đã boot, SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`.
- Sửa ba vùng lệnh tổng36 byte và tính lại prefix hash; giữ patch unlock16 byte, toàn bộ DER, H3 BL2, H4 AEE và DTB/DTBO.
- [Builder](../experiments/lk_global_region_v1/build_region_lk.py) từ chối input khác hash, kiểm instruction gốc và giữ nguyên mọi member khác main/prefix. Build lặp lại trùng byte.
- [Validation](../experiments/lk_global_region_v1/validation.json): 528 ca gate chạy cả code cũ/mới; 4 ca getter/wrapper/Secure ID; 10 DER signature còn nguyên hợp lệ; 6 lần chạy parser thật lấy đúng hash theo H3/H4.

Mô phỏng chạy thực memcmp, strcmp, memcpy, branch, xử lý độ dài key và ABI; đọc efuse, dconfig, SKU và kết quả chain/key extraction được mô hình hóa, logging/PAC được bypass. Các test xác nhận logic vùng và lan truyền lỗi trong phạm vi đó; không xác nhận chuỗi boot máy thật hay chữ ký của một ROM Global chưa được cung cấp.

Không flash, không đổi setting, không sửa modem/preloader/Android hoặc cài module. Không bổ sung lệnh ghi efuse. Không gọi artifact là “fix 5G”. Bản cũ đã boot giữ nguyên; checkpoint trước patch được push ở tag `before-lk-global-region-patch-20261103`.

## Biến thể trên file HyperOS 3 đã patch của người dùng

Theo yêu cầu tiếp theo, đã tạo [bản H3 riêng](../experiments/lk_h3_global_region_v1/README_VI.md) trực tiếp từ `lk_hyperos3_patched_unlockBL/dash.img`, input SHA256 `e164fb22511d93584afab2183c275374d7ab892dd22e3a2132c4f3b1e245ac18`. Không lấy H3 original để thay đầu vào, không ghép thành phần H4. Bản đầu vào và hai artifact H4 cũ giữ nguyên.

Ba vùng lệnh trong **file H3 patched**: SKU`0x68f84`, fallback khóa GL`0x88810`, union khóa boot`0xc55dc`. Đã định vị lại theo native bytes của H3, giữ unlock tại`0x89a2c`, tính lại prefix main cert2. Các member khác, DER, padding và mọi byte ngoài phạm vi cho phép giữ nguyên từ input.

Output `lk_hyperos3_patched_unlockBL_CN_GLOBAL_REGION_TEST.img`, 4.082.928 byte, SHA256 `dbabb26989e9270a1b2ebd747820dca7f3630779bb5b5f54b21c30209a4b00b8`. [Validation H3](../experiments/lk_h3_global_region_v1/validation.json) đạt532 ca CPU,10 DER signature và6 ca parser; [address map](../experiments/lk_h3_global_region_v1/replay_address_map.json) ghi các địa chỉ helper đã đối chiếu riêng. Đây là kết quả mô phỏng, chưa flash/boot test và không xác nhận5G hoặc tương thích ROM Global bất kỳ. Checkpoint trước sửa: `before-h3-patched-global-region-20261103`.
