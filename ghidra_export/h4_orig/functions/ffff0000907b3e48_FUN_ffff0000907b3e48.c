// ffff0000907b3e48  size=364  FUN_ffff0000907b3e48

void FUN_ffff0000907b3e48(long param_1)

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
    iVar1 = FUN_ffff0000907a4810(&DAT_ffff00009090d780,0x1c,&local_60);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077eae8(param_1,&local_60,0x20);
      if (iVar1 != 0) {
        FUN_ffff00009077e11c
                  (s___s__SW_hash_result_compare_fail_ffff0000907e8a03,s_SEC_CFG_ffff00009080355a);
        FUN_ffff00009077e11c(s___s__SW_hash__ffff0000907da274,s_SEC_CFG_ffff00009080355a);
        uVar2 = 0;
        do {
          FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)((long)&local_60 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
        FUN_ffff00009077e11c(s___s__pattern__ffff0000907e713c,s_SEC_CFG_ffff00009080355a);
        uVar2 = 0;
        do {
          FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)(param_1 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
      }
    }
    else {
      FUN_ffff00009077e11c
                (s___s__SW_hash_error__0x_x_ffff0000907fad7a,s_SEC_CFG_ffff00009080355a,iVar1);
    }
  }
  return;
}


