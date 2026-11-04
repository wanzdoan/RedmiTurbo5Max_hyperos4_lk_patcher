# Unlock bootloader cho LK ENG người dùng gửi — 2026-11-04

File mới: [lk_hyperos3_ENG_UNLOCKBL_TEST.img](lk_hyperos3_ENG_UNLOCKBL_TEST.img), **4.043.792 byte**.

SHA256: `0d237263a24f6bf41fa33b5dc43bb7d71e5b61f1977331c3f9f1812ad5000466`.

Đầu vào: `C:\Users\Administrator\Downloads\lk.img`, 4.043.616 byte, SHA256 `6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d`. Đã lưu [bản gốc nguyên byte](input/lk_eng_original.img) và checkpoint GitHub trước sửa. “HyperOS3 ENG” là mô tả người dùng cung cấp; binary có các marker `userdebug`, nhưng chưa xác nhận đầy đủ ROM xuất xứ trên máy thật.

## Patch gì?

Định vị lại hàm `mi_get_lock_state` trên chính binary ENG, bằng tham chiếu chuỗi, lệnh máy và pseudocode mới. Whole-file offset **`0x898e4`**, payload offset`0x896e4`, địa chỉ`0xffff0000907896e4`:

```text
Original: 3f2303d5ff4305d1fd7b12a9fc5713a9
Patched:  68008052080000b900008052c0035fd6
```

```asm
mov w8, #3
str w8, [x0]
mov w0, #0
ret
```

Hành vi mới: ghi trạng thái3 (unlocked) vào con trỏ đầu ra, trả thành công0. Các wrapper, kiểm tra trạng thái xác thực boot và phản hồi fastboot đã được thực thi mô phỏng trên mã ENG thật. Bỏ qua phần đọc/kiểm magic và chữ ký của getter; không ghi unlock vĩnh viễn vào RPMB và không tương đương một thao tác unlock chính thức được lưu bền.

## Vì sao có sửa phần chứng chỉ?

Tầng BL2 trong file ENG vẫn yêu cầu xác thực main LK: nhánh policy thật trả auth_required1 trong cả bốn ca đọc trạng thái bảo mật đã mô hình hóa. Nhãn ENG không tự làm bước này biến mất.

BL2 của file mới khác BL2 các bản H3 trước. Đã chạy trực tiếp bộ đọc của chính nó tại payload`0x6ea70`; nó tìm hash trong toàn cert2 và đọc được prefix180 byte nằm trước DER có chữ ký. Sau khi patch main, builder thêm prefix chứa hash header/payload mới, giữ nguyên DER gốc. Cách này không phải ký lại bằng private key và không sửa BL2.

Main cert2 tăng982→1162 byte; căn record16 byte làm tổng file tăng176 byte. **Tất cả record khác main/cert2, gồm BL2, AEE, DTB/DTBO và certificate/padding đi kèm, nguyên byte từ input ENG.** Không ghép thành phần H3 release hoặc H4; không thêm patch vùng Global hay cấu hình radio.

## Kiểm chứng

[validation.json](validation.json) ghi:

- **342 ca CPU main:** 300 ca getter/hai wrapper trên100 giá trị đầu vào; 28 ca predicate xác thực boot; 7 ca lock boolean; 7 ca phản hồi fastboot, gồm control state locked/unlocked. Kiểm output word, vùng nhớ lân cận, stack và thanh ghi cần bảo toàn.
- **9 ca parser BL2 ENG thật:** cert gốc, cert artifact mới và prefix control đã đổi hash; mỗi loại chạy ba mask. Prefix control chứng minh parser lấy hash ngoài DER, không chỉ tình cờ trùng hash đã ký. Chỉ bypass PAC trong các ca parser.
- **4 ca policy BL2 ENG thật:** lookup, so chuỗi, wrapper/getter và chọn policy chạy thực; chỉ mô hình hóa helper đọc secure state, logging và PAC.
- **10 chữ ký DER giữ lại hợp lệ** bằng kiểm RSA-PSS offline; hash các payload không phải main khớp chứng chỉ. Main signed digest cũ giữ nguyên, khác digest đã patch; BL2 parser chọn digest prefix mới.
- Build lặp lại trùng byte, input khác hash bị từ chối; kiểm toàn bộ member khác giữ nguyên.

**Chưa flash hoặc kiểm thử boot bản này trên điện thoại.** Các ca mô phỏng không thay thế toàn bộ chuỗi xác thực/nạp ảnh, tương thích preloader, rollback/fuse hoặc boot máy thật. Không có thao tác ADB/fastboot trên thiết bị trong tác vụ này.

## Tái tạo

```powershell
python experiments/lk_h3_eng_unlock_v1/build_eng_unlock.py
python experiments/lk_h3_eng_unlock_v1/validate_eng_unlock.py
```

Xem [manifest](manifest.json), [inventory input](input_inventory.json), [getter pseudocode](analysis_main/ffff0000907896e4.c), [danh sách function](analysis_main/functions.tsv). Pseudocode được phục hồi bằng Ghidra12.1.2 trên carrier byte-identical; không phải source gốc. Dùng `inspect_eng.py --cache <thư-mục-tạm>` để tái tạo carrier; không nạp carrier ELF lên điện thoại.

Checkpoint input trước sửa: `before-h3-eng-unlock-patch-20261104`, commit`51bd2ae` trong repo private. Giữ nguyên input và mọi artifact cũ để quay lại.
