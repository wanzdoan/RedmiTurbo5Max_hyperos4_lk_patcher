// ffff0000907b3c28  size=20  FUN_ffff0000907b3c28

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907b3c28(undefined4 *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  int iVar3;
  
  if (param_1 == (undefined4 *)0x0) {
    return 0x100006;
  }
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


