
void FUN_ffff0000907a9850(ulong *param_1,ulong *param_2,uint param_3)

{
  ulong *puVar1;
  uint uVar2;
  long *plVar3;
  ulong uVar4;
  ulong uVar5;
  ulong *puVar6;
  ulong uVar7;
  ulong *puVar8;
  long lVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  long *plVar14;
  ulong local_30 [6];
  
  local_30[4] = 0;
  local_30[5] = 0;
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  if (param_3 == 0) {
    uVar4 = 0;
    lVar9 = 0;
    plVar3 = DAT_ffff00009090afc0;
    goto LAB_ffff0000907a99e8;
  }
  uVar4 = (ulong)param_3;
  puVar6 = param_2;
  puVar8 = param_1;
  uVar7 = uVar4;
  do {
    uVar7 = uVar7 - 1;
    *puVar8 = *puVar6;
    puVar6 = puVar6 + 1;
    puVar8 = puVar8 + 1;
  } while (uVar7 != 0);
  if (param_3 < 2) {
    uVar5 = 0;
LAB_ffff0000907a98d8:
    lVar9 = uVar4 - uVar5;
    puVar6 = local_30 + uVar5;
    puVar8 = param_2 + uVar5 + 3;
    do {
      lVar9 = lVar9 + -1;
      *puVar6 = *puVar8;
      puVar6 = puVar6 + 1;
      puVar8 = puVar8 + 1;
    } while (lVar9 != 0);
  }
  else {
    uVar5 = uVar4 & 0xfffffffe;
    puVar6 = param_2 + 4;
    puVar8 = local_30 + 1;
    uVar7 = uVar5;
    do {
      puVar1 = puVar6 + -1;
      uVar10 = *puVar6;
      puVar6 = puVar6 + 2;
      uVar7 = uVar7 - 2;
      puVar8[-1] = *puVar1;
      *puVar8 = uVar10;
      puVar8 = puVar8 + 2;
    } while (uVar7 != 0);
    if (uVar5 != uVar4) goto LAB_ffff0000907a98d8;
  }
  uVar7 = 0;
  uVar5 = 0;
  do {
    uVar10 = uVar5 + 1;
    uVar11 = param_1[uVar5];
    uVar12 = uVar11 + uVar7 + local_30[uVar5];
    param_1[uVar5] = uVar12;
    if (uVar12 != uVar11) {
      uVar7 = (ulong)(uVar12 < uVar11);
    }
    uVar5 = uVar10;
  } while (uVar4 != uVar10);
  uVar5 = 0;
  local_30[0] = 0;
  local_30[1] = param_2[3];
  local_30[2] = param_2[4];
  uVar10 = 0;
  do {
    uVar12 = uVar10 + 1;
    uVar13 = param_1[uVar10];
    uVar11 = uVar13 + uVar5 + local_30[uVar10];
    param_1[uVar10] = uVar11;
    if (uVar11 != uVar13) {
      uVar5 = (ulong)(uVar11 < uVar13);
    }
    uVar10 = uVar12;
  } while (uVar4 != uVar12);
  uVar10 = 0;
  local_30[0] = param_2[5];
  local_30[1] = param_2[5];
  local_30[2] = 0;
  puVar6 = local_30;
  puVar8 = param_1;
  uVar12 = uVar4;
  do {
    uVar13 = *puVar8;
    uVar11 = uVar13 + uVar10 + *puVar6;
    if (uVar11 != uVar13) {
      uVar10 = (ulong)(uVar11 < uVar13);
    }
    uVar12 = uVar12 - 1;
    *puVar8 = uVar11;
    puVar6 = puVar6 + 1;
    puVar8 = puVar8 + 1;
  } while (uVar12 != 0);
  lVar9 = uVar10 + uVar5 + uVar7;
  plVar3 = DAT_ffff00009090afc0;
LAB_ffff0000907a99e8:
  do {
    uVar2 = param_3;
    if ((int)lVar9 == 0) {
      do {
        uVar2 = uVar2 - 1;
        if ((int)uVar2 < 0) break;
        if (param_1[uVar2] < (ulong)plVar3[uVar2]) {
          return;
        }
      } while (param_1[uVar2] <= (ulong)plVar3[uVar2]);
    }
    uVar7 = 0;
    puVar6 = param_1;
    plVar14 = plVar3;
    uVar5 = uVar4;
    if (param_3 != 0) {
      do {
        uVar10 = *plVar14 + uVar7;
        if (uVar10 != 0) {
          uVar7 = (ulong)(*puVar6 < uVar10);
        }
        uVar5 = uVar5 - 1;
        *puVar6 = *puVar6 - uVar10;
        puVar6 = puVar6 + 1;
        plVar14 = plVar14 + 1;
      } while (uVar5 != 0);
    }
    lVar9 = (long)(int)lVar9 - uVar7;
  } while( true );
}


