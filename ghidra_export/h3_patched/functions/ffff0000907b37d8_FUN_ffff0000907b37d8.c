// ffff0000907b37d8  size=364  FUN_ffff0000907b37d8

void FUN_ffff0000907b37d8(long param_1)

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
    iVar1 = FUN_ffff0000907a4550(&DAT_ffff00009090d6c0,0x1c,&local_60);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077e820(param_1,&local_60,0x20);
      if (iVar1 != 0) {
        FUN_ffff00009077de54
                  (s___s__SW_hash_result_compare_fail_ffff0000907e88a8,s_SEC_CFG_ffff000090803332);
        FUN_ffff00009077de54(s___s__SW_hash__ffff0000907da1c6,s_SEC_CFG_ffff000090803332);
        uVar2 = 0;
        do {
          FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)((long)&local_60 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
        FUN_ffff00009077de54(s___s__pattern__ffff0000907e6fe1,s_SEC_CFG_ffff000090803332);
        uVar2 = 0;
        do {
          FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)(param_1 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      }
    }
    else {
      FUN_ffff00009077de54
                (s___s__SW_hash_error__0x_x_ffff0000907fabde,s_SEC_CFG_ffff000090803332,iVar1);
    }
  }
  return;
}


