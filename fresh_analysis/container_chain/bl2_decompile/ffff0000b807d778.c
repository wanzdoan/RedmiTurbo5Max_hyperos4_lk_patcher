
void FUN_ffff0000b807d778(long *param_1,int param_2)

{
  byte *pbVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  byte *pbVar7;
  long lVar8;
  uint uVar9;
  byte bVar10;
  uint uVar11;
  long lVar12;
  long lVar13;
  uint uVar14;
  ulong uVar15;
  byte *pbVar16;
  byte *pbVar17;
  long lVar18;
  byte *pbVar19;
  byte *pbVar20;
  byte *pbVar21;
  undefined4 uVar22;
  char *pcVar23;
  ulong uVar24;
  uint uVar25;
  long lVar26;
  ulong uVar27;
  long lVar28;
  byte *pbVar29;
  long lVar30;
  uint uVar31;
  ulong uVar32;
  
  lVar13 = param_1[7];
  lVar12 = param_1[4];
  pbVar19 = (byte *)param_1[3];
  uVar3 = *(uint *)(lVar13 + 0x78);
  uVar5 = *(uint *)(lVar13 + 0x7c);
  iVar4 = *(int *)(lVar13 + 0x3c);
  uVar6 = *(uint *)(lVar13 + 0x40);
  pbVar16 = (byte *)*param_1;
  uVar9 = *(uint *)(lVar13 + 0x44);
  pbVar7 = *(byte **)(lVar13 + 0x48);
  uVar15 = *(ulong *)(lVar13 + 0x50);
  lVar18 = *(long *)(lVar13 + 0x68);
  lVar8 = *(long *)(lVar13 + 0x70);
  pbVar1 = pbVar16 + ((int)param_1[1] - 5);
  uVar24 = (ulong)*(uint *)(lVar13 + 0x58);
  pbVar21 = pbVar19;
LAB_ffff0000b807d800:
  uVar14 = (uint)uVar24;
  if (uVar14 < 0xf) {
    bVar10 = *pbVar16;
    pbVar20 = pbVar16 + 1;
    pbVar16 = pbVar16 + 2;
    uVar27 = uVar24 & 0x3f;
    uVar24 = (ulong)(uVar14 + 0x10);
    uVar15 = ((ulong)bVar10 << uVar27) + uVar15 + ((ulong)*pbVar20 << ((ulong)(uVar14 + 8) & 0x3f));
  }
  pbVar20 = (byte *)(lVar18 + (uVar15 & (uint)~(-1 << (ulong)(uVar3 & 0x1f))) * 4);
  while( true ) {
    bVar10 = *pbVar20;
    uVar15 = uVar15 >> ((ulong)pbVar20[1] & 0x3f);
    uVar14 = (int)uVar24 - (uint)pbVar20[1];
    uVar24 = (ulong)uVar14;
    if (bVar10 == 0) break;
    pbVar17 = pbVar16;
    if ((bVar10 >> 4 & 1) != 0) {
      uVar25 = (uint)*(ushort *)(pbVar20 + 2);
      uVar31 = bVar10 & 0xf;
      if ((bVar10 & 0xf) != 0) {
        if (uVar14 < uVar31) {
          pbVar17 = pbVar16 + 1;
          uVar14 = uVar14 + 8;
          uVar15 = ((ulong)*pbVar16 << (uVar24 & 0x3f)) + uVar15;
        }
        uVar24 = (ulong)(uVar14 - uVar31);
        uVar14 = (uint)uVar15;
        uVar15 = uVar15 >> uVar31;
        uVar25 = (uVar14 & (-1 << (ulong)uVar31 ^ 0xffffffffU)) + uVar25;
      }
      uVar14 = (uint)uVar24;
      if (uVar14 < 0xf) {
        bVar10 = *pbVar17;
        pbVar16 = pbVar17 + 1;
        pbVar17 = pbVar17 + 2;
        uVar27 = uVar24 & 0x3f;
        uVar24 = (ulong)(uVar14 + 0x10);
        uVar15 = ((ulong)bVar10 << uVar27) + uVar15 +
                 ((ulong)*pbVar16 << ((ulong)(uVar14 + 8) & 0x3f));
      }
      pbVar20 = (byte *)(lVar8 + (uVar15 & (uint)~(-1 << (ulong)(uVar5 & 0x1f))) * 4);
      goto LAB_ffff0000b807d8f4;
    }
    if ((bVar10 >> 6 & 1) != 0) {
      if ((bVar10 >> 5 & 1) != 0) {
        uVar22 = 0x3f3f;
        goto LAB_ffff0000b807dbc4;
      }
      pcVar23 = s_invalid_literal_length_code_ffff0000b809ff9d;
      goto LAB_ffff0000b807dbbc;
    }
    pbVar20 = (byte *)(lVar18 + (ulong)*(ushort *)(pbVar20 + 2) * 4 +
                      (uVar15 & (uint)~(-1 << (ulong)(bVar10 & 0x1f))) * 4);
  }
  *pbVar21 = pbVar20[2];
  pbVar20 = pbVar21 + 1;
  goto LAB_ffff0000b807d874;
LAB_ffff0000b807d8f4:
  bVar10 = *pbVar20;
  uVar15 = uVar15 >> ((ulong)pbVar20[1] & 0x3f);
  uVar14 = (int)uVar24 - (uint)pbVar20[1];
  uVar24 = (ulong)uVar14;
  if ((bVar10 >> 4 & 1) != 0) goto LAB_ffff0000b807d928;
  pcVar23 = s_invalid_distance_code_ffff0000b809d240;
  if ((bVar10 >> 6 & 1) != 0) goto LAB_ffff0000b807dbbc;
  pbVar20 = (byte *)(lVar8 + (ulong)*(ushort *)(pbVar20 + 2) * 4 +
                    (uVar15 & (uint)~(-1 << (ulong)(bVar10 & 0x1f))) * 4);
  goto LAB_ffff0000b807d8f4;
LAB_ffff0000b807d928:
  uVar2 = bVar10 & 0xf;
  pbVar16 = pbVar17;
  uVar31 = uVar14;
  if (uVar14 < uVar2) {
    pbVar16 = pbVar17 + 1;
    uVar15 = ((ulong)*pbVar17 << (uVar24 & 0x3f)) + uVar15;
    uVar31 = uVar14 + 8;
    if (uVar31 < uVar2) {
      uVar15 = ((ulong)pbVar17[1] << ((ulong)uVar31 & 0x3f)) + uVar15;
      pbVar16 = pbVar17 + 2;
      uVar31 = uVar14 + 0x10;
    }
  }
  uVar11 = (int)pbVar21 - ((int)pbVar19 - (param_2 - (int)lVar12));
  uVar27 = uVar15 & (uint)~(-1 << uVar2);
  uVar15 = uVar15 >> uVar2;
  uVar27 = uVar27 + *(ushort *)(pbVar20 + 2);
  uVar14 = uVar31 - uVar2;
  uVar24 = (ulong)uVar14;
  uVar31 = (uint)uVar27 - uVar11;
  if ((uint)uVar27 < uVar11 || uVar31 == 0) {
    lVar28 = -(uVar27 & 0xffffffff);
    lVar26 = 0;
    uVar14 = uVar25;
    do {
      lVar30 = lVar26;
      pbVar20 = pbVar21 + lVar30 + (1 - (uVar27 & 0xffffffff));
      pbVar17 = pbVar21 + lVar30;
      uVar14 = uVar14 - 3;
      lVar26 = lVar30 + 3;
      *pbVar17 = pbVar20[-1];
      pbVar17[1] = *pbVar20;
      pbVar17[2] = pbVar20[1];
    } while (2 < uVar14);
    pbVar17 = pbVar21 + lVar26;
    pbVar20 = pbVar17;
    if (uVar25 != (uint)lVar26) {
      *pbVar17 = pbVar17[lVar28];
      pbVar20 = pbVar17 + 1;
      if (uVar25 - 1 != (uint)lVar26) {
        pbVar17[1] = pbVar21[lVar28 + lVar26 + 1];
        pbVar20 = pbVar21 + lVar30 + 5;
      }
    }
  }
  else {
    if ((uVar6 < uVar31) &&
       (pbVar17 = pbVar16, pcVar23 = s_invalid_distance_too_far_back_ffff0000b8095052,
       *(int *)(lVar13 + 0x1be8) != 0)) {
LAB_ffff0000b807dbbc:
      uVar22 = 0x3f51;
      param_1[6] = (long)pcVar23;
      pbVar16 = pbVar17;
LAB_ffff0000b807dbc4:
      *(undefined4 *)(lVar13 + 8) = uVar22;
      pbVar20 = pbVar21;
LAB_ffff0000b807dbc8:
      param_1[3] = (long)pbVar20;
      lVar18 = (long)pbVar16 - (ulong)(uVar14 >> 3);
      *param_1 = lVar18;
      *(int *)(param_1 + 4) = ((int)(pbVar19 + ((int)lVar12 - 0x101)) - (int)pbVar20) + 0x101;
      *(int *)(param_1 + 1) = ((int)pbVar1 - (int)lVar18) + 5;
      *(ulong *)(lVar13 + 0x50) = uVar15 & (uint)~(-1 << (ulong)(uVar14 & 7));
      *(uint *)(lVar13 + 0x58) = uVar14 & 7;
      return;
    }
    pbVar17 = pbVar21;
    if (uVar9 == 0) {
      uVar14 = uVar25 - uVar31;
      pbVar20 = pbVar7 + (iVar4 - uVar31);
      pbVar29 = pbVar7 + (iVar4 - uVar31);
      if (uVar31 <= uVar25 && uVar14 != 0) {
        do {
          uVar31 = uVar31 - 1;
          pbVar17 = pbVar21 + 1;
          *pbVar21 = *pbVar20;
          pbVar21 = pbVar17;
          pbVar20 = pbVar20 + 1;
        } while (uVar31 != 0);
        pbVar29 = pbVar17 + -uVar27;
        uVar25 = uVar14;
      }
    }
    else if (uVar9 < uVar31) {
      uVar32 = (ulong)((uVar9 + iVar4) - uVar31);
      uVar14 = uVar25 - (uVar31 - uVar9);
      if (uVar25 < uVar31 - uVar9 || uVar14 == 0) {
        pbVar29 = pbVar7 + uVar32;
      }
      else {
        do {
          pbVar20 = pbVar7 + uVar32;
          uVar31 = uVar31 - 1;
          uVar32 = uVar32 + 1;
          pbVar17 = pbVar21 + 1;
          *pbVar21 = *pbVar20;
          pbVar21 = pbVar17;
        } while (uVar9 != uVar31);
        pbVar29 = pbVar7;
        uVar25 = uVar14;
        if (uVar9 < uVar14) {
          lVar26 = 0;
          do {
            pbVar17[lVar26] = pbVar7[lVar26];
            lVar26 = lVar26 + 1;
          } while (uVar9 != (uint)lVar26);
          pbVar17 = pbVar17 + lVar26;
          pbVar29 = pbVar17 + -(uVar27 & 0xffffffff);
          uVar25 = uVar14 - uVar9;
        }
      }
    }
    else {
      uVar14 = uVar25 - uVar31;
      pbVar20 = pbVar7 + (uVar9 - uVar31);
      pbVar29 = pbVar7 + (uVar9 - uVar31);
      if (uVar31 <= uVar25 && uVar14 != 0) {
        do {
          uVar31 = uVar31 - 1;
          pbVar17 = pbVar21 + 1;
          *pbVar21 = *pbVar20;
          pbVar21 = pbVar17;
          pbVar20 = pbVar20 + 1;
        } while (uVar31 != 0);
        pbVar29 = pbVar17 + -(uVar27 & 0xffffffff);
        uVar25 = uVar14;
      }
    }
    for (; 2 < uVar25; uVar25 = uVar25 - 3) {
      *pbVar17 = *pbVar29;
      pbVar17[1] = pbVar29[1];
      pbVar17[2] = pbVar29[2];
      pbVar17 = pbVar17 + 3;
      pbVar29 = pbVar29 + 3;
    }
    pbVar20 = pbVar17;
    if (uVar25 != 0) {
      *pbVar17 = *pbVar29;
      pbVar20 = pbVar17 + 1;
      if (uVar25 != 1) {
        pbVar17[1] = pbVar29[1];
        pbVar20 = pbVar17 + 2;
      }
    }
  }
LAB_ffff0000b807d874:
  uVar14 = (uint)uVar24;
  if ((pbVar1 <= pbVar16) || (pbVar21 = pbVar20, pbVar19 + ((int)lVar12 - 0x101) <= pbVar20))
  goto LAB_ffff0000b807dbc8;
  goto LAB_ffff0000b807d800;
}


