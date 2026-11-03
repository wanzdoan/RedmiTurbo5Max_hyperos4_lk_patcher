
undefined4 FUN_ffff0000be092230(long param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  *(undefined4 *)(param_1 + 4) = 0x10050000;
  iVar1 = FUN_ffff0000be0959f0(param_1 + 0xb0,0x40000,0x40000,1);
  uVar2 = 0;
  if (iVar1 != 0) {
    uVar2 = 0x7261;
  }
  return uVar2;
}


