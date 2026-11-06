# Changelog

## 2026-11-06 — Điều tra Integrity H4 khi cùng LK đạt đủ trên H3

- Người dùng xác nhận banking H4 đầy đủ, H4 China không root; cùng LK v3 MTE_OFF trên H3 đạt đủ ba mức nhưng H4 không có mức nào trong app Play Integrity API Checker.
- [Audit mới](../fresh_analysis/h4_integrity_20261106/README_VI.md): sender nguyên byte stock, bốn ca native metadata/state fixtures đạt; không phải attestation thực hoặc verdict Google.
- Giữ firmware nguyên byte; chưa có dữ liệu ADB/verdict/chứng thư để xác định lỗi và chưa tạo patch. Đã push tag checkpoint `before-h4-integrity-investigation-20261106` tại `4f1bcd5`.


## 2026-11-06 — Người dùng xác nhận Policy H4 v3 MTE_OFF boot

- Chốt đúng biến thể `6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52` đã vào Android; normal và diagnostic vẫn chưa có báo cáo boot.
- Cập nhật [biên bản runtime](../experiments/lk_h4_policy_bl2_control_v3/runtime_confirmation.json), README và metadata/bundle; giữ nguyên bytes và tên cả ba ảnh v3, mọi artifact cũ.
- So với v2 MTE_OFF, main/cert không đổi, bộ ba BL2 có chữ ký đổi về donor thương mại đã boot. Chưa kết luận cơ chế lỗi cụ thể hoặc xác nhận OTA/banking/Integrity/stock H4 preloader.


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

## 2026-11-05 — Trở lại H4 hybrid đã boot, chỉ thêm MTE-off

- Input f57d19c9... đúng artifact user từng báo chạy; giữ nguyên source vàruntime report. Checkpointc84149f/tag `before-h4-booted-hybrid-mte-off-20261105` đã push trước sửa.
- [Bản riêng](../experiments/lk_h4_booted_hybrid_mte_off_v1/README_VI.md) SHA7873361e63ade5e6840d985c23c44bbb01fe449e7f89fd3ebbdba0d3d244e2a4,4083424bytes. H4branch34740/file34940 b.eq→b34784; chỉmainprefix180 đổi kèm theo. Unlock16byte/H3BL2/H4AEE-DTB/DER nguyênbyte.
- 48MTE+328unlockcontrols+3H3BL2parser=379native và10DER đạt. Không thêm region/HWC/fakelock; không flash, bản mới chưa boot test.

## 2026-11-06 — ENG stock-policy China, không mở HWC/khóa Global

- Checkpoint449f57a/tag `before-eng-stock-policy-china-only-20261106` trước sửa; giữ user rename H4 MTE-off nguyên byte, sửa link docs theo tên rút gọn.
- [Hai ảnh riêng](../experiments/lk_eng_stock_policy_cn_only_v1/README_VI.md) từ ENG gốc6f386: defaultSHAcfd5c474... vàMTEoffSHAacd35520..., mỗiảnh4043792bytes. Chỉ callback trust ENG2048/China4096, digestCN nằm trong callback216byte; không dùng block từ chối vùng làmdata. HWC/flash-check/lock/AVBpolicy gốc nguyênbyte. MTEoff thêmbranch34d20, prefixmain180 cập nhật.
- Mỗiảnh348native+10DER đạt, Globalkeytrusted0, HWCgốcCNkểcảcountryGlobal. Khôngflash/chưaboottest, khôngroot/relock/OTAclaim.

## 2026-11-06 — H4 stock-policy auto HWC: port chức năng, chưa boot

- Người dùng làm rõ nhánh H3 tốt nhất là `stock_policy_auto_hwc` trong ZIP sáu file, có báo cáo dùng ROM/khóa/banking tốt; chưa đọc lại hash biến thể đang chạy.
- Dựng hai ảnh riêng H4 main/AEE/DTB + signed BL2 ENG H3, nhận khóa ENG/CN/GL, mở chốt vùng đã xác định, auto HWC và normal/MTE_OFF. Giữ getter/setter, AVB flags/root handoff H4; không force khóa/green.
- Đã kiểm offline390 trường hợp main có đối chứng/fixture +3parser +10DER mỗi ảnh. Main fastboot query-error và AEE release gates vẫn được phân biệt trong docs; không gọi là chuyển đầy đủ sang ENG chính thức.
- Chưa flash, lock hoặc chứng minh boot với stock preloader H4; không suy từ bản H4 f57 dùng BL2 H3 release. Xem [artifact và evidence](../experiments/lk_h4_eng_policy_auto_hwc_v1/README_VI.md).

## 2026-11-06 — Kiểm đúng preloader H4 với hai LK H4 stock-policy mới

- Đúng preloader SHA104bb972...; bảy ảnh tham chiếu/new được khóa hash. Chạy coordinator cert2/ASN.1/key compare/PSS native; SHA/RSA dùng backend toán học host, không ép signature-valid.
- 7cert2 positive,7cặp hash native,8locator,21parser BL2,21đối chứng lỗi,48policy đạt kiểm tra phạm vi; hai H4 mới và nhánh H3 stock-policy giữ cùng signed BL2 ENG triple.
- Không thay firmware hoặc flash/lock/đọc trạng thái bảo mật điện thoại. Chưa xác nhận hardware acceptance/rollback/handoff/full boot. Docs và ZIP evidence H4 cập nhật; hashes ảnh giữ nguyên. [Evidence](../fresh_analysis/h4_preloader_lk_acceptance_20261106/README_VI.md).

## 2026-11-06 — V1 H4 MTE_OFF vào recovery; ứng viên v2 bỏ gate Secure ID tại boot caller

- User báo thử China và Global với preloader ENG `preloader_dash (1).bin` SHA75e93936...; chỉ vào recovery, không thông báo chữ đỏ. Chưa có log/readback hoặc actual lock-state.
- Phát hiện caller H4 `0x14714` gọi Secure-ID checker sau AVB, lỗi gọi recovery; caller ENG H3 không có block tương ứng. V1 chỉ mở chốt vùng bên trong, vẫn giữ gate tổng này.
- V2 đổi riêng instruction payload0x147ac `b.eq` thành `b` tới0x147d4 và cập nhật hash prefix; giữ getter/setter/AVB/root handoff và mọi chức năng v1. Đây là bỏ một vendor hardware-binding gate tại call site, không sửa provisioning.
- Mỗi ảnh:96cặp caller,8cặp checker gốc;8ENG control chung. Hồi quy390trường hợp main +3parser +10DER mỗi ảnh, cùng native audit BL2 trên stock preloader H4. Chưa flash/boot bản sửa; chưa chứng minh nguyên nhân recovery thực. [Docs và file](../experiments/lk_h4_eng_policy_recovery_fix_v2/README_VI.md).

## 2026-11-06 — V2 không sửa được recovery; kiểm AVB toàn hàm và đối chứng BL2 v3

- User báo v2 fastboot unlocked:no, cho flash, vẫn recovery; China OS4.0.3.0 sạch không root, ENG H3 boot cùng ROM. Không có máy/log/readback; tiếp tục offline theo yêu cầu.
- Chạy native avb_slot_verify + loader đến chọn image buffers, danh sách yêu cầu lấy từ pointer table firmware gồm boot/vendor_boot/init_boot/countrycode. H4 v2 và H3 ENG đều qua fixtures locked4/rollback0/CN; cmdline và5packet root handoff khớp với OEM-key/SMC fixtures.5đối chứng hash/signature/IO/rollback lỗi đúng. Chưa kiểm full hardware/kernel/FBE.
- Bản unlocked H4 f57 dùng BL2 H3 thương mại, v2 dùng ENG BL2: đây là biến số thứ hai ngoài trạng thái khóa. Hai v3 policy giữ nguyên main/cert v2, chỉ đổi BL2 triple sang đúng donor f57; normal8ac64a98...,off6282d02b.... Control unlocked ENG/off52aab527... tách trong diagnostic.
- Kiểm DER/parser/preloader auth thành phần và native AVB/loader trên từng ảnh; không nâng thành boot thành công. Giữ mọi firmware cũ nguyên byte, không thêm bypass chữ ký/hash. [Docs](../experiments/lk_h4_policy_bl2_control_v3/README_VI.md).
