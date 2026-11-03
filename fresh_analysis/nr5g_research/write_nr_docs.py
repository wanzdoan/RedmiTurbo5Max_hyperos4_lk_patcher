from pathlib import Path
import json,hashlib
root=Path.cwd();fresh=root/'fresh_analysis';nr=fresh/'nr5g_research'
text='''# Nghiên cứu 5G/NR trên Redmi Turbo 5 Max

Cập nhật 2026-11-03. **Chưa có bằng chứng đăng ký NR thành công.** Theo yêu cầu cuối của người dùng, hiện để **5G độc lập bật cho VinaPhone** để họ mang máy ra ngoài thử. Không tự hoàn nguyên thiết lập này trong lúc chờ field test.

## Trạng thái hiện tại được để lại

| Mục | Giá trị |
|---|---|
| ROM | OS4.0.3.0.XPLCNXM |
| SIM slot 0 | Viettel, PLMN 45204 |
| SIM slot 1, đang dùng dữ liệu | VinaPhone, PLMN 45202 |
| `ro.vendor.radio.5g` | `2`, giá trị gốc đã khôi phục |
| `Settings.System:5g_sa_mode_disabled` | `0`, người dùng yêu cầu giữ bật cho thử ngoài đường |
| `preferred_network_mode` | `33,33`, không đổi |
| Phản hồi modem slot Vina | `+E5GOPT: 3` |
| Trạng thái trong ứng dụng Phone | `mSaStatus[1]=true`, `mNsaStatus[1]=false` |
| Đăng ký 5G ngoài thực địa | Chờ người dùng xác nhận |

`mode 3` ở đây là phản hồi cấu hình vận hành từ modem, không phải mã xác nhận đang đăng ký mạng NR. Tại snapshot đặt cấu hình, network type Vina còn Unknown/tìm mạng. Không tuyên bố đã mở 5G thành công chỉ từ biểu tượng hoặc một giá trị cấu hình.

Bản ghi cấu hình để đi thử: [vina_outdoor_setup.json](../fresh_analysis/nr5g_research/runtime/vina_outdoor_setup.json). LK đã boot vẫn SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`, không đổi byte.

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
| Người dùng yêu cầu để Vina 5G đi thử ngoài đường | Giữ property gốc2, bật lại SA key0; query3 và SA status true | Đang để bật theo yêu cầu, chờ field test |

Điểm sửa lại so với giả định ban đầu: kỳ vọng generic enableSA sẽ giữ NSA không áp dụng cho máy này vì Phone đi nhánh SA-only. Không dùng kết quả “EVENT_SET...configuration is done” làm thành công: RIL log vẫn có GENERIC_FAILURE/ERROR. Parser transaction ghép đúng reader/slot để đối chiếu status thực tế.

Một số flag hỗ trợ mã ẩn trong XML đóng gói có giá trị false. Không dùng riêng default XML để suy toàn effective guard: trên máy này receiver thực tế đã xử lý và key thay đổi đúng chu kỳ, là bằng chứng runtime chức năng đi qua được. `cmd phone cc` bị chặn trên user build dù ADB root; không dùng nó để áp cấu hình.

## Phần modem đã định vị

Modem có schema thật cho feature/data và bảng band theo nhà mạng: LID960/961; NR disable-list2375, enable-list2392, operator-band2380, ENDC combination2437, band-support-PLMN2439. Có cả danh sách đảo để tắt band khi PLMN không thuộc nhóm cho phép. Đã tìm file runtime SP80_636/SP81_133 nhưng body lưu trữ chưa giải mã; không áp offset schema lên ciphertext.

Đã phục hồi 138.558 symbol entry và schema XML từ debug database nằm trong modem image. Handler tiếp theo cần hiểu gồm `atp_e5gopt_hdlr`0x9119f0a6, `l5ath_e5gopt_set_req`0x911abd5c và `l5ath_e5gopt_cmd_handle`0x911abe3a. Đây mới là tên/địa chỉ được metadata khai báo; chưa decode/execute được handler để biết điều kiện từ chối NSA.

MCF có tên các mạng Việt Nam không chứng minh các mạng đó được mở NR. Chưa xác định active SBP bit/list nào chặn 45202/45204. Không có bản patch NVRAM/modem được tạo hoặc tuyên bố hoạt động. Xem [modem findings](../fresh_analysis/nr5g_research/modem/FINDINGS.md).

## Khôi phục sau field test

Khi người dùng muốn trả về cấu hình trước thử:

```powershell
python fresh_analysis/nr5g_research/sa_mode_trial.py --rollback
```

Lệnh dùng backup `runtime/sa_trial_backup.json`, đưa setting về trạng thái mặc định rồi xóa key nếu ban đầu không tồn tại. Nó có thể đổi trạng thái đăng ký mạng ngắn. Không chạy tự động khi người dùng đang thử ngoài đường. Property hiện đã là2; thử dual-mode đã rollback xong.

Không chạy lại `setup_vina_outdoor.py` như một module tự khởi động; đây là bản ghi thao tác có guard cho initial state, không phải bản fix persistent đã chứng minh. Cần kết quả field test trước khi đóng gói giải pháp lâu dài.

## Bằng chứng và tái tạo

- `runtime/before_sa_*`, `after_sa_*`: trạng thái trước/sau thử SA-only.
- `runtime/sa_only_trial_result.json`, `dual_mode_trial_result.json`: kết quả và rollback.
- `runtime/e5gopt_transactions.json`: command/status gắn cùng slot/reader.
- `runtime/carrier_sa_policy.json`, `android/xiaomi_carrier_sa_policy.json`: policy và source APK.
- `runtime/vina_outdoor_setup.json`: trạng thái cuối theo yêu cầu user.
- `ril/pseudocode`, `ril/inventory.json`: focused native exports, có warnings/EXTERNAL boundaries, không toàn source.
- `modem/at_nr_handler_symbols.json`: handler targets, chưa có instruction mapping/proof.

Các log được chọn lọc và che identifier; không lưu raw SIM/NVRAM body. Database Ghidra RIL sinh tự động vượt giới hạn file GitHub được giữ local và bỏ khỏi Git; code đầu vào, scripts, focused exports và báo cáo được version hóa.

Nguồn tham khảo kiến trúc Android: [carrier configuration](https://source.android.com/docs/core/connect/carrier), [allowed-network type masks](https://developer.android.com/reference/android/telephony/TelephonyManager#setAllowedNetworkTypesForReason(int,%20long)), [carrier NR availability](https://developer.android.com/reference/android/telephony/CarrierConfigManager#KEY_CARRIER_NR_AVAILABILITIES_INT_ARRAY). Chúng giải thích API chung, không thay thế bằng chứng riêng của binary dash.
'''
(root/'docs/07-nr5g-investigation.md').write_text(text,encoding='utf-8')
(nr/'README_VI.md').write_text('''# Nghiên cứu 5G/NR

Tài liệu tổng hợp hiện tại: [docs/07-nr5g-investigation.md](../../docs/07-nr5g-investigation.md).

Đã tìm thấy policy SA của VinaPhone và nhánh Phone SA-only. Thử cho phép NSA bị modem trả ERROR. Chưa có NR registration thành công.

**Trạng thái được người dùng yêu cầu giữ để thử ngoài đường:** ro.vendor.radio.5g=2 (gốc), 5g_sa_mode_disabled=0, VinaPhone modem query mode3, SA status true. Chờ kết quả field test. Không tự rollback trước khi người dùng thử.

LK/firmware không đổi. Các phép thử và rollback đều có backup/evidence/commit riêng. Không coi script thử là module fix đã được kiểm chứng.
''',encoding='utf-8')
p=root/'docs/README.md';s=p.read_text(encoding='utf-8');s=s.replace('| [Changelog](CHANGELOG.md) |', '| [07 — Nghiên cứu 5G/NR](07-nr5g-investigation.md) | LK, policy Vina/Viettel, SA-only, hai phép thử và field test đang chờ |\n| [Changelog](CHANGELOG.md) |');p.write_text(s,encoding='utf-8')
p=root/'README.md';s=p.read_text(encoding='utf-8');s+='\nNghiên cứu tiếp theo: [5G/NR và cấu hình để thử VinaPhone ngoài thực địa](docs/07-nr5g-investigation.md). Chưa có bản patch 5G hoạt động được xác nhận.\n';p.write_text(s,encoding='utf-8')
p=root/'docs/CHANGELOG.md';s=p.read_text(encoding='utf-8');s+='''\n## 2026-11-03 — Nghiên cứu 5G sau LK\n\n- Lưu checkpoint before-5g-research-20261103, khảo sát LK/DTB/modem/MCF và đọc shipped telephony code qua ADB theo yêu cầu.\n- Xác định VinaPhone kế thừa SA-off, Viettel có SA-on override; máy có ro.vendor.radio.5g=2 chọn SA-only.\n- Người dùng cho phép thử mạng. Đã thử SA override, thử runtime SA+NSA; modem từ chối NSA và các thử trước đã rollback.\n- Theo yêu cầu mới, bật lại SA cho VinaPhone để người dùng mang ra ngoài thử; trạng thái cuối lưu trong vina_outdoor_setup.json. Chưa xác nhận đăng ký NR.\n- LK và firmware giữ nguyên SHA256. Không flash hoặc chỉnh NVRAM/cài module persistent.\n''';p.write_text(s,encoding='utf-8')
p=root/'AGENTS.md';s=p.read_text(encoding='utf-8');s+='''\n## Trạng thái nghiên cứu 5G hiện tại\n\n- Đọc docs/07-nr5g-investigation.md và fresh_analysis/nr5g_research/runtime/vina_outdoor_setup.json.\n- Người dùng đã cho phép thử chế độ mạng, sau đó yêu cầu giữ VinaPhone bật 5G để thử ngoài đường. Đang để ro.vendor.radio.5g=2 (gốc), Settings.System 5g_sa_mode_disabled=0, modem SA true/mode3; chưa chứng minh NR registration.\n- Không tự rollback thiết lập này trong lúc user field test. Các trial trước đã rollback; không coi ERROR là thành công. Không flash/NVRAM để tiếp tục nếu chưa có mục tiêu/điều kiện được chứng minh.\n''';p.write_text(s,encoding='utf-8')
# Exclude only the regenerable >100MiB radio database from distributable ZIP, matching Git ignore.
p=fresh/'package_results.py';s=p.read_text(encoding='utf-8-sig');s=s.replace("and not p.name.endswith('.lock~')}","and not p.name.endswith('.lock~') and not p.is_relative_to(fresh/'nr5g_research/ril/ghidra_db')}");p.write_text(s,encoding='utf-8')
img=fresh/'output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img';assert hashlib.sha256(img.read_bytes()).hexdigest()=='f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
print('NR research docs updated; field test pending; firmware unchanged.')
