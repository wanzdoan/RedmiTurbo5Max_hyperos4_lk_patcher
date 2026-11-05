# Sửa các chốt CN/Global còn thiếu trong Fenrir HWC/MTE-off

Ngày 2026-11-05. **Bản trước chỉ chọn HWC, chưa bỏ các chốt CN/Global. Gọi nó là “mở all region” là quá mức.** Người dùng báo đang ROM POCO HyperOS3 và nạp vào thì máy chỉ về preloader, không lên; ROM đích không có trên máy tính và chưa có log chỉ ra stage dừng.

File người dùng để lại `../lk_fenrir_only_auto_hwc_mte_off_v1/lk_fakeloc_vip.img` trùng nguyên hash `8d616abb...` của bản trước. Đây là nhận diện file trên máy tính, không phải readback partition đã flash.

File sửa riêng: [lk_FENRIR_CN_GLOBAL_GATES_HWC_MTE_OFF_V2_TEST.img](lk_FENRIR_CN_GLOBAL_GATES_HWC_MTE_OFF_V2_TEST.img), **4.083.872 byte**.

SHA256: `e6bd99c91854a916fc675a57f5f18c139b72868a5ebdafd7b72088b03afd3e2f`.

**Chưa flash hoặc boot test bản sửa. Không xác nhận đã chữa nguyên nhân máy về preloader.** Đây là bản sửa các chốt vùng cụ thể, không phải bảo đảm mọi ROM POCO, model hoặc khóa ký đều tương thích.

## Bốn chốt thực sự còn nguyên ở bản trước

| Main payload | Bản trước | Bản sửa |
|---|---|---|
| `0x68d84` | Khi secure boot/CN hardware đang áp dụng, SKU init_boot ngoài CN/NATIVE có thể vào panic | Nhảy qua đúng nhánh từ chối SKU; giữ các kiểm production/efuse/error trước đó |
| `0x88610..0x88628` | Certificate image-key256 bị so theo vùng | Thử thêm khóa Global256 nhúng khi mismatch; giữ chain verify, key extraction và lỗi tương ứng |
| `0xc53dc` | Callback AVB chọn một khóa CN hoặc Global4096 theo hardware region | Dùng nhánh union CN/Global có sẵn; unknown key vẫn không trusted |
| `0x87098` | Helper trong đường Secure-ID có thể gọi reboot recovery khi software SKU khác CN nhưng hardware là CN | Bỏ đúng nhánh reboot vì lệch vùng; giữ các phần xác thực Secure-ID khác |

Chốt thứ tư quan trọng với Fenrir: common wrapper của nó báo locked4, nên đường Secure-ID không được bỏ qua như các bản force-unlocked3 trước đây. HWC xuất sang Android không làm thay đổi hardware string/cache mà helper này so sánh. Không sửa hardware cache để giả chữ ký/binding phần cứng.

Patch không return-success cả hàm Secure-ID, không bỏ xác thực certificate chain hoặc thay khóa lạ thành trusted. Những kiểm tra khác có thể tiếp tục từ chối ROM không đúng model/định dạng/khóa; chưa có ảnh ROM POCO thực tế để kiểm tất cả các điều kiện đó.

## Giữ lại và thay đổi

Input là đúng bản Fenrir HWC/MTE-off SHA `8d616abb51cd48373d7a892013c080b902a811983133bfc42d1a0accd73e30e8`, vốn từ Fenrir `dee32d18...`. Không lấy các hybrid stock/debug đã bị báo không dùng được làm input.

Chỉ sửa40 byte code tại bốn span trên và cập nhật main cert2 prefix90 byte. Giữ HWC tự chọn vùng, ép nhánh MTE suppress xuất `arm64.nomte`, các cơ chế fakelock/AVB/flash/erase khác của Fenrir, toàn bộ BL2/AEE/DTB/certificates/trailer và DER đã ký. Không thêm patch v2, guard bảo trì hoặc ghi efuse/RPMB. Builder kiểm mọi byte ngoài allowlist nguyên byte.

Image-key fallback có giới hạn: trên non-CN hardware, nhánh unknown-key mismatch trước đây có trường hợp bị bỏ qua; fallback mới làm trường hợp đó bị từ chối. Bản này nhắm thiết bị CN của dự án với khóa CN/Global đã nhúng, không phải mở arbitrary key.

## Bằng chứng trước/sau

```powershell
python experiments/lk_fenrir_cn_global_gates_v2/build_region.py
python experiments/lk_fenrir_cn_global_gates_v2/validate_region.py
```

Không dùng Python `-O`. [validation.json](validation.json) ghi:

- **768 ca region gate native**, gồm cả input cũ và output mới: SKU CN/NATIVE/GLOBAL/POCO/EEA/TW/IN/rỗng, hardware/SBC/dconfig/error fixtures; khóa CN/GL/unknown, 2048/4096; lời gọi recovery khi lệch vùng.
- **147 ca HWC**, **24 ca MTE-off**, **3 ca parser BL2** trên bytes cuối.
- **10 chữ ký DER gốc** xác thực offline; parser BL2 H3 chọn hash main mới trong prefix.

Tổng942 ca native có giới hạn. Các phép memcmp/strcmp/memcpy, nhánh và epilogue chạy thật trong emulator. Secure-boot/efuse/dconfig, status chain/key extraction và đích reboot được mock; không thử reboot thật. Các SKU “POCO” là fixture, không phải dữ liệu lấy từ ROM trên điện thoại.

Test cũ tái hiện panic hoặc không trusted/recovery-call ở các fixture tương ứng; test mới đi qua các chốt vùng đó, vẫn giữ lỗi chain/extract/efuse và unknown-key refusal trong phạm vi đã kiểm. Không suy ra thất bại thực tế của điện thoại chắc chắn do các chốt này.

## Trạng thái còn chưa biết

ROM POCO H3 đích, build, vbmeta/public keys, preloader thực lúc lỗi và log boot chưa có trong đợt này. Máy về preloader có thể phản ánh lỗi tầng nạp hoặc reset sau một lỗi LK; chưa xác định trường hợp nào. Không coi việc parser/chữ ký/test CPU đạt là xác nhận preloader, kernel hoặc ROM đã boot.

Không thao tác ADB, flash, reboot, NVRAM hoặc preloader. Các artifact cũ, kể cả `lk_fakeloc_vip.img` người dùng thêm, giữ nguyên và có checkpoint push ở `before-fenrir-full-region-audit-20261105` tại `5d029fe`.
