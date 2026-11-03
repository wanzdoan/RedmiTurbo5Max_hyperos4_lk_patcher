
int FUN_ffff0000907ad2fc(byte *param_1)

{
  int iVar1;
  uint uVar2;
  
  iVar1 = 0;
  if (param_1 != (byte *)0x0) {
    uVar2 = (uint)*param_1;
    if (*param_1 == 0) {
      return 0;
    }
    iVar1 = 0;
    do {
      param_1 = param_1 + 1;
      iVar1 = uVar2 + iVar1 * 10;
      uVar2 = (uint)*param_1;
      iVar1 = iVar1 + -0x30;
    } while (uVar2 != 0);
  }
  return iVar1;
}


