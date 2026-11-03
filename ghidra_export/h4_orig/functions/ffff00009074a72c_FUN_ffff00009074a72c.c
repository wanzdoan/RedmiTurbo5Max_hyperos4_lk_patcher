// ffff00009074a72c  size=76  FUN_ffff00009074a72c

undefined8 FUN_ffff00009074a72c(uint param_1,uint *param_2)

{
  undefined8 uVar1;
  uint uVar2;
  
  if (param_1 < 8) {
    uVar1 = 0;
    uVar2 = *(uint *)(&DAT_ffff00009081ac98 + (ulong)param_1 * 0x20) &
            **(uint **)(&DAT_ffff00009081ac90 + (ulong)param_1 * 0x20);
  }
  else {
    uVar2 = 0;
    uVar1 = 0x1000;
  }
  *param_2 = uVar2;
  return uVar1;
}


