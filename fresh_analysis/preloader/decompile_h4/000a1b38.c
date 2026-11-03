// File offset entry: 0xa1b38

undefined8 FUN_020a1a38(ulong param_1,ulong param_2)

{
  undefined8 uVar1;
  
  if ((bRam0000000002122238 & 1) == 0) {
    if (param_2 < 0x10001) {
      uVar1 = func_0x020a1ae0(0,param_1 & 0xfffffffffffff000,param_1 & 0xfffffffffffff000,0x1000,0);
      if ((int)uVar1 == 0) {
        bRam0000000002122238 = 1;
        uRam0000000002122240 = param_1;
      }
    }
    else {
      uVar1 = 0x10201a;
    }
  }
  else {
    uVar1 = 0;
  }
  return uVar1;
}


