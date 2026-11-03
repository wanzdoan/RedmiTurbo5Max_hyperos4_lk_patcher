
void FUN_ffff0000be05c0bc(long param_1,long param_2,long param_3)

{
  ushort *puVar1;
  byte bVar2;
  ushort uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  ushort uVar9;
  long lVar10;
  ulong uVar11;
  uint uVar12;
  int iVar13;
  
  if (*(int *)(param_1 + 0x170c) == 0) {
    uVar7 = *(uint *)(param_1 + 0x1734);
  }
  else {
    uVar12 = 0;
    do {
      lVar10 = *(long *)(param_1 + 0x1700);
      bVar2 = *(byte *)(lVar10 + (ulong)(uVar12 + 2));
      uVar4 = (uint)CONCAT11(*(undefined1 *)(lVar10 + (ulong)(uVar12 + 1)),
                             *(undefined1 *)(lVar10 + (ulong)uVar12));
      if (uVar4 == 0) {
        puVar1 = (ushort *)(param_2 + (ulong)bVar2 * 4);
        uVar7 = *(uint *)(param_1 + 0x1734);
        uVar9 = *puVar1;
        uVar4 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar9 << (ulong)(uVar7 & 0x1f);
        uVar8 = (uint)puVar1[1];
        *(short *)(param_1 + 0x1730) = (short)uVar4;
        if ((int)(0x10 - uVar8) < (int)uVar7) {
          lVar10 = *(long *)(param_1 + 0x28);
          *(long *)(param_1 + 0x28) = lVar10 + 1;
          *(char *)(*(long *)(param_1 + 0x10) + lVar10) = (char)uVar4;
          lVar10 = *(long *)(param_1 + 0x28);
          *(long *)(param_1 + 0x28) = lVar10 + 1;
          *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar10) = *(undefined1 *)(param_1 + 0x1731);
          uVar7 = (uVar8 + *(int *)(param_1 + 0x1734)) - 0x10;
          uVar9 = uVar9 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
          goto LAB_ffff0000be05c154;
        }
        uVar7 = uVar7 + uVar8;
LAB_ffff0000be05c158:
        *(uint *)(param_1 + 0x1734) = uVar7;
      }
      else {
        uVar11 = (ulong)(byte)(&DAT_ffff0000be0d187e)[bVar2];
        uVar7 = *(uint *)(param_1 + 0x1734);
        lVar10 = param_2 + uVar11 * 4;
        uVar9 = *(ushort *)(lVar10 + 0x404);
        uVar3 = *(ushort *)(lVar10 + 0x406);
        uVar8 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar9 << (ulong)(uVar7 & 0x1f);
        *(short *)(param_1 + 0x1730) = (short)uVar8;
        if ((int)(0x10 - (uint)uVar3) < (int)uVar7) {
          lVar10 = *(long *)(param_1 + 0x28);
          *(long *)(param_1 + 0x28) = lVar10 + 1;
          *(char *)(*(long *)(param_1 + 0x10) + lVar10) = (char)uVar8;
          lVar10 = *(long *)(param_1 + 0x28);
          *(long *)(param_1 + 0x28) = lVar10 + 1;
          *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar10) = *(undefined1 *)(param_1 + 0x1731);
          uVar7 = ((uint)uVar3 + *(int *)(param_1 + 0x1734)) - 0x10;
          uVar9 = uVar9 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
          uVar8 = (uint)uVar9;
          *(ushort *)(param_1 + 0x1730) = uVar9;
        }
        else {
          uVar7 = uVar7 + uVar3;
        }
        *(uint *)(param_1 + 0x1734) = uVar7;
        if (0xffffffffffffffeb < uVar11 - 0x1c) {
          iVar13 = *(int *)(&DAT_ffff0000be0d1e78 + uVar11 * 4);
          uVar6 = (uint)bVar2 - *(int *)(&DAT_ffff0000be0d1fc4 + uVar11 * 4);
          if (0x10 - iVar13 < (int)uVar7) {
            lVar10 = *(long *)(param_1 + 0x28);
            uVar8 = uVar8 | uVar6 << (ulong)(uVar7 & 0x1f);
            *(short *)(param_1 + 0x1730) = (short)uVar8;
            *(long *)(param_1 + 0x28) = lVar10 + 1;
            *(char *)(*(long *)(param_1 + 0x10) + lVar10) = (char)uVar8;
            lVar10 = *(long *)(param_1 + 0x28);
            *(long *)(param_1 + 0x28) = lVar10 + 1;
            *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar10) = *(undefined1 *)(param_1 + 0x1731);
            uVar7 = (iVar13 + *(int *)(param_1 + 0x1734)) - 0x10;
            uVar8 = (uVar6 & 0xffff) >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
          }
          else {
            uVar5 = uVar7 & 0x1f;
            uVar7 = iVar13 + uVar7;
            uVar8 = uVar8 | uVar6 << (ulong)uVar5;
          }
          *(short *)(param_1 + 0x1730) = (short)uVar8;
          *(uint *)(param_1 + 0x1734) = uVar7;
        }
        uVar5 = uVar4 - 1;
        uVar6 = uVar5;
        if (0x100 < uVar4) {
          uVar6 = (uVar5 >> 7) + 0x100;
        }
        bVar2 = (&DAT_ffff0000be0d167e)[uVar6];
        uVar11 = (ulong)bVar2;
        puVar1 = (ushort *)(param_3 + uVar11 * 4);
        uVar9 = *puVar1;
        uVar3 = puVar1[1];
        uVar8 = uVar8 | (uint)uVar9 << (ulong)(uVar7 & 0x1f);
        *(short *)(param_1 + 0x1730) = (short)uVar8;
        if ((int)(0x10 - (uint)uVar3) < (int)uVar7) {
          lVar10 = *(long *)(param_1 + 0x28);
          *(long *)(param_1 + 0x28) = lVar10 + 1;
          *(char *)(*(long *)(param_1 + 0x10) + lVar10) = (char)uVar8;
          lVar10 = *(long *)(param_1 + 0x28);
          *(long *)(param_1 + 0x28) = lVar10 + 1;
          *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar10) = *(undefined1 *)(param_1 + 0x1731);
          uVar7 = ((uint)uVar3 + *(int *)(param_1 + 0x1734)) - 0x10;
          uVar9 = uVar9 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
          uVar8 = (uint)uVar9;
          *(ushort *)(param_1 + 0x1730) = uVar9;
        }
        else {
          uVar7 = uVar7 + uVar3;
        }
        *(uint *)(param_1 + 0x1734) = uVar7;
        if (3 < bVar2) {
          iVar13 = *(int *)(&DAT_ffff0000be0d1eec + uVar11 * 4);
          uVar5 = uVar5 - *(int *)(&DAT_ffff0000be0d2038 + uVar11 * 4);
          if (0x10 - iVar13 < (int)uVar7) {
            lVar10 = *(long *)(param_1 + 0x28);
            uVar8 = uVar8 | uVar5 << (ulong)(uVar7 & 0x1f);
            *(short *)(param_1 + 0x1730) = (short)uVar8;
            *(long *)(param_1 + 0x28) = lVar10 + 1;
            *(char *)(*(long *)(param_1 + 0x10) + lVar10) = (char)uVar8;
            lVar10 = *(long *)(param_1 + 0x28);
            *(long *)(param_1 + 0x28) = lVar10 + 1;
            *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar10) = *(undefined1 *)(param_1 + 0x1731);
            uVar7 = (iVar13 + *(int *)(param_1 + 0x1734)) - 0x10;
            uVar9 = (ushort)((uVar5 & 0xffff) >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f))
            ;
          }
          else {
            uVar4 = uVar7 & 0x1f;
            uVar7 = iVar13 + uVar7;
            uVar9 = (ushort)uVar8 | (ushort)(uVar5 << (ulong)uVar4);
          }
LAB_ffff0000be05c154:
          *(ushort *)(param_1 + 0x1730) = uVar9;
          goto LAB_ffff0000be05c158;
        }
      }
      uVar12 = uVar12 + 3;
    } while (uVar12 < *(uint *)(param_1 + 0x170c));
  }
  uVar9 = *(ushort *)(param_2 + 0x400);
  uVar3 = *(ushort *)(param_2 + 0x402);
  uVar12 = (uint)*(ushort *)(param_1 + 0x1730) | (uint)uVar9 << (ulong)(uVar7 & 0x1f);
  *(short *)(param_1 + 0x1730) = (short)uVar12;
  if ((int)(0x10 - (uint)uVar3) < (int)uVar7) {
    lVar10 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar10 + 1;
    *(char *)(*(long *)(param_1 + 0x10) + lVar10) = (char)uVar12;
    lVar10 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar10 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar10) = *(undefined1 *)(param_1 + 0x1731);
    iVar13 = (uint)uVar3 + *(int *)(param_1 + 0x1734) + -0x10;
    *(ushort *)(param_1 + 0x1730) = uVar9 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
  }
  else {
    iVar13 = uVar7 + uVar3;
  }
  *(int *)(param_1 + 0x1734) = iVar13;
  return;
}


