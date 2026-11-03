// ffff0000907bc698  size=220  FUN_ffff0000907bc698

undefined8 FUN_ffff0000907bc698(undefined8 param_1,undefined8 param_2,ulong param_3,ulong *param_4)

{
  ulong uVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar1 = thunk_FUN_ffff00009077ef88();
  uVar2 = 6;
  if (((param_3 < 0x41) && (uVar1 < 0x20)) && (uVar1 = *param_4, uVar1 < 10)) {
    uVar3 = FUN_ffff0000907c14dc
                      (s___AVB__ffff0000907e3dc8,param_1,s__ROOT_DIGEST__ffff0000907fac4d,0);
    param_4[uVar1 + 1] = uVar3;
    if (uVar3 != 0) {
      FUN_ffff0000907c17ec();
      uVar3 = FUN_ffff0000907c182c(param_2,param_3);
      param_4[uVar1 + 0xb] = uVar3;
      if (uVar3 != 0) {
        *param_4 = *param_4 + 1;
        return 0;
      }
      if (param_4[uVar1 + 1] != 0) {
        FUN_ffff0000907c09a0();
      }
    }
    if (param_4[uVar1 + 0xb] != 0) {
      FUN_ffff0000907c09a0();
    }
    uVar2 = 1;
  }
  return uVar2;
}


