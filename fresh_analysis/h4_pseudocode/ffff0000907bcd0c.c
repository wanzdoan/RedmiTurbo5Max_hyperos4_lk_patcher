
undefined8 FUN_ffff0000907bcd0c(undefined8 param_1,undefined8 param_2,ulong param_3,ulong *param_4)

{
  ulong uVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar1 = thunk_FUN_ffff00009077f250();
  uVar2 = 6;
  if (((param_3 < 0x41) && (uVar1 < 0x20)) && (uVar1 = *param_4, uVar1 < 10)) {
    uVar3 = FUN_ffff0000907c1b50
                      (s___AVB__ffff0000907e3e99,param_1,s__ROOT_DIGEST__ffff0000907fade9,0);
    param_4[uVar1 + 1] = uVar3;
    if (uVar3 != 0) {
      FUN_ffff0000907c1e60();
      uVar3 = FUN_ffff0000907c1ea0(param_2,param_3);
      param_4[uVar1 + 0xb] = uVar3;
      if (uVar3 != 0) {
        *param_4 = *param_4 + 1;
        return 0;
      }
      if (param_4[uVar1 + 1] != 0) {
        FUN_ffff0000907c1014();
      }
    }
    if (param_4[uVar1 + 0xb] != 0) {
      FUN_ffff0000907c1014();
    }
    uVar2 = 1;
  }
  return uVar2;
}


