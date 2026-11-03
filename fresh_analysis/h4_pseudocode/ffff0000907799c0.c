
void FUN_ffff0000907799c0(long param_1,long *param_2)

{
  bool bVar1;
  long lVar2;
  short *psVar3;
  byte bVar4;
  byte bVar5;
  ushort uVar6;
  ushort uVar7;
  uint uVar8;
  int iVar9;
  int iVar10;
  ulong uVar11;
  int iVar12;
  long lVar13;
  long lVar14;
  ulong uVar15;
  long lVar16;
  long lVar17;
  uint uVar18;
  uint uVar19;
  ulong uVar20;
  long *plVar21;
  long lVar22;
  int iVar23;
  undefined2 *puVar24;
  long lVar25;
  uint uVar26;
  uint uVar27;
  long lVar28;
  int iVar29;
  long lVar30;
  short *psVar31;
  int *piVar32;
  ushort *puVar33;
  ushort auStack_30 [4];
  short local_28;
  short local_26;
  short local_24;
  short local_22;
  short local_20;
  short local_1e;
  short local_1c;
  short local_1a;
  short local_18;
  short local_16;
  short local_14;
  short local_12;
  
  lVar14 = *param_2;
  uVar26 = *(uint *)(param_2[2] + 0x14);
  lVar16 = *(long *)param_2[2];
  *(undefined8 *)(param_1 + 0x14b4) = 0x23d00000000;
  if ((int)uVar26 < 1) {
    uVar18 = 0;
    uVar15 = 0xffffffff;
  }
  else {
    uVar20 = 0;
    puVar24 = (undefined2 *)(lVar14 + 2);
    uVar15 = 0xffffffff;
    do {
      if (puVar24[-1] == 0) {
        *puVar24 = 0;
      }
      else {
        lVar13 = (long)*(int *)(param_1 + 0x14b4) + 1;
        uVar15 = uVar20 & 0xffffffff;
        *(int *)(param_1 + 0x14b4) = (int)lVar13;
        *(int *)(param_1 + lVar13 * 4 + 0xbc0) = (int)uVar20;
        *(undefined1 *)(param_1 + 0x14bc + uVar20) = 0;
      }
      uVar27 = (uint)uVar15;
      puVar24 = puVar24 + 2;
      uVar20 = uVar20 + 1;
    } while (uVar26 != uVar20);
    uVar18 = *(uint *)(param_1 + 0x14b4);
    if (1 < (int)uVar18) goto LAB_ffff000090779a50;
  }
  do {
    uVar27 = (uint)uVar15;
    uVar19 = 0;
    if ((int)uVar27 < 2) {
      uVar19 = uVar27 + 1;
    }
    *(int *)(param_1 + 0x14b4) = (int)((long)(int)uVar18 + 1);
    *(uint *)(param_1 + ((long)(int)uVar18 + 1) * 4 + 0xbc0) = uVar19;
    *(undefined2 *)(lVar14 + (-(ulong)(uVar19 >> 0x1f) & 0xfffffffc00000000 | (ulong)uVar19 << 2)) =
         1;
    *(undefined1 *)(param_1 + (int)uVar19 + 0x14bc) = 0;
    if ((int)uVar27 < 2) {
      uVar27 = uVar27 + 1;
    }
    uVar15 = (ulong)uVar27;
    *(long *)(param_1 + 0x1718) = *(long *)(param_1 + 0x1718) + -1;
    if (lVar16 != 0) {
      *(ulong *)(param_1 + 0x1720) =
           *(long *)(param_1 + 0x1720) - (ulong)*(ushort *)(lVar16 + (long)(int)uVar19 * 4 + 2);
    }
    uVar18 = *(uint *)(param_1 + 0x14b4);
  } while ((int)uVar18 < 2);
LAB_ffff000090779a50:
  *(uint *)(param_2 + 1) = uVar27;
  uVar15 = (ulong)(uVar18 >> 1);
  do {
    iVar9 = *(int *)(param_1 + 0x14b4);
    uVar19 = (uint)uVar15;
    uVar18 = uVar19 * 2;
    iVar23 = *(int *)(param_1 + uVar15 * 4 + 0xbc0);
    if ((int)uVar18 <= iVar9) {
      uVar20 = uVar15 & 0xffffffff;
      do {
        uVar8 = uVar18;
        if ((int)uVar18 < iVar9) {
          uVar11 = (ulong)(int)uVar18;
          lVar16 = (long)*(int *)(param_1 + 0xbc0 + (uVar11 | 1) * 4);
          lVar13 = (long)*(int *)(param_1 + 0xbc0 + (long)(int)uVar18 * 4);
          uVar6 = *(ushort *)(lVar14 + lVar16 * 4);
          uVar7 = *(ushort *)(lVar14 + lVar13 * 4);
          if ((uVar6 < uVar7) ||
             ((uVar6 == uVar7 &&
              (*(byte *)(param_1 + 0x14bc + lVar16) <= *(byte *)(param_1 + 0x14bc + lVar13))))) {
            uVar11 = uVar11 | 1;
            uVar8 = uVar18 | 1;
          }
        }
        else {
          uVar11 = (ulong)(int)uVar18;
        }
        uVar6 = *(ushort *)(lVar14 + (long)iVar23 * 4);
        uVar18 = *(uint *)(param_1 + uVar11 * 4 + 0xbc0);
        uVar7 = *(ushort *)
                 (lVar14 + (-(ulong)(uVar18 >> 0x1f) & 0xfffffffc00000000 | (ulong)uVar18 << 2));
        uVar19 = (uint)uVar20;
        if ((uVar6 < uVar7) ||
           ((uVar6 == uVar7 &&
            (*(byte *)(param_1 + 0x14bc + (long)iVar23) <= *(byte *)(param_1 + (int)uVar18 + 0x14bc)
            )))) break;
        *(uint *)(param_1 + (long)(int)uVar19 * 4 + 0xbc0) = uVar18;
        uVar18 = uVar8 * 2;
        iVar9 = *(int *)(param_1 + 0x14b4);
        uVar20 = (ulong)uVar8;
        uVar19 = uVar8;
      } while ((int)uVar18 <= iVar9);
    }
    uVar20 = uVar15 - 1;
    *(int *)(param_1 + (long)(int)uVar19 * 4 + 0xbc0) = iVar23;
    bVar1 = 0 < (long)uVar15;
    uVar15 = uVar20;
  } while (uVar20 != 0 && bVar1);
  iVar9 = *(int *)(param_1 + 0x14b4);
  lVar16 = (long)(int)uVar26;
  do {
    iVar10 = iVar9 + -1;
    *(int *)(param_1 + 0x14b4) = iVar10;
    iVar23 = *(int *)(param_1 + (long)iVar9 * 4 + 0xbc0);
    iVar29 = *(int *)(param_1 + 0xbc4);
    *(int *)(param_1 + 0xbc4) = iVar23;
    if (iVar9 < 3) {
      uVar18 = 1;
    }
    else {
      uVar26 = 2;
      uVar18 = 1;
      do {
        uVar19 = uVar26;
        if ((int)uVar26 < iVar10) {
          uVar15 = (ulong)(int)uVar26;
          lVar13 = (long)*(int *)(param_1 + 0xbc0 + (uVar15 | 1) * 4);
          lVar25 = (long)*(int *)(param_1 + 0xbc0 + (long)(int)uVar26 * 4);
          uVar6 = *(ushort *)(lVar14 + lVar13 * 4);
          uVar7 = *(ushort *)(lVar14 + lVar25 * 4);
          if ((uVar6 < uVar7) ||
             ((uVar6 == uVar7 &&
              (*(byte *)(param_1 + 0x14bc + lVar13) <= *(byte *)(param_1 + 0x14bc + lVar25))))) {
            uVar15 = uVar15 | 1;
            uVar19 = uVar26 | 1;
          }
        }
        else {
          uVar15 = (ulong)(int)uVar26;
        }
        uVar6 = *(ushort *)(lVar14 + (long)iVar23 * 4);
        uVar26 = *(uint *)(param_1 + uVar15 * 4 + 0xbc0);
        uVar7 = *(ushort *)
                 (lVar14 + (-(ulong)(uVar26 >> 0x1f) & 0xfffffffc00000000 | (ulong)uVar26 << 2));
        if ((uVar6 < uVar7) ||
           ((uVar6 == uVar7 &&
            (*(byte *)(param_1 + 0x14bc + (long)iVar23) <= *(byte *)(param_1 + (int)uVar26 + 0x14bc)
            )))) break;
        *(uint *)(param_1 + (long)(int)uVar18 * 4 + 0xbc0) = uVar26;
        uVar26 = uVar19 * 2;
        iVar10 = *(int *)(param_1 + 0x14b4);
        uVar18 = uVar19;
      } while ((int)uVar26 <= iVar10);
    }
    lVar13 = param_1 + 0xbc0;
    *(int *)(lVar13 + (long)(int)uVar18 * 4) = iVar23;
    psVar3 = (short *)(lVar14 + (long)iVar29 * 4);
    iVar9 = *(int *)(param_1 + 0xbc4);
    lVar25 = (long)*(int *)(param_1 + 0x14b8) + -1;
    psVar31 = (short *)(lVar14 + (long)iVar9 * 4);
    *(int *)(param_1 + 0x14b8) = (int)lVar25;
    *(int *)(lVar13 + lVar25 * 4) = iVar29;
    lVar25 = (long)*(int *)(param_1 + 0x14b8) + -1;
    *(int *)(param_1 + 0x14b8) = (int)lVar25;
    *(int *)(lVar13 + lVar25 * 4) = iVar9;
    puVar33 = (ushort *)(lVar14 + lVar16 * 4);
    lVar13 = param_1 + 0x14bc;
    *puVar33 = *psVar31 + *psVar3;
    bVar4 = *(byte *)(lVar13 + iVar29);
    bVar5 = *(byte *)(lVar13 + iVar9);
    if (bVar4 <= bVar5) {
      bVar4 = bVar5;
    }
    *(byte *)(lVar13 + lVar16) = bVar4 + 1;
    psVar31[1] = (short)lVar16;
    psVar3[1] = (short)lVar16;
    iVar9 = *(int *)(param_1 + 0x14b4);
    *(int *)(param_1 + 0xbc4) = (int)lVar16;
    if (iVar9 < 2) {
      uVar26 = 1;
    }
    else {
      uVar18 = 2;
      uVar26 = 1;
      do {
        uVar19 = uVar18;
        if ((int)uVar18 < iVar9) {
          uVar15 = (ulong)(int)uVar18;
          lVar28 = (long)*(int *)(param_1 + 0xbc0 + (uVar15 | 1) * 4);
          lVar25 = (long)*(int *)(param_1 + 0xbc0 + (long)(int)uVar18 * 4);
          uVar6 = *(ushort *)(lVar14 + lVar28 * 4);
          uVar7 = *(ushort *)(lVar14 + lVar25 * 4);
          if ((uVar6 < uVar7) ||
             ((uVar6 == uVar7 &&
              (*(byte *)(param_1 + 0x14bc + lVar28) <= *(byte *)(param_1 + 0x14bc + lVar25))))) {
            uVar15 = uVar15 | 1;
            uVar19 = uVar18 | 1;
          }
        }
        else {
          uVar15 = (ulong)(int)uVar18;
        }
        uVar6 = *puVar33;
        uVar18 = *(uint *)(param_1 + uVar15 * 4 + 0xbc0);
        uVar7 = *(ushort *)
                 (lVar14 + (-(ulong)(uVar18 >> 0x1f) & 0xfffffffc00000000 | (ulong)uVar18 << 2));
        if ((uVar6 < uVar7) ||
           ((uVar6 == uVar7 &&
            (*(byte *)(lVar13 + lVar16) <= *(byte *)(param_1 + (int)uVar18 + 0x14bc))))) break;
        *(uint *)(param_1 + (long)(int)uVar26 * 4 + 0xbc0) = uVar18;
        uVar18 = uVar19 * 2;
        iVar9 = *(int *)(param_1 + 0x14b4);
        uVar26 = uVar19;
      } while ((int)uVar18 <= iVar9);
    }
    *(int *)(param_1 + (long)(int)uVar26 * 4 + 0xbc0) = (int)lVar16;
    lVar16 = lVar16 + 1;
    iVar9 = *(int *)(param_1 + 0x14b4);
    if (iVar9 < 2) {
      lVar16 = (long)*(int *)(param_1 + 0x14b8) + -1;
      *(int *)(param_1 + 0x14b8) = (int)lVar16;
      *(undefined4 *)(param_1 + 0xbc0 + lVar16 * 4) = *(undefined4 *)(param_1 + 0xbc4);
      plVar21 = (long *)param_2[2];
      lVar17 = *param_2;
      lVar25 = param_2[1];
      lVar16 = *plVar21;
      lVar13 = plVar21[1];
      lVar28 = plVar21[2];
      iVar9 = (int)plVar21[3];
      lVar22 = (long)iVar9;
      *(undefined8 *)(param_1 + 0xba0) = 0;
      *(undefined8 *)(param_1 + 0xba8) = 0;
      *(undefined8 *)(param_1 + 0xbb0) = 0;
      *(undefined8 *)(param_1 + 3000) = 0;
      *(undefined2 *)
       (lVar17 + (long)*(int *)(param_1 + 0xbc0 + (long)*(int *)(param_1 + 0x14b8) * 4) * 4 + 2) = 0
      ;
      if (*(int *)(param_1 + 0x14b8) < 0x23c) {
        iVar23 = 0;
        lVar30 = (long)*(int *)(param_1 + 0x14b8);
        do {
          iVar29 = *(int *)(param_1 + 0xbc4 + lVar30 * 4);
          puVar33 = (ushort *)(lVar17 + (long)iVar29 * 4);
          uVar6 = *(ushort *)(lVar17 + (ulong)puVar33[1] * 4 + 2);
          if ((int)(uint)uVar6 < iVar9) {
            iVar10 = uVar6 + 1;
          }
          else {
            iVar23 = iVar23 + 1;
            iVar10 = iVar9;
          }
          puVar33[1] = (ushort)iVar10;
          if (iVar29 <= (int)lVar25) {
            lVar2 = param_1 + (long)iVar10 * 2;
            *(short *)(lVar2 + 0xba0) = *(short *)(lVar2 + 0xba0) + 1;
            if (iVar29 < (int)lVar28) {
              iVar12 = 0;
            }
            else {
              iVar12 = *(int *)(lVar13 + (long)(iVar29 - (int)lVar28) * 4);
            }
            uVar6 = *puVar33;
            *(ulong *)(param_1 + 0x1718) =
                 *(long *)(param_1 + 0x1718) + (ulong)uVar6 * (ulong)(uint)(iVar12 + iVar10);
            if (lVar16 != 0) {
              *(ulong *)(param_1 + 0x1720) =
                   *(long *)(param_1 + 0x1720) +
                   (ulong)uVar6 * (ulong)(iVar12 + (uint)*(ushort *)(lVar16 + (long)iVar29 * 4 + 2))
              ;
            }
          }
          iVar29 = (int)lVar30;
          lVar30 = lVar30 + 1;
        } while (iVar29 != 0x23b);
        if (iVar23 != 0) {
          psVar3 = (short *)(param_1 + lVar22 * 2 + 0xba0);
          lVar16 = (lVar22 << 0x20) + 0x100000000;
          lVar13 = lVar16;
          psVar31 = psVar3;
          do {
            do {
              psVar31 = psVar31 + -1;
              lVar13 = lVar13 + -0x100000000;
            } while (*psVar31 == 0);
            lVar13 = param_1 + (lVar13 >> 0x1f);
            iVar29 = iVar23 + -2;
            *psVar31 = *psVar31 + -1;
            *(short *)(lVar13 + 0xba0) = *(short *)(lVar13 + 0xba0) + 2;
            *psVar3 = *psVar3 + -1;
            bVar1 = 1 < iVar23;
            lVar13 = lVar16;
            psVar31 = psVar3;
            iVar23 = iVar29;
          } while (iVar29 != 0 && bVar1);
          if (iVar9 != 0) {
            iVar9 = 0x23d;
            do {
              uVar6 = *(ushort *)(param_1 + lVar22 * 2 + 0xba0);
              uVar26 = (uint)uVar6;
              if (uVar6 != 0) {
                do {
                  piVar32 = (int *)(param_1 + 0xbbc + (long)iVar9 * 4);
                  do {
                    iVar23 = *piVar32;
                    iVar9 = iVar9 + -1;
                    piVar32 = piVar32 + -1;
                  } while ((int)lVar25 < iVar23);
                  puVar33 = (ushort *)(lVar17 + (long)iVar23 * 4 + 2);
                  uVar6 = *puVar33;
                  if ((uint)lVar22 != (uint)uVar6) {
                    *(ulong *)(param_1 + 0x1718) =
                         *(long *)(param_1 + 0x1718) +
                         (lVar22 - (ulong)uVar6) * (ulong)*(ushort *)(lVar17 + (long)iVar23 * 4);
                    *puVar33 = (ushort)lVar22;
                  }
                  uVar26 = uVar26 - 1;
                } while (uVar26 != 0);
              }
              lVar22 = lVar22 + -1;
            } while ((int)lVar22 != 0);
          }
        }
      }
      auStack_30[1] = *(short *)(param_1 + 0xba0) * 2;
      auStack_30[2] = (auStack_30[1] + *(short *)(param_1 + 0xba2)) * 2;
      auStack_30[3] = (auStack_30[2] + *(short *)(param_1 + 0xba4)) * 2;
      local_28 = (auStack_30[3] + *(short *)(param_1 + 0xba6)) * 2;
      local_26 = (local_28 + *(short *)(param_1 + 0xba8)) * 2;
      local_24 = (local_26 + *(short *)(param_1 + 0xbaa)) * 2;
      local_22 = (local_24 + *(short *)(param_1 + 0xbac)) * 2;
      local_20 = (local_22 + *(short *)(param_1 + 0xbae)) * 2;
      local_1e = (local_20 + *(short *)(param_1 + 0xbb0)) * 2;
      local_1c = (local_1e + *(short *)(param_1 + 0xbb2)) * 2;
      local_1a = (local_1c + *(short *)(param_1 + 0xbb4)) * 2;
      local_18 = (local_1a + *(short *)(param_1 + 0xbb6)) * 2;
      local_16 = (*(short *)(param_1 + 3000) + local_18) * 2;
      local_14 = (*(short *)(param_1 + 0xbba) + local_16) * 2;
      local_12 = (local_14 + *(short *)(param_1 + 0xbbc)) * 2;
      if (-1 < (int)uVar27) {
        uVar15 = 0;
        do {
          uVar6 = *(ushort *)(lVar14 + uVar15 * 4 + 2);
          if (uVar6 != 0) {
            uVar18 = 0;
            uVar26 = uVar6 + 1;
            uVar19 = (uint)auStack_30[uVar6];
            auStack_30[uVar6] = auStack_30[uVar6] + 1;
            do {
              uVar8 = uVar19 & 1;
              uVar19 = uVar19 >> 1;
              uVar8 = uVar18 | uVar8;
              uVar26 = uVar26 - 1;
              uVar18 = uVar8 << 1;
            } while (1 < uVar26);
            *(short *)(lVar14 + uVar15 * 4) = (short)uVar8;
          }
          uVar15 = uVar15 + 1;
        } while (uVar15 != uVar27 + 1);
      }
      return;
    }
  } while( true );
}


