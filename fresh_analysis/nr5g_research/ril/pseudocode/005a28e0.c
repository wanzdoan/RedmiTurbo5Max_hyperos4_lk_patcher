
undefined * xiaomi_build_region_get(void)

{
  uint uVar1;
  char cVar2;
  long lVar3;
  char *pcVar4;
  
  if ((DAT_005ef55c & 1) == 0) {
    xiaomi_get_property_value("ro.miui.build.region",&DAT_005ef45c,&DAT_001f5b96);
  }
  DAT_005ef55c = 1;
  DAT_005ef55b = 0;
  cVar2 = xiaomi_hal_get_logcat_print_flag(0x80);
  if (cVar2 != '\0') {
    uVar1 = *(uint *)PTR_g_xiaomi_ril_process_instance_id_005d4608;
    if (uVar1 < 2) {
      pcVar4 = "vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp";
      lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp"
                            ,0x2f,0x47);
      if (lVar3 != 0) {
        lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp"
                              ,0x2f,0x47);
        pcVar4 = (char *)(lVar3 + 1);
      }
      xiaomi_utils_syslog(7,"SUB[%d][%s:%d] %s: build_region: %s",uVar1,pcVar4,0xade,
                          "xiaomi_build_region_get",&DAT_005ef45c);
    }
    else if (uVar1 == 0xff) {
      pcVar4 = "vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp";
      lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp"
                            ,0x2f,0x47);
      if (lVar3 != 0) {
        lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp"
                              ,0x2f,0x47);
        pcVar4 = (char *)(lVar3 + 1);
      }
      xiaomi_utils_syslog(7,"[%s:%d] %s: build_region: %s",pcVar4,0xade,"xiaomi_build_region_get",
                          &DAT_005ef45c);
    }
    else {
      xiaomi_get_current_pid_name();
      pcVar4 = "vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp";
      lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp"
                            ,0x2f,0x47);
      if (lVar3 != 0) {
        lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp"
                              ,0x2f,0x47);
        pcVar4 = (char *)(lVar3 + 1);
      }
      xiaomi_utils_syslog(7,"[%s][%s:%d] %s: build_region: %s",&DAT_005ede60,pcVar4,0xade,
                          "xiaomi_build_region_get",&DAT_005ef45c);
    }
  }
  cVar2 = xiaomi_hal_get_shell_print_flag(0x80);
  if (cVar2 != '\0') {
    pcVar4 = "vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp";
    lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp",
                          0x2f,0x47);
    if (lVar3 != 0) {
      lVar3 = __strrchr_chk("vendor/xiaomi/proprietary/modem/hal_cmn/utils/src/xiaomi_utils_cmn.cpp"
                            ,0x2f,0x47);
      pcVar4 = (char *)(lVar3 + 1);
    }
    printf("RIL[%s:%d] %s: build_region: %s\n",pcVar4,0xade,"xiaomi_build_region_get",&DAT_005ef45c)
    ;
  }
  return &DAT_005ef45c;
}


