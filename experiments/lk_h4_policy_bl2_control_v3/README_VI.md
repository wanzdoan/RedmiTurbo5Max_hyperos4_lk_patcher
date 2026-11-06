# H4 policy v3: tách biến số locked và bộ nạp BL2

**Bản đối chứng để tiếp tục tìm lỗi, chưa xác nhận sửa recovery.** User báo v2 vẫn recovery với China H4 sạch, không root; đổi ENG H3 thì boot. Fastboot v2 báo `unlocked:no` và cho flash. User không có máy ở đây, yêu cầu chỉ kiểm offline.

## Vì sao bản H4 unlocked cũ lên mà bản locked mới chưa lên?

Đang có **hai biến số thay đổi đồng thời**:

| Ảnh | Main và trạng thái | Bộ nạp BL2 chạy trước main |
|---|---|---|
| H4 unlocked đã boot `f57d19c9...` | H4, getter ép unlocked3 | H3 thương mại có chữ ký |
| H4 policy v2 bị báo recovery | H4 đã port, getter lưu bền | ENG H3 có chữ ký |
| Hai bản policy v3 này | **Main/cert main nguyên byte v2** | **Đúng bộ ba BL2/cert1/cert2 từ f57 đã boot** |
| Control chẩn đoán riêng | V2 MTE_OFF chỉ ép getter3 | Giữ nguyên BL2 ENG của v2 |

Vì vậy chưa thể nói chắc chỉ do locked. V3 policy không bỏ thêm kiểm chữ ký/hash/rollback, không sửa getter thành fake-locked. Nó chỉ loại một khác biệt so với ảnh H4 từng chạy. Chưa có chứng cứ thực tế BL2 ENG là nguyên nhân; cùng chữ ký hợp lệ không chứng minh bàn giao phần cứng tương đương.

## File policy — giữ cơ chế khóa v2

| Biến thể | File | SHA256 |
|---|---|---|
| MTE_OFF | [Policy v3 MTE_OFF](lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img) | `6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52` |
| MTE gốc | [Policy v3 normal](lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_TEST.img) | `8ac64a981d880b943992ba19dac927665e045e28d47b88c8f77bc664b4b2b664` |

Mỗi file4083424byte. Giữ all-region/HWC/MTE tương ứng, AVB flags, getter/setter, fastboot gates và Secure-ID call-site skip của v2 nguyên byte. AEE/DTB/DTBO H4 cũng nguyên byte và trùng các record tương ứng của f57. **Nếu dữ liệu lưu bền cho unlocked thì policy vẫn báo unlocked; không tự khóa máy.**

## File chẩn đoán unlocked — khác mục đích

[UNLOCKED ENG BL2 MTE_OFF CONTROL](diagnostic/lk_H4_V3_UNLOCKED_ENG_BL2_MTE_OFF_CONTROL.img),4100080byte, SHA256`52aab5273eb49ab733e461c991ac0bc6ce0d1dde94912c0516e452f2ae7f2779`.

Chỉ thay16byte getter của v2 bằng đúng patch state3 của f57 và tính lại prefix. Bộ nạp ENG, mọi feature khác giữ v2. Đây là **control unlocked**, không phải file locked, không dùng kết quả của nó để chứng minh banking/Integrity khi locked. Không ghi trạng thái unlock vào RPMB hoặc chạy lệnh unlock/lock; builder chỉ tạo file host.

[ZIP ba file và evidence](lk_h4_v3_bl2_comparison_bundle.zip). Thư mục `diagnostic` trong ZIP tách control unlocked khỏi hai ảnh policy.

Khi có kết quả thực tế sau này, việc policy với BL2 thương mại boot được còn v2 cùng main không boot sẽ là bằng chứng khoanh vùng phía bộ nạp/handoff. Nếu control unlocked ENG boot nhưng policy vẫn không, cần truy tiếp các đường theo trạng thái khóa/dữ liệu thiết bị. Nếu tất cả đều recovery thì phải xét phần main port/MTE/bootconfig/môi trường; không gọi một thay đổi bất kỳ là fix chỉ vì fastboot nhận file.

## Kiểm offline mạnh hơn đợt trước

[Đối chiếu ROM China native](../../fresh_analysis/h4_recovery_v3_20261106/README_VI.md): toàn hàm AVB slot verification và loader được chạy trên các ảnh thật trong ROM người dùng. Danh sách yêu cầu lấy từ firmware: boot/vendor_boot/init_boot/countrycode. H4 v2 và ENG H3 locked4/flags0 đều trả0, chọn cùng buffer, tạo cùng cmdline AVB và cùng5packet root handoff dưới fixtures rõ ràng. Sai signature/hash/IO/rollback được phát hiện; không bỏ qua lỗi để báo PASS.

Với **ba ảnh v3 cuối**:

- Builder buộc đúng SHA nguồn/donor, rebuild trùng byte, sai nguồn bị từ chối. Hai policy chỉ đổi signed BL2 triple/layout; toàn main và certificate main bằng v2. Control chỉ đổi getter16byte/prefix, ngoài ra bằng v2.
- Mỗi ảnh:10DER signatures,3parser BL2 đúng họ, locator + coordinator cert2/PSS + helper/comparator hash của preloader H4 stock. Cả3qua phạm vi offline, không phải hardware acceptance.
- Chạy native AVB slot/loader trên đúng ROM China cho từng ảnh; policy trả device_state locked với stored-state fixture4. Control chạy **getter force3 thật**, tạo device_state unlocked. Root packet policy giữ lock1, control cho lock0/keyhash0 như thiết kế. AVB/OS metadata không bị giả.
- Bản tham chiếu f57 cũng được đưa qua native slot/loader với getter force3 thật. Xác nhận boot vật lý của nó vẫn là báo cáo lịch sử của user, không phải lượt thử mới.
- Main CPU evidence v2 giữ giá trị bằng **byte identity** với hai policy; không giả vờ đã chạy lại toàn bộ390case khi không đổi main. [validation.json](validation.json) và [booted_reference_validation.json](booted_reference_validation.json) gắn hash ảnh cuối.

Các mocks/giới hạn: stored lock, rollback0, hardwareCN, GUID, persistence, đuôi partition zero-fill, OEM key cache và secure-monitor đều có fixture; SHA/IO/heap/copy dùng backend host, RSA/descriptor/comparison/control native. PAC bypass trong RAM emulator, file không đổi. Native main replay **không chạy BL2 bàn giao thật**, không chạy boot mode/BCB thật, DTB/bootconfig hoàn chỉnh, kernel/FBE/verity mount hoặc trusted world. Chưa biết nguyên nhân recovery máy thật.

## Nguồn và tái tạo

Nguồn v2 normal SHA`f93f513e...`, off`48912f04...`. Donor duy nhất là ảnh f57 đã được user báo boot SHA`f57d19c9...`, không lấy bản H3 ENG hoặc stock H4 thay thế mà giữ tên donor. Các nguồn/v1/v2/H3 ZIP giữ nguyên bytes.

```powershell
python experiments/lk_h4_policy_bl2_control_v3/build_controls.py
python experiments/lk_h4_policy_bl2_control_v3/validate_controls.py
python experiments/lk_h4_policy_bl2_control_v3/package_release.py
```

Không dùng Python`-O`. Checkpoint đã push trước build:`before-h4-bl2-donor-control-build-20261106` tại`a71b22e`; repo private. Không flash, reboot, lock/unlock/erase hay đổi dữ liệu máy. Không hứa banking/Integrity/OTA hoặc stock preloader tương thích từ kết quả offline.
