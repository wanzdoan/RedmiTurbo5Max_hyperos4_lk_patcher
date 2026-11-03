
undefined4 FUN_ffff000090775030(long *param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  uint uVar3;
  uint uVar4;
  byte bVar5;
  short sVar6;
  ushort uVar7;
  int iVar8;
  undefined *puVar9;
  undefined *puVar10;
  uint uVar11;
  ulong uVar12;
  ulong uVar13;
  long lVar14;
  long lVar15;
  uint uVar16;
  int iVar17;
  long lVar18;
  
  puVar10 = PTR_DAT_ffff000090839a18;
  puVar9 = PTR_DAT_ffff000090839a10;
LAB_ffff000090775074:
  do {
    if (*(uint *)((long)param_1 + 0xb4) < 0x106) {
      FUN_ffff000090772d90(param_1);
      uVar11 = *(uint *)((long)param_1 + 0xb4);
      if ((param_2 == 0) && (uVar11 < 0x106)) {
        return 0;
      }
      if (uVar11 == 0) {
        uVar11 = *(uint *)((long)param_1 + 0xac);
        uVar12 = (ulong)uVar11;
        if (1 < uVar11) {
          uVar11 = 2;
        }
        *(uint *)((long)param_1 + 0x172c) = uVar11;
        if (param_2 != 4) {
          if (*(int *)((long)param_1 + 0x170c) != 0) {
            uVar13 = param_1[0x13];
            if ((long)uVar13 < 0) {
              lVar18 = 0;
            }
            else {
              lVar18 = param_1[0xc] + (uVar13 & 0xffffffff);
            }
            FUN_ffff000090779150(param_1,lVar18,uVar12 - uVar13,0);
            lVar14 = *param_1;
            lVar18 = *(long *)(lVar14 + 0x38);
            param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
            FUN_ffff000090778f5c(lVar18);
            uVar11 = *(uint *)(lVar14 + 0x20);
            if (*(uint *)(lVar18 + 0x28) <= *(uint *)(lVar14 + 0x20)) {
              uVar11 = *(uint *)(lVar18 + 0x28);
            }
            uVar12 = (ulong)uVar11;
            if (uVar11 != 0) {
              FUN_ffff00009077eb20
                        (*(undefined8 *)(lVar14 + 0x18),*(undefined8 *)(lVar18 + 0x20),uVar12);
              lVar2 = *(long *)(lVar18 + 0x20);
              lVar15 = *(long *)(lVar18 + 0x28);
              *(ulong *)(lVar14 + 0x18) = *(long *)(lVar14 + 0x18) + uVar12;
              lVar15 = lVar15 - uVar12;
              *(ulong *)(lVar14 + 0x28) = *(long *)(lVar14 + 0x28) + uVar12;
              *(uint *)(lVar14 + 0x20) = *(int *)(lVar14 + 0x20) - uVar11;
              *(ulong *)(lVar18 + 0x20) = lVar2 + uVar12;
              *(long *)(lVar18 + 0x28) = lVar15;
              if (lVar15 == 0) {
                *(undefined8 *)(lVar18 + 0x20) = *(undefined8 *)(lVar18 + 0x10);
              }
            }
            if (*(int *)(*param_1 + 0x20) == 0) {
              return 0;
            }
          }
          return 1;
        }
        uVar13 = param_1[0x13];
        if ((long)uVar13 < 0) {
          lVar18 = 0;
        }
        else {
          lVar18 = param_1[0xc] + (uVar13 & 0xffffffff);
        }
        FUN_ffff000090779150(param_1,lVar18,uVar12 - uVar13,1);
        lVar14 = *param_1;
        lVar18 = *(long *)(lVar14 + 0x38);
        param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
        FUN_ffff000090778f5c(lVar18);
        uVar11 = *(uint *)(lVar14 + 0x20);
        if (*(uint *)(lVar18 + 0x28) <= *(uint *)(lVar14 + 0x20)) {
          uVar11 = *(uint *)(lVar18 + 0x28);
        }
        uVar12 = (ulong)uVar11;
        if (uVar11 != 0) {
          FUN_ffff00009077eb20(*(undefined8 *)(lVar14 + 0x18),*(undefined8 *)(lVar18 + 0x20),uVar12)
          ;
          lVar2 = *(long *)(lVar18 + 0x20);
          lVar15 = *(long *)(lVar18 + 0x28);
          *(ulong *)(lVar14 + 0x18) = *(long *)(lVar14 + 0x18) + uVar12;
          lVar15 = lVar15 - uVar12;
          *(ulong *)(lVar14 + 0x28) = *(long *)(lVar14 + 0x28) + uVar12;
          *(uint *)(lVar14 + 0x20) = *(int *)(lVar14 + 0x20) - uVar11;
          *(ulong *)(lVar18 + 0x20) = lVar2 + uVar12;
          *(long *)(lVar18 + 0x28) = lVar15;
          if (lVar15 == 0) {
            *(undefined8 *)(lVar18 + 0x20) = *(undefined8 *)(lVar18 + 0x10);
          }
        }
        if (*(int *)(*param_1 + 0x20) != 0) {
          return 3;
        }
        return 2;
      }
      if (2 < uVar11) goto LAB_ffff0000907750a4;
LAB_ffff000090775100:
      uVar11 = *(uint *)(param_1 + 0x14);
      if (2 < uVar11) goto LAB_ffff00009077510c;
LAB_ffff00009077529c:
      uVar11 = *(uint *)((long)param_1 + 0x170c);
      bVar5 = *(byte *)(param_1[0xc] + (ulong)*(uint *)((long)param_1 + 0xac));
      *(uint *)((long)param_1 + 0x170c) = uVar11 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar11) = 0;
      uVar11 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar11 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar11) = 0;
      uVar11 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar11 + 1;
      *(byte *)(param_1[0x2e0] + (ulong)uVar11) = bVar5;
      iVar8 = *(int *)((long)param_1 + 0xb4);
      iVar17 = *(int *)((long)param_1 + 0x170c);
      lVar18 = param_1[0x2e2];
      uVar11 = *(int *)((long)param_1 + 0xac) + 1;
      *(short *)((long)param_1 + (ulong)bVar5 * 4 + 0xd4) =
           *(short *)((long)param_1 + (ulong)bVar5 * 4 + 0xd4) + 1;
      *(int *)((long)param_1 + 0xb4) = iVar8 + -1;
      *(uint *)((long)param_1 + 0xac) = uVar11;
      if (iVar17 != (int)lVar18) goto LAB_ffff000090775074;
    }
    else {
LAB_ffff0000907750a4:
      uVar11 = *(uint *)((long)param_1 + 0xac);
      uVar16 = ((int)param_1[0x10] << (ulong)(*(uint *)(param_1 + 0x12) & 0x1f) ^
               (uint)*(byte *)(param_1[0xc] + (ulong)(uVar11 + 2))) &
               *(uint *)((long)param_1 + 0x8c);
      lVar18 = param_1[0xf];
      lVar14 = (ulong)uVar16 * 2;
      *(uint *)(param_1 + 0x10) = uVar16;
      uVar7 = *(ushort *)(lVar18 + lVar14);
      *(ushort *)(param_1[0xe] + (ulong)(*(uint *)(param_1 + 0xb) & uVar11) * 2) = uVar7;
      *(short *)(lVar18 + lVar14) = (short)uVar11;
      if ((uVar7 == 0) || ((int)param_1[10] - 0x106U < uVar11 - uVar7)) goto LAB_ffff000090775100;
      uVar11 = FUN_ffff000090775c64(param_1);
      *(uint *)(param_1 + 0x14) = uVar11;
      if (uVar11 < 3) goto LAB_ffff00009077529c;
LAB_ffff00009077510c:
      uVar16 = *(uint *)((long)param_1 + 0x170c);
      iVar8 = *(int *)((long)param_1 + 0xac) - (int)param_1[0x16];
      *(uint *)((long)param_1 + 0x170c) = uVar16 + 1;
      *(char *)(param_1[0x2e0] + (ulong)uVar16) = (char)iVar8;
      uVar16 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar16 + 1;
      *(char *)(param_1[0x2e0] + (ulong)uVar16) = (char)((uint)iVar8 >> 8);
      uVar16 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar16 + 1;
      *(char *)(param_1[0x2e0] + (ulong)uVar16) = (char)(uVar11 - 3);
      uVar16 = iVar8 - 1;
      if ((uVar16 & 0xff00) != 0) {
        uVar16 = (uVar16 >> 7 & 0x1ff) + 0x100;
      }
      uVar3 = *(uint *)(param_1 + 0x18);
      *(short *)((long)param_1 + (ulong)(byte)puVar9[(ulong)(uVar11 - 3) & 0xff] * 4 + 0x4d8) =
           *(short *)((long)param_1 + (ulong)(byte)puVar9[(ulong)(uVar11 - 3) & 0xff] * 4 + 0x4d8) +
           1;
      bVar5 = puVar10[(ulong)uVar16 & 0xffff];
      uVar16 = *(uint *)(param_1 + 0x14);
      sVar6 = *(short *)((long)param_1 + (ulong)bVar5 * 4 + 0x9c8);
      uVar11 = *(int *)((long)param_1 + 0xb4) - uVar16;
      lVar18 = param_1[0x2e2];
      iVar8 = *(int *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0xb4) = uVar11;
      *(short *)((long)param_1 + (ulong)bVar5 * 4 + 0x9c8) = sVar6 + 1;
      if ((uVar3 < uVar16) || (uVar11 < 3)) {
        *(undefined4 *)(param_1 + 0x14) = 0;
        uVar11 = *(int *)((long)param_1 + 0xac) + uVar16;
        *(uint *)((long)param_1 + 0xac) = uVar11;
        uVar16 = (uint)*(byte *)(param_1[0xc] + (ulong)uVar11);
        *(uint *)(param_1 + 0x10) = uVar16;
        *(uint *)(param_1 + 0x10) =
             (uVar16 << (ulong)(*(uint *)(param_1 + 0x12) & 0x1f) ^
             (uint)*(byte *)(param_1[0xc] + (ulong)(uVar11 + 1))) & *(uint *)((long)param_1 + 0x8c);
      }
      else {
        lVar14 = param_1[0xe];
        lVar2 = param_1[0xf];
        iVar17 = uVar16 - 2;
        uVar3 = *(uint *)((long)param_1 + 0x8c);
        uVar1 = *(uint *)(param_1 + 0x12);
        lVar15 = param_1[0xc];
        uVar11 = *(int *)((long)param_1 + 0xac) + 1;
        uVar4 = *(uint *)(param_1 + 0xb);
        *(uint *)(param_1 + 0x14) = uVar16 - 1;
        uVar12 = (ulong)*(uint *)(param_1 + 0x10);
        do {
          *(uint *)((long)param_1 + 0xac) = uVar11;
          bVar5 = *(byte *)(lVar15 + (ulong)(uVar11 + 2));
          *(int *)(param_1 + 0x14) = iVar17;
          iVar17 = iVar17 + -1;
          uVar16 = ((int)uVar12 << (ulong)(uVar1 & 0x1f) ^ (uint)bVar5) & uVar3;
          uVar12 = (ulong)uVar16;
          *(uint *)(param_1 + 0x10) = uVar16;
          *(undefined2 *)(lVar14 + (ulong)(uVar4 & uVar11) * 2) =
               *(undefined2 *)(lVar2 + uVar12 * 2);
          *(short *)(lVar2 + uVar12 * 2) = (short)uVar11;
          uVar11 = uVar11 + 1;
        } while (iVar17 != -1);
        *(uint *)((long)param_1 + 0xac) = uVar11;
      }
      if (iVar8 != (int)lVar18) goto LAB_ffff000090775074;
    }
    uVar12 = param_1[0x13];
    if ((long)uVar12 < 0) {
      lVar18 = 0;
    }
    else {
      lVar18 = param_1[0xc] + (uVar12 & 0xffffffff);
    }
    FUN_ffff000090779150(param_1,lVar18,uVar11 - uVar12,0);
    lVar14 = *param_1;
    lVar18 = *(long *)(lVar14 + 0x38);
    param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
    FUN_ffff000090778f5c(lVar18);
    uVar11 = *(uint *)(lVar14 + 0x20);
    if (*(uint *)(lVar18 + 0x28) <= *(uint *)(lVar14 + 0x20)) {
      uVar11 = *(uint *)(lVar18 + 0x28);
    }
    uVar12 = (ulong)uVar11;
    if (uVar11 != 0) {
      FUN_ffff00009077eb20(*(undefined8 *)(lVar14 + 0x18),*(undefined8 *)(lVar18 + 0x20),uVar12);
      lVar2 = *(long *)(lVar18 + 0x20);
      lVar15 = *(long *)(lVar18 + 0x28);
      *(ulong *)(lVar14 + 0x18) = *(long *)(lVar14 + 0x18) + uVar12;
      lVar15 = lVar15 - uVar12;
      *(ulong *)(lVar14 + 0x28) = *(long *)(lVar14 + 0x28) + uVar12;
      *(uint *)(lVar14 + 0x20) = *(int *)(lVar14 + 0x20) - uVar11;
      *(ulong *)(lVar18 + 0x20) = lVar2 + uVar12;
      *(long *)(lVar18 + 0x28) = lVar15;
      if (lVar15 == 0) {
        *(undefined8 *)(lVar18 + 0x20) = *(undefined8 *)(lVar18 + 0x10);
      }
    }
    if (*(int *)(*param_1 + 0x20) == 0) {
      return 0;
    }
  } while( true );
}


