// ffff000090755448  size=500  FUN_ffff000090755448

void FUN_ffff000090755448(undefined4 param_1,undefined8 param_2)

{
  long lVar1;
  undefined4 uVar2;
  uint uVar3;
  ulong uVar4;
  uint *puVar5;
  uint uVar6;
  long lVar7;
  code *pcVar8;
  long lVar9;
  ulong uVar10;
  
  uVar4 = FUN_ffff0000907590bc();
  if ((uVar4 & 1) == 0) {
    uVar2 = 0x14;
  }
  else {
    uVar2 = FUN_ffff0000907590d0(param_1);
  }
  FUN_ffff000090759404(param_1,param_2);
  puVar5 = (uint *)FUN_ffff0000907593e8(param_1);
  uVar3 = FUN_ffff0000907590e8(param_1);
  lVar1 = DAT_ffff0000908399c8;
  uVar10 = (ulong)(uVar3 - 1);
  if (0 < (int)uVar3) {
    lVar7 = *(long *)(DAT_ffff0000908399c8 + (ulong)*puVar5 * 8);
    if ((lVar7 != 0) && (pcVar8 = *(code **)(lVar7 + 0xa8), pcVar8 != (code *)0x0)) {
      if (uVar3 - 1 == 0) {
        uVar6 = 0x102;
      }
      else {
        uVar6 = puVar5[1];
      }
      (*pcVar8)((ulong)*puVar5,0x102,uVar6,1,param_2);
    }
    if (uVar3 != 1) {
      puVar5 = puVar5 + 2;
      lVar7 = (ulong)uVar3 - 1;
      do {
        uVar10 = uVar10 - 1;
        lVar9 = *(long *)(lVar1 + (ulong)puVar5[-1] * 8);
        if ((lVar9 != 0) && (pcVar8 = *(code **)(lVar9 + 0xa8), pcVar8 != (code *)0x0)) {
          if (uVar10 == 0) {
            uVar3 = 0x102;
          }
          else {
            uVar3 = *puVar5;
          }
          (*pcVar8)((ulong)puVar5[-1],puVar5[-2],uVar3,1,param_2);
        }
        puVar5 = puVar5 + 1;
        lVar7 = lVar7 + -1;
      } while (lVar7 != 0);
    }
  }
  if ((uVar4 & 1) != 0) {
    FUN_ffff000090759404(uVar2,param_2);
    puVar5 = (uint *)FUN_ffff0000907593e8(uVar2);
    uVar3 = FUN_ffff0000907590e8(uVar2);
    lVar1 = DAT_ffff0000908399c8;
    uVar4 = (ulong)(uVar3 - 1);
    if (0 < (int)uVar3) {
      lVar7 = *(long *)(DAT_ffff0000908399c8 + (ulong)*puVar5 * 8);
      if ((lVar7 != 0) && (pcVar8 = *(code **)(lVar7 + 0xa8), pcVar8 != (code *)0x0)) {
        if (uVar3 - 1 == 0) {
          uVar6 = 0x102;
        }
        else {
          uVar6 = puVar5[1];
        }
        (*pcVar8)((ulong)*puVar5,0x102,uVar6,1,param_2);
      }
      if (uVar3 != 1) {
        puVar5 = puVar5 + 2;
        lVar7 = (ulong)uVar3 - 1;
        do {
          uVar4 = uVar4 - 1;
          lVar9 = *(long *)(lVar1 + (ulong)puVar5[-1] * 8);
          if ((lVar9 != 0) && (pcVar8 = *(code **)(lVar9 + 0xa8), pcVar8 != (code *)0x0)) {
            if (uVar4 == 0) {
              uVar3 = 0x102;
            }
            else {
              uVar3 = *puVar5;
            }
            (*pcVar8)((ulong)puVar5[-1],puVar5[-2],uVar3,1,param_2);
          }
          lVar7 = lVar7 + -1;
          puVar5 = puVar5 + 1;
        } while (lVar7 != 0);
      }
    }
  }
  return;
}


