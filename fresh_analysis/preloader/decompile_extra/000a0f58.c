// File offset entry: 0xa0f58

undefined4 FUN_020a0e58(byte *param_1,byte *param_2,uint param_3)

{
  ulong uVar1;
  
  if (param_3 != 0) {
    uVar1 = (ulong)param_3;
    do {
      if (*param_1 != *param_2) {
        if (*param_1 <= *param_2) {
          return 0xffffffff;
        }
        return 1;
      }
      param_2 = param_2 + 1;
      param_1 = param_1 + 1;
      uVar1 = uVar1 - 1;
    } while (uVar1 != 0);
  }
  return 0;
}


