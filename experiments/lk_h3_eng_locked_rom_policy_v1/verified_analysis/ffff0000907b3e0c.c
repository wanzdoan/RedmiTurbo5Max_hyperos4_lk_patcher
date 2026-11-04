
void FUN_ffff0000907b3e0c(long param_1)

{
  int iVar1;
  ulong uVar2;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;

  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  if (param_1 != 0) {
    iVar1 = FUN_ffff0000907a4b20(&DAT_ffff00009090d6c0,0x1c,&local_60);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077f450(param_1,&local_60,0x20);
      if (iVar1 != 0) {
        FUN_ffff00009077ea84
                  (s___s__SW_hash_result_compare_fail_ffff0000907e12df,s_SEC_CFG_ffff0000907fc53b);
        FUN_ffff00009077ea84(s___s__SW_hash__ffff0000907d26ab,s_SEC_CFG_ffff0000907fc53b);
        uVar2 = 0;
        do {
          FUN_ffff00009077ea84(&DAT_ffff0000907ccf39,*(undefined1 *)((long)&local_60 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
        FUN_ffff00009077ea84(s___s__pattern__ffff0000907df99e,s_SEC_CFG_ffff0000907fc53b);
        uVar2 = 0;
        do {
          FUN_ffff00009077ea84(&DAT_ffff0000907ccf39,*(undefined1 *)(param_1 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077ea84(&DAT_ffff0000907f104e);
      }
    }
    else {
      FUN_ffff00009077ea84
                (s___s__SW_hash_error__0x_x_ffff0000907f3bdc,s_SEC_CFG_ffff0000907fc53b,iVar1);
    }
  }
  return;
}
