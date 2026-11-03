
void FUN_ffff0000be08db6c(void)

{
  ulong uVar1;
  
  FUN_ffff0000be05fd98
            (s___s__Dump_major_raw_data_seccfg__ffff0000be0bdd5a,s_SEC_CFG_ffff0000be0c6d63);
  uVar1 = 0;
  do {
    FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)((long)&DAT_ffff0000be1b7c40 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
  FUN_ffff0000be05fd98(s___s__Analysis_major_seccfg__ffff0000be0b294e,s_SEC_CFG_ffff0000be0c6d63);
  FUN_ffff0000be05fd98
            (s___s__magic_number___x_ffff0000be0bdd7c,s_SEC_CFG_ffff0000be0c6d63,
             DAT_ffff0000be1b7c40);
  FUN_ffff0000be05fd98
            (s___s__seccfg_ver___x_ffff0000be0b4c86,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c44)
  ;
  FUN_ffff0000be05fd98
            (s___s__seccfg_size___x_ffff0000be0bca89,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c48
            );
  FUN_ffff0000be05fd98
            (s___s__lock_state___x_ffff0000be0b3993,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c4c)
  ;
  FUN_ffff0000be05fd98
            (s___s__end_pattern___x_ffff0000be0a9cae,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c58
            );
  FUN_ffff0000be05fd98(s___s__seccfg_cipher__ffff0000be0a7848,s_SEC_CFG_ffff0000be0c6d63);
  uVar1 = 0;
  do {
    FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,(&DAT_ffff0000be1b7c5c)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
    }
  } while (uVar1 != 0x20);
  FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
  FUN_ffff0000be05fd98
            (s___s__Dump_backup_raw_data_seccfg_ffff0000be0c4aca,s_SEC_CFG_ffff0000be0c6d63);
  uVar1 = 0;
  do {
    FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)((long)&DAT_ffff0000be1b7c80 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
  FUN_ffff0000be05fd98(s___s__Analysis_backup_seccfg__ffff0000be0ae16a,s_SEC_CFG_ffff0000be0c6d63);
  FUN_ffff0000be05fd98
            (s___s__magic_number___x_ffff0000be0bdd7c,s_SEC_CFG_ffff0000be0c6d63,
             DAT_ffff0000be1b7c80);
  FUN_ffff0000be05fd98
            (s___s__seccfg_ver___x_ffff0000be0b4c86,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c84)
  ;
  FUN_ffff0000be05fd98
            (s___s__seccfg_size___x_ffff0000be0bca89,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c88
            );
  FUN_ffff0000be05fd98
            (s___s__lock_state___x_ffff0000be0b3993,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c8c)
  ;
  FUN_ffff0000be05fd98
            (s___s__end_pattern___x_ffff0000be0a9cae,s_SEC_CFG_ffff0000be0c6d63,DAT_ffff0000be1b7c98
            );
  FUN_ffff0000be05fd98(s___s__seccfg_cipher__ffff0000be0a7848,s_SEC_CFG_ffff0000be0c6d63);
  uVar1 = 0;
  do {
    FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,(&DAT_ffff0000be1b7c9c)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
    }
  } while (uVar1 != 0x20);
  FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
  return;
}


