
void FUN_ffff0000907b3978(void)

{
  ulong uVar1;
  
  FUN_ffff00009077e11c
            (s___s__Dump_major_raw_data_seccfg__ffff0000907f59f9,s_SEC_CFG_ffff00009080355a);
  uVar1 = 0;
  do {
    FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)((long)&DAT_ffff00009090d780 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
  FUN_ffff00009077e11c(s___s__Analysis_major_seccfg__ffff0000907e589e,s_SEC_CFG_ffff00009080355a);
  FUN_ffff00009077e11c
            (s___s__magic_number___x_ffff0000907f5a1b,s_SEC_CFG_ffff00009080355a,
             DAT_ffff00009090d780);
  FUN_ffff00009077e11c
            (s___s__seccfg_ver___x_ffff0000907e8a27,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d784)
  ;
  FUN_ffff00009077e11c
            (s___s__seccfg_size___x_ffff0000907f3c57,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d788
            );
  FUN_ffff00009077e11c
            (s___s__lock_state___x_ffff0000907e714c,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d78c)
  ;
  FUN_ffff00009077e11c
            (s___s__end_pattern___x_ffff0000907d899e,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d798
            );
  FUN_ffff00009077e11c(s___s__seccfg_cipher__ffff0000907d5417,s_SEC_CFG_ffff00009080355a);
  uVar1 = 0;
  do {
    FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,(&DAT_ffff00009090d79c)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
    }
  } while (uVar1 != 0x20);
  FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
  FUN_ffff00009077e11c
            (s___s__Dump_backup_raw_data_seccfg_ffff000090800101,s_SEC_CFG_ffff00009080355a);
  uVar1 = 0;
  do {
    FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)((long)&DAT_ffff00009090d7c0 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
  FUN_ffff00009077e11c(s___s__Analysis_backup_seccfg__ffff0000907df087,s_SEC_CFG_ffff00009080355a);
  FUN_ffff00009077e11c
            (s___s__magic_number___x_ffff0000907f5a1b,s_SEC_CFG_ffff00009080355a,
             DAT_ffff00009090d7c0);
  FUN_ffff00009077e11c
            (s___s__seccfg_ver___x_ffff0000907e8a27,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d7c4)
  ;
  FUN_ffff00009077e11c
            (s___s__seccfg_size___x_ffff0000907f3c57,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d7c8
            );
  FUN_ffff00009077e11c
            (s___s__lock_state___x_ffff0000907e714c,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d7cc)
  ;
  FUN_ffff00009077e11c
            (s___s__end_pattern___x_ffff0000907d899e,s_SEC_CFG_ffff00009080355a,DAT_ffff00009090d7d8
            );
  FUN_ffff00009077e11c(s___s__seccfg_cipher__ffff0000907d5417,s_SEC_CFG_ffff00009080355a);
  uVar1 = 0;
  do {
    FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,(&DAT_ffff00009090d7dc)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
    }
  } while (uVar1 != 0x20);
  FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
  return;
}


