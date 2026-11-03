
void FUN_ffff0000be08df48(long param_1)

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
    iVar1 = FUN_ffff0000be07f8cc(&DAT_ffff0000be1b7c40,0x1c,&local_60);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000be060778(param_1,&local_60,0x20);
      if (iVar1 != 0) {
        FUN_ffff0000be05fd98
                  (s___s__SW_hash_result_compare_fail_ffff0000be0b4c62,s_SEC_CFG_ffff0000be0c6d63);
        FUN_ffff0000be05fd98(s___s__SW_hash__ffff0000be0ab083,s_SEC_CFG_ffff0000be0c6d63);
        uVar2 = 0;
        do {
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)((long)&local_60 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
        FUN_ffff0000be05fd98(s___s__pattern__ffff0000be0b3983,s_SEC_CFG_ffff0000be0c6d63);
        uVar2 = 0;
        do {
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)(param_1 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
      }
    }
    else {
      FUN_ffff0000be05fd98
                (s___s__SW_hash_error__0x_x_ffff0000be0c15cf,s_SEC_CFG_ffff0000be0c6d63,iVar1);
    }
  }
  return;
}


