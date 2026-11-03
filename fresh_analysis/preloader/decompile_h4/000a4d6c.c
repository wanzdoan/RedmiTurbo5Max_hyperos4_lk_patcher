// File offset entry: 0xa4d6c

undefined8 FUN_020a4c6c(long param_1,uint param_2,uint param_3,undefined8 param_4,uint param_5)

{
  ulong uVar1;
  undefined8 uVar2;
  
  if ((param_5 & 0xffffffef) == 0x20) {
    if (param_3 < 0x10) {
      func_0x02091664(param_4,0,param_5);
      if (param_3 != 0) {
        uVar1 = (ulong)param_2;
        param_2 = param_3 + param_2;
        func_0x02091664(param_1 + uVar1,0,param_3);
      }
      uVar2 = func_0x0209c58c(param_1,param_2,param_4,param_5 == 0x30);
      if ((int)uVar2 == 0) {
        func_0x020a0fec(param_4,param_5);
        uVar2 = 0;
      }
    }
    else {
      uVar2 = 0x101008;
    }
  }
  else {
    uVar2 = 0xffffffff;
  }
  return uVar2;
}


