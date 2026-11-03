
undefined8 FUN_ffff0000b8062600(long *param_1,long param_2)

{
  undefined8 uVar1;
  ulong uVar2;
  
  if (param_1 == (long *)0x0) {
    return 0xfffffffe;
  }
  if (param_2 != 0) {
    uVar2 = param_1[9];
    if (0x40 < uVar2) {
      return 0xfffffffc;
    }
    param_1[9] = 0;
    *param_1 = *param_1 + uVar2 * 8;
    uVar1 = FUN_ffff0000b80681cc(&DAT_ffff0000b81b7080);
    return uVar1;
  }
  return 0xfffffffd;
}


