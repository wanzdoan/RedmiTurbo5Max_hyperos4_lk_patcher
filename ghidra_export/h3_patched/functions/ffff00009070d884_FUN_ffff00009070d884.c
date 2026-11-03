// ffff00009070d884  size=252  FUN_ffff00009070d884

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009070d884(undefined8 param_1,undefined8 *param_2)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  long lVar4;
  undefined8 uVar5;
  long *plVar6;
  undefined8 uVar7;
  ulong uVar8;
  undefined8 *puVar9;
  undefined4 local_28;
  undefined1 local_24;
  
  local_24 = 0;
  local_28 = 0xa0d0920;
  _DAT_ffff00009083d3c0 = FUN_ffff00009070d980(param_1,&local_28);
  if (_DAT_ffff00009083d3c0 != 0) {
    _DAT_ffff00009083d3c8 = FUN_ffff00009070d980(0,&local_28);
    uVar1 = FUN_ffff00009077d670(_DAT_ffff00009083d3c8,0,10);
    if (uVar1 < 0x59) {
      uVar8 = (ulong)(uVar1 + 2);
      if (uVar1 != 0) {
        lVar4 = uVar8 - 2;
        plVar6 = (long *)&DAT_ffff00009083d3d0;
        do {
          lVar2 = FUN_ffff00009070d980(0,&local_28);
          *plVar6 = lVar2;
          if (lVar2 == 0) {
            return 1;
          }
          uVar3 = FUN_ffff00009077ef88();
          if (0x13 < uVar3) {
            return 1;
          }
          plVar6 = plVar6 + 1;
          lVar4 = lVar4 + -1;
        } while (lVar4 != 0);
      }
      puVar9 = (undefined8 *)&DAT_ffff00009083d3c0;
      do {
        uVar7 = *puVar9;
        uVar5 = *param_2;
        lVar4 = FUN_ffff00009077ef88(uVar7);
        FUN_ffff00009077efe4(uVar5,uVar7,lVar4 + 1);
        uVar8 = uVar8 - 1;
        param_2 = param_2 + 1;
        puVar9 = puVar9 + 1;
      } while (uVar8 != 0);
      return 0;
    }
  }
  return 1;
}


