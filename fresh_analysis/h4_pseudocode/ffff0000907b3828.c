
int FUN_ffff0000907b3828(void)

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
  
  iVar1 = FUN_ffff0000907b361c();
  if (iVar1 != 0) {
    return iVar1;
  }
  if (DAT_ffff00009090d780 == 0x4d4d4d4d) {
    iVar1 = FUN_ffff0000907b2f98();
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s___s___s__Check_major_integrity_f_ffff0000907ef175,s_SEC_CFG_ffff00009080355a,
                 s_sec_get_seccfg_ffff0000907e710e);
      FUN_ffff0000907b3978();
      goto LAB_ffff0000907b3888;
    }
    if (DAT_ffff00009090d7c0 != 0x4d4d4d4d) {
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
    iVar1 = FUN_ffff0000907b81d4(s_seccfg_ffff0000907d1c70,&local_60,0x3c,0x3c);
joined_r0xffff0000907b3940:
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___s__clear_bak_fail__ffff0000907e5884;
LAB_ffff0000907b394c:
    FUN_ffff00009077e11c(pcVar2,s_SEC_CFG_ffff00009080355a,s_sec_get_seccfg_ffff0000907e710e);
  }
  else {
LAB_ffff0000907b3888:
    if (DAT_ffff00009090d7c0 == 0x4d4d4d4d) {
      iVar1 = FUN_ffff0000907b31a0();
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000907b3560();
        if (iVar1 == 0) {
          iVar1 = FUN_ffff0000907b3338();
          goto joined_r0xffff0000907b3940;
        }
        pcVar2 = s___s___s__seccfg_restore_fail__ffff0000907fad5b;
        goto LAB_ffff0000907b394c;
      }
      FUN_ffff00009077e11c
                (s___s___s__Check_backup_integrity_f_ffff0000907d2289,s_SEC_CFG_ffff00009080355a,
                 s_sec_get_seccfg_ffff0000907e710e);
      FUN_ffff0000907b3978();
    }
    iVar1 = FUN_ffff0000907b36f4();
    FUN_ffff0000907b3978();
  }
  return iVar1;
}


