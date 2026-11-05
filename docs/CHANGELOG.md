# Changelog

## 2026-11-04 — Tiền kiểm lock thật và OTA China

- Push checkpoint `before-stock-relock-ota-research-20261104` ở`e3e27af`, giữ mọi artifact trước.
- 152 ca native getter/setter và20 ca chốt vùng stock, không I/O thiết bị trong mô phỏng. Xác định lock ghi sentinel thay token, getter lỗi thường về locked4 và có nhánh khởi tạo magic.
- Theo ủy quyền chỉ đọc bằng su: hai LK live trùng token-key clone69089..., kernel bootconfig unlocked/orange trái props bị che; log ghi token256 byte được chấp nhận và Secure ID bị skip.
- Secinfo fallback kiểm signature OEM H3/H4 và3 binding đạt;12 ca native matcher đạt. Primary RPMB chưa đọc. Không quy fallback máy này là hỏng hoặc quy ca bạn người dùng từ máy này.
- 4 metadata AVB ký đúng khóa China;5/6 descriptor đã đọc đạt, init_boot_a lệch hash; init_boot_b và file host sạch trùng nhau. Chưa ghi phục hồi, lock, flash, reboot hoặc gọi RPMB/efuse.
- Lưu kết quả đã lược thông tin và script; raw riêng thiết bị ở thư mục tạm ngoài Git. Chưa chứng minh lock/OTA máy thật.

## 2026-11-04 — Unlock bản LK ENG mới

- Lưu input Downloads/lk.img SHA256`6f386241...` nguyên byte; push commit`51bd2ae`, tag`before-h3-eng-unlock-patch-20261104` trước sửa.
- Tìm lại getter main ở file0x898e4, patch state3/success0; giữ BL2/AEE/DTB và mọi thành phần của ENG, không ghép build khác.
- Chạy chính BL2 ENG mới: policy vẫn yêu cầu auth, parser chọn được prefix hash ngoài DER. Thêm prefix180 byte, giữ signed DER, output tăng176 byte.
- Output SHA256`0d237263a24f6bf41fa33b5dc43bb7d71e5b61f1977331c3f9f1812ad5000466`;342 ca CPU main,9 parser,4 policy và10 DER signature đạt. Chưa flash/boot máy thật.

## 2026-11-03 — Thêm CN/Global trên H3 đã patch

- Checkpoint đã push trước sửa: `before-h3-patched-global-region-20261103`.
- Dùng đúng `lk_hyperos3_patched_unlockBL/dash.img` input e164..., định vị lại ba chốt vùng trên binary H3 patched; không dùng offset hoặc thành phần H4.
- Tạo artifact riêng SHA256 `dbabb26989e9270a1b2ebd747820dca7f3630779bb5b5f54b21c30209a4b00b8`, giữ unlock và các thay đổi cũ, cập nhật prefix main cert2.
- 532 ca CPU,10 chữ ký DER và6 ca parser đạt; chưa flash hoặc boot test, không tuyên bố fix5G.

## 2026-11-03 — LK-only: modem handoff và chốt vùng ROM

- Giữ checkpoint nghiên cứu `363ee37`, tag `before-lk-global-region-patch-20261103` trước tạo firmware mới.
- Lần từ LK image_type13 sang kernel thật: mask0x7b đã có N/NR0x40; không tạo patch 5G giả dựa trên descriptor.
- Tạo riêng LK thử bỏ chốt SKU và cho khóa CN/GL đi qua hai đường xác thực vùng, giữ unlock và xác thực chain/khóa lạ trong nhánh được kiểm tra.
- 532 ca CPU, 10 DER signature, 6 ca parser và kiểm deterministic/input identity đạt. Chưa có ROM Global đích hoặc máy thật xác nhận; không flash hay sửa Android/modem/preloader.

## 2026-11-03 — Hoàn nguyên radio và bộ thử region riêng

- Người dùng báo hai lần sọc màu/đen màn, có mở MBBank; evidence còn lại không đủ xác định nguyên nhân. Lưu log thô riêng tư ngoài Git và metadata chọn lọc trong crash_review.
- Hoàn nguyên thử nghiệm mạng theo yêu cầu: property 5g=2, key SA absent/null, preferred mode 33,33. Checkpoint đã push: `0d74255`, tag `radio-restored-before-region-research-20261103`.
- Tạo `experiments/radio_region_global_v1`: LK đã boot giữ nguyên byte, kèm module chỉ đổi `ro.vendor.radio.build_region=global`. Chưa cài, chưa xác nhận sửa được 5G; không tuyên bố đây là LK patch 5G.
- Kiểm tra ZIP/source, LF, hash LK, syntax shell và các guard installer bằng mock trên máy tính. Không flash hoặc thay cấu hình máy trong bước này.

## 2026-11-03 — Versioning và cập nhật kết quả

- Dừng kiểm tra preloader theo yêu cầu người dùng sau khi họ báo tự test được.
- Ghi ROM OS4.0.3.0.XPLCNXM, boot với ENG H3, fastboot unlocked yes và báo cáo test stock H4 theo ngữ cảnh.
- Tạo GitHub repo private, commit checkpoint `134da6d`, push tag `boot-working-before-docs-20261103` trước khi sửa docs.
- Thêm root README, docs đầy đủ, runtime sidecar và workflow phiên bản/rollback cho agent tương lai.
- Giữ nguyên firmware/hash; cập nhật bundle và tài liệu bằng commit riêng, tag `docs-confirmed-boot-20261103`.

## 2026-11-03 — Reverse và bàn giao ban đầu

- Reverse ba LK và hai preloader từ đầu; phát hiện patch getter state3/return0 dài 16 byte.
- Xác định donor H3 khác build, không port thay đổi DTBO/sạc không liên quan.
- Phục hồi ASN.1 hash prefix, hash domains, DER/PSS và donor signature relationship.
- Phát hiện BL2 H4 chọn hash trong TBS, chặn cách prefix H3 ở đường đã xét.
- Loại đối chứng chỉ patch main H4; tạo hybrid H4 BL33/AEE/DTBs + intact signed H3 BL2 triple.
- Chạy CPU/parser/crypto/rebuild checks, xuất pseudocode và bàn giao artifact.
- Người dùng báo boot thành công sau bàn giao.

## 2026-11-03 — Nghiên cứu 5G sau LK

- Lưu checkpoint before-5g-research-20261103, khảo sát LK/DTB/modem/MCF và đọc shipped telephony code qua ADB theo yêu cầu.
- Xác định VinaPhone kế thừa SA-off, Viettel có SA-on override; máy có ro.vendor.radio.5g=2 chọn SA-only.
- Người dùng cho phép thử mạng. Đã thử SA override, thử runtime SA+NSA; modem từ chối NSA và các thử trước đã rollback.
- Theo yêu cầu mới, bật lại SA cho VinaPhone để người dùng mang ra ngoài thử; trạng thái cuối lưu trong vina_outdoor_setup.json. Chưa xác nhận đăng ký NR.
- LK và firmware giữ nguyên SHA256. Không flash hoặc chỉnh NVRAM/cài module persistent.

## 2026-11-05 — HWC tự chọn vùng cho ENG unlocked và fakelock v2

- Checkpoint0975d26 và tag `before-eng-unlocked-fakelock-auto-hwc-20261105` đã push trước sửa.
- Hai [biến thể HWC](../experiments/lk_h3_eng_auto_hwc_v1/README_VI.md) giữ nguyên input, callback tin khóa và toàn bộ BL2. Literal GLOBAL nằm trong block thay thế, tránh ghi vào epilogue callback của input.
- Unlocked64e91b35... đạt486 ca main; fakelockv25345d07e... đạt599 ca main; mỗi ảnh10DER+3parser. Chưa flash/boot test, chưa đổi MTE.

## 2026-11-05 — Ba cặp all-region HWC, MTE mặc định/off

- Theo xác nhận user: ENG unlocked, fakelock v2, stock-policy; mỗi nhánh có hai file riêng. [Docs và ZIP sáu file](../experiments/lk_h3_eng_mte_off_v1/README_VI.md). Các file non-off và input trước giữ nguyên byte.
- Checkpoint71caf4c/tag `before-eng-three-variants-mte-off-20261105` đã push trước thêm patch MTE. Đổi duy nhất nhánh main0x34d20 và prefix trong từng ảnh off; không sửa preloader.
- [MTE research](../fresh_analysis/mte_research/README_VI.md) tìm preloader debug OR7 và đường kernel_mte_init → arm64.nomte có thật. 48LK+168preloader native case đạt. Không quy MTE là nguyên nhân crash cũ, không mô tả tắt ngược preloader/secure-world.
- Final MTE-off: unlocked3cc3dd40...534main, fakelockv29af2950d...647main, stock-policyb2535248...208main; mỗi ảnh10DER+3parser. Chưa flash/boot test.

## 2026-11-05 — Kiểm tra lại tương thích stock preloader theo yêu cầu mới

- Checkpoint `before-eng-stock-preloader-compatibility-20261105` đã push trước nghiên cứu; không flash/sửa firmware. [Docs11](11-eng-preloader-compatibility.md).
- Xác nhận preloader đúng ROMH4 là104bb...; stockH3China317487.../Global4f43... khác file bcde... từng gọi ENG. Policy3stock qua288 lời gọi native tương đương;66caDFD nêu khác biệt thật về marker/timeout nhưng chưa xác định đó là nguyên nhân brick.
- 10 chữ ký BL2+5 cặp hash đạt; sáu ENG giữ cùng nền BL2/AEE/DTB. H3patched và H4hybrid cùng BL2code/header/cert1; cert2 cùngTBS nhưng chữ kýPSS khác, đều hợp lệ. Hybrid triple từ H3original.
- Không suy parser BL2H4 gây ca brick H3patched+H4preloader; không cấp xác nhận flash stockH3/H4 cho sáuENG từ phân tích tĩnh.

## 2026-11-05 — Phân tích Fenrir bên ngoài so với fakelock v2

- Checkpoint53e9a62/tag `before-fenrir-fakelock-analysis-20261105` đã push trước công việc. Inputdee32d18... giữ nguyên trong fresh_analysis/fenrir_review/input.
- MainFenrir chỉ khác63byte trên H3stock, AEE11byte; BL2/DTB/DTBO nguyên byte. Commonwrapperstate4, setterverifiedbootzero; mở gate main/AEE. Certificateprefixđơn90,10DERgốcvalid,12parsernative; không ký OEM mới.
- 199main(publicseccfg/LKSgồm10),80AEE,160H3/Fenrirroot+20v2rootnativecases đạt. 80crosscheckpacket cho hashOEM/lock1/state0 giốngv2; không chứng minh attestation tốt hơn. Fenrirche cachelỗi; publicseccfg giữactualgetter khác LKS1, v2 cóoverride riêng.
- Không patch v2, không flash, không boot/OTA/Integrity claim. [Docs](../fresh_analysis/fenrir_review/README_VI.md) và findings.json tách evidence/gaps.

## 2026-11-05 — Hai hybrid Fenrir/v2 trên nền stock H3 và debug H3

- User cho biết Fenrir root được; stock-policy flash init_boot root thành công nhưng boot recovery, phục hồi init_boot gốc thì vào OS. Không quy đây là lỗi v2 hoặc nguyên nhân đã log-proven. Báo cáo riêng trong runtime_confirmation.json.
- Checkpoint552072a/tag `before-fenrir-v2-hybrid-two-bases-20261105`; prototype chưa boot tại46c11af/tag `before-hybrid-rollback-maintenance-guard-20261105`. Không sửa artifact đã boot hoặc sáu file trước.
- Tạo đúng2[ảnh hybrid](../experiments/lk_fenrir_v2_hybrid_v1/README_VI.md): stock50e1fe9d... vàdebuga8b894d4..., từFenrir vàdebug6f386 riêng, không ghép BL2 chéo. Commonlocked4/cachegreen0/AVBallow1 +v2publicseccfg/flash.locked +HWCauto; MTEgốc. Debug vốn không có cácgateFenrir phải bỏ trênrelease nênAEEdebug giữ nguyên.
- Rootpathnative hashmismatch3 đượcfilter/callerflags1 cho qua; lỗiIO/descriptorvẫnloại. Predicate theo locked còn có đườngmaintenance rollback, không phảiinitbootverifylần2; NOP riêngcall688b8/69688 giữ skip của v2, helperOTP chung nguyênbyte.
- Finalnative406stock/472debug, mỗiảnh10DERgiữlại; chưa boot/root/OTA/Integrity. ZIP2ảnh cómanifest/CRC/SHA kiểm.

## 2026-11-05 — Quay về Fenrir gốc, chỉ HWC và MTE-off

- User báo file hybrid không dùng được, giống LK gốc; chưa cung cấp exact flashedhash/log. Theo yêu cầu mới không tiếp tục ghép v2 hoặc guard bảo trì.
- Checkpointdafba4f/tag `before-fenrir-only-hwc-mte-20261105`; inputFenrirdee32..., output8d616abb51cd48373d7a892013c080b902a811983133bfc42d1a0accd73e30e8,4083872bytes. [Docs](../experiments/lk_fenrir_only_auto_hwc_mte_off_v1/README_VI.md).
- Chỉ3spanHWC152bytes+MTEbranch4bytes stock344a8, mainprefix90 cập nhật. Toàn bộ byte khác/Fenrircore/AEE/BL2/cert/DTB/trailer nguyên byte. 230native+10DERpass, chưa flash/boot/root test.

## 2026-11-05 — Sửa cách gọi all-region và audit lỗi POCO H3

- User báo bản8d616...trênROMPOCOH3 chỉ vềpreloader. Localuserfilelk_fakeloc_vip.img trùnghash8d616..., đượccheckpoint5d029fe/tagbefore-fenrir-full-region-audit-20261105. Không cóROMđích/log/readbackđểchốtstage.
- Xác nhận HWC-only chưa mở CN/Global; không gọi đó là full all-region. [Bản sửa gate riêng](../experiments/lk_fenrir_cn_global_gates_v2/README_VI.md) SHAe6bd99c91854a916fc675a57f5f18c139b72868a5ebdafd7b72088b03afd3e2f, thêmSKU68d84,imagekey88610,AVBkeyc53dc vàSecureIDregion-recovery87098. Fenrirlocked4khiếnchốtcuốicònreachable, khácunlocked3.
- 768gate+147HWC+24MTE+3parser=942native,10DERđạt; giữchain/error/unknownkeychecks. ChưaPOCOdeviceboot, chưaquyđâylànguyênnhânhayđãchữalỗipreloader.
