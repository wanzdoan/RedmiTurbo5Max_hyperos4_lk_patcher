
undefined4 FUN_ffff0000907c5978(undefined8 param_1,long param_2,ulong *param_3)

{
  int iVar1;
  undefined4 uVar2;
  long unaff_x29;

  if (param_3 == (ulong *)0x0) {
    uVar2 = 2;
  }
  else {
    *param_3 = 0;
    uVar2 = 2;
    iVar1 = FUN_ffff000090788584(2,unaff_x29 + 0x1c);
    if ((iVar1 == 0) &&
       ((param_2 != 1 || (iVar1 = FUN_ffff000090788584(3,unaff_x29 + 0x1c), iVar1 == 0)))) {
      uVar2 = 0;
      *param_3 = (ulong)*(uint *)(unaff_x29 + 0x1c);
    }
  }
  return uVar2;
}
