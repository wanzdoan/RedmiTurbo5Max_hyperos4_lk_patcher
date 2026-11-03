// ffff000090796e58  size=144  FUN_ffff000090796e58

undefined8 FUN_ffff000090796e58(long *param_1,long param_2,ulong param_3)

{
  undefined8 uVar1;
  ulong uVar2;
  
  if ((((uint)param_3 | (uint)param_2) & 3) == 0) {
    if (param_3 != 0) {
      uVar2 = 0;
      do {
        if (((param_2 != 0) || (uVar2 < 0x38)) || (0x48 < uVar2)) {
          FUN_ffff00009077cd34(*param_1 + param_2 + uVar2,0xf);
        }
        uVar2 = uVar2 + 0x10;
      } while (uVar2 < param_3);
    }
    uVar1 = 0;
  }
  else {
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


