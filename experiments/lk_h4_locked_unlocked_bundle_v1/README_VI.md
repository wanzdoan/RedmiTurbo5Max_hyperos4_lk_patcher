# Bộ H4 locked tự chọn vùng và unlocked hai chế độ MTE

Theo yêu cầu quay lại nhánh locked/unlocked, bộ này chứa **ba file riêng**, không có fakelock. Tất cả là bản sao **nguyên byte** từ các ảnh đã lưu: bản locked v3 đã có chức năng tự chọn vùng, bản unlocked mặc định đã có và bản unlocked MTE_OFF đã được dựng riêng trước đó. Không sửa lại ảnh đã chạy chỉ để đổi tên hoặc gom bộ.

| Lựa chọn | File | SHA256 |
|---|---|---|
| **Locked policy + auto HWC/all-region, MTE_OFF** | [lk_H4_LOCKED_POLICY_AUTO_HWC_MTE_OFF.img](locked/lk_H4_LOCKED_POLICY_AUTO_HWC_MTE_OFF.img) | `6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52` |
| **Unlocked + MTE mặc định** | [lk_H4_UNLOCKED_MTE_DEFAULT.img](unlocked/lk_H4_UNLOCKED_MTE_DEFAULT.img) | `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171` |
| **Unlocked + ép MTE_OFF** | [lk_H4_UNLOCKED_MTE_OFF.img](unlocked/lk_H4_UNLOCKED_MTE_OFF.img) | `7873361e63ade5e6840d985c23c44bbb01fe449e7f89fd3ebbdba0d3d244e2a4` |

Mỗi ảnh **4.083.424 byte**. [ZIP ba file](lk_h4_locked_unlocked_three_files.zip), [release_manifest.json](release_manifest.json) và [SHA256SUMS.txt](SHA256SUMS.txt).

## Bản locked tự nhận vùng như thế nào?

Giữ đúng **Policy v3 MTE_OFF đã được người dùng xác nhận boot và banking**, không dùng bản H4 v1/v2 từng vào recovery hoặc bản fakelock mới. Trong main v3 đã có:

- Đọc mã vùng ROM trong `countrycode` theo slot; mã `cn` không phân biệt chữ hoa/thường chọn HWC `CN`.
- Mã vùng khác không rỗng chọn HWC `GLOBAL`, gồm các đầu vào Global/EEA/TW/IN/ID trong kiểm thử. Đây là cách phân loại CN/non-CN của LK, không tạo chuỗi HWC riêng cho từng vùng.
- Không có/không đọc đủ/đọc lỗi mã vùng thì dùng HWC phần cứng; không giữ dữ liệu đọc dở.
- Giữ các chốt vùng đã mở và callback chấp nhận khóa ENG/CN/GL đã biết; khóa bị sửa không tự được tin cậy.

Getter khóa gốc vẫn sử dụng trạng thái lưu bền. Nếu trạng thái đó là unlocked thì policy vẫn báo unlocked; **flash file này không tự chạy quy trình khóa máy**. Nó không ép fake locked/green như bản fakelock đã tạm gác. MTE_OFF giữ đúng bản policy v3 bạn đang dùng tốt.

Phạm vi all-region là chính sách vùng/HWC và khóa đã nêu. Boot thực tế từng ROM Global/EEA/TW chưa được xác nhận từ việc đóng gói này; hỗ trợ các model phần cứng khác cũng không được suy ra từ HWC.

## Unlocked: MTE mặc định và OFF

`MTE_DEFAULT` là lựa chọn thường gọi “MTE ON” khi đối chiếu với bản OFF: nó **giữ chính sách MTE gốc**, không ép tắt. Việc LK chọn đường nào còn phụ thuộc các bit cấu hình và vùng bộ nhớ do chuỗi boot cung cấp. Vì vậy không đặt tên file `FORCE_ON` hoặc khẳng định MTE luôn bật trên mọi preloader.

`MTE_OFF` chỉ khác bản DEFAULT một nhánh lệnh payload `0x34740` (file `0x34940`, `20020054 → 11000014`) và prefix hash main 180 byte được tính lại; ép đường sẵn có xuất `arm64.nomte`. Getter unlocked, bộ nạp, AEE, DTB/DTBO và DER có chữ ký giữ nguyên. Không sửa preloader và không tắt hồi tố hoạt động của preloader đã chạy trước LK.

Hai bản unlocked lấy đúng nhánh H4 unlocked f57, giữ hành vi vùng/HWC của nhánh này; chức năng auto HWC/all-region trong bộ ba thuộc **bản locked** theo yêu cầu hiện tại.

## Nguồn và runtime report

| File trong bộ | Nguồn nguyên byte | Trạng thái đã biết |
|---|---|---|
| Locked auto HWC/MTE_OFF | [Policy v3 off](../lk_h4_policy_bl2_control_v3/README_VI.md), `6282d02b…` | Người dùng đã xác nhận Android/banking trên đúng hash; có báo cáo Integrity H4 fail riêng |
| Unlocked DEFAULT | [Hybrid H4 unlocked ban đầu](../../docs/04-runtime-and-preloader.md), `f57d19c9…` | Người dùng đã xác nhận boot/unlocked yes; báo cáo stock H4 preloader trước đó chỉ thuộc đúng bản này |
| Unlocked OFF | [Nhánh riêng MTE_OFF](../lk_h4_booted_hybrid_mte_off_v1/README_VI.md), `7873361e…` | Đã kiểm offline; chưa có xác nhận boot đúng ảnh này |

Các tên nguồn và bytes được giữ nguyên. Cả ba dùng H4 main + bộ nạp BL2 có chữ ký H3 thương mại + H4 AEE/DTBs; không phải toàn bộ LK H4 ENG hoặc thuần H4. [runtime_confirmation.json](runtime_confirmation.json) tách rõ báo cáo nguồn với việc không có lượt flash/boot mới trong tác vụ này.

## Kiểm chứng bộ cuối

[validation.json](validation.json): kiểm đúng hash/byte identity của cả ba file, từ chối input sai hash, kiểm hai unlocked chỉ khác MTE/prefix và bộ ba BL2 có chữ ký của cả ba giống nhau. **150 ca native mục tiêu** gồm 72 HWC (cả fallback/lỗi đọc), 6 khóa hợp lệ/bị sửa và 72 MTE trên ba ảnh. Các test HWC, key callback, điều kiện MTE và xuất `arm64.nomte` thực thi mã native trong mô phỏng; phần cứng/IO/formatter và crypto-mode có fixture.

Các kiểm DER/parser/AVB đã có gắn đúng hash nguồn tiếp tục áp dụng qua byte identity. Không chạy lại toàn bộ CPU/AVB chỉ để tạo bản sao; không biến test fixture thành kiểm toàn bộ thiết bị. ZIP kiểm CRC, hash từng entry và tính tái lập. Không flash, lock/unlock, reboot, đổi cài đặt hoặc ghi dữ liệu bảo mật trên máy.

```powershell
python experiments/lk_h4_locked_unlocked_bundle_v1/build_bundle.py
python experiments/lk_h4_locked_unlocked_bundle_v1/validate_selection.py
python experiments/lk_h4_locked_unlocked_bundle_v1/package_release.py
```

Không dùng Python `-O`. Checkpoint trước công việc: `before-h4-locked-unlocked-bundle-v1-20261106` tại `6ac0224`, đã push repo private. Fakelock giữ làm lịch sử, không đưa vào bộ này; nhánh Integrity vẫn tạm dừng theo yêu cầu.
