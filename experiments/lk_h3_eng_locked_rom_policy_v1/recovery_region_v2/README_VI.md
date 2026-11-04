# Điều tra v1 boot Global vào recovery — chưa tạo LK sửa tiếp

Trạng thái: **ANALYSIS_ONLY_PENDING_NORMAL_GLOBAL_ROM_COMPONENTS**. Ngày2026-11-04. Thư mục có tên `recovery_region_v2` để giữ riêng lượt nghiên cứu, **chưa có artifact LK v2** và chưa xác định đủ điều kiện gây lỗi để sửa firmware.

Cập nhật sau đó: người dùng chủ động yêu cầu thử đổi HWC. Đã tạo [biến thể thử HWC=GLOBAL](../hwc_global_v1/README_VI.md) trong thư mục riêng. Đây là phép thử giả thuyết, chưa thay đổi kết luận rằng điều kiện gốc của lỗi chưa được xác định; không có artifact firmware trong thư mục nghiên cứu recovery này.

Người dùng báo bản [LK ENG giữ cơ chế khóa gốc + CN/Global v1](../README_VI.md) vào recovery sau flash ROM Global; ENG unlock chạy cùng ROM thì boot được. Người dùng xác nhận chỉ bỏ `lk` và `preloader`, giữ các ảnh ROM khác. Báo cáo China “test bình thường” chưa được đối chiếu theo từng hash artifact. [Runtime report](runtime_failure_report.json) tách thông tin người dùng khỏi kiểm chứng trên máy tính.

## Đầu vào thật đã đọc

- `Downloads/vendor_boot.img`: SHA256 `381a47c837e67fc46b9442f5a1c9ca0444c4b97f67c9f3b8fa82e7facf84a285`, 67.108.864 byte. Property trong ramdisk xác định Global `OS3.0.304.0.WPLMIXM`, model/codename `POCO/dash_global/dash`.
- `Downloads/vbmeta.img`: SHA256 `4d6ad1a94bcbf4edc3cfc3ffb94978b61c8441c94a72c6466e3d4f976e263837`. Lưu [bản tham chiếu nguyên byte](input/vbmeta.img).
- `system/bin/recovery` trong ramdisk: SHA256 `b4a558ce3801e1c84903ef2d1752e8a5d86fc1beecee1adbb8f64f379a3be483`.
- `system/bin/init` của ramdisk recovery: SHA256 `073ea3296c5465ef4b078c918e886a4250eddcddbc5d2bf862f6f086a2b581e9`. Không đồng nhất nó với chương trình/rc/dịch vụ trong các phân vùng Android normal boot chưa được cung cấp.

[Inventory](target_inventory.json) ghi nguồn, hash và property. Không flash hoặc đọc thiết bị; ADB hiện không thấy thiết bị.

## Những kiểm tra đã hoàn tất

1. **Không phải thiếu khóa Global tại callback v1 đã sửa.** [Kiểm chữ ký và chạy mã LK thật](global_vbmeta_validation.json) xác nhận vbmeta Global có chữ ký RSA4096 hợp lệ. Modulus SHA256 `798f1f2b...38c49cb` đúng khóa Global v1 đã cho phép. Callback ENG gốc trả trusted0, callback v1 trả trusted1. Điều này xác nhận riêng cổng tin khóa, không xác nhận toàn bộ chuỗi boot.
2. **Hai ảnh người dùng gửi khớp nhau.** [Descriptor trong vbmeta](global_vbmeta_descriptors.json) xác nhận hash của vendor_boot trùng ảnh thực đã gửi. [Footer vendor_boot](vendor_boot_avb_footer.json) cũng có hash payload đúng; footer riêng có algorithm0, không phải nguồn chứa khóa ký root.
3. Vbmeta còn khai báo `countrycode`, `dtbo`, `init_boot`, `pvmfw` và các chuỗi `boot`, `vbmeta_system`, `vbmeta_vendor`. Chưa readback các phân vùng thực hoặc xác minh hết chuỗi này. `countrycode` được ký là ASCII `global` với zero padding đủ32 byte; không suy ra phân vùng đang flash sai khi chưa đọc nó. Người dùng báo đã flash các ảnh còn lại.
4. **Xác định đúng tài nguyên hiển thị thông báo.** [Ảnh PNG fragment0](input/version_mismatch_fragment0.png) thực sự chứa “This system version can't be installed on this device”. [Asset inventory](recovery_message_assets.json) ghi cả hai fragment chứa tài nguyên cùng tên.
5. Recovery nhận long option `show_version_mismatch`, switch value83 (`S`), tại option struct file offset `0x328360`; [bản ghi](recovery_error_option.json). Trong `start_recovery`, case`0x53` bật cờ; đường hiển thị tiếp tục xét `ro.boot.verifiedbootstate=green` trước khi gọi phương thức UI cho lỗi này.
6. Trong init cung cấp cùng recovery, builtin `do_reboot_to_recovery` có nhánh nhận `--version_mismatch` rồi thêm `--show_version_mismatch` vào thông điệp recovery. Đây là **đường chuyển thông báo**, chưa phải điều kiện gốc quyết định ROM/hardware không tương thích.

## Vì sao chưa sửa HWC hoặc bỏ xác thực rộng?

Recovery có đọc `ro.boot.hwc`, nhưng xref đã định vị thuộc constructor UI và cache thông tin phần cứng. Chỉ thấy chuỗi này chưa chứng minh nó là chốt khiến Android chuyển sang recovery. Thay HWC chỉ để ảnh lỗi biến mất có thể không sửa việc normal boot bị từ chối.

Trong LK ENG v1, vị trí tạo bootarg HWC đã định vị riêng trên input ENG: payload `0x13380` đặt con trỏ đối số HWC cho `snprintf` tại `0x133a0`; key format tại `0xf5a3e`. **Không sửa các bytes này.** Không lấy offset Secure ID/chốt CN của LK H3 thương mại hoặc H4 áp sang ENG vì các hàm/bố cục khác nhau.

Không tự tắt xác thực toàn bộ ảnh, rollback, ép unlocked hoặc fake verified-boot state để làm lỗi biến mất. Yêu cầu hiện tại là giữ cơ chế khóa ENG gốc và xác định đúng chốt Global; các bản unlock/fakelock trước là tác vụ khác.

## Bằng chứng và phạm vi reverse

- `recovery_analysis`: export Ghidra từ đúng ELF Global. Ghidra dùng image base `0x100000`; địa chỉ export trừ base này để đối chiếu ELF VA gốc.
- Hàm `00229120.c`: `start_recovery`, case`0x53` và lựa chọn UI.
- Hàm `002251a0.c`: `IsDeviceUnlocked`, so property với chuỗi `orange`.
- Hàm `0025f720.c`: constructor UI/cache HWC; không xem sự tồn tại property này là bằng chứng đủ của kiểm tra vùng.
- `init_analysis/001e4f50.c` và `.asm`: builtin chuyển tham số lỗi. Lượt export `-noanalysis` còn ký hiệu unresolved và có ca decompile không hoàn tất trong `functions.tsv`; không xem toàn bộ init đã được reverse hoàn chỉnh. Chỉ dùng các so sánh/nhánh đã đối chiếu lệnh máy.
- `ghidra_recovery.log`, `ghidra_init.log`: giữ cảnh báo và trạng thái công cụ, không coi ERROR/decompile thất bại là kiểm chứng thành công.

## Phần còn thiếu để sửa có căn cứ

Cần đường dẫn **cả thư mục ROM Global**, `super.img`/`super.img.*`, hoặc các ảnh đã tách gồm `system`, `vendor`, `mi_ext` để tìm rc/dịch vụ/thư viện phát `reboot_to_recovery --version_mismatch`. Nếu kiểm tra dựa trên dữ liệu LK truyền xuống, phải truy đúng consumer rồi mới sửa điểm xuất trong LK. Nếu consumer đọc trạng thái độc lập từ lớp bảo mật hoặc lỗi nằm ở một phân vùng chưa khớp, cần ghi rõ giới hạn LK-only; không giả định đổi country/HWC sẽ chữa được.

V1 vẫn giữ nguyên SHA256 `ad3d66e3c374ff3a83fc73ba30880fe1d18ee049b3c2ec54ff6e97e45815760b`. Chưa có hash ảnh sửa mới, chưa có xác nhận boot Global sau sửa.

## Tái tạo phần kiểm tra ảnh

```powershell
python experiments/lk_h3_eng_locked_rom_policy_v1/recovery_region_v2/extract_reference.py
python experiments/lk_h3_eng_locked_rom_policy_v1/recovery_region_v2/inspect_supplied_avb.py
```

Các lệnh từ chối input khác hash và không thay input, artifact v1, firmware điện thoại hoặc report kiểm thử cũ. Checkpoint trước lượt nghiên cứu đã push private: tag `before-eng-stock-global-recovery-v2-20261104`, commit `676ab31`.
