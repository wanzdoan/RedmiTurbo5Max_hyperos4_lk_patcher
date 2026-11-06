# Bộ H4 v2 — cả locked và unlocked đều auto HWC/all-region

Bộ này sửa thiếu sót của bộ trước: **cả bốn file đều có tự chọn vùng/HWC và cùng chính sách vùng từ H4 policy v3**. Hai file locked giữ nguyên bytes v3; hai file unlocked mới lấy từng nền v3 tương ứng, chỉ thay getter unlock 16 byte bằng đúng getter từ H4 unlocked đã boot và cập nhật hash prefix main. Không có fakelock, không ghép bộ nạp ENG của các bản v1/v2 từng bị recovery.

| Lựa chọn | File | SHA256 |
|---|---|---|
| Locked + auto HWC + MTE mặc định | [lk_H4_LOCKED_AUTO_HWC_MTE_DEFAULT.img](locked/lk_H4_LOCKED_AUTO_HWC_MTE_DEFAULT.img) | `8ac64a981d880b943992ba19dac927665e045e28d47b88c8f77bc664b4b2b664` |
| Locked + auto HWC + MTE_OFF | [lk_H4_LOCKED_AUTO_HWC_MTE_OFF.img](locked/lk_H4_LOCKED_AUTO_HWC_MTE_OFF.img) | `6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52` |
| **Unlocked + auto HWC + MTE mặc định** | [lk_H4_UNLOCKED_AUTO_HWC_MTE_DEFAULT.img](unlocked/lk_H4_UNLOCKED_AUTO_HWC_MTE_DEFAULT.img) | `3b06a144aef5e2afbcf117c09b5fd8fa6412cde692e547442e3e840224227d83` |
| **Unlocked + auto HWC + MTE_OFF** | [lk_H4_UNLOCKED_AUTO_HWC_MTE_OFF.img](unlocked/lk_H4_UNLOCKED_AUTO_HWC_MTE_OFF.img) | `ccfb3b0059fab66622a8b3555c760a22621ace7a651f86824e233341988aae6e` |

Mỗi file 4.083.424 byte. [ZIP bốn file đã sửa](lk_h4_all_region_four_files_v2.zip), [release_manifest.json](release_manifest.json), [SHA256SUMS.txt](SHA256SUMS.txt).

## Auto HWC/all-region ở cả hai nhánh

- Đọc `countrycode` theo slot ROM; `cn` không phân biệt hoa/thường → HWC `CN`.
- Mã khác không rỗng → HWC `GLOBAL`, gồm Global/EEA/TW/IN/ID trong ca kiểm. LK phân loại CN/non-CN, không tạo HWC riêng cho từng khu vực.
- Không đọc được/thiếu/đọc dở → HWC phần cứng. Không dùng dữ liệu countrycode còn sót sau lỗi đọc.
- Giữ các chốt SKU/CN/Global đã mở, fallback khóa ảnh CN/GL và callback AVB cho các khóa ENG/CN/GL đã biết từ policy v3. Lỗi chain/lấy khóa và khóa bị sửa không được coi thành hợp lệ.

Đây là hỗ trợ logic vùng và HWC trong LK; chưa có thử boot từng ROM Global/EEA/TW hoặc các model phần cứng khác với hai ảnh unlocked mới.

## Locked và unlocked khác gì?

**Locked policy:** giữ getter/setter gốc và trạng thái lưu bền của v3. File không tự thực hiện quy trình khóa máy; nếu dữ liệu khóa là unlocked thì policy vẫn báo unlocked. Chỉ đúng bản locked MTE_OFF `6282d02b…` có báo cáo boot/banking lịch sử của người dùng.

**Unlocked:** getter native payload `0x89aec`, file `0x89cec`, trả state3/success0 bằng đúng 16 byte từ H4 unlocked f57. Không ghi khóa lưu bền. Toàn bộ phần main còn lại, các hook HWC/chốt vùng, bộ nạp BL2 có chữ ký H3 thương mại, AEE/DTB/DTBO H4 và DER giữ nguyên từng nền locked tương ứng. Tính lại prefix hash main 180 byte để bộ đọc hiện có nhận đúng main mới.

Giữ nguyên thông tin công khai và packet root theo đường **unlocked thật**: native AVB xuất `device_state=unlocked`, packet lock0 và hash rỗng theo thiết kế unlocked gốc. Không ghép formatter hoặc packet giả locked/green của fakelock. Hai ảnh unlocked này là ảnh mới, chưa được người dùng xác nhận boot/root/banking/Integrity.

So với H4 unlocked f57 ban đầu, hai ảnh mới có toàn bộ nền policy v3 gồm auto HWC, chính sách vùng/khóa, gate main và Secure-ID call-site skip vốn có của v3; không chỉ sao chép f57 rồi đổi tên. So với đúng nền locked v3 tương ứng, thay đổi được giới hạn vào getter16byte và hash prefix.

## MTE mặc định/OFF

DEFAULT là chính sách MTE gốc, thường gọi “ON” khi đối chiếu với bản OFF, nhưng **không ép bật trên mọi cấu hình**. LK còn xét các bit cấu hình và vùng bộ nhớ từ chuỗi boot.

OFF giữ nhánh có sẵn xuất `arm64.nomte`. Trong từng cặp locked hoặc unlocked, chỉ nhánh MTE payload `0x34740`/file `0x34940` và hash prefix khác nhau. Không sửa preloader và không tắt hồi tố những hoạt động đã chạy trước LK.

## Kiểm chứng

[validation.json](validation.json) ghi **632 ca main native** trên đúng bốn ảnh cuối:

| Nhóm | Số ca |
|---|---:|
| Auto HWC, cả hai slot/CN/non-CN/fallback/lỗi đọc | 288 |
| Khóa AVB CN/GL/ENG thật và khóa bị sửa | 24 |
| MTE DEFAULT/OFF với các bit và bộ nhớ | 96 |
| Getter unlock, wrapper và quyết định verify của hai ảnh mới | 92 |
| Chốt SKU, khóa ảnh CN/GL/unknown, lỗi chain/lấy khóa và chốt vùng | 100 |
| Gate flash/erase với lỗi quyền | 32 |

Ngoài ra, **mỗi ảnh unlocked mới** có 10 DER signature và 3 parser BL2 thương mại qua kiểm mới; prefix đúng main/header hiện tại. Locator/chứng chỉ/hash của preloader H4 stock được chạy tại boundary native với trusted-key/crypto fixtures. Hai locked giữ nguyên bytes/hash và kế thừa evidence nguồn.

Toàn hàm AVB slot và loader native chạy trên ROM China H4 sạch cho cả hai ảnh unlocked: return0, getter force3 thật thực thi, chọn được các buffer boot/vendor_boot/init_boot, cmdline unlocked và packet root lock0/hash0. Không mô phỏng toàn bộ BL2 handoff, kernel/Android/TEE hoặc Google. IO/heap/hardware/formatter/rollback/khóa và secure-monitor tại các boundary có fixture rõ trong report.

Builder từ chối sai hash nguồn/donor, build lặp lại trùng byte, giữ các member/DER ngoài vùng cho phép nguyên byte. Hai locked giữ đúng bytes nguồn; so từng cặp DEFAULT/OFF chỉ có nhánh MTE/prefix khác nhau. ZIP kiểm CRC, entry hash và tính tái lập.

## Nguồn, phiên bản và tái tạo

Nguồn policy [v3 normal/off](../lk_h4_policy_bl2_control_v3/README_VI.md): `8ac64a98…`/`6282d02b…`. Getter lấy từ [H4 unlocked f57](../../fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img), SHA `f57d19c9…`. Cả bộ vẫn là H4 patched release main + signed BL2 H3 thương mại + H4 AEE/DTBs, không phải toàn bộ LK ENG H4 hoặc thuần H4.

```powershell
python experiments/lk_h4_all_region_four_files_v2/build_all_region.py
python experiments/lk_h4_all_region_four_files_v2/validate_all_region.py
python experiments/lk_h4_all_region_four_files_v2/package_release.py
```

Không dùng Python `-O`. Checkpoint trước sửa `before-h4-unlocked-auto-hwc-v2-20261106` tại `cca22c1` đã push repo private. [Bộ trước](../lk_h4_locked_unlocked_bundle_v1/README_VI.md) giữ nguyên để khôi phục; hai unlocked của bộ trước chưa có auto HWC và không phải hai ảnh mới này. [Runtime report](runtime_confirmation.json) không chuyển kết quả boot nền sang hai ảnh unlocked mới. Không flash, reboot, lock/unlock máy hoặc ghi dữ liệu bảo mật trong tác vụ này.
