
void FUN_ffff0000907be1c0(uint *param_1,uint *param_2,long param_3,uint *param_4)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  uint *puVar7;
  ulong uVar8;
  ulong uVar9;
  long lVar10;
  ulong uVar11;
  
  if (*param_1 != 0) {
    uVar6 = 0;
    do {
      param_2[uVar6] = 0;
      uVar6 = uVar6 + 1;
      uVar9 = (ulong)*param_1;
    } while (uVar6 < uVar9);
    if (uVar9 != 0) {
      puVar7 = *(uint **)(param_1 + 2);
      uVar6 = 0;
      do {
        uVar2 = *(uint *)(param_3 + uVar6 * 4);
        uVar8 = (ulong)*param_2 + (ulong)*param_4 * (ulong)uVar2;
        uVar3 = param_1[1] * (int)uVar8;
        uVar11 = (uVar8 & 0xffffffff) + (ulong)*puVar7 * (ulong)uVar3;
        if (uVar9 < 2) {
          lVar10 = 0;
        }
        else {
          lVar10 = 0;
          do {
            puVar1 = param_2 + lVar10;
            lVar4 = lVar10 + 1;
            lVar5 = lVar10 + 1;
            uVar9 = lVar10 + 2;
            lVar10 = lVar10 + 1;
            uVar8 = (uVar8 >> 0x20) + (ulong)param_4[lVar4] * (ulong)uVar2 + (ulong)puVar1[1];
            uVar11 = (uVar11 >> 0x20) + (ulong)puVar7[lVar5] * (ulong)uVar3 + (uVar8 & 0xffffffff);
            *puVar1 = (uint)uVar11;
          } while (uVar9 < *param_1);
        }
        uVar11 = (uVar11 >> 0x20) + (uVar8 >> 0x20);
        param_2[lVar10] = (uint)uVar11;
        uVar9 = (ulong)*param_1;
        if ((uVar11 >> 0x20 != 0) && (uVar9 != 0)) {
          lVar10 = 0;
          uVar11 = 0;
          do {
            uVar8 = uVar11 + 1;
            lVar10 = (lVar10 - (ulong)puVar7[uVar11]) + (ulong)param_2[uVar11];
            param_2[uVar11] = (uint)lVar10;
            lVar10 = lVar10 >> 0x20;
            uVar9 = (ulong)*param_1;
            uVar11 = uVar8;
          } while (uVar8 < uVar9);
        }
        uVar6 = uVar6 + 1;
      } while (uVar6 < uVar9);
    }
  }
  return;
}


