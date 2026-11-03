
undefined8 FUN_ffff0000b8049710(undefined8 *param_1)

{
  long *plVar1;
  long lVar2;
  undefined8 *puVar3;
  undefined8 uVar4;
  undefined **ppuVar5;
  long lVar6;
  undefined *puVar7;
  undefined8 *puVar8;
  undefined8 *puVar9;
  long lVar10;
  ulong uVar11;
  ulong uVar12;
  
  if ((undefined **)PTR_LOOP_ffff0000b8102030 != &PTR_LOOP_ffff0000b8102028) {
    ppuVar5 = (undefined **)PTR_LOOP_ffff0000b8102030;
    do {
      if (*(uint *)((long)param_1 + 0x1c) < *(uint *)((long)ppuVar5 + 0x1c))
      goto LAB_ffff0000b8049760;
      ppuVar5 = (undefined **)ppuVar5[1];
    } while (ppuVar5 != &PTR_LOOP_ffff0000b8102028);
  }
  ppuVar5 = &PTR_LOOP_ffff0000b8102028;
LAB_ffff0000b8049760:
  uVar12 = param_1[5];
  param_1[1] = ppuVar5;
  puVar7 = *ppuVar5;
  plVar1 = param_1 + 8;
  param_1[6] = 0;
  uVar11 = uVar12 >> 0xc;
  *param_1 = puVar7;
  puVar7 = *ppuVar5;
  *ppuVar5 = (undefined *)param_1;
  *(undefined8 **)(puVar7 + 8) = param_1;
  param_1[8] = plVar1;
  param_1[9] = plVar1;
  uVar4 = FUN_ffff0000b80495fc(uVar11 * 0x18);
  param_1[7] = uVar4;
  FUN_ffff0000b804d070(uVar4,0,uVar11 * 0x18);
  if (0xfff < uVar12) {
    lVar2 = param_1[6];
    puVar3 = (undefined8 *)param_1[7];
    lVar6 = 0;
    if (uVar11 < 2) {
      uVar11 = 1;
    }
    puVar9 = (undefined8 *)param_1[8];
    puVar8 = puVar3;
    uVar12 = uVar11;
    do {
      *puVar8 = puVar9;
      puVar9 = puVar3 + lVar6 * 3;
      lVar10 = *plVar1;
      puVar8[1] = plVar1;
      lVar6 = lVar6 + 1;
      *plVar1 = (long)puVar8;
      uVar12 = uVar12 - 1;
      *(undefined8 **)(lVar10 + 8) = puVar8;
      puVar8 = puVar8 + 3;
    } while (uVar12 != 0);
    param_1[6] = lVar2 + uVar11;
  }
  return 0;
}


