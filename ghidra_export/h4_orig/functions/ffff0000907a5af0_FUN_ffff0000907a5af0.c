// ffff0000907a5af0  size=96  FUN_ffff0000907a5af0

undefined8 FUN_ffff0000907a5af0(long *param_1,long param_2)

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
    uVar1 = FUN_ffff0000907aada8(0xffff00009090aec0);
    return uVar1;
  }
  return 0xfffffffd;
}


