
void FUN_ffff00009071ed4c(int *param_1,uint param_2)

{
  ulong uVar1;
  uint uVar2;
  int iVar3;
  
  uVar1 = (ulong)param_2;
  if ((&DAT_ffff0000908605ec)[uVar1] == '\0') {
    (&DAT_ffff0000908605ec)[uVar1] = 1;
    *(undefined8 *)(&DAT_ffff0000908605f0 + uVar1 * 0xc) = 0x84d000000000;
    *(undefined4 *)(&DAT_ffff0000908605f8 + uVar1 * 0xc) = 500;
  }
  if (param_2 != 2) {
    FUN_ffff00009071eeac(param_1);
    return;
  }
  if (DAT_ffff0000908605e8 == 0) {
    iVar3 = 0;
  }
  else {
    iVar3 = 0;
    uVar2 = 0;
    do {
      FUN_ffff00009071eeac(param_1,uVar2);
      uVar2 = uVar2 + 1;
      iVar3 = *param_1 + iVar3;
    } while (uVar2 < DAT_ffff0000908605e8);
  }
  *param_1 = iVar3;
  return;
}


