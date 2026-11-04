
void FUN_ffff0000907b393c(void)

{
  ulong uVar1;

  FUN_ffff00009077ea84
            (s___s__Dump_major_raw_data_seccfg__ffff0000907ee66e,s_SEC_CFG_ffff0000907fc53b);
  uVar1 = 0;
  do {
    FUN_ffff00009077ea84(&DAT_ffff0000907ccf39,*(undefined1 *)((long)&DAT_ffff00009090d6c0 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
  FUN_ffff00009077ea84(s___s__Analysis_major_seccfg__ffff0000907ddf83,s_SEC_CFG_ffff0000907fc53b);
  FUN_ffff00009077ea84
            (s___s__magic_number___x_ffff0000907ee690,s_SEC_CFG_ffff0000907fc53b,
             DAT_ffff00009090d6c0);
  FUN_ffff00009077ea84
            (s___s__seccfg_ver___x_ffff0000907e1303,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d6c4)
  ;
  FUN_ffff00009077ea84
            (s___s__seccfg_size___x_ffff0000907ec936,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d6c8
            );
  FUN_ffff00009077ea84
            (s___s__lock_state___x_ffff0000907df9ae,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d6cc)
  ;
  FUN_ffff00009077ea84
            (s___s__end_pattern___x_ffff0000907d0d92,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d6d8
            );
  FUN_ffff00009077ea84(s___s__seccfg_cipher__ffff0000907cd61c,s_SEC_CFG_ffff0000907fc53b);
  uVar1 = 0;
  do {
    FUN_ffff00009077ea84(&DAT_ffff0000907ccf39,(&DAT_ffff00009090d6dc)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
    }
  } while (uVar1 != 0x20);
  FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
  FUN_ffff00009077ea84
            (s___s__Dump_backup_raw_data_seccfg_ffff0000907f8fce,s_SEC_CFG_ffff0000907fc53b);
  uVar1 = 0;
  do {
    FUN_ffff00009077ea84(&DAT_ffff0000907ccf39,*(undefined1 *)((long)&DAT_ffff00009090d700 + uVar1))
    ;
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
    }
  } while (uVar1 != 0x3c);
  FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
  FUN_ffff00009077ea84(s___s__Analysis_backup_seccfg__ffff0000907d760b,s_SEC_CFG_ffff0000907fc53b);
  FUN_ffff00009077ea84
            (s___s__magic_number___x_ffff0000907ee690,s_SEC_CFG_ffff0000907fc53b,
             DAT_ffff00009090d700);
  FUN_ffff00009077ea84
            (s___s__seccfg_ver___x_ffff0000907e1303,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d704)
  ;
  FUN_ffff00009077ea84
            (s___s__seccfg_size___x_ffff0000907ec936,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d708
            );
  FUN_ffff00009077ea84
            (s___s__lock_state___x_ffff0000907df9ae,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d70c)
  ;
  FUN_ffff00009077ea84
            (s___s__end_pattern___x_ffff0000907d0d92,s_SEC_CFG_ffff0000907fc53b,DAT_ffff00009090d718
            );
  FUN_ffff00009077ea84(s___s__seccfg_cipher__ffff0000907cd61c,s_SEC_CFG_ffff0000907fc53b);
  uVar1 = 0;
  do {
    FUN_ffff00009077ea84(&DAT_ffff0000907ccf39,(&DAT_ffff00009090d71c)[uVar1]);
    uVar1 = uVar1 + 1;
    if ((uVar1 & 0xf) == 0) {
      FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
    }
  } while (uVar1 != 0x20);
  FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
  return;
}
