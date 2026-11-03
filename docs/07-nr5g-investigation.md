# Nghiên cứu 5G/NR trên Redmi Turbo 5 Max

Cập nhật 2026-11-03. **Chưa có bằng chứng đăng ký NR thành công.** Sau hai lần sọc màu/đen màn, người dùng yêu cầu hoàn nguyên; các thay đổi thử SA/NSA đã được trả về cấu hình trước thử. Bộ thử vùng radio mới được đóng gói riêng, chưa cài lên máy.

Kết quả mới trong [docs08](08-lk-rom-region.md): kernel thực đã diễn giải descriptor LK type13 với bit NR có sẵn. Đã định vị và tạo bản thử LK bỏ chốt ROM CN/Global theo yêu cầu LK-only; chưa flash, chưa chứng minh sửa 5G. Bộ module phía Android ở đợt trước không được cài hoặc tiếp tục áp dụng.

## Trạng thái hiện tại được để lại

| Mục | Giá trị |
|---|---|
| ROM | OS4.0.3.0.XPLCNXM |
| SIM slot 0 | Viettel, PLMN 45204 |
| SIM slot 1, đang dùng dữ liệu | VinaPhone, PLMN 45202 |
| `ro.vendor.radio.5g` | `2`, giá trị gốc đã khôi phục |
| `Settings.System:5g_sa_mode_disabled` | Không tồn tại; `settings get` trả `null`, đã hoàn nguyên |
| `preferred_network_mode` | `33,33`, không đổi |
| Radio log khi hoàn nguyên | Phone gửi `AT+E5GOPT=1` cho hai slot; không suy thành NR registration |
| Network type tại snapshot sau hoàn nguyên | `LTE,Unknown`; không xác nhận cả hai SIM đã ổn định |
| Đăng ký 5G ngoài thực địa | Chưa có xác nhận thành công |

`mode 3` trong thử nghiệm trước chỉ là cấu hình vận hành của modem, không phải xác nhận đang đăng ký NR. Không tuyên bố đã mở 5G thành công chỉ từ biểu tượng hoặc một giá trị cấu hình.

Bản ghi cuối: [rollback_and_current_state.json](../fresh_analysis/nr5g_research/crash_review/rollback_and_current_state.json). [vina_outdoor_setup.json](../fresh_analysis/nr5g_research/runtime/vina_outdoor_setup.json) là trạng thái lịch sử đã được hoàn nguyên. LK đã boot vẫn SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`, không đổi byte.

## Sự cố màn hình và bộ thử vùng radio riêng

Người dùng báo hai lần sọc màu/đen màn và phải giữ nguồn, có mở MBBank. Lịch sử ghi forced-longkey lúc 22:17:17 và 22:19:04. Log cũ hỏng hoặc chứa nhiều byte zero; dấu vết còn lại không đủ quy nguyên nhân cho ứng dụng, module root, radio hay phần cứng. Chưa có bằng chứng kết luận thay đổi 5G gây lỗi hoặc hoàn toàn vô can. Evidence chọn lọc ở `fresh_analysis/nr5g_research/crash_review`; log thô riêng tư không đưa vào Git. Không tự tái hiện lỗi hoặc bật lại SA/NSA.

Yêu cầu mới: thử đổi `ro.vendor.radio.build_region=cn`. Thuộc tính được khai báo trong vendor_boot ramdisk và `/vendor/build.prop`; LK xuất `androidboot.ptcountrycode`, chưa có đường đã chứng minh để patch LK thay trực tiếp thuộc tính này. Native radio còn đọc `ro.miui.build.region` (`ril/pseudocode/005a28e0.c`) và `persist.vendor.radio.build_region` (`005a2b00.c`, `005a2b70.c`). Chưa chứng minh `cn` là nguyên nhân chặn NR.

[Bộ thử global v1](../experiments/radio_region_global_v1/README_VI.md) chứa bản sao LK nguyên byte và module KernelSU Next chỉ override `ro.vendor.radio.build_region=global`; giữ các vùng khác và cấu hình SA/NSA. `global` là giả thuyết thử, chưa được chứng minh mở NR. Chỉ kiểm tra host, chưa cài module/flash. Không gọi đây là LK patch fix 5G. Tắt/gỡ module và reboot để quay lại thuộc tính gốc.

## Kết luận về LK

Rà actual binary/pseudocode main LK, bộ BL2 H3 trong hybrid, AEE, 13.078 property main DTB và 2.425 property DTBO chưa xác lập một công tắc NR/MCC-MNC/band Việt Nam trong LK. Đây là negative search có giới hạn, không phải chứng minh mọi code path đều không liên quan.

LK đọc countrycode rồi truyền `androidboot.ptcountrycode=cn` cho Android. Có luồng `md_capability` từ modem CHECK_HEADER+0x14 chuyển vào CCCI tag, không phải một hằng LK tự gán để ép bật NR. Các `nr-*` dưới DFD là số core phục vụ crash dump; các band-path wifi/bt thuộc phần kết nối không phải băng tần di động.

Hybrid chỉ sửa 16 byte getter unlock trong main; AEE/DTB/DTBO giữ H4, BL2 là signed H3. Chưa có bằng chứng 16 byte unlock gây giới hạn 4G+. Xem [LK findings](../fresh_analysis/nr5g_research/lk/FINDINGS.md).

## Hai chặn cụ thể ở Android

Đã lấy read-only shipped APK/JAR/library từ đúng máy dash, hash trong `device_static/complete_manifest.json`.

### 1. VinaPhone kế thừa mặc định tắt 5G độc lập

Package `com.xiaomi.carrierconfig` trong CarrierStudioCarrierConfig.apk:

- `res/xml/vendor_miui.xml`, default carrier: `carrier_sa_mode_available_bool=false`, `carrier_disable_sa_mode_bool=true`.
- `assets/device/dash/res/xml/vendor_device.xml`, MCC452/MNC04: bật available, tắt disable cho Viettel.
- Cùng file, MCC452/MNC02: cấu hình Wi-Fi calling nhưng không override hai field SA, nên VinaPhone kế thừa mặc định tắt.

Runtime carrier_config xác nhận đúng hai cặp giá trị cho slot0/slot1. Hai SIM có user allowed-network mask916479 (`0xdfbff`), có bit NR524288. Vì vậy không phải chỉ quên chọn mạng 5G ở giao diện.

`FiveGManagerBase.getFiveGNetworkMode()` dùng origMode, cloud và carrier-disable để chọn mode. Trước thử: orig=-1, cloud disabled/enabled đều false, data slot1 carrier-disabled=true -> mode0. Override System `5g_sa_mode_disabled=0` được `fetchSaModeCarrierStatus()` dùng để vượt cờ disable này.

### 2. Phone đánh dấu máy là SA-only

`MiuiPhoneUtils.is5gSaOnlyDevice()` trả `ro.vendor.radio.5g == 2`. Giá trị thực tế bằng2. Khi bật SA, Phone chọn `enableSaForSaOnlyDevice()` và gửi `AT+E5GOPT=3,2`; khi tắt chọn mode1. Không đi nhánh hỗ trợ cả SA và NSA.

SA là 5G độc lập. NSA là 5G chạy kết hợp với 4G. Việc Phone đánh dấu SA-only là quan sát phần mềm; chưa chứng minh nguyên nhân là giới hạn phần cứng, cấu hình modem hay chính sách sản phẩm.

## Các phép thử đã được người dùng cho phép

Trước khi thử đã lưu/push checkpoint và backup. Không flash, không sửa file modem/NVRAM hay cài module root. Các lệnh radio được chính ứng dụng Phone phát sau event tích hợp của ROM.

| Phép thử | Kết quả | Xử lý |
|---|---|---|
| Bật override SA qua receiver mã ẩn7282583 | Key0, Phone nhận SA=true; modem nhận mode3; Vina chưa đăng ký NR, có OUT_OF_SERVICE | Hoàn nguyên key về absent; sau đó đã quan sát LTE,LTE |
| Tạm đổi runtime property2→3, bật override SA | Phone chọn nhánh NSA/SA; `AT+E5GOPT=5` và `AT+E5GOPT=7,2` trả ERROR; query vẫn1 | Trả property2 và key absent, lưu kết quả thất bại |
| Người dùng yêu cầu để Vina 5G đi thử ngoài đường | Giữ property gốc2, bật lại SA key0; query3 và SA status true | Sau đó đã hoàn nguyên theo yêu cầu vì gặp lỗi màn hình |

Điểm sửa lại so với giả định ban đầu: kỳ vọng generic enableSA sẽ giữ NSA không áp dụng cho máy này vì Phone đi nhánh SA-only. Không dùng kết quả “EVENT_SET...configuration is done” làm thành công: RIL log vẫn có GENERIC_FAILURE/ERROR. Parser transaction ghép đúng reader/slot để đối chiếu status thực tế.

Một số flag hỗ trợ mã ẩn trong XML đóng gói có giá trị false. Không dùng riêng default XML để suy toàn effective guard: trên máy này receiver thực tế đã xử lý và key thay đổi đúng chu kỳ, là bằng chứng runtime chức năng đi qua được. `cmd phone cc` bị chặn trên user build dù ADB root; không dùng nó để áp cấu hình.

## Phần modem đã định vị

Modem có schema thật cho feature/data và bảng band theo nhà mạng: LID960/961; NR disable-list2375, enable-list2392, operator-band2380, ENDC combination2437, band-support-PLMN2439. Có cả danh sách đảo để tắt band khi PLMN không thuộc nhóm cho phép. Đã tìm file runtime SP80_636/SP81_133 nhưng body lưu trữ chưa giải mã; không áp offset schema lên ciphertext.

Đã phục hồi 138.558 symbol entry và schema XML từ debug database nằm trong modem image. Handler tiếp theo cần hiểu gồm `atp_e5gopt_hdlr`0x9119f0a6, `l5ath_e5gopt_set_req`0x911abd5c và `l5ath_e5gopt_cmd_handle`0x911abe3a. Đây mới là tên/địa chỉ được metadata khai báo; chưa decode/execute được handler để biết điều kiện từ chối NSA.

MCF có tên các mạng Việt Nam không chứng minh các mạng đó được mở NR. Chưa xác định active SBP bit/list nào chặn 45202/45204. Không có bản patch NVRAM/modem được tạo hoặc tuyên bố hoạt động. Xem [modem findings](../fresh_analysis/nr5g_research/modem/FINDINGS.md).

## Khôi phục sau field test

Đã chạy hoàn nguyên theo yêu cầu người dùng; lệnh dùng trong quy trình:

```powershell
python fresh_analysis/nr5g_research/sa_mode_trial.py --rollback
```

Lệnh dùng backup `runtime/sa_trial_backup.json`, đưa setting về trạng thái mặc định rồi xóa key nếu ban đầu không tồn tại. Property đã là2; thử dual-mode cũng rollback xong. Không tự chạy lại các trial để kiểm tra docs.

Không chạy lại `setup_vina_outdoor.py` như một module tự khởi động; đây là bản ghi thao tác có guard cho initial state, không phải bản fix persistent đã chứng minh. Bộ region mới cũng chưa phải giải pháp đã kiểm chứng.

## Bằng chứng và tái tạo

- `runtime/before_sa_*`, `after_sa_*`: trạng thái trước/sau thử SA-only.
- `runtime/sa_only_trial_result.json`, `dual_mode_trial_result.json`: kết quả và rollback.
- `runtime/e5gopt_transactions.json`: command/status gắn cùng slot/reader.
- `runtime/carrier_sa_policy.json`, `android/xiaomi_carrier_sa_policy.json`: policy và source APK.
- `runtime/vina_outdoor_setup.json`: trạng thái lịch sử trước hoàn nguyên; xem `crash_review/rollback_and_current_state.json` để biết trạng thái mới.
- `ril/pseudocode`, `ril/inventory.json`: focused native exports, có warnings/EXTERNAL boundaries, không toàn source.
- `modem/at_nr_handler_symbols.json`: handler targets, chưa có instruction mapping/proof.

Các log được chọn lọc và che identifier; không lưu raw SIM/NVRAM body. Database Ghidra RIL sinh tự động vượt giới hạn file GitHub được giữ local và bỏ khỏi Git; code đầu vào, scripts, focused exports và báo cáo được version hóa.

Nguồn tham khảo kiến trúc Android: [carrier configuration](https://source.android.com/docs/core/connect/carrier), [allowed-network type masks](https://developer.android.com/reference/android/telephony/TelephonyManager#setAllowedNetworkTypesForReason(int,%20long)), [carrier NR availability](https://developer.android.com/reference/android/telephony/CarrierConfigManager#KEY_CARRIER_NR_AVAILABILITIES_INT_ARRAY). Chúng giải thích API chung, không thay thế bằng chứng riêng của binary dash.
