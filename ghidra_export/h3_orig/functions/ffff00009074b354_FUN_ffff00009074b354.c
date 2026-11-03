// ffff00009074b354  size=164  FUN_ffff00009074b354

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009074b354(undefined8 param_1,long param_2)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  
  if (*(int *)(param_2 + 0xc) != 0) {
    if (*(char *)(param_2 + 0x2d8) == '\0') {
      uVar3 = *(undefined4 *)(param_2 + 0x928);
      uVar2 = *(undefined4 *)(param_2 + 0x92c);
    }
    else {
      iVar1 = (int)param_1;
      if (iVar1 < 0xb8) {
        if (iVar1 == 0x18) {
          uVar2 = *(undefined4 *)(param_2 + 0x92c);
          uVar3 = _DAT_ffff000090877b84;
        }
        else {
          if (iVar1 != 0x1e) {
            return 0;
          }
          uVar2 = *(undefined4 *)(param_2 + 0x92c);
          uVar3 = _DAT_ffff000090877b9c;
        }
      }
      else if (iVar1 == 0xb8) {
        uVar2 = *(undefined4 *)(param_2 + 0x92c);
        uVar3 = _DAT_ffff000090877b90;
      }
      else {
        if (iVar1 != 0xb9) {
          return 0;
        }
        uVar2 = *(undefined4 *)(param_2 + 0x92c);
        uVar3 = _DAT_ffff000090877ba8;
      }
    }
    FUN_ffff00009074b54c(param_1,uVar3,uVar2,*(undefined4 *)(param_2 + 0x924));
  }
  return 0;
}


