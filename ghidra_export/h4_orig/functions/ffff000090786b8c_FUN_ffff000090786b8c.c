// ffff000090786b8c  size=1108  FUN_ffff000090786b8c

int FUN_ffff000090786b8c(void)

{
  int iVar1;
  uint uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  char *pcVar7;
  long lVar8;
  ulong uVar9;
  int local_a3c;
  undefined8 local_a38;
  undefined8 local_a30;
  undefined8 local_a28;
  undefined8 uStack_a20;
  undefined8 local_a18;
  undefined8 uStack_a10;
  undefined8 local_a08;
  undefined8 uStack_a00;
  undefined8 local_9f8;
  undefined8 uStack_9f0;
  undefined8 local_9e8;
  undefined8 uStack_9e0;
  undefined8 local_9d8;
  undefined8 uStack_9d0;
  undefined8 local_9c8;
  undefined8 uStack_9c0;
  undefined8 local_9b8;
  undefined8 local_9b0;
  undefined8 uStack_9a8;
  undefined8 local_9a0;
  undefined8 uStack_998;
  undefined8 local_990;
  undefined8 uStack_988;
  undefined2 local_980;
  undefined8 local_978;
  undefined8 uStack_970;
  undefined8 local_968;
  undefined8 uStack_960;
  undefined8 local_958;
  undefined8 uStack_950;
  undefined8 local_948;
  undefined8 uStack_940;
  undefined8 local_938;
  undefined8 uStack_930;
  undefined8 local_928;
  undefined8 uStack_920;
  undefined8 local_918;
  undefined8 uStack_910;
  undefined8 local_908;
  undefined8 uStack_900;
  undefined8 local_8f8;
  undefined8 uStack_8f0;
  undefined8 local_8e8;
  undefined8 uStack_8e0;
  undefined8 local_8d8;
  undefined8 uStack_8d0;
  undefined8 local_8c8;
  undefined8 uStack_8c0;
  undefined8 local_8b8;
  undefined8 uStack_8b0;
  undefined8 local_8a8;
  undefined8 uStack_8a0;
  undefined8 local_898;
  undefined8 uStack_890;
  undefined8 local_888;
  undefined8 uStack_880;
  undefined8 local_878;
  undefined8 uStack_870;
  undefined8 local_868;
  undefined8 uStack_860;
  undefined8 local_858;
  undefined1 auStack_850 [2048];
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_Is_Secure_ID_ffff0000907db944);
  FUN_ffff00009077ee50(auStack_850,0,0x800);
  local_858 = 0;
  local_868 = 0;
  uStack_860 = 0;
  local_878 = 0;
  uStack_870 = 0;
  local_888 = 0;
  uStack_880 = 0;
  local_898 = 0;
  uStack_890 = 0;
  local_8a8 = 0;
  uStack_8a0 = 0;
  local_8b8 = 0;
  uStack_8b0 = 0;
  local_8c8 = 0;
  uStack_8c0 = 0;
  local_8d8 = 0;
  uStack_8d0 = 0;
  local_8e8 = 0;
  uStack_8e0 = 0;
  local_8f8 = 0;
  uStack_8f0 = 0;
  local_908 = 0;
  uStack_900 = 0;
  local_918 = 0;
  uStack_910 = 0;
  local_928 = 0;
  uStack_920 = 0;
  local_938 = 0;
  uStack_930 = 0;
  local_948 = 0;
  uStack_940 = 0;
  local_958 = 0;
  uStack_950 = 0;
  local_968 = 0;
  uStack_960 = 0;
  local_978 = 0;
  uStack_970 = 0;
  local_980 = 0;
  local_990 = 0;
  uStack_988 = 0;
  local_9a0 = 0;
  uStack_998 = 0;
  local_9b0 = 0;
  uStack_9a8 = 0;
  local_9b8 = 0;
  local_9c8 = 0;
  uStack_9c0 = 0;
  local_9d8 = 0;
  uStack_9d0 = 0;
  local_9e8 = 0;
  uStack_9e0 = 0;
  local_9f8 = 0;
  uStack_9f0 = 0;
  local_a08 = 0;
  uStack_a00 = 0;
  local_a18 = 0;
  uStack_a10 = 0;
  local_a28 = 0;
  uStack_a20 = 0;
  local_a38 = 0;
  local_a30 = 0;
  local_a3c = 0;
  iVar1 = FUN_ffff000090788a10(&local_a3c);
  if (iVar1 == 0) {
    FUN_ffff00009077e11c(s_lock_state__d_ffff0000907f735e,local_a3c);
    FUN_ffff00009077e11c(s_is_factory_mode__d_ffff0000907dd416,0);
    uVar2 = thunk_FUN_ffff00009074b22c(0x1b);
    uVar9 = (ulong)((uVar2 & 0xe) == 0xe);
    FUN_ffff00009077e11c(s_sbc_status__d___s_ffff0000907db951,uVar9,uVar9 * 4 + -0xffff6f7ede31);
    if ((local_a3c == 3) || ((uVar2 & 0xe) != 0xe)) {
      FUN_ffff00009077e11c(s_no_need_to_verify_secure_id__cou_ffff0000907d6cb7);
      return 0;
    }
    iVar1 = FUN_ffff000090786fe0(auStack_850,(long)&local_858 + 4);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_Error_getting_secid_from_rpmb____ffff0000907d6ce5,iVar1);
      iVar1 = FUN_ffff000090787514(auStack_850,(long)&local_858 + 4);
      if (iVar1 != 0) {
        FUN_ffff00009077e11c(s_Error_getting_secinfo_from_Parti_ffff0000907f1f7e,iVar1);
        return iVar1;
      }
    }
    FUN_ffff00009071c6a0(&uStack_8b0);
    iVar1 = FUN_ffff000090787150(&uStack_8b0,auStack_850,0x42);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff000090787150((long)&uStack_890 + 4,auStack_850,0x43);
      if (iVar1 == 0) {
        uVar3 = thunk_FUN_ffff00009074b22c(0xc);
        uVar4 = thunk_FUN_ffff00009074b22c(0xd);
        uVar5 = thunk_FUN_ffff00009074b22c(0xe);
        uVar6 = thunk_FUN_ffff00009074b22c(0xf);
        FUN_ffff00009077ee50(&local_978,0,200);
        iVar1 = FUN_ffff00009079482c(&local_a18);
        if (iVar1 == 0) {
          lVar8 = FUN_ffff00009076fe0c((long)&local_a08 + 3,0x81,&local_a38);
          if (lVar8 == 0) {
            FUN_ffff00009077e11c(s_hash_ufsid__failed____ffff0000907ebfd3);
            return -1;
          }
          FUN_ffff00009077f6fc
                    (&local_978,200,s__s_08x_08x_08x_08x_ffff0000907dbf7f,&DAT_ffff0000907df381,
                     uVar3,uVar4,uVar5,uVar6);
          FUN_ffff00009077f6fc((long)&local_958 + 2,0xa6,&DAT_ffff0000907f5cad,local_a38 & 0xff);
          FUN_ffff00009077f6fc((long)&local_958 + 4,0xa4,&DAT_ffff0000907f5cad,local_a38._1_1_);
          FUN_ffff00009077f6fc((long)&local_958 + 6,0xa2,&DAT_ffff0000907f5cad,local_a38._2_1_);
          FUN_ffff00009077f6fc(&uStack_950,0xa0,&DAT_ffff0000907f5cad,local_a38._3_1_);
          FUN_ffff00009077f6fc((long)&uStack_950 + 2,0x9e,&DAT_ffff0000907f5cad,local_a38._4_1_);
          FUN_ffff00009077f6fc((long)&uStack_950 + 4,0x9c,&DAT_ffff0000907f5cad,local_a38._5_1_);
          FUN_ffff00009077f6fc((long)&uStack_950 + 6,0x9a,&DAT_ffff0000907f5cad,local_a38._6_1_);
          FUN_ffff00009077f6fc(&local_948,0x98,&DAT_ffff0000907f5cad,local_a38._7_1_);
          FUN_ffff00009077f6fc((long)&local_948 + 2,0x96,&DAT_ffff0000907f5cad,local_a30 & 0xff);
          FUN_ffff00009077f6fc((long)&local_948 + 4,0x94,&DAT_ffff0000907f5cad,local_a30._1_1_);
          FUN_ffff00009077f6fc((long)&local_948 + 6,0x92,&DAT_ffff0000907f5cad,local_a30._2_1_);
          iVar1 = FUN_ffff00009077f6fc(&uStack_940,0x90,&DAT_ffff0000907f5cad,local_a30._3_1_);
          if (iVar1 < 0) {
            pcVar7 = s_snprintf_cpuid_error_____ffff0000907e6cdd;
          }
          else {
            iVar1 = FUN_ffff000090787150(&local_978,auStack_850,0x41);
            if (iVar1 == 0) {
              iVar1 = FUN_ffff000090787318((long)&uStack_890 + 4);
              if (iVar1 != 0) {
                FUN_ffff00009077e11c(s_check_sw_rom_failed__ffff0000907ed302);
                FUN_ffff000090768818();
                return iVar1;
              }
              FUN_ffff00009077e11c(s_check_sw_rom_success__ffff0000907d6d3e);
              FUN_ffff000090768818();
              FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_Is_Secure_ID_ffff0000907db944);
              return 0;
            }
            pcVar7 = s_check_cpiud_failed__ffff000090801801;
          }
        }
        else {
          pcVar7 = s_ufs_id_get_failed____ffff0000907feb28;
        }
      }
      else {
        pcVar7 = s_check_hwc_failed__ffff0000907d6d2b;
      }
    }
    else {
      pcVar7 = s_check_hwlevel_failed__ffff0000907d9e5e;
    }
  }
  else {
    pcVar7 = s_Failed_to_get_current_lock_statu_ffff0000907eb8a2;
  }
  FUN_ffff00009077e11c(pcVar7);
  return iVar1;
}


