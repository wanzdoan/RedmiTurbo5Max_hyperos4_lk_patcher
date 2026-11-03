// ffff0000907752b8  size=1764  FUN_ffff0000907752b8

undefined4 FUN_ffff0000907752b8(long *param_1,int param_2)

{
  int iVar1;
  int iVar2;
  long lVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  byte bVar8;
  short sVar9;
  ushort uVar10;
  uint uVar11;
  undefined *puVar12;
  undefined *puVar13;
  uint uVar14;
  uint uVar15;
  ulong uVar16;
  ulong uVar17;
  int iVar18;
  long lVar19;
  long lVar20;
  long lVar21;
  
  puVar13 = PTR_DAT_ffff0000908399f8;
  puVar12 = PTR_DAT_ffff0000908399f0;
LAB_ffff000090775320:
  do {
    if (*(uint *)((long)param_1 + 0xb4) < 0x106) {
      FUN_ffff000090772ac8(param_1);
      uVar15 = *(uint *)((long)param_1 + 0xb4);
      if ((param_2 == 0) && (uVar15 < 0x106)) {
        return 0;
      }
      if (uVar15 == 0) {
        if ((int)param_1[0x15] != 0) {
          uVar15 = *(uint *)((long)param_1 + 0x170c);
          bVar8 = *(byte *)(param_1[0xc] + (ulong)(*(int *)((long)param_1 + 0xac) - 1));
          *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
          *(undefined1 *)(param_1[0x2e0] + (ulong)uVar15) = 0;
          uVar15 = *(uint *)((long)param_1 + 0x170c);
          *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
          *(undefined1 *)(param_1[0x2e0] + (ulong)uVar15) = 0;
          uVar15 = *(uint *)((long)param_1 + 0x170c);
          *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
          *(byte *)(param_1[0x2e0] + (ulong)uVar15) = bVar8;
          sVar9 = *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0xd4);
          *(undefined4 *)(param_1 + 0x15) = 0;
          *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0xd4) = sVar9 + 1;
        }
        uVar15 = *(uint *)((long)param_1 + 0xac);
        uVar16 = (ulong)uVar15;
        if (1 < uVar15) {
          uVar15 = 2;
        }
        *(uint *)((long)param_1 + 0x172c) = uVar15;
        if (param_2 != 4) {
          if (*(int *)((long)param_1 + 0x170c) != 0) {
            uVar17 = param_1[0x13];
            if ((long)uVar17 < 0) {
              lVar21 = 0;
            }
            else {
              lVar21 = param_1[0xc] + (uVar17 & 0xffffffff);
            }
            FUN_ffff000090778e88(param_1,lVar21,uVar16 - uVar17,0);
            lVar19 = *param_1;
            lVar21 = *(long *)(lVar19 + 0x38);
            param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
            FUN_ffff000090778c94(lVar21);
            uVar15 = *(uint *)(lVar19 + 0x20);
            if (*(uint *)(lVar21 + 0x28) <= *(uint *)(lVar19 + 0x20)) {
              uVar15 = *(uint *)(lVar21 + 0x28);
            }
            uVar16 = (ulong)uVar15;
            if (uVar15 != 0) {
              FUN_ffff00009077e858
                        (*(undefined8 *)(lVar19 + 0x18),*(undefined8 *)(lVar21 + 0x20),uVar16);
              lVar20 = *(long *)(lVar21 + 0x20);
              lVar3 = *(long *)(lVar21 + 0x28);
              *(ulong *)(lVar19 + 0x18) = *(long *)(lVar19 + 0x18) + uVar16;
              lVar3 = lVar3 - uVar16;
              *(ulong *)(lVar19 + 0x28) = *(long *)(lVar19 + 0x28) + uVar16;
              *(uint *)(lVar19 + 0x20) = *(int *)(lVar19 + 0x20) - uVar15;
              *(ulong *)(lVar21 + 0x20) = lVar20 + uVar16;
              *(long *)(lVar21 + 0x28) = lVar3;
              if (lVar3 == 0) {
                *(undefined8 *)(lVar21 + 0x20) = *(undefined8 *)(lVar21 + 0x10);
              }
            }
            if (*(int *)(*param_1 + 0x20) == 0) {
              return 0;
            }
          }
          return 1;
        }
        uVar17 = param_1[0x13];
        if ((long)uVar17 < 0) {
          lVar21 = 0;
        }
        else {
          lVar21 = param_1[0xc] + (uVar17 & 0xffffffff);
        }
        FUN_ffff000090778e88(param_1,lVar21,uVar16 - uVar17,1);
        lVar19 = *param_1;
        lVar21 = *(long *)(lVar19 + 0x38);
        param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
        FUN_ffff000090778c94(lVar21);
        uVar15 = *(uint *)(lVar19 + 0x20);
        if (*(uint *)(lVar21 + 0x28) <= *(uint *)(lVar19 + 0x20)) {
          uVar15 = *(uint *)(lVar21 + 0x28);
        }
        uVar16 = (ulong)uVar15;
        if (uVar15 != 0) {
          FUN_ffff00009077e858(*(undefined8 *)(lVar19 + 0x18),*(undefined8 *)(lVar21 + 0x20),uVar16)
          ;
          lVar20 = *(long *)(lVar21 + 0x20);
          lVar3 = *(long *)(lVar21 + 0x28);
          *(ulong *)(lVar19 + 0x18) = *(long *)(lVar19 + 0x18) + uVar16;
          lVar3 = lVar3 - uVar16;
          *(ulong *)(lVar19 + 0x28) = *(long *)(lVar19 + 0x28) + uVar16;
          *(uint *)(lVar19 + 0x20) = *(int *)(lVar19 + 0x20) - uVar15;
          *(ulong *)(lVar21 + 0x20) = lVar20 + uVar16;
          *(long *)(lVar21 + 0x28) = lVar3;
          if (lVar3 == 0) {
            *(undefined8 *)(lVar21 + 0x20) = *(undefined8 *)(lVar21 + 0x10);
          }
        }
        if (*(int *)(*param_1 + 0x20) == 0) {
          return 2;
        }
        return 3;
      }
      if (2 < uVar15) goto LAB_ffff000090775350;
      uVar4 = *(uint *)(param_1 + 0x14);
      *(uint *)(param_1 + 0x17) = uVar4;
      *(undefined4 *)(param_1 + 0x14) = 2;
      *(int *)((long)param_1 + 0xa4) = (int)param_1[0x16];
LAB_ffff000090775408:
      uVar14 = 2;
    }
    else {
LAB_ffff000090775350:
      uVar15 = *(uint *)((long)param_1 + 0xac);
      uVar14 = 2;
      uVar11 = ((int)param_1[0x10] << (ulong)(*(uint *)(param_1 + 0x12) & 0x1f) ^
               (uint)*(byte *)(param_1[0xc] + (ulong)(uVar15 + 2))) &
               *(uint *)((long)param_1 + 0x8c);
      lVar21 = param_1[0xf];
      lVar19 = (ulong)uVar11 * 2;
      uVar4 = *(uint *)(param_1 + 0x14);
      *(uint *)(param_1 + 0x10) = uVar11;
      uVar10 = *(ushort *)(lVar21 + lVar19);
      *(undefined4 *)(param_1 + 0x14) = 2;
      *(int *)((long)param_1 + 0xa4) = (int)param_1[0x16];
      *(uint *)(param_1 + 0x17) = uVar4;
      *(ushort *)(param_1[0xe] + (ulong)(*(uint *)(param_1 + 0xb) & uVar15) * 2) = uVar10;
      *(short *)(lVar21 + lVar19) = (short)uVar15;
      if (uVar10 == 0) goto LAB_ffff000090775408;
      if ((uVar4 < *(uint *)(param_1 + 0x18)) && (uVar15 - uVar10 <= (int)param_1[10] - 0x106U)) {
        uVar14 = FUN_ffff00009077599c(param_1);
        *(uint *)(param_1 + 0x14) = uVar14;
        if (uVar14 < 6) {
          if ((int)param_1[0x19] == 1) {
LAB_ffff0000907753e8:
            uVar14 = 2;
            *(undefined4 *)(param_1 + 0x14) = 2;
          }
          else if (uVar14 == 3) {
            if (0x1000 < (uint)(*(int *)((long)param_1 + 0xac) - (int)param_1[0x16]))
            goto LAB_ffff0000907753e8;
            uVar14 = 3;
          }
        }
      }
      uVar4 = *(uint *)(param_1 + 0x17);
    }
    if (uVar4 < 3) {
LAB_ffff000090775578:
      if ((int)param_1[0x15] == 0) {
        *(undefined4 *)(param_1 + 0x15) = 1;
        *(int *)((long)param_1 + 0xac) = *(int *)((long)param_1 + 0xac) + 1;
        *(int *)((long)param_1 + 0xb4) = *(int *)((long)param_1 + 0xb4) + -1;
        goto LAB_ffff000090775320;
      }
      uVar15 = *(uint *)((long)param_1 + 0x170c);
      bVar8 = *(byte *)(param_1[0xc] + (ulong)(*(int *)((long)param_1 + 0xac) - 1));
      *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar15) = 0;
      uVar15 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar15) = 0;
      uVar15 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
      *(byte *)(param_1[0x2e0] + (ulong)uVar15) = bVar8;
      iVar1 = *(int *)((long)param_1 + 0x170c);
      lVar21 = param_1[0x2e2];
      *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0xd4) =
           *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0xd4) + 1;
      if (iVar1 == (int)lVar21) {
        uVar16 = param_1[0x13];
        if ((long)uVar16 < 0) {
          lVar21 = 0;
        }
        else {
          lVar21 = param_1[0xc] + (uVar16 & 0xffffffff);
        }
        FUN_ffff000090778e88(param_1,lVar21,*(uint *)((long)param_1 + 0xac) - uVar16,0);
        lVar19 = *param_1;
        lVar21 = *(long *)(lVar19 + 0x38);
        param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
        FUN_ffff000090778c94(lVar21);
        uVar15 = *(uint *)(lVar19 + 0x20);
        if (*(uint *)(lVar21 + 0x28) <= *(uint *)(lVar19 + 0x20)) {
          uVar15 = *(uint *)(lVar21 + 0x28);
        }
        uVar16 = (ulong)uVar15;
        if (uVar15 != 0) {
          FUN_ffff00009077e858(*(undefined8 *)(lVar19 + 0x18),*(undefined8 *)(lVar21 + 0x20),uVar16)
          ;
          lVar20 = *(long *)(lVar21 + 0x20);
          lVar3 = *(long *)(lVar21 + 0x28);
          *(ulong *)(lVar19 + 0x18) = *(long *)(lVar19 + 0x18) + uVar16;
          lVar3 = lVar3 - uVar16;
          *(ulong *)(lVar19 + 0x28) = *(long *)(lVar19 + 0x28) + uVar16;
          *(uint *)(lVar19 + 0x20) = *(int *)(lVar19 + 0x20) - uVar15;
          *(ulong *)(lVar21 + 0x20) = lVar20 + uVar16;
          *(long *)(lVar21 + 0x28) = lVar3;
          if (lVar3 == 0) {
            *(undefined8 *)(lVar21 + 0x20) = *(undefined8 *)(lVar21 + 0x10);
          }
        }
      }
      iVar1 = *(int *)(*param_1 + 0x20);
      *(int *)((long)param_1 + 0xac) = *(int *)((long)param_1 + 0xac) + 1;
      *(int *)((long)param_1 + 0xb4) = *(int *)((long)param_1 + 0xb4) + -1;
    }
    else {
      if (uVar4 < uVar14) goto LAB_ffff000090775578;
      uVar15 = *(uint *)((long)param_1 + 0x170c);
      iVar5 = *(int *)((long)param_1 + 0xac);
      iVar1 = iVar5 + ~*(uint *)((long)param_1 + 0xa4);
      *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
      iVar6 = *(int *)((long)param_1 + 0xb4);
      *(char *)(param_1[0x2e0] + (ulong)uVar15) = (char)iVar1;
      uVar15 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
      *(char *)(param_1[0x2e0] + (ulong)uVar15) = (char)((uint)iVar1 >> 8);
      uVar15 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar15 + 1;
      *(char *)(param_1[0x2e0] + (ulong)uVar15) = (char)(uVar4 - 3);
      uVar15 = iVar1 - 1;
      if ((uVar15 & 0xff00) != 0) {
        uVar15 = (uVar15 >> 7 & 0x1ff) + 0x100;
      }
      iVar7 = *(int *)((long)param_1 + 0xac);
      iVar1 = *(int *)((long)param_1 + 0xb4);
      iVar2 = (int)param_1[0x17];
      *(short *)((long)param_1 + (ulong)(byte)puVar12[(ulong)(uVar4 - 3) & 0xff] * 4 + 0x4d8) =
           *(short *)((long)param_1 + (ulong)(byte)puVar12[(ulong)(uVar4 - 3) & 0xff] * 4 + 0x4d8) +
           1;
      bVar8 = puVar13[(ulong)uVar15 & 0xffff];
      iVar18 = iVar2 + -3;
      *(int *)((long)param_1 + 0xb4) = (iVar1 - iVar2) + 1;
      *(int *)(param_1 + 0x17) = iVar2 + -2;
      iVar1 = *(int *)((long)param_1 + 0x170c);
      *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0x9c8) =
           *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0x9c8) + 1;
      lVar21 = param_1[0x2e2];
      uVar16 = (ulong)(iVar7 + 1);
      do {
        uVar15 = (uint)uVar16;
        *(uint *)((long)param_1 + 0xac) = uVar15;
        if (uVar15 <= (iVar5 + iVar6) - 3U) {
          uVar4 = ((int)param_1[0x10] << (ulong)(*(uint *)(param_1 + 0x12) & 0x1f) ^
                  (uint)*(byte *)(param_1[0xc] + (ulong)(uVar15 + 2))) &
                  *(uint *)((long)param_1 + 0x8c);
          lVar19 = param_1[0xf];
          lVar20 = (ulong)uVar4 * 2;
          *(uint *)(param_1 + 0x10) = uVar4;
          *(undefined2 *)(param_1[0xe] + (*(uint *)(param_1 + 0xb) & uVar16) * 2) =
               *(undefined2 *)(lVar19 + lVar20);
          *(short *)(lVar19 + lVar20) = (short)uVar16;
        }
        *(int *)(param_1 + 0x17) = iVar18;
        iVar18 = iVar18 + -1;
        uVar16 = (ulong)(uVar15 + 1);
      } while (iVar18 != -1);
      *(undefined4 *)(param_1 + 0x14) = 2;
      *(undefined4 *)(param_1 + 0x15) = 0;
      *(uint *)((long)param_1 + 0xac) = uVar15 + 1;
      if (iVar1 != (int)lVar21) goto LAB_ffff000090775320;
      uVar17 = param_1[0x13];
      if ((long)uVar17 < 0) {
        lVar21 = 0;
      }
      else {
        lVar21 = param_1[0xc] + (uVar17 & 0xffffffff);
      }
      FUN_ffff000090778e88(param_1,lVar21,uVar16 - uVar17,0);
      lVar19 = *param_1;
      lVar21 = *(long *)(lVar19 + 0x38);
      param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
      FUN_ffff000090778c94(lVar21);
      uVar15 = *(uint *)(lVar19 + 0x20);
      if (*(uint *)(lVar21 + 0x28) <= *(uint *)(lVar19 + 0x20)) {
        uVar15 = *(uint *)(lVar21 + 0x28);
      }
      uVar16 = (ulong)uVar15;
      if (uVar15 != 0) {
        FUN_ffff00009077e858(*(undefined8 *)(lVar19 + 0x18),*(undefined8 *)(lVar21 + 0x20),uVar16);
        lVar20 = *(long *)(lVar21 + 0x20);
        lVar3 = *(long *)(lVar21 + 0x28);
        *(ulong *)(lVar19 + 0x18) = *(long *)(lVar19 + 0x18) + uVar16;
        lVar3 = lVar3 - uVar16;
        *(ulong *)(lVar19 + 0x28) = *(long *)(lVar19 + 0x28) + uVar16;
        *(uint *)(lVar19 + 0x20) = *(int *)(lVar19 + 0x20) - uVar15;
        *(ulong *)(lVar21 + 0x20) = lVar20 + uVar16;
        *(long *)(lVar21 + 0x28) = lVar3;
        if (lVar3 == 0) {
          *(undefined8 *)(lVar21 + 0x20) = *(undefined8 *)(lVar21 + 0x10);
        }
      }
      iVar1 = *(int *)(*param_1 + 0x20);
    }
    if (iVar1 == 0) {
      return 0;
    }
  } while( true );
}


