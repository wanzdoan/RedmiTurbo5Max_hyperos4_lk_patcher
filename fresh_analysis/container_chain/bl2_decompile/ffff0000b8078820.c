
int * FUN_ffff0000b8078820(undefined8 *param_1,uint *param_2)

{
  uint uVar1;
  int *piVar2;
  
  piVar2 = (int *)0x0;
  if (param_1 != (undefined8 *)0x0) {
    piVar2 = (int *)*param_1;
    if (*piVar2 != 0x3000000) {
      return (int *)0x0;
    }
    if (param_2 != (uint *)0x0) {
      uVar1 = (piVar2[1] & 0xff00ff00U) >> 8 | (piVar2[1] & 0xff00ffU) << 8;
      uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
      if ((int)uVar1 < 0) {
        return (int *)0x0;
      }
      *param_2 = uVar1;
    }
    piVar2 = piVar2 + 3;
  }
  return piVar2;
}


