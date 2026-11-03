
undefined8 FUN_ffff0000b8063124(long *param_1,long param_2)

{
  undefined8 uVar1;
  ulong uVar2;
  
  if (param_1 == (long *)0x0) {
    return 0xfffffffe;
  }
  if (param_2 != 0) {
    uVar2 = param_1[10];
    if (0x80 < uVar2) {
      return 0xfffffffc;
    }
    param_1[10] = 0;
    *param_1 = *param_1 + uVar2 * 8;
    uVar1 = FUN_ffff0000b80683dc(&DAT_ffff0000b81b7100);
    return uVar1;
  }
  return 0xfffffffd;
}


