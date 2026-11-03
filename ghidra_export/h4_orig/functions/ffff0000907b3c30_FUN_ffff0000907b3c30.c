// ffff0000907b3c30  size=72  FUN_ffff0000907b3c30

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907b3c30(undefined4 *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  int iVar3;
  
  iVar3 = FUN_ffff0000907b3828();
  uVar1 = _DAT_ffff00009090d78c;
  uVar2 = 0;
  if (iVar3 != 0) {
    uVar1 = 1;
    uVar2 = 0x103000;
  }
  *param_1 = uVar1;
  return uVar2;
}


