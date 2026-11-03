
ulong FUN_ffff0000b804683c(ulong param_1,byte *param_2,uint param_3)

{
  byte *pbVar1;
  byte *pbVar2;
  long lVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  long lVar9;
  long lVar10;
  long lVar11;
  long lVar12;
  long lVar13;
  long lVar14;
  long lVar15;
  long lVar16;
  long lVar17;
  bool bVar18;
  ulong uVar19;
  ulong uVar20;
  ulong uVar21;
  ulong uVar22;
  int iVar23;
  byte *pbVar24;
  
  uVar19 = (ulong)param_3;
  uVar20 = param_1 >> 0x10 & 0xffff;
  param_1 = param_1 & 0xffff;
  if (uVar19 == 1) {
    param_1 = param_1 + *param_2;
    uVar19 = param_1 - 0xfff1;
    if (param_1 < 0xfff1) {
      uVar19 = param_1;
    }
    uVar22 = (uVar19 + uVar20) * 0x10000;
    uVar21 = uVar22 - 0xfff10000;
    if (uVar19 + uVar20 < 0xfff1) {
      uVar21 = uVar22;
    }
    return uVar21 | uVar19;
  }
  if (param_2 == (byte *)0x0) {
    return 1;
  }
  if (uVar19 < 0x10) {
    for (; uVar19 != 0; uVar19 = uVar19 - 1) {
      param_1 = param_1 + *param_2;
      uVar20 = param_1 + uVar20;
      param_2 = param_2 + 1;
    }
    uVar20 = uVar20 % 0xfff1;
    uVar21 = param_1 - 0xfff1;
    if (param_1 < 0xfff1) {
      uVar21 = param_1;
    }
  }
  else {
    if (param_3 >> 4 < 0x15b) {
LAB_ffff0000b804670c:
      do {
        uVar19 = uVar19 - 0x10;
        lVar3 = param_1 + *param_2;
        lVar4 = lVar3 + (ulong)param_2[1];
        lVar5 = lVar4 + (ulong)param_2[2];
        lVar6 = lVar5 + (ulong)param_2[3];
        lVar7 = lVar6 + (ulong)param_2[4];
        lVar8 = lVar7 + (ulong)param_2[5];
        lVar9 = lVar8 + (ulong)param_2[6];
        lVar10 = lVar9 + (ulong)param_2[7];
        lVar11 = lVar10 + (ulong)param_2[8];
        lVar12 = lVar11 + (ulong)param_2[9];
        lVar13 = lVar12 + (ulong)param_2[10];
        lVar14 = lVar13 + (ulong)param_2[0xb];
        lVar15 = lVar14 + (ulong)param_2[0xc];
        lVar16 = lVar15 + (ulong)param_2[0xd];
        pbVar24 = param_2 + 0xf;
        lVar17 = lVar16 + (ulong)param_2[0xe];
        param_2 = param_2 + 0x10;
        param_1 = lVar17 + (ulong)*pbVar24;
        uVar20 = lVar3 + uVar20 + lVar4 + lVar5 + lVar6 + lVar7 + lVar8 + lVar9 + lVar10 + lVar11 +
                 lVar12 + lVar13 + lVar14 + lVar15 + lVar16 + lVar17 + param_1;
        if (uVar19 < 0x10) goto joined_r0xffff0000b80467dc;
      } while( true );
    }
    do {
      uVar19 = uVar19 - 0x15b0;
      iVar23 = -0x15b;
      pbVar24 = param_2;
      do {
        bVar18 = iVar23 != -1;
        iVar23 = iVar23 + 1;
        lVar3 = param_1 + *pbVar24;
        lVar4 = lVar3 + (ulong)pbVar24[1];
        lVar5 = lVar4 + (ulong)pbVar24[2];
        lVar6 = lVar5 + (ulong)pbVar24[3];
        lVar7 = lVar6 + (ulong)pbVar24[4];
        lVar8 = lVar7 + (ulong)pbVar24[5];
        lVar9 = lVar8 + (ulong)pbVar24[6];
        lVar10 = lVar9 + (ulong)pbVar24[7];
        pbVar1 = pbVar24 + 0xe;
        lVar11 = lVar10 + (ulong)pbVar24[8];
        lVar12 = lVar11 + (ulong)pbVar24[9];
        lVar13 = lVar12 + (ulong)pbVar24[10];
        lVar14 = lVar13 + (ulong)pbVar24[0xb];
        lVar15 = lVar14 + (ulong)pbVar24[0xc];
        pbVar2 = pbVar24 + 0xf;
        lVar16 = lVar15 + (ulong)pbVar24[0xd];
        pbVar24 = pbVar24 + 0x10;
        lVar17 = lVar16 + (ulong)*pbVar1;
        param_1 = lVar17 + (ulong)*pbVar2;
        uVar20 = lVar3 + uVar20 + lVar4 + lVar5 + lVar6 + lVar7 + lVar8 + lVar9 + lVar10 + lVar11 +
                 lVar12 + lVar13 + lVar14 + lVar15 + lVar16 + lVar17 + param_1;
      } while (bVar18);
      param_2 = param_2 + 0x15b0;
      param_1 = param_1 % 0xfff1;
      uVar20 = uVar20 % 0xfff1;
    } while (0x15a < uVar19 >> 4);
    uVar21 = param_1;
    if (uVar19 != 0) {
      if (0xf < uVar19) goto LAB_ffff0000b804670c;
      do {
        uVar19 = uVar19 - 1;
        param_1 = param_1 + *param_2;
        uVar20 = param_1 + uVar20;
        param_2 = param_2 + 1;
joined_r0xffff0000b80467dc:
      } while (uVar19 != 0);
      uVar20 = uVar20 % 0xfff1;
      uVar21 = param_1 % 0xfff1;
    }
  }
  return uVar21 | uVar20 << 0x10;
}


