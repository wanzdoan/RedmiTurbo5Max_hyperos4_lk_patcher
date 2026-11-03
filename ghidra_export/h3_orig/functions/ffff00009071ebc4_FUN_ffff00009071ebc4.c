// ffff00009071ebc4  size=212  FUN_ffff00009071ebc4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071ebc4(int *param_1,uint param_2)

{
  ulong uVar1;
  uint uVar2;
  int iVar3;
  
  uVar1 = (ulong)param_2;
  if (*(char *)(uVar1 - 0xffff6f79fab0) == '\0') {
    *(undefined1 *)(uVar1 - 0xffff6f79fab0) = 1;
    *(undefined8 *)(uVar1 * 0xc + -0xffff6f79faac) = 0x84d000000000;
    *(undefined4 *)(uVar1 * 0xc + -0xffff6f79faa4) = 500;
  }
  if (param_2 != 2) {
    FUN_ffff00009071ed24(param_1);
    return;
  }
  if (_DAT_ffff00009086054c == 0) {
    iVar3 = 0;
  }
  else {
    iVar3 = 0;
    uVar2 = 0;
    do {
      FUN_ffff00009071ed24(param_1,uVar2);
      uVar2 = uVar2 + 1;
      iVar3 = *param_1 + iVar3;
    } while (uVar2 < _DAT_ffff00009086054c);
  }
  *param_1 = iVar3;
  return;
}


