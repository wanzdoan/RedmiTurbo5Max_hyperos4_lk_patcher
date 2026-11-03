
void FUN_ffff0000b8070264(long param_1)

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
    iVar1 = FUN_ffff0000b8061f0c(&DAT_ffff0000b81b9980,0x1c,&local_60);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000b804cd08(param_1,&local_60,0x20);
      if (iVar1 != 0) {
        FUN_ffff0000b804c33c
                  (s___s__SW_hash_result_compare_fail_ffff0000b8092f2f,s_SEC_CFG_ffff0000b80a18d5);
        FUN_ffff0000b804c33c(s___s__SW_hash__ffff0000b808b404,s_SEC_CFG_ffff0000b80a18d5);
        uVar2 = 0;
        do {
          FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)((long)&local_60 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
        FUN_ffff0000b804c33c(s___s__pattern__ffff0000b8092138,s_SEC_CFG_ffff0000b80a18d5);
        uVar2 = 0;
        do {
          FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)(param_1 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
      }
    }
    else {
      FUN_ffff0000b804c33c
                (s___s__SW_hash_error__0x_x_ffff0000b809d1ff,s_SEC_CFG_ffff0000b80a18d5,iVar1);
    }
  }
  return;
}


