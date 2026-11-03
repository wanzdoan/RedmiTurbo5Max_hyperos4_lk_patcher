
void FUN_ffff000090755448(undefined4 param_1,undefined8 param_2)

{
  undefined *puVar1;
  undefined4 uVar2;
  uint uVar3;
  ulong uVar4;
  uint *puVar5;
  uint uVar6;
  code *pcVar7;
  long lVar8;
  ulong uVar9;
  
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
  puVar1 = PTR_DAT_ffff0000908399c8;
  uVar9 = (ulong)(uVar3 - 1);
  if (0 < (int)uVar3) {
    if ((*(long *)(PTR_DAT_ffff0000908399c8 + (ulong)*puVar5 * 8) != 0) &&
       (pcVar7 = *(code **)(*(long *)(PTR_DAT_ffff0000908399c8 + (ulong)*puVar5 * 8) + 0xa8),
       pcVar7 != (code *)0x0)) {
      if (uVar3 - 1 == 0) {
        uVar6 = 0x102;
      }
      else {
        uVar6 = puVar5[1];
      }
      (*pcVar7)((ulong)*puVar5,0x102,uVar6,1,param_2);
    }
    if (uVar3 != 1) {
      puVar5 = puVar5 + 2;
      lVar8 = (ulong)uVar3 - 1;
      do {
        uVar9 = uVar9 - 1;
        if ((*(long *)(puVar1 + (ulong)puVar5[-1] * 8) != 0) &&
           (pcVar7 = *(code **)(*(long *)(puVar1 + (ulong)puVar5[-1] * 8) + 0xa8),
           pcVar7 != (code *)0x0)) {
          if (uVar9 == 0) {
            uVar3 = 0x102;
          }
          else {
            uVar3 = *puVar5;
          }
          (*pcVar7)((ulong)puVar5[-1],puVar5[-2],uVar3,1,param_2);
        }
        puVar5 = puVar5 + 1;
        lVar8 = lVar8 + -1;
      } while (lVar8 != 0);
    }
  }
  if ((uVar4 & 1) != 0) {
    FUN_ffff000090759404(uVar2,param_2);
    puVar5 = (uint *)FUN_ffff0000907593e8(uVar2);
    uVar3 = FUN_ffff0000907590e8(uVar2);
    puVar1 = PTR_DAT_ffff0000908399c8;
    uVar4 = (ulong)(uVar3 - 1);
    if (0 < (int)uVar3) {
      if ((*(long *)(PTR_DAT_ffff0000908399c8 + (ulong)*puVar5 * 8) != 0) &&
         (pcVar7 = *(code **)(*(long *)(PTR_DAT_ffff0000908399c8 + (ulong)*puVar5 * 8) + 0xa8),
         pcVar7 != (code *)0x0)) {
        if (uVar3 - 1 == 0) {
          uVar6 = 0x102;
        }
        else {
          uVar6 = puVar5[1];
        }
        (*pcVar7)((ulong)*puVar5,0x102,uVar6,1,param_2);
      }
      if (uVar3 != 1) {
        puVar5 = puVar5 + 2;
        lVar8 = (ulong)uVar3 - 1;
        do {
          uVar4 = uVar4 - 1;
          if ((*(long *)(puVar1 + (ulong)puVar5[-1] * 8) != 0) &&
             (pcVar7 = *(code **)(*(long *)(puVar1 + (ulong)puVar5[-1] * 8) + 0xa8),
             pcVar7 != (code *)0x0)) {
            if (uVar4 == 0) {
              uVar3 = 0x102;
            }
            else {
              uVar3 = *puVar5;
            }
            (*pcVar7)((ulong)puVar5[-1],puVar5[-2],uVar3,1,param_2);
          }
          lVar8 = lVar8 + -1;
          puVar5 = puVar5 + 1;
        } while (lVar8 != 0);
      }
    }
  }
  return;
}


