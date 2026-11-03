
void FUN_ffff0000b8081da4(long param_1,long param_2,int param_3)

{
  ushort uVar1;
  ushort uVar2;
  uint uVar3;
  int iVar4;
  ushort uVar5;
  int iVar6;
  uint uVar7;
  long lVar8;
  uint uVar9;
  long lVar10;
  ulong uVar11;
  uint uVar12;
  ulong uVar13;
  int iVar14;
  int iVar15;
  uint uVar16;
  ulong uVar17;
  
  if (-1 < param_3) {
    uVar5 = *(ushort *)(param_2 + 2);
    uVar11 = 0;
    iVar4 = 0;
    iVar15 = 0x8a;
    if (uVar5 != 0) {
      iVar15 = 7;
    }
    iVar14 = 3;
    if (uVar5 != 0) {
      iVar14 = 4;
    }
    uVar17 = 0xffffffff;
    do {
      uVar13 = (ulong)uVar5;
      uVar11 = uVar11 + 1;
      uVar1 = *(ushort *)(param_2 + uVar11 * 4 + 2);
      iVar6 = iVar4 + 1;
      uVar3 = (uint)uVar5;
      uVar12 = (uint)uVar1;
      if ((iVar15 <= iVar6) || (uVar3 != uVar12)) {
        if (iVar6 < iVar14) {
          lVar8 = param_1 + uVar13 * 4;
          uVar9 = *(uint *)(param_1 + 0x1734);
          do {
            uVar5 = *(ushort *)(lVar8 + 0xabc);
            uVar7 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar5 << (ulong)(uVar9 & 0x1f);
            uVar16 = (uint)*(ushort *)(lVar8 + 0xabe);
            *(short *)(param_1 + 0x1730) = (short)uVar7;
            if ((int)(0x10 - uVar16) < (int)uVar9) {
              lVar10 = *(long *)(param_1 + 0x28);
              *(long *)(param_1 + 0x28) = lVar10 + 1;
              *(char *)(*(long *)(param_1 + 0x10) + lVar10) = (char)uVar7;
              lVar10 = *(long *)(param_1 + 0x28);
              *(long *)(param_1 + 0x28) = lVar10 + 1;
              *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar10) =
                   *(undefined1 *)(param_1 + 0x1731);
              uVar9 = (uVar16 + *(int *)(param_1 + 0x1734)) - 0x10;
              *(ushort *)(param_1 + 0x1730) =
                   uVar5 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
            }
            else {
              uVar9 = uVar9 + uVar16;
            }
            iVar6 = iVar6 + -1;
            *(uint *)(param_1 + 0x1734) = uVar9;
          } while (iVar6 != 0);
        }
        else {
          if (uVar3 == 0) {
            uVar9 = *(uint *)(param_1 + 0x1734);
            if (iVar4 < 10) {
              uVar5 = *(ushort *)(param_1 + 0xb00);
              uVar2 = *(ushort *)(param_1 + 0xb02);
              uVar7 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar5 << (ulong)(uVar9 & 0x1f);
              if ((int)(0x10 - (uint)uVar2) < (int)uVar9) {
                lVar8 = *(long *)(param_1 + 0x28);
                *(short *)(param_1 + 0x1730) = (short)uVar7;
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(char *)(*(long *)(param_1 + 0x10) + lVar8) = (char)uVar7;
                lVar8 = *(long *)(param_1 + 0x28);
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar8) =
                     *(undefined1 *)(param_1 + 0x1731);
                uVar9 = ((uint)uVar2 + *(int *)(param_1 + 0x1734)) - 0x10;
                uVar7 = (uint)(uVar5 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f));
              }
              else {
                uVar9 = uVar9 + uVar2;
              }
              uVar16 = iVar4 + 0xfffeU << (ulong)(uVar9 & 0x1f);
              *(uint *)(param_1 + 0x1734) = uVar9;
              if ((int)uVar9 < 0xe) {
                uVar5 = (ushort)uVar7 | (ushort)uVar16;
                iVar15 = uVar9 + 3;
LAB_ffff0000b8082220:
                *(ushort *)(param_1 + 0x1730) = uVar5;
              }
              else {
                lVar8 = *(long *)(param_1 + 0x28);
                uVar7 = uVar7 | uVar16;
                *(short *)(param_1 + 0x1730) = (short)uVar7;
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(char *)(*(long *)(param_1 + 0x10) + lVar8) = (char)uVar7;
                lVar8 = *(long *)(param_1 + 0x28);
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar8) =
                     *(undefined1 *)(param_1 + 0x1731);
                iVar15 = *(int *)(param_1 + 0x1734) + -0xd;
                *(short *)(param_1 + 0x1730) =
                     (short)((iVar4 + 0xfffeU & 0xffff) >>
                            (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f));
              }
            }
            else {
              uVar5 = *(ushort *)(param_1 + 0xb04);
              uVar2 = *(ushort *)(param_1 + 0xb06);
              uVar7 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar5 << (ulong)(uVar9 & 0x1f);
              if ((int)(0x10 - (uint)uVar2) < (int)uVar9) {
                lVar8 = *(long *)(param_1 + 0x28);
                *(short *)(param_1 + 0x1730) = (short)uVar7;
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(char *)(*(long *)(param_1 + 0x10) + lVar8) = (char)uVar7;
                lVar8 = *(long *)(param_1 + 0x28);
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar8) =
                     *(undefined1 *)(param_1 + 0x1731);
                uVar9 = ((uint)uVar2 + *(int *)(param_1 + 0x1734)) - 0x10;
                uVar7 = (uint)(uVar5 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f));
              }
              else {
                uVar9 = uVar9 + uVar2;
              }
              uVar16 = iVar4 + 0xfff6U << (ulong)(uVar9 & 0x1f);
              *(uint *)(param_1 + 0x1734) = uVar9;
              if ((int)uVar9 < 10) {
                uVar5 = (ushort)uVar7 | (ushort)uVar16;
                iVar15 = uVar9 + 7;
                goto LAB_ffff0000b8082220;
              }
              lVar8 = *(long *)(param_1 + 0x28);
              uVar7 = uVar7 | uVar16;
              *(short *)(param_1 + 0x1730) = (short)uVar7;
              *(long *)(param_1 + 0x28) = lVar8 + 1;
              *(char *)(*(long *)(param_1 + 0x10) + lVar8) = (char)uVar7;
              lVar8 = *(long *)(param_1 + 0x28);
              *(long *)(param_1 + 0x28) = lVar8 + 1;
              *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar8) = *(undefined1 *)(param_1 + 0x1731)
              ;
              iVar15 = *(int *)(param_1 + 0x1734) + -9;
              *(short *)(param_1 + 0x1730) =
                   (short)((iVar4 + 0xfff6U & 0xffff) >>
                          (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f));
            }
          }
          else {
            if (uVar3 == (uint)uVar17) {
              uVar9 = *(uint *)(param_1 + 0x1734);
              iVar4 = iVar6;
            }
            else {
              lVar8 = param_1 + uVar13 * 4;
              uVar9 = *(uint *)(param_1 + 0x1734);
              uVar5 = *(ushort *)(lVar8 + 0xabc);
              uVar2 = *(ushort *)(lVar8 + 0xabe);
              uVar7 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar5 << (ulong)(uVar9 & 0x1f);
              *(short *)(param_1 + 0x1730) = (short)uVar7;
              if ((int)(0x10 - (uint)uVar2) < (int)uVar9) {
                lVar8 = *(long *)(param_1 + 0x28);
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(char *)(*(long *)(param_1 + 0x10) + lVar8) = (char)uVar7;
                lVar8 = *(long *)(param_1 + 0x28);
                *(long *)(param_1 + 0x28) = lVar8 + 1;
                *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar8) =
                     *(undefined1 *)(param_1 + 0x1731);
                uVar9 = ((uint)uVar2 + *(int *)(param_1 + 0x1734)) - 0x10;
                *(ushort *)(param_1 + 0x1730) =
                     uVar5 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
              }
              else {
                uVar9 = uVar9 + uVar2;
              }
              *(uint *)(param_1 + 0x1734) = uVar9;
            }
            uVar5 = *(ushort *)(param_1 + 0xafc);
            uVar2 = *(ushort *)(param_1 + 0xafe);
            uVar7 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar5 << (ulong)(uVar9 & 0x1f);
            if ((int)(0x10 - (uint)uVar2) < (int)uVar9) {
              lVar8 = *(long *)(param_1 + 0x28);
              *(short *)(param_1 + 0x1730) = (short)uVar7;
              *(long *)(param_1 + 0x28) = lVar8 + 1;
              *(char *)(*(long *)(param_1 + 0x10) + lVar8) = (char)uVar7;
              lVar8 = *(long *)(param_1 + 0x28);
              *(long *)(param_1 + 0x28) = lVar8 + 1;
              *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar8) = *(undefined1 *)(param_1 + 0x1731)
              ;
              uVar7 = (uint)(uVar5 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f));
              uVar9 = ((uint)uVar2 + *(int *)(param_1 + 0x1734)) - 0x10;
            }
            else {
              uVar9 = uVar9 + uVar2;
            }
            uVar16 = iVar4 + 0xfffd;
            *(uint *)(param_1 + 0x1734) = uVar9;
            if ((int)uVar9 < 0xf) {
              uVar5 = (ushort)uVar7 | (ushort)(uVar16 << (ulong)(uVar9 & 0x1f));
              iVar15 = uVar9 + 2;
              goto LAB_ffff0000b8082220;
            }
            lVar8 = *(long *)(param_1 + 0x28);
            uVar7 = uVar7 | uVar16 << (ulong)(uVar9 & 0x1f);
            *(short *)(param_1 + 0x1730) = (short)uVar7;
            *(long *)(param_1 + 0x28) = lVar8 + 1;
            *(char *)(*(long *)(param_1 + 0x10) + lVar8) = (char)uVar7;
            lVar8 = *(long *)(param_1 + 0x28);
            *(long *)(param_1 + 0x28) = lVar8 + 1;
            *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar8) = *(undefined1 *)(param_1 + 0x1731);
            iVar15 = *(int *)(param_1 + 0x1734) + -0xe;
            *(short *)(param_1 + 0x1730) =
                 (short)((uVar16 & 0xffff) >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f));
          }
          *(int *)(param_1 + 0x1734) = iVar15;
        }
        iVar6 = 0;
        uVar17 = uVar13;
        if (uVar12 == 0) {
          iVar15 = 0x8a;
          iVar14 = 3;
        }
        else {
          iVar14 = 3;
          if (uVar3 != uVar12) {
            iVar14 = 4;
          }
          iVar15 = 6;
          if (uVar3 != uVar12) {
            iVar15 = 7;
          }
        }
      }
      iVar4 = iVar6;
      uVar5 = uVar1;
    } while (uVar11 != param_3 + 1);
  }
  return;
}


