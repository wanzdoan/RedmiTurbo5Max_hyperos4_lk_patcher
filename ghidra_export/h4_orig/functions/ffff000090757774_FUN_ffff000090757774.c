// ffff000090757774  size=128  FUN_ffff000090757774

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090757774(int param_1,long param_2)

{
  int *piVar1;
  int iVar2;
  int iVar3;
  
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    if (param_1 == 0x27) {
      piVar1 = (int *)&DAT_ffff00009087b610;
      iVar2 = *(int *)(param_2 + 0x2d4) + 4;
      iVar3 = *(int *)(param_2 + 0x2d0) + 4;
      _DAT_ffff00009087b618 = 4;
      *(int *)(param_2 + 0x2d4) = iVar2;
      *(int *)(param_2 + 0x2d0) = iVar3;
    }
    else {
      if (param_1 != 0x26) {
        return 0;
      }
      piVar1 = (int *)&DAT_ffff00009087b604;
      iVar2 = *(int *)(param_2 + 0x2cc) + 4;
      iVar3 = *(int *)(param_2 + 0x2c8) + 4;
      _DAT_ffff00009087b60c = 4;
      *(int *)(param_2 + 0x2cc) = iVar2;
      *(int *)(param_2 + 0x2c8) = iVar3;
    }
    *piVar1 = iVar3;
    piVar1[1] = iVar2;
  }
  return 0;
}


