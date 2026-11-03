// ffff00009076bccc  size=828  FUN_ffff00009076bccc

ulong FUN_ffff00009076bccc(ulong param_1,byte *param_2,ulong param_3)

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
  ulong uVar18;
  bool bVar19;
  ulong uVar20;
  ulong uVar21;
  ulong uVar22;
  int iVar23;
  byte *pbVar24;
  
  uVar20 = param_1 >> 0x10 & 0xffff;
  param_1 = param_1 & 0xffff;
  if (param_3 == 1) {
    param_1 = param_1 + *param_2;
    uVar21 = param_1 - 0xfff1;
    if (param_1 < 0xfff1) {
      uVar21 = param_1;
    }
    uVar22 = (uVar21 + uVar20) * 0x10000;
    uVar18 = uVar22 - 0xfff10000;
    if (uVar21 + uVar20 < 0xfff1) {
      uVar18 = uVar22;
    }
    return uVar18 | uVar21;
  }
  if (param_2 == (byte *)0x0) {
    return 1;
  }
  if (param_3 < 0x10) {
    for (; param_3 != 0; param_3 = param_3 - 1) {
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
LAB_ffff00009076bed8:
      do {
        param_3 = param_3 - 0x10;
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
        if (param_3 < 0x10) goto joined_r0xffff00009076bfa8;
      } while( true );
    }
    do {
      param_3 = param_3 - 0x15b0;
      iVar23 = -0x15b;
      pbVar24 = param_2;
      do {
        bVar19 = iVar23 != -1;
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
      } while (bVar19);
      param_2 = param_2 + 0x15b0;
      param_1 = param_1 % 0xfff1;
      uVar20 = uVar20 % 0xfff1;
    } while (0x15a < param_3 >> 4);
    uVar21 = param_1;
    if (param_3 != 0) {
      if (0xf < param_3) goto LAB_ffff00009076bed8;
      do {
        param_3 = param_3 - 1;
        param_1 = param_1 + *param_2;
        uVar20 = param_1 + uVar20;
        param_2 = param_2 + 1;
joined_r0xffff00009076bfa8:
      } while (param_3 != 0);
      uVar20 = uVar20 % 0xfff1;
      uVar21 = param_1 % 0xfff1;
    }
  }
  return uVar21 | uVar20 << 0x10;
}


