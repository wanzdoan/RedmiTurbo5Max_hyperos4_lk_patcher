
void FUN_ffff0000b806ffb4(void)

{
  ulong uVar1;
  
  FUN_ffff0000b804c33c
            (s___s__Dump_major_raw_data_seccfg__ffff0000b809a4d1,s_SEC_CFG_ffff0000b80a18d5);
  uVar1 = 0;
  do {
    FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)((long)&DAT_ffff0000b81b9980 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
  FUN_ffff0000b804c33c(s___s__Analysis_major_seccfg__ffff0000b80913d6,s_SEC_CFG_ffff0000b80a18d5);
  FUN_ffff0000b804c33c
            (s___s__magic_number___x_ffff0000b809a4f3,s_SEC_CFG_ffff0000b80a18d5,
             (undefined4)DAT_ffff0000b81b9980);
  FUN_ffff0000b804c33c
            (s___s__seccfg_ver___x_ffff0000b8092f53,s_SEC_CFG_ffff0000b80a18d5,
             DAT_ffff0000b81b9980._4_4_);
  FUN_ffff0000b804c33c
            (s___s__seccfg_size___x_ffff0000b80996f9,s_SEC_CFG_ffff0000b80a18d5,
             (undefined4)DAT_ffff0000b81b9988);
  FUN_ffff0000b804c33c
            (s___s__lock_state___x_ffff0000b8092148,s_SEC_CFG_ffff0000b80a18d5,
             DAT_ffff0000b81b9988._4_4_);
  FUN_ffff0000b804c33c
            (s___s__end_pattern___x_ffff0000b808a572,s_SEC_CFG_ffff0000b80a18d5,DAT_ffff0000b81b9998
            );
  FUN_ffff0000b804c33c(s___s__seccfg_cipher__ffff0000b8088845,s_SEC_CFG_ffff0000b80a18d5);
  uVar1 = 0;
  do {
    FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,(&DAT_ffff0000b81b999c)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
    }
  } while (uVar1 != 0x20);
  FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
  FUN_ffff0000b804c33c
            (s___s__Dump_backup_raw_data_seccfg_ffff0000b809ff3d,s_SEC_CFG_ffff0000b80a18d5);
  uVar1 = 0;
  do {
    FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)((long)&DAT_ffff0000b81b99c0 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
  FUN_ffff0000b804c33c(s___s__Analysis_backup_seccfg__ffff0000b808d95e,s_SEC_CFG_ffff0000b80a18d5);
  FUN_ffff0000b804c33c
            (s___s__magic_number___x_ffff0000b809a4f3,s_SEC_CFG_ffff0000b80a18d5,
             DAT_ffff0000b81b99c0);
  FUN_ffff0000b804c33c
            (s___s__seccfg_ver___x_ffff0000b8092f53,s_SEC_CFG_ffff0000b80a18d5,DAT_ffff0000b81b99c4)
  ;
  FUN_ffff0000b804c33c
            (s___s__seccfg_size___x_ffff0000b80996f9,s_SEC_CFG_ffff0000b80a18d5,DAT_ffff0000b81b99c8
            );
  FUN_ffff0000b804c33c
            (s___s__lock_state___x_ffff0000b8092148,s_SEC_CFG_ffff0000b80a18d5,DAT_ffff0000b81b99cc)
  ;
  FUN_ffff0000b804c33c
            (s___s__end_pattern___x_ffff0000b808a572,s_SEC_CFG_ffff0000b80a18d5,DAT_ffff0000b81b99d8
            );
  FUN_ffff0000b804c33c(s___s__seccfg_cipher__ffff0000b8088845,s_SEC_CFG_ffff0000b80a18d5);
  uVar1 = 0;
  do {
    FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,(&DAT_ffff0000b81b99dc)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
    }
  } while (uVar1 != 0x20);
  FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
  return;
}


