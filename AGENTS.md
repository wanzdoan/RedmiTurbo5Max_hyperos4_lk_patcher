# Hướng dẫn làm việc trong repository

## Yêu cầu phiên bản của người dùng

- Repo GitHub phải giữ PRIVATE: `wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher`.
- Trước khi sửa file hiện có, kiểm tra Git; trạng thái trước sửa phải có checkpoint commit có thể quay lại và được push. Nếu workspace có thay đổi chưa lưu, lưu checkpoint trước công việc mới. Không bỏ mất thay đổi của người dùng.
- Với sửa logic patch/firmware, thêm tag checkpoint trước sửa; commit thay đổi riêng và push. Không amend/rewrite/force-push các mốc đã bàn giao hoặc đã boot.
- Không cần xin lại phép cho commit/push thông thường trong repo private này khi nằm trong tác vụ người dùng đã giao; không thay đổi visibility.

## Nguồn đúng và artifact

- Đọc root README và docs/README.md trước. Đợt reverse hiện hành ở fresh_analysis; các thư mục cũ được giữ làm lịch sử, không mặc định đúng.
- Bản đã được người dùng xác nhận chạy: fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img.
- SHA256: f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171.
- Nó là H4 main/BL33 patch 16 byte + H3 signed BL2 triple + H4 AEE/DTBs. Không mô tả là thuần H4.
- Sửa docs không thay bytes/tên artifact đã chạy. Phân biệt machine-test scope với runtime report của người dùng trong runtime_confirmation.json.
- Chỉ thay firmware hoặc flash thiết bị khi tác vụ người dùng thực sự yêu cầu; không tự flash để kiểm docs. Người dùng đã yêu cầu dừng điều tra stock preloader sau khi họ tự test được.

## Kiểm chứng

- Không chạy builder bằng Python -O vì assert đang bảo vệ input/layout.
- Khi đổi firmware/patch, chạy tests phù hợp trong docs/03-build-and-validation.md và ghi input/output hashes.
- Khi chỉ sửa docs, kiểm link/metadata, firmware hash và bundle; không cần lặp toàn bộ mô phỏng CPU.
- Duy trì docs và commit/tag để lần sau có thể lấy lại bản cũ mà không hard reset hay force push.

## Trạng thái nghiên cứu 5G hiện tại

- Đọc docs/07-nr5g-investigation.md và fresh_analysis/nr5g_research/crash_review/rollback_and_current_state.json. vina_outdoor_setup.json chỉ là lịch sử, không còn là trạng thái cuối.
- Sau hai lần sọc màu/đen màn, người dùng yêu cầu hoàn nguyên. Đã xác nhận ro.vendor.radio.5g=2, Settings.System 5g_sa_mode_disabled absent/null, preferred_network_mode=33,33. Nguyên nhân treo chưa xác định; không tự bật lại thử nghiệm SA/NSA.
- Yêu cầu mới là bộ thử ro.vendor.radio.build_region=global tách riêng. experiments/radio_region_global_v1 chứa LK nguyên byte đã boot + module property-only; chưa cài lên máy, chưa chứng minh sửa được NR. Không mô tả LK sao chép là LK patch 5G.
- Không coi ERROR là thành công. Không flash/NVRAM để tiếp tục nếu chưa có mục tiêu/điều kiện được chứng minh.
- Người dùng sau đó yêu cầu chỉ sửa LK và tìm chốt ROM China/Global. Đọc docs/08-lk-rom-region.md. experiments/lk_global_region_v1 là LK thử riêng đã qua CPU/parser/DER checks, chưa flash/boot test và không phải fix5G đã chứng minh. Bản f57... đã boot vẫn là điểm khôi phục.
- Evidence mới: H4 modem image_type13 -> kernel ccci_util_lib mask0x7b đã có N/NR0x40. Không patch descriptor13->14 để tuyên bố bật NR. Không cài module region phía Android trong phạm vi LK-only.
- Người dùng yêu cầu thêm biến thể từ đúng H3 đã patch: experiments/lk_h3_global_region_v1. Input là lk_hyperos3_patched_unlockBL/dash.img hash e164..., không phải H3 original và không ghép H4. Output dbabb269... giữ unlock/các sửa cũ, chỉ thêm ba chốt vùng và cập nhật prefix; qua mô phỏng nhưng chưa boot test. Giữ nguyên toàn bộ input/artifact cũ.
- Ngày2026-11-04 có input LK ENG mới từ Downloads/lk.img SHA6f386241...; bản gốc được lưu nguyên byte trong experiments/lk_h3_eng_unlock_v1/input. Đọc docs/09-h3-eng-unlock.md. Artifact unlock ENG output0d237263... chỉ patch getter và thêm prefix, giữ chính BL2 ENG mới (parser0x6ea70), không ghép donor và không thêm region/5G. Qua342 CPUmain+9parser+4policy+10DER; chưa flash/boot test. Không dùng offset H3/H4 cũ cho input này.
- Mục tiêu mới là lock thật + OTA China trước; user chỉ cho lấy thông tin qua ADB/su, nhấn mạnh không flash. Đọc docs/10-stock-relock-and-ota.md và fresh_analysis/relock_ota_research. Chưa có quy trình relock an toàn được chứng minh; không chạy lock/flash/reboot/erase hoặc trực tiếp RPMB/efuse.
- Live4/11: H4 China OS4.0.3.0, slotA; cả hai LK là token clone69089...; kernel/log unlocked3/orange dù props Android bị che locked/green. init_boot_a lệch hash ký; init_boot_b và file host gốc đúng3d2062... .4 AVB metadata hợp chữ ký China,5 hash ảnh khác đạt; hashtree/rollback/cảslot chưa đủ.
- Secinfo fallback trên máy hiện tại có chữ ký OEM H3/H4 và3 binding khớp bootconfig; literal thật SECINFO-BOOT!, không dùng label Ghidra sanitizedSECINFO_BOOT. Primary RPMB vẫn chưa đọc; fallback hợp lệ không chứng minh cả gate sẽ pass. Lock chuẩn ghi đè token bằng sentinel và có thể wipe; chưa có backup chữ ký256 byte hiện tại.
- Raw private (LK/init_boot/secinfo/log) chỉ giữ ngoài Git ở đường dẫn trong live_readonly_snapshot.json. Không đưa CPUID/token/provisioning raw vào repo. Getter LK gốc có nhánh ghi magic nên không coi mọi getvar gọi getter là chỉ đọc. Ca bạn user brick cả không chạy lock nhưng thiếu đúng build/log; không gán nguyên nhân từ máy hiện tại.

- Ngày2026-11-05 user yêu cầu dừng nghiên cứu lock/OTA. Bảo tồn kết quả, không tiếp tục thu thập live hoặc thử lock. Nhiệm vụ mới: hai biến thể HWC tự động trên fakelockv2 và ENGunlocked, cùng nghiên cứu MTE của preloader ENG; chưa có ủy quyền flash.

- 2026-11-05: hai bản HWC tự chọn vùng từ ENG unlocked0d237... và fakelockv272e170... ở experiments/lk_h3_eng_auto_hwc_v1. Output64e91b35... và5345d07e..., chưa flash/boot test, chưa sửa MTE. Xem README/manifest/validation; giữ nguyên callback gốc, GLOBAL literal trong block0x133dc, không dùng0xc5954 của bản stock-policy tham chiếu.

- 2026-11-05 user xác nhận bộ6file: ENG unlocked, fakelockv2, stock-policy; tất cả HWC auto, mỗi nhánh bản MTE mặc định và MTE_OFF riêng. Xem experiments/lk_h3_eng_mte_off_v1/README_VI.md và release_manifest.json. Patch off payload0x34d20/file0x34f20 20020054→11000014, ép nhánh LK có sẵn xuất arm64.nomte, cập nhật prefix. Không sửa preloader/misc; không gọi đây là tắt MTE hồi tố trong preloader/secure-world. Outputoff3cc3dd40...,9af2950d...,b2535248...;534/647/208main+10DER+3parser mỗi ảnh, chưa boot test. Nghiên cứu fresh_analysis/mte_research có48LK+168preloader; raw boot-LUN giữ ngoàiGit. Không flash hoặc tự đổi MTE/misc trên máy.

- Ngày2026-11-05 user mở lại điều tra stock preloader để đánh giá sáuENG; đây là ủy quyền nghiên cứu offline mới, không flash. Đọc docs/11-eng-preloader-compatibility.md và hai thư mục fresh_analysis/{preloader_compatibility_20261105,eng_family_compatibility_20261105}. StockH3China317487.../Global4f43...; stockH4104bb... đúngROM. 288policy+66DFDnative và10BL2signature/5hashpair đạt nhưng chưa chốt nguyên nhân brick hoặc xác nhận stockH3/H4boot6ENG. H3patchedBL2code/header/cert1giốnghybrid, cert2TBSgiống nhưngPSSsignaturekhác; hybridtripleexactH3original. Không tuyên bốtripleH3patched/hybridnguyênbytegiốngnhau. MTE/HWCpatchtrongmain khôngsửalỗi trướcmain.

- Fenrir external input ngày05-11: fresh_analysis/fenrir_review/input/dash-fenrir-versioned.bin SHAdee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113,4083872bytes. Đây là patch trênH3stock, không phải nềnENG/userdebug6f386. Main63bytes/AEE11bytes; BL2/DTB/DTBO stocknguyênbyte; prefix90main/AEE;768zero tail. Commonwrapperstate4, cachegreen0, mởgate main/AEE. Xem README/findings/nativeJSON:199main+80AEE+160rootH3/Fenrir+20v2root+12parser,10DERvalid. Packetkey/lock/state không tốt hơnv2đượcchứngminh; seccfg riêng vẫnactualgetter(v2fakepublic1), checảnhbáocachelỗi. Không tựmergepatch, khôngflash hoặcclaimOTA/Integrity/stockpreloadercompat.

- User ngày05-11đãủyquyền kết hợpFenrir+v2 thành2ảnh, vượt yêu cầu trước đó chỉresearch/khôngtựmerge. Finalởexperiments/lk_fenrir_v2_hybrid_v1: stockSHA50e1fe9d598da67021ba22fe4e67c52ccd539a076ada116ccbdd2f66a7daec02/debugSHAa8b894d476d8af889b0f82f746a523d19591d4e30c2a122dd0c7b17302692a62. Common4,cache0,AVBallow1,publicseccfg1/flash.locked1,HWCauto,MTEgốc. StocknguồnFenrirdee32/debugnguồn6f386, giữownBL2/AEE/DTB;debugAEEgatesđãcompileoutnênkhôngpatch. Nativeinitboothash/filter/callerroot37vàNOPguard6mỗiảnh;total406/472+10DER. GuardNOPcallstock688b8/debug69688 không sửaOTPhelper. Prototype41125/c2c2chỉlịchsử46c11af(tagbefore-hybrid-rollback-maintenance-guard...), chưaboot;khôngnhầmhashfinal. XemREADME/manifest/validation/runtime_confirmation; userbáostock-policy(khôngphảiv2)flashrootinitbootxongrecovery;Fenrirrootđượcchỉuserreport. Chưaxácnhận2ảnhmớiboot/root/preloaderstock/OTA/Integrity,khôngtựflash. HWCportmớigiữHWcopyearlyđểconsumerởgiữaluồngkhôngđọcdữliệuchưasẵn;khôngsửabytebảncũ.

- Yêu cầu mới nhất05-11 sau userbáohybridskhôngdùngđược: chỉFenrirgốcdee32... +HWCauto+MTEoff, KHÔNGghépv2/guardmaintenance. Finalexperiments/lk_fenrir_only_auto_hwc_mte_off_v1 SHA8d616abb51cd48373d7a892013c080b902a811983133bfc42d1a0accd73e30e8. ChỉHWCstock13a78/14050/140b0 vàMTEstock344a8 20020054→11000014 +mainprefix90; mọi bytekhác nguyênFenrir, gồmcallbảotrì688b8vàpublicseccfg/LKS/report. 230native+10DER, chưaboot/root. Khôngdùnghybrid50e1/a8b8làminput; khôngtựthêmpatchlock/verify/OTP hayflash. Runtimereporthybridkhôngdùngđược nhưngkhôngexacthash/log; giữlịchsử,khôngquynguyênnhân.

- Latest05-11: userbáo8d616FenrirHWC/MTEtrênPOCOH3 chỉvềpreloader; ROMkhôngtrênPC/build/logchưacó. Localuntrackedlk_fakeloc_vip.imgtrùng8d616,đãcheckpoint5d029fe+tagbefore-fenrir-full-region-audit-20261105. HWC-onlyKHÔNGmởCN/Globalgates;khônggọiallregionboot. Newexperiments/lk_fenrir_cn_global_gates_v2 SHAe6bd99c91854a916fc675a57f5f18c139b72868a5ebdafd7b72088b03afd3e2f adds4gateSKU68d84/key88610/AVBc53dc/SecureIDregionrecovery87098 trên8d616;40bytecode+prefix90,khôngv2/OTPguard. 942native+10DER;POCOSKUfixturekhôngROMthật,chưaboot/flash/root/preloadercompatproof. Giữchain/extract/fuseerrors;unknownkeynottrusted;nonCNunknownimagemismatchbecomesstricter. KhôngbypasswholeSecureID,khôngtựflash.

- Latest05-11userquaylạibảnH4đãbáochạythànhcông: basef57d19c9... (fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img), khôngFenrir/H3debug. Newexperiments/lk_h4_booted_hybrid_mte_off_v1 outputSHA7873361e63ade5e6840d985c23c44bbb01fe449e7f89fd3ebbdba0d3d244e2a4,4083424bytes. ChỉH4MTEbranchpayload34740/file34940 20020054→11000014 vàprefix180, nguồn vàmọibytekhácgiữnguyên. NativeH4hook346c8/init346f4/global16dac0/lookup1a348/append19078. 379native+10DER, chưaboot; khôngthêmHWC/region/fakelock,khôngtựflash. Giữsourcef57 vàruntime reportgốcnguyênbyte; khôngsuybootnguồnlàbootảnhMTEoff.

- Latest06-11user yêu cầu ENGgốc stock_policy China-only,2file(default/MTEoff),khôngHWCGlobal. Experiments/lk_eng_stock_policy_cn_only_v1 từ6f386, defaultSHA cfd5c47422590843371f2e2b119491ba13aebe800f0850a8214bf69d6010c7c9 /offSHA acd35520ae9d2e0ac6511c6b28ece743793b24c240370b077dde6539e7f7c525,4043792bytes. Chỉcallbackc5888..c595f trustENG2048+CN4096,CNdigestinline;khôngGLhash/không68e7cbranch/khôngHWCpatch. HWCgốcCNtrênhwChina, khôngépCNtrênhwGlobal. Stockgetter/setter/verify/report giữgốc,khôngforceunlock/fakelock;sourceunlocked/factory/configbypassvẫngốc. OffthêmMTE34d20 vàmainprefix180;348native+10DERmỗiảnh,chưaboot. UserđổitênH4MTEoffthànhlk_hyperos4_MTE_OFF_TEST.img hash787...nguyênbyte; đãcheckpoint449f57a/tagbefore-eng-stock-policy-china-only-20261106, giữtênmới,oldbuildercóthểtáitạotêncanonical.


- 2026-11-06: người dùng xác nhận Policy H4 v3 MTE_OFF đã vào Android, chọn rõ file experiments/lk_h4_policy_bl2_control_v3/lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img SHA256 6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52. Giữ nguyên tên/bytes. Main/cert main nguyên v2 MTE_OFF; chỉ đổi signed BL2/cert1/cert2/layout về đúng bộ H3 thương mại từ f57, không phải ENG BL2. Normal/diagnostic chưa có runtime report. Xem README và runtime_confirmation.json tại thư mục v3: báo cáo người dùng, không đọc lại máy; không tự suy fastboot locked/banking/Integrity/OTA/Global/root/stock H4 preloader hoặc MTE hiệu lực. Cấu hình China sạch + preloader ENG chỉ là ngữ cảnh trước, chưa reconfirm lần boot. Không flash/lock/reboot để kiểm docs.


- 2026-11-06 user làm rõ cùng Policy H4 v3 MTE_OFF 6282d02b trên ROM H3 đạt cả ba mức Integrity, trên H4 China OS4.0.3.0.XPLCNXM không root không được mức nào; banking H4 hoạt động đầy đủ. Kiểm trong Play Store, chưa có nguyên văn verdict/lỗi hoặc phân biệt chắc màn Integrity với Play Protect. Nghiên cứu fresh_analysis/h4_integrity_20261106: sender root-trust H4 nguyên byte stock; 4 native metadata/state fixtures đạt, không phải thử chứng thư/TEE/Google. ADB chưa thấy máy, collector allowlist chỉ đọc và ghi ngoài Git; không tự giả root chứng thực/phiên bản hoặc tuyên bố fix. Cùng LK/hai ROM là đối chứng chính; chưa có firmware sửa Integrity.
