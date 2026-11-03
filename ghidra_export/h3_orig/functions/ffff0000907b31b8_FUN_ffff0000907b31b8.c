// ffff0000907b31b8  size=336  FUN_ffff0000907b31b8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907b31b8(void)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined4 local_28;
  
  iVar1 = FUN_ffff0000907b2fac();
  if (iVar1 != 0) {
    return iVar1;
  }
  if (_DAT_ffff00009090d6c0 == 0x4d4d4d4d) {
    iVar1 = FUN_ffff0000907b2928();
    if (iVar1 != 0) {
      FUN_ffff00009077de54
                (s___s___s__Check_major_integrity_f_ffff0000907ef004,s_SEC_CFG_ffff000090803332,
                 s_sec_get_seccfg_ffff0000907e6fca);
      FUN_ffff0000907b3308();
      goto LAB_ffff0000907b3218;
    }
    if (_DAT_ffff00009090d700 != 0x4d4d4d4d) {
      return 0;
    }
    local_28 = 0;
    local_38 = 0;
    uStack_30 = 0;
    local_48 = 0;
    uStack_40 = 0;
    local_58 = 0;
    uStack_50 = 0;
    local_60 = 0;
    iVar1 = FUN_ffff0000907b7b60(s_seccfg_ffff0000907d1c33,&local_60,0x3c,0x3c);
joined_r0xffff0000907b32d0:
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___s__clear_bak_fail__ffff0000907e5780;
LAB_ffff0000907b32dc:
    FUN_ffff00009077de54(pcVar2,s_SEC_CFG_ffff000090803332,s_sec_get_seccfg_ffff0000907e6fca);
  }
  else {
LAB_ffff0000907b3218:
    if (_DAT_ffff00009090d700 == 0x4d4d4d4d) {
      iVar1 = FUN_ffff0000907b2b30();
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000907b2ef0();
        if (iVar1 == 0) {
          iVar1 = FUN_ffff0000907b2cc8();
          goto joined_r0xffff0000907b32d0;
        }
        pcVar2 = s___s___s__seccfg_restore_fail__ffff0000907fabbf;
        goto LAB_ffff0000907b32dc;
      }
      FUN_ffff00009077de54
                (s___s___s__Check_backup_integrity_f_ffff0000907d224c,s_SEC_CFG_ffff000090803332,
                 s_sec_get_seccfg_ffff0000907e6fca);
      FUN_ffff0000907b3308();
    }
    iVar1 = FUN_ffff0000907b3084();
    FUN_ffff0000907b3308();
  }
  return iVar1;
}


