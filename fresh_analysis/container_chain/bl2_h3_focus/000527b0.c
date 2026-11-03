// File offset entry: 0x527b0

int FUN_ffff0000b80527b0(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined *puVar4;
  undefined1 auStack_230 [256];
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  uint local_14;
  
  iVar1 = FUN_ffff0000b806a5ec();
  if (iVar1 != 0) {
    return iVar1;
  }
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  local_14 = thunk_FUN_ffff0000b802c980(0x1b);
  if (((local_14 ^ 0xffffffff) & 0xe) == 0) {
    iVar1 = FUN_ffff0000b804cdbc(param_2,s_dconfig_ffff0000b808413a);
    if ((iVar1 == 0) ||
       (iVar1 = FUN_ffff0000b804cdbc(param_2,s_dconfig_dt_ffff0000b80941de), iVar1 == 0)) {
      pcVar3 = s_do_not_verify_dconfig_s_imgkey_ffff0000b809f8fb;
    }
    else {
      lVar2 = FUN_ffff0000b8013f6c(s_bypass_check_imgkey_ffff0000b80877ba);
      iVar1 = FUN_ffff0000b801409c();
      if (((iVar1 == 0) || (lVar2 == 0)) ||
         (iVar1 = FUN_ffff0000b804cdbc(lVar2,&DAT_ffff0000b808d49a), iVar1 != 0)) {
        iVar1 = FUN_ffff0000b802c370(0,&local_14);
        if (iVar1 != 0) {
          FUN_ffff0000b804beb0(s_Read_efuse_fail_in_sboot__0x_x_ffff0000b8084a96,iVar1);
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000b804b3a4
                    (s_platform_mediatek_common_sboot_s_ffff0000b809917b,0x23f,&DAT_ffff0000b8085ec6
                    );
        }
        iVar1 = FUN_ffff0000b806b588(&local_130,0x100);
        if (iVar1 == 0) {
          if ((char)local_14 == '\0') {
            FUN_ffff0000b804beb0(s_lk_verify_with_CN_imgkey_ffff0000b808f362);
            puVar4 = &DAT_ffff0000b80a86b1;
          }
          else {
            FUN_ffff0000b804beb0(s_lk_verify_with_GL_imgkey_ffff0000b80967af);
            puVar4 = &DAT_ffff0000b80a87b1;
          }
          FUN_ffff0000b804c8b4(auStack_230,puVar4,0x100);
          iVar1 = FUN_ffff0000b804c87c(&local_130,auStack_230,0x100);
          if (iVar1 == 0) {
            return 0;
          }
          FUN_ffff0000b804beb0(s_img_region_not_match__ffff0000b8090dd3);
          return -(uint)((char)local_14 == '\0');
        }
        FUN_ffff0000b804beb0(s_lk_Get_img_pubk_fail__0x_x_ffff0000b808d49e,iVar1);
        return iVar1;
      }
      pcVar3 = s_lk_bypass_check_region_img_key_ffff0000b8091c1e;
    }
    FUN_ffff0000b804beb0(pcVar3);
  }
  return 0;
}


