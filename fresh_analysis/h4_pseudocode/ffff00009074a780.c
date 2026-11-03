
undefined8 FUN_ffff00009074a780(uint param_1,uint *param_2)

{
  undefined8 uVar1;
  uint uVar2;
  
  if (param_2 != (uint *)0x0) {
    if (param_1 < 9) {
      uVar1 = 0;
      uVar2 = *(uint *)(&DAT_ffff00009081ad98 + (ulong)param_1 * 0x20) &
              **(uint **)(&DAT_ffff00009081ad90 + (ulong)param_1 * 0x20);
    }
    else {
      uVar2 = 0;
      uVar1 = 0x1000;
    }
    *param_2 = uVar2;
    return uVar1;
  }
  return 0x2000;
}


